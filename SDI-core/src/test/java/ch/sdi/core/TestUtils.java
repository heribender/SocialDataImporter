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

package ch.sdi.core;

import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
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

    private static final String TEST_PROPERTY_SOURCE_NAME = "PropertySourceForTest";

    /**
     * @param aEnv
     * @param aKey
     * @param aValue
     */
    public static void addToEnvironment( ConfigurableEnvironment aEnv, String aKey, String aValue )
    {
        Map<String, Object> map = getOrCreatePropertySource( aEnv, TEST_PROPERTY_SOURCE_NAME );

        myLog.debug( "setting property " + aKey + " = " + aValue + " into the environment" );
        map.remove( aKey );
        map.put( aKey, aValue );
    }

    public static void removeFromEnvironment( ConfigurableEnvironment aEnv, String aKey )
    {
        Map<String, Object> map = getOrCreatePropertySource( aEnv, TEST_PROPERTY_SOURCE_NAME );

        myLog.debug( "removing property " + aKey + " from the environment" );
        map.remove( aKey );
    }

    public static void replaceInEnvironment( ConfigurableEnvironment aEnv, String aKey, String aValue )
    {
        Map<String, Object> map = getOrCreatePropertySource( aEnv, TEST_PROPERTY_SOURCE_NAME );

        myLog.debug( "replacing property " + aKey + " in the environment. New value: " + aValue  );
        map.remove( aKey );
        map.put( aKey, aValue );
    }

    /**
     * @param aEnv
     * @param aPropertySourceName
     * @return
     */
    private static Map<String, Object> getOrCreatePropertySource( ConfigurableEnvironment aEnv,
                                                                  String aPropertySourceName )
    {
        MutablePropertySources mps = aEnv.getPropertySources();
        PropertySource<?> ps = mps.get( aPropertySourceName );
        PropertiesPropertySource pps = null;

        if ( ps == null )
        {
            Properties props = new Properties();
            pps = new PropertiesPropertySource( aPropertySourceName, props );
            mps.addFirst( pps );
        }
        else
        {
            Assert.assertTrue( ps instanceof PropertiesPropertySource );
            pps = (PropertiesPropertySource) ps;
        }

        Map<String,Object> map = pps.getSource();
        return map;
    }

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
