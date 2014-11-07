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



package ch.sdi.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;


/**
 * TODO
 *
 * @version 1.0 (06.11.2014)
 * @author  Heri
 */
public class TestUtils
{


    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( TestUtils.class );

    /**
     * @param aEnv
     * @return
     */
    public static void debugPropertySources( ConfigurableEnvironment aEnv )
    {
        MutablePropertySources propertySources = aEnv.getPropertySources();
        for ( PropertySource<?> propertySource : propertySources )
        {
            myLog.debug( "PropertySource: " + propertySource );
        }
    }

}
