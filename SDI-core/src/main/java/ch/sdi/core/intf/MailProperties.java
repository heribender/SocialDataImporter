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



package ch.sdi.core.intf;

import ch.sdi.core.annotations.SdiProps;
import ch.sdi.core.impl.cfg.ConfigHelper;
import ch.sdi.core.intf.cfg.SdiProperties;


/**
 * TODO
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@SdiProps()
public interface MailProperties extends SdiProperties
{

    public static final String BEAN_NAME = MailProperties.class.getSimpleName();
    public static final String RESOURCE_NAME =
            ConfigHelper.makePropertyResourceName( MailProperties.class );


    public static final String SENDER_NAME = "mail.sender.name";
    public static final String SENDER_ADDRESS = "mail.sender.address";
    public static final String SUBJECT = "mail.subject";
    public static final String BODY = "mail.body";
    public static final String BODY_TEMPLATE = "mail.body.template";
    public static final String CONTENT_TYPE = "mail.contenttype";
    public static final String HOST = "mail.smtp.host";
    public static final String PORT = "mail.smtp.port";
    public static final String PORT_SSL = "mail.smtp.port.ssl";
    public static final String SMTP_USER = "mail.smtp.user";
    public static final String SMTP_PASSWORD = "mail.smtp.password";
    public static final String SSL = "mail.ssl";
    public static final String SSL_ON_CONNECT = "mail.ssl.onconnect";
    public static final String START_TLS_REQUIRED = "mail.start.tls.required";
    public static final String START_TLS_ENABLED = "mail.start.tls.enabled";

}
