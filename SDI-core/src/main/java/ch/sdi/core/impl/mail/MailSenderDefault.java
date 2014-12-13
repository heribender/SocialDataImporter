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

import javax.mail.Authenticator;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.intf.MailProperties;
import ch.sdi.core.intf.MailSender;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * TODO
 *
 * @version 1.0 (06.11.2014)
 * @author  Heri
 */
@Component
public class MailSenderDefault implements MailSender<Email>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailSenderDefault.class );

    @Autowired
    private ConfigurableEnvironment myEnv;

    private String myHost;
    private int myPort;
    private Authenticator myAuthenticator;
    private boolean mySslOnConnect;
    private boolean myStartTlsRequired;
    private String mySenderAddress;
    private boolean myDryRun;
    private String myContentType;

    @Override
    public void sendMail( Email aMail ) throws SdiException
    {
        aMail.setHostName( myHost );
        if ( mySslOnConnect )
        {
            aMail.setSslSmtpPort( "" + myPort );
        }
        else
        {
            aMail.setSmtpPort( myPort );
        } // if..else mySslOnConnect

        aMail.setAuthenticator( myAuthenticator );
        aMail.setSSLOnConnect( mySslOnConnect );
        aMail.setStartTLSRequired( myStartTlsRequired );

        try
        {
            aMail.setFrom( mySenderAddress );
        }
        catch ( EmailException t )
        {
            throw new SdiException( "Problems setting the sender address to mail: " + mySenderAddress,
                                    t,
                                    SdiException.EXIT_CODE_MAIL_ERROR );
        }

        if ( myDryRun )
        {
            myLog.debug( "DryRun is set. Not sending the mail" );
            // TODO: save local in output dir
        }
        else
        {
            try
            {
                aMail.send();
                myLog.debug( "mail successfully sent" );
            }
            catch ( Throwable t )
            {
                throw new SdiException( "Problems sending a mail",
                                        t,
                                        SdiException.EXIT_CODE_MAIL_ERROR );
            }
        } // if..else myDryRun

    }

    public void init() throws SdiException
    {
        myDryRun = ConfigUtils.getBooleanProperty( myEnv, SdiMainProperties.KEY_DRYRUN, false );

        try
        {
            myHost = myEnv.getRequiredProperty( MailProperties.KEY_HOST );
            myPort = ConfigUtils.getIntProperty( myEnv, MailProperties.KEY_PORT, 25 );
            myAuthenticator = new DefaultAuthenticator( myEnv.getProperty( MailProperties.KEY_SMTP_USER ),
                                                        myEnv.getProperty( MailProperties.KEY_SMTP_PASSWORD ) );
            mySslOnConnect = ConfigUtils.getBooleanProperty( myEnv, MailProperties.KEY_SSL_ON_CONNECT, false );
            myStartTlsRequired = ConfigUtils.getBooleanProperty( myEnv,
                                                                 MailProperties.KEY_START_TLS_REQUIRED, false );
            mySenderAddress = myEnv.getRequiredProperty( MailProperties.KEY_SENDER_ADDRESS );
            myContentType = myEnv.getProperty( MailProperties.KEY_SENDER_ADDRESS, "text/plain" );

           myLog.info( "Mail-Configuration: "
                       + "\n    myHost            : " + myHost
                       + "\n    myPort            : " + myPort
                       + "\n    mySslOnConnect    : " + mySslOnConnect
                       + "\n    myStartTlsRequired: " + myStartTlsRequired
                       + "\n    mySenderAddress   : " + mySenderAddress
                       + "\n    myContentType     : " + myContentType

                       );
        }
        catch ( Throwable t )
        {
            throw new SdiException( "Problems initializing the mail parameters",
                                    t,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }

    }


}
