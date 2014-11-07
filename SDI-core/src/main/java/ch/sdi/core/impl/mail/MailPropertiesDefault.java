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

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ch.sdi.core.intf.MailProperties;
import ch.sdi.core.intf.ParserMappingProperties;


/**
 * Configuration class for loading the default mail.properties file.<p>
 *
 * If you implement your own configuration class which implements {@link MailProperties} and
 * provide it on the classpath defaults are not loaded. In this case you have to define all needed
 * properties for this configuration artifact.<p>
 *
 * Another way of overloading default properties is providing a same name properties file prefixed with
 * "user.". In this case the defaults are loaded and then overwritten by the properties found in your
 * user.xxx.properties file.<p>
 *
 * @version 1.0 (07.11.2014)
 * @author  Heri
 */
@ConditionalOnMissingClass(ParserMappingProperties.class )
@Configuration
@PropertySource("classpath:/" + "Mail.properties" )
public class MailPropertiesDefault implements MailProperties
{

}
