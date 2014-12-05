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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiDuplicatePersonException;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.impl.data.PersonKey;
import ch.sdi.core.intf.CustomPreparePersonJob;
import ch.sdi.core.intf.CustomTargetJobContext;
import ch.sdi.core.intf.FtpJob;
import ch.sdi.core.intf.PasswordEncryptor;
import ch.sdi.core.intf.SqlJob;
import ch.sdi.core.intf.TargetJob;
import ch.sdi.plugins.oxwall.job.OxMailJob;


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
    public static final String KEY_AVATAR_HASH = "person.avatar.hash";
    public static final String KEY_PASSWORD = "person.password";
    public static final String KEY_ENCRYPTED_PASSWORD = "person.enc_password";
    public static final String KEY_PERSON_FULLNAME = "person.fullName";


    @Autowired
    private Environment myEnv;
    @Autowired( required = false )
    private CustomPreparePersonJob myCustomPreparePersonJob;
    @Autowired
    private PasswordEncryptor myPasswordEncryptor;
    @Autowired
    private OxMailJob myMailJob;
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
        // Basically I'd prefer to execute first the FTP job because in case of a failure in the
        // succeeding jobs the lowest damage is done in the system. But oxwalls avatar picture files
        // need the database ID of the new user for the filenames.
        result.add( mySqlJob );
        result.add( myFtpJob );
        result.add( myMailJob );
        return result;
    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#prepare()
     */
    @Override
    public void prepare() throws SdiException
    {
    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#preparePerson()
     */
    @Override
    public void preparePerson( Person<?> aPerson ) throws SdiException
    {
        if ( mySqlJob.isAlreadyPresent( aPerson ) )
        {
            throw new SdiDuplicatePersonException( aPerson );
        } // if mySqlJob.isAlreadyPresent( aPerson )

        preparePassword( aPerson );
        prepareAvatarHash( aPerson );

        String fullname = aPerson.getGivenname() + " "
                + ( StringUtils.hasText( aPerson.getMiddlename() ) ? (aPerson.getMiddlename() + " ") : "" )
                + aPerson.getFamilyName();
        aPerson.setProperty( KEY_PERSON_FULLNAME, fullname );

        if ( myCustomPreparePersonJob != null )
        {
            myCustomPreparePersonJob.execute( aPerson );
        }
        else
        {
            myLog.debug( "Skipping custom prepare job (not configured)" );
        }

        mySqlJob.startTransaction();

    }

    /**
     * @param aPerson
     */
    private void prepareAvatarHash( Person<?> aPerson )
    {
        BufferedImage bufferedImage = aPerson.getProperty( PersonKey.THING_IMAGE.getKeyName(),
                                                           BufferedImage.class );
        if ( bufferedImage == null )
        {
            myLog.debug( "No avatar available" );
            return;
        } // if bufferedImage == null

        String hash = RandomStringUtils.random( 10, "0123456789" );
        myLog.debug( "Generated hash for avatar " + hash );
        aPerson.setProperty( KEY_AVATAR_HASH, hash );
    }

    /**
     * @param aPerson
     */
    private void preparePassword( Person<?> aPerson )
    {
        String password = RandomStringUtils.random( 8, true, true );
        String encrypted = myPasswordEncryptor.encrypt( password );
        aPerson.setProperty( KEY_PASSWORD, password );
        aPerson.setProperty( KEY_ENCRYPTED_PASSWORD, encrypted );
    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#finalizePerson()
     */
    @Override
    public void finalizePerson( Person<?> aPerson, SdiException aException ) throws SdiException
    {
        if ( aException == null )
        {
            mySqlJob.commitTransaction();
        }
        else
        {
            mySqlJob.rollbackTransaction();
        } // if..else aException == null
    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#release()
     */
    @Override
    public void release( SdiException aException ) throws SdiException
    {
    }

}
