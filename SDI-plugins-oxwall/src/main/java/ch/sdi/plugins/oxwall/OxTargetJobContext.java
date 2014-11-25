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


package ch.sdi.plugins.oxwall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.intf.CustomPreparePersonJob;
import ch.sdi.core.intf.CustomTargetJobContext;
import ch.sdi.core.intf.FtpJob;
import ch.sdi.core.intf.MailJob;
import ch.sdi.core.intf.PasswordEncryptor;
import ch.sdi.core.intf.SqlJob;
import ch.sdi.core.intf.TargetJob;


/**
 * TODO
 *
 * @version 1.0 (24.11.2014)
 * @author  Heri
 */
@Component
public class OxTargetJobContext implements CustomTargetJobContext
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxTargetJobContext.class );

    @Autowired
    private Environment myEnv;
    @Autowired( required = false )
    private CustomPreparePersonJob myCustomPreparePersonJob;
    @Autowired
    private PasswordEncryptor myPasswordEncryptor;
    @Autowired
    private MailJob myMailJob;
    @Autowired
    private FtpJob myFtpJob;
    @Autowired
    private SqlJob mySqlJob;

    /**
     * Constructor
     *
     */
    public OxTargetJobContext()
    {
        super();
    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#getJobs()
     */
    @Override
    public Collection<? extends TargetJob> getJobs() throws SdiException
    {
        List<TargetJob> result = new ArrayList<TargetJob>();
        result.add( myFtpJob );
        result.add( mySqlJob );
        result.add( myMailJob );
        return result;
    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#prepare()
     */
    @Override
    public void prepare() throws SdiException
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#preparePerson()
     */
    @Override
    public void preparePerson( Person<?> aPerson ) throws SdiException
    {
        // TODO: check duplicate

        String password = RandomStringUtils.random( 8, true, true );
        String encrypted = myPasswordEncryptor.encrypt( password );


        // TODO: generate Avatar-Pictures

        if ( myCustomPreparePersonJob != null )
        {
            myCustomPreparePersonJob.execute( aPerson );
        }
        else
        {
            myLog.debug( "Skipping custom prepare job (not configured)" );
        }
    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#finalizePerson()
     */
    @Override
    public void finalizePerson( Person<?> aPerson ) throws SdiException
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#release()
     */
    @Override
    public void release() throws SdiException
    {
        // TODO Auto-generated method stub

    }

}
