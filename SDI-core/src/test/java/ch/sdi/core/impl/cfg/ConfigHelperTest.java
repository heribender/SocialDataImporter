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



package ch.sdi.core.impl.cfg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
 *
 * @version 1.0 (04.11.2014)
 * @author  Heri
 */
public class ConfigHelperTest
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConfigHelperTest.class );
    private ConfigHelper myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        myClassUnderTest = new ConfigHelper();
    }

    /**
     * Test method for {@link ch.sdi.core.impl.cfg.ConfigHelper#overrideByUserProperties()}.
     */
    @Test
    public void testOverrideByUserProperties()
    {
        myClassUnderTest.overrideByUserProperties();
    }

}
