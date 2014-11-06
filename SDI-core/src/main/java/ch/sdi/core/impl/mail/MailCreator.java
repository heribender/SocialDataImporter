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
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ch.sdi.core.impl.data.ImportDataItem;
import ch.sdi.core.intf.ParserMappingProperties;


/**
 * TODO
 *
 * @version 1.0 (02.11.2014)
 * @author Heri
 */
@Configuration
@PropertySource( "classpath:/" + ParserMappingProperties.RESOURCE_NAME )
public class MailCreator
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailCreator.class );

    public Email createMailFor( ImportDataItem aData )
    {
        // Recipient's email ID needs to be mentioned.
//        String to = "heri@lamp.vm";
        String to = "sdi-test@web.de";

        // Sender's email ID needs to be mentioned
        String from = "sdi-test@web.de";


        Email email = new SimpleEmail();
        email.setHostName("smtp.web.de");
        email.setSmtpPort( 25 );
        email.setSslSmtpPort( "587" );
        email.setAuthenticator(new DefaultAuthenticator("sdi-test", "SocialDataImporter"));
        email.setSSLOnConnect(false);
        email.setStartTLSRequired(true);
        try
        {
            email.setFrom( from );
            email.setSubject("TestMail");
            email.setMsg("This is a test mail ... :-)");
            email.addTo( to );
            email.send();
        }
        catch ( Exception t )
        {
            myLog.error( "Exception caught: ", t );
        }

        return email;


    }
}
