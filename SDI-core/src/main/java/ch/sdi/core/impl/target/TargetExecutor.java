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


package ch.sdi.core.impl.target;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiDuplicatePersonException;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.intf.CustomTargetJobContext;
import ch.sdi.core.intf.SdiMainProperties;
import ch.sdi.core.intf.TargetJob;
import ch.sdi.core.intf.TargetJobContext;
import ch.sdi.report.ReportMsg;


/**
 * The target executor executes all jobs on each person. It has no knowledge on the business rules of
 * the target platform but provides a consistent walk through each needed step, collecting information
 * on each step etc.
 * <p>
 * It also prepares the configured output directory where reports etc. are written to. The name of the
 * output directory is also put to the environment (key dynamic.outputDir.file).
 * <p>
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
    private ConfigurableEnvironment myEnv;
    @Autowired
    private DefaultTargetJobContext myDefaultJobContext;
    @Autowired( required=false )
    private CustomTargetJobContext myCustomTargetJobContext;
    private TargetJobContext myTargetJobContext;
    private boolean mySkipFailedPersons;
    private Map<Person<?>,Throwable> myFailedPersons;
    private Collection<Person<?>> myDuplicatePersons;
    private Collection<Person<?>> myProcessedPersons;
    private File myOutputDir;


    /**
     * Executes the whole target phase.
     * <p>
     * The real work is done within the jobs which are fetched from the custom target platform
     * implementation.
     *
     * @param aPersons
     * @throws SdiException
     */
    public void execute( Collection<? extends Person<?>> aPersons) throws SdiException
    {
        boolean dryRun = ConfigUtils.getBooleanProperty( myEnv, SdiMainProperties.KEY_DRYRUN, false );
        mySkipFailedPersons = dryRun ? true : ConfigUtils.getBooleanProperty( myEnv, SdiMainProperties.KEY_TARGET_IGNORE_FAILED_PERSON, false );

        myFailedPersons = new LinkedHashMap<Person<?>,Throwable>();
        myDuplicatePersons = new ArrayList<Person<?>>();
        myProcessedPersons = new ArrayList<Person<?>>();

        myOutputDir = prepareOutputDir();
        ConfigUtils.addToEnvironment( myEnv, ConfigUtils.KEY_PROP_OUTPUT_DIR, myOutputDir );

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

        myLog.info( "Going to prepare the target job context" );
        myTargetJobContext.prepare();

        try
        {
            Collection<? extends TargetJob> jobs = myTargetJobContext.getJobs();

            try
            {

                for ( TargetJob job : jobs )
                {
                    myLog.info( "Going to prepare the target job " + job.getClass().getSimpleName() );
                    job.init();
                }

                for ( Person<?> person : aPersons )
                {
                    processPerson( person, jobs );
                }

                if ( myFailedPersons.isEmpty() )
                {
                    myLog.info( "All data successfully handled for target" );
                } // if failedPersons.isEmpty()

            }
            finally
            {
                for ( TargetJob job : jobs )
                {
                    myLog.info( "Going to close the target job " + job.getClass().getSimpleName() );
                    job.close();
                }
            }
        }
        catch ( Throwable t )
        {
            SdiException newEx = SdiException.toSdiException( t );
            throw newEx;
        }
        finally
        {
            if ( !myProcessedPersons.isEmpty() )
            {
                myLog.info( new ReportMsg( ReportMsg.ReportType.TARGET, "ProcessedPersons", myProcessedPersons ) );
            } // if failedPersons.isEmpty()

            if ( !myFailedPersons.isEmpty() )
            {
                myLog.info( new ReportMsg( ReportMsg.ReportType.TARGET_PROBLEM, "FailedPersons", myFailedPersons ) );
            } // if failedPersons.isEmpty()

            if ( !myDuplicatePersons.isEmpty() )
            {
                myLog.info( new ReportMsg( ReportMsg.ReportType.TARGET, "DuplicatePersons", myDuplicatePersons ) );
            } // if failedPersons.isEmpty()

            myLog.info( "Going to release the target job context" );
            myTargetJobContext.release( null );
        }

    }

    /**
     * @param aPerson
     * @return
     */
    private void processPerson( Person<?> aPerson, Collection<? extends TargetJob> aJobs ) throws SdiException
    {
        myLog.info( "processing person " + aPerson.getEMail() );  // TODO: make primary key configurable

        try
        {
            myTargetJobContext.preparePerson( aPerson );

            for ( TargetJob job : aJobs )
            {
                job.execute( aPerson );
            }

            myProcessedPersons.add( aPerson );
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

            SdiException newEx = SdiException.toSdiException( t, SdiException.EXIT_CODE_IMPORT_ERROR );
            myTargetJobContext.finalizePerson( aPerson, newEx );

            if ( mySkipFailedPersons )
            {
                myLog.warn( "Problem while processing person " + aPerson.getEMail() // TODO: make primary key configurable
                            + "; continuing with next person", t );
                return;
            }

            throw newEx;
        }

        myTargetJobContext.finalizePerson( aPerson, null );

    }

    /**
     * @return
     * @throws IOException
     */
    private File prepareOutputDir() throws SdiException
    {
        String root = myEnv.getProperty( SdiMainProperties.KEY_OUTPUT_DIR );
        if ( !StringUtils.hasText( root ) )
        {
            myLog.warn( SdiMainProperties.KEY_OUTPUT_DIR + " not set. Setting to default ./../output" );
            root = "./../output";
        } // if !StringUtils.hasText( root )

        File file = new File( root );

        if ( file.exists() )
        {
            if ( !file.isDirectory() )
            {
                throw new SdiException( "Configured output dir is an existing file instead of a directory",
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            } // if !file.isDirectory()
        } // if !file.exists()


        DateFormat df = new SimpleDateFormat( "yyyyMMdd_hhmmss" );
        File result = new File( file.getPath(), df.format( new Date() ) );

        String outputDir;
        try
        {
            outputDir = result.getCanonicalPath();
        }
        catch ( IOException t )
        {
            throw new SdiException( "Problems with output path " + result.getPath(),
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }

        myLog.debug( "Creating output dir " + outputDir );

        if ( !result.mkdirs() )
        {
            throw new SdiException( "Cannot create output directory " + result,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }

        return result;
    }

}
