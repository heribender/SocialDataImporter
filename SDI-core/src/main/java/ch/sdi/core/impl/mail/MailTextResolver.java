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


package ch.sdi.core.impl.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.intf.MailProperties;


/**
 * Resolves the mail subject and the mail body.
 * <p>
 * Configure the subject with key sdi.mail.subject.
 * <p>
 * The body can be configured directly by key sdi.mail.body or - if you want to use a template file - by the
 * key sdi.mail.body.template. If latter is the case, also the charset must be configured by key
 * sdi.mail.body.template.charset.
 * <p>
 * A configured body template file takes precedence over the key sdi.mail.body.
 *
 * @version 1.0 (11.12.2014)
 * @author  Heri
 */
@Component
public class MailTextResolver
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailTextResolver.class );

    private static final String KEY_PERSON_TEMP = "tempPersonInEnv";
    private static final String KEY_BODY_BY_TEMPLATE_FILE = "dyn.mail.body.bytemplatefile";
    public static final String KEY_EXTRA_MAIL_BODY_FILE = "person.extraMailBody.filename";
    public static final String KEY_EXTRA_MAIL_BODY_TEXT = "person.extraMailBody.text";


    @Autowired
    private ConfigurableEnvironment myEnv;

    private String myBodyKey;

    /**
     * Initializes the mail content configuration and provides the resolved body in the environment with
     * the key dyn.mail.body.bytemplatefile
     * <p>
     * A configured body template file (sdi.mail.body.template) takes precedence over the key
     * sdi.mail.body
     * <p>
     * @throws SdiException on any problem
     */
    public void init() throws SdiException
    {
        myBodyKey = MailProperties.KEY_BODY;

        if ( myEnv.containsProperty( MailProperties.KEY_BODY_TEMPLATE ) )
        {
            String charsetName;

            try
            {
                charsetName = myEnv.getRequiredProperty( MailProperties.KEY_BODY_TEMPLATE_CHARSET );
            }
            catch ( Throwable t )
            {
                throw new SdiException( "Property " + MailProperties.KEY_BODY_TEMPLATE + " is defined but "
                                        + "not " + MailProperties.KEY_BODY_TEMPLATE_CHARSET,
                                        t,
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            }

            Charset charset;

            try
            {
                charset = Charset.forName( charsetName );
            }
            catch ( Throwable t )
            {
                throw new SdiException( "Property " + MailProperties.KEY_BODY_TEMPLATE_CHARSET
                                        + " (" + charsetName + ") is invalid",
                                        t,
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            }

            String fileName = myEnv.getProperty( MailProperties.KEY_BODY_TEMPLATE );
            String body = loadFile( charset, fileName );
            myBodyKey = KEY_BODY_BY_TEMPLATE_FILE;
            ConfigUtils.addToEnvironment( myEnv, myBodyKey, body );

        } // if myEnv.containsProperty( MailProperties.BODY_TEMPLATE )
    }

    /**
     * @param aCharset
     * @param aFileName
     * @return
     * @throws SdiException
     */
    private String loadFile( Charset aCharset, String aFileName ) throws SdiException
    {
        File file = new File( aFileName );

        String body;
        try
        {
            myLog.debug( "Loading mail body from template file " + file.getCanonicalPath() );

            InputStream is = new FileInputStream( file );

            try
            {
                body = StreamUtils.copyToString( is, aCharset );
            }
            finally
            {
                is.close();
            }
            myLog.debug( "Loaded mail body from template file " + body );
        }
        catch ( Throwable t )
        {
            throw new SdiException( "Cannot load mail template " + aFileName,
                                    t,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }
        return body;
    }

    public String getResolvedBody( Person<?> aPerson ) throws SdiException
    {
        if ( aPerson.containsProperty( KEY_EXTRA_MAIL_BODY_FILE ) )
        {
            String key = KEY_EXTRA_MAIL_BODY_TEXT;

            try
            {
                String fileName = aPerson.getStringProperty( KEY_EXTRA_MAIL_BODY_FILE );
                String body = loadFile( Charset.forName( "UTF-8" ), fileName );
                ConfigUtils.addToEnvironment( myEnv, key, body );
                return resolveValue( aPerson, key );
            }
            finally
            {
                ConfigUtils.removeFromEnvironment( myEnv, key );
            }
        }

        return resolveValue( aPerson, myBodyKey );

    }

    public String getResolvedSubject( Person<?> aPerson ) throws SdiException
    {
        return resolveValue( aPerson, MailProperties.KEY_SUBJECT );
    }

    /**
     * @param aPerson
     * @param aValue
     * @return
     * @throws SdiException
     */
    private String resolveValue( Person<?> aPerson, String aKey ) throws SdiException
    {
        myLog.debug( "Resolving value for key " + aKey );
        MapPropertySource mps = new MapPropertySource( KEY_PERSON_TEMP, aPerson.getSource() );
        myEnv.getPropertySources().addFirst( mps );
        try
        {
            return myEnv.getRequiredProperty( aKey );
        }
        catch ( Throwable t )
        {
            throw new SdiException( "Problems loading property " + aKey + ". Original exception message:\n"
                                    + t.getMessage(),
                                    t,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );

        }
        finally
        {
            myEnv.getPropertySources().remove( KEY_PERSON_TEMP );
        }
    }

}
