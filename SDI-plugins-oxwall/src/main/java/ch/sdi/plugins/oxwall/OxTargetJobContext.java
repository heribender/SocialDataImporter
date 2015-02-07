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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiDuplicatePersonException;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.impl.data.PersonKey;
import ch.sdi.core.impl.data.converter.ConverterImage;
import ch.sdi.core.impl.mail.MailJobDefault;
import ch.sdi.core.intf.CustomTargetJobContext;
import ch.sdi.core.intf.FtpJob;
import ch.sdi.core.intf.PasswordEncryptor;
import ch.sdi.core.intf.TargetJob;
import ch.sdi.plugins.oxwall.job.OxSqlJob;


/**
 * Implements the global business know how on the oxwall target platform.
 * <p>
 * It provides the target executor with the needed jobs.
 * <p>
 * In preparePerson which is called before a person is processed the needed data is prepared (like
 * password generation, formatting the avatar pictures) and a DB transaction is started. In finalizePerson
 * the DB transaction is committed or rollbacked (depending on the presence of an exception).
 * <p>
 * If Spring finds a implementation of CustomPreparePersonJob its execute() method is called in
 * preparePerson().
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
    /** the primary key of DB entity */
    public static final String KEY_PERSON_USER_ID = "person.userId";
    public static final String KEY_PERSON_PREPARED_AVATAR_FILES = "person.preparedAvatarFiles";


    @Autowired
    private Environment myEnv;
    @Autowired
    private PasswordEncryptor myPasswordEncryptor;
    @Autowired
    private MailJobDefault myMailJob;
    @Autowired
    private FtpJob myFtpJob;
    @Autowired
    private OxSqlJob mySqlJob;

    private boolean myHasAvatar;


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

        if ( myHasAvatar )
        {
            result.add( myFtpJob );
        }

        result.add( myMailJob );

        return result;
    }

    /**
     * @see ch.sdi.core.intf.TargetJobContext#prepare()
     */
    @Override
    public void prepare() throws SdiException
    {
        myHasAvatar = ConfigUtils.getBooleanProperty( myEnv, OxTargetConfiguration.KEY_HAS_AVATAR );
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
        }

        preparePassword( aPerson );

        if ( myHasAvatar )
        {
            prepareAvatar( aPerson );
        }

        String fullname = aPerson.getGivenname() + " "
                + ( StringUtils.hasText( aPerson.getMiddlename() ) ? (aPerson.getMiddlename() + " ") : "" )
                + aPerson.getFamilyName();
        aPerson.setProperty( KEY_PERSON_FULLNAME, fullname );

        mySqlJob.startTransaction();

    }

    /**
     * @param aPerson
     * @throws SdiException
     */
    private void prepareAvatar( Person<?> aPerson ) throws SdiException
    {
        BufferedImage origImage = aPerson.getProperty( PersonKey.THING_IMAGE.getKeyName(),
                                                           BufferedImage.class );
        if ( origImage == null )
        {
            myLog.debug( "No avatar available" );
            return;
        } // if bufferedImage == null

        /*
         * Oxwall knows three avatar files:
         *      - avatar_<userId>_<hash>.jpg (1)
         *      - avatar_big_<userId>_<hash>.jpg (2)
         *      - avatar_original_<userId>_<hash>.jpg (3)
         *   where:
         *      (1) 90x90 pixels, 96 dpi, 24 pixelBits
         *      (2) 190x190 pixels, 96 dpi, 24 pixelBits
         *      (3) any size (original uploaded)
         *   Linux-Access-Rights: -rw-r--r--
         *  The hash is entered in table ow_base_avatar (see OxAvatar)
         */

        Long hash;
        while ( true )
        {
            /* Note: the hash field is defined as int(11), existing hashes all have 10 digits, but start
             * with '1'. Inserting a value which exceeds the Integer.MAX_VALUE is not possible. So we
             * only randomize 9 digits and prefix it with '1', so it is for sure below the limit.
             */
            hash = Long.valueOf( "1" + RandomStringUtils.random( 9, "0123456789" ) );
            if ( !mySqlJob.isAvatarHashPresent( hash ) )
            {
                break;
            }
        }
        myLog.debug( "Generated hash for avatar " + hash );
        aPerson.setProperty( KEY_AVATAR_HASH, hash );

        // since the userId is part of the full filename, we prepare here only the input streams
        // associate with the file name prefix. The full file name is then composed in FtpJob
        Map<String, InputStream> filesToUpload = new HashMap<String, InputStream>();

        filesToUpload.put( "avatar_",
                           toInputStream( ConverterImage.resizeImage( origImage, 90, 90 ) ) );
        filesToUpload.put( "avatar_big_",
                           toInputStream( ConverterImage.resizeImage( origImage, 190, 190 ) ) );
        filesToUpload.put( "avatar_original_",
                           toInputStream( origImage ) );

        aPerson.setProperty( KEY_PERSON_PREPARED_AVATAR_FILES, filesToUpload );
    }

    /**
     * @param aImage
     * @return
     * @throws SdiException
     */
    protected InputStream toInputStream( BufferedImage aImage ) throws SdiException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try
        {
            ImageIO.write( aImage, "JPG", os );
        }
        catch ( IOException t )
        {

            throw new SdiException( "Problems writing image to stream",
                                    t,
                                    SdiException.EXIT_CODE_UNKNOWN_ERROR );
        }

        return new ByteArrayInputStream( os.toByteArray() );
    }

    /**
     * @param aPerson
     */
    private void preparePassword( Person<?> aPerson )
    {
        String password = RandomStringUtils.random( 8, true, true );
        myLog.trace( "Generated password for user " + aPerson.getEMail() + ": " + password );
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
