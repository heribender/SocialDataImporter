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



package ch.sdi.core.impl.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import ch.sdi.core.intf.ParserMappingProperties;


/**
 * TODO
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@Configuration
@PropertySource("classpath:/" + ParserMappingProperties.RESOURCE_NAME )
// more than one @PropertySources(value = {@PropertySource("classpath:/datasource.properties")})
public class ParserMapping
{
    /** */
    public static final String PARSER_USERNAMEKEY = "parser.usernamekey";

    @Autowired
    private Environment env;

    /**
     * @return usernameKey
     */
    public String getUsernameKey()
    {
        return env.getProperty( PARSER_USERNAMEKEY );
    }

}
