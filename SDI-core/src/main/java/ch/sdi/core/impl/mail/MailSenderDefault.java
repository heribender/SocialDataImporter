/**
 * Copyright (c) 2014 Nena1.ch. All rights reserved.
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
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

import ch.sdi.core.impl.cfg.ConfigHelper;
import ch.sdi.core.intf.MailProperties;
import ch.sdi.core.intf.mail.MailSender;


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
        aMail.setHostName( env.getRequiredProperty( MailProperties.HOST ) );
        aMail.setSmtpPort( ConfigHelper.getIntProperty( env, MailProperties.PORT, 25 ) );
        aMail.setSslSmtpPort( env.getProperty( MailProperties.HOST ) );
        aMail.setAuthenticator(new DefaultAuthenticator( env.getProperty( MailProperties.SMTP_USER ),
                                                         env.getProperty( MailProperties.SMTP_PASSWORD ) ) );
        aMail.setSSLOnConnect( ConfigHelper.getBooleanProperty( env, MailProperties.SSL_ON_CONNECT, false ) );
        aMail.setStartTLSRequired( ConfigHelper.getBooleanProperty( env, MailProperties.START_TLS_REQUIRED, false ) );
        aMail.setFrom( env.getRequiredProperty( MailProperties.SENDER_ADDRESS ) );
        aMail.send();
        myLog.debug( "mail successfully sent" );
    }
}
