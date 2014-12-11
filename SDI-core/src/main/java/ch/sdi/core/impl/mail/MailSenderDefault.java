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

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.intf.MailProperties;
import ch.sdi.core.intf.MailSender;


/**
 * TODO
 *
 * @version 1.0 (06.11.2014)
 * @author  Heri
 */
@Component
public class MailSenderDefault implements MailSender<Email>
{

    @Autowired
    private ConfigurableEnvironment  env;


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailSenderDefault.class );

    @Override
    public void sendMail( Email aMail ) throws EmailException
    {
        aMail.setHostName( env.getRequiredProperty( MailProperties.KEY_HOST ) );
        aMail.setSmtpPort( ConfigUtils.getIntProperty( env, MailProperties.KEY_PORT, 25 ) );
        aMail.setSslSmtpPort( env.getProperty( MailProperties.KEY_HOST ) );
        aMail.setAuthenticator(new DefaultAuthenticator( env.getProperty( MailProperties.KEY_SMTP_USER ),
                                                         env.getProperty( MailProperties.KEY_SMTP_PASSWORD ) ) );
        aMail.setSSLOnConnect( ConfigUtils.getBooleanProperty( env, MailProperties.KEY_SSL_ON_CONNECT, false ) );
        aMail.setStartTLSRequired( ConfigUtils.getBooleanProperty( env, MailProperties.KEY_START_TLS_REQUIRED, false ) );
        aMail.setFrom( env.getRequiredProperty( MailProperties.KEY_SENDER_ADDRESS ) );
        aMail.send();
        myLog.debug( "mail successfully sent" );
    }
}
