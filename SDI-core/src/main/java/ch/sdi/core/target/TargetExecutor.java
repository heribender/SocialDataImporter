/**
 * Copyright (c) 2014 by the original author or authors.
 *
 * This code is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package ch.sdi.core.target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiDuplicatePersonException;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigHelper;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.intf.CustomTargetJobContext;
import ch.sdi.core.intf.SdiMainProperties;
import ch.sdi.core.intf.TargetJob;
import ch.sdi.core.intf.TargetJobContext;
import ch.sdi.report.ReportMsg;


/**
 * TODO
 *
 * @version 1.0 (15.11.2014)
 * @author  Heri
 */
@Component
public class TargetExecutor
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( TargetExecutor.class );
    @Autowired
    private Environment myEnv;
    @Autowired
    private DefaultTargetJobContext myDefaultJobContext;
    @Autowired
    private CustomTargetJobContext myCustomTargetJobContext;
    private TargetJobContext myTargetJobContext;
    private boolean mySkipFailedPersons;
    private Map<Person<?>,Throwable> myFailedPersons;
    private Collection<Person<?>> myDuplicatePersons;


    public void execute( Collection<? extends Person<?>> aPersons) throws SdiException
    {
        boolean dryRun = ConfigHelper.getBooleanProperty( myEnv, SdiMainProperties.KEY_DRYRUN, false );
        mySkipFailedPersons = dryRun ? true : ConfigHelper.getBooleanProperty( myEnv, SdiMainProperties.KEY_TARGET_IGNORE_FAILED_PERSON, false );

        myFailedPersons = new TreeMap<Person<?>,Throwable>();
        myDuplicatePersons = new ArrayList<Person<?>>();

        /*
         * Because ConditionalOnMissingBean annotation does obviously not work for beans which must be
         * injected we let both inject here, the DefaultJobContext and a possible CustomTargetJobContext.
         * Note that the DefaultJobContext does not do anything, so the whole application would be
         * senseless without a CustomTargetJobContext implementation available.
         */
        myTargetJobContext = myDefaultJobContext;
        if ( myCustomTargetJobContext != null )
        {
            myTargetJobContext = myCustomTargetJobContext;
        } // if myCustomTargetJobContext != null

        myTargetJobContext.prepare();

        try
        {
            for ( Person<?> person : aPersons )
            {
                processPerson( person );
            }

            if ( myFailedPersons.isEmpty() )
            {
                myLog.info( "All data successfully handled for target" );
            } // if failedPersons.isEmpty()

        }
        catch ( Throwable t )
        {
            SdiException newEx = SdiException.toSdiException( t );
            myTargetJobContext.release( newEx );
            throw newEx;
        }
        finally
        {
            if ( !myFailedPersons.isEmpty() )
            {
                myLog.info( new ReportMsg( ReportMsg.ReportType.TARGET_PROBLEM, "FailedPersons", myFailedPersons ) );
            } // if failedPersons.isEmpty()

            if ( !myDuplicatePersons.isEmpty() )
            {
                myLog.info( new ReportMsg( ReportMsg.ReportType.TARGET, "DuplicatePersons", myDuplicatePersons ) );
            } // if failedPersons.isEmpty()

            myTargetJobContext.release( null );
        }


    }

    /**
     * @param aPerson
     * @return
     */
    private void processPerson( Person<?> aPerson ) throws SdiException
    {
        myLog.debug( "processing person " + aPerson.getEMail() );  // TODO: make primary key configurable

        try
        {
            myTargetJobContext.preparePerson( aPerson );

            for ( TargetJob job : myTargetJobContext.getJobs() )
            {
                job.execute( aPerson );
            }

            myTargetJobContext.finalizePerson( aPerson, null );

        }
        catch ( SdiDuplicatePersonException t )
        {
            myLog.info( "Person " + aPerson.getFamilyName() +
                    " already a user of the target platform. Skip" );
            myDuplicatePersons.add( aPerson );
            myTargetJobContext.finalizePerson( aPerson, t );
        }
        catch ( Throwable t )
        {
            myFailedPersons.put( aPerson, t );

            if ( mySkipFailedPersons )
            {
                myLog.warn( "Problem while processing person " + aPerson.getEMail() // TODO: make primary key configurable
                            + "; continuing with next person", t );
                return;
            }

            SdiException newEx = SdiException.toSdiException( t, SdiException.EXIT_CODE_IMPORT_ERROR );

            myTargetJobContext.finalizePerson( aPerson, newEx );

            throw newEx;
        }

    }

}
