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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;


/**
 * Utilites for tests
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
     * Removes all key/value pairs from environment
     * @param aEnv
     */
    public static void removeAllFromEnvironment( ConfigurableEnvironment aEnv )
    {
        MutablePropertySources propertySources = aEnv.getPropertySources();
        List<PropertySource<?>> toRemove = new ArrayList<PropertySource<?>>( );
        propertySources.forEach( p -> toRemove.add( p ) );
        toRemove.forEach( p -> propertySources.remove( p.getName() ) );
    }

    /**
     * Adds the given key/value pair to the environment into the PropertySourceForTest collection
     *
     * @param aEnv the environment
     * @param aKey the key
     * @param aValue the value
     */
    public static void addToEnvironment( ConfigurableEnvironment aEnv, String aKey, String aValue )
            throws SdiException
    {
        Map<String, Object> map = ConfigUtils.getOrCreatePropertySource( aEnv, TEST_PROPERTY_SOURCE_NAME );

        myLog.debug( "setting property " + aKey + " = " + aValue + " into the environment" );
        map.remove( aKey );
        map.put( aKey, aValue );
    }

    /**
     * Removes the given key from the environments collection PropertySourceForTest
     *
     * @param aEnv the environment
     * @param aKey the key
     */
    public static void removeFromEnvironment( ConfigurableEnvironment aEnv, String aKey )
            throws SdiException
    {
        Map<String, Object> map = ConfigUtils.getOrCreatePropertySource( aEnv, TEST_PROPERTY_SOURCE_NAME );

        myLog.debug( "removing property " + aKey + " from the environment" );
        map.remove( aKey );
    }

    /**
     * Replaces the value of given key in the environments collection PropertySourceForTest
     *
     * @param aEnv the environment
     * @param aKey the key
     * @param aValue the new value
     */
    public static void replaceInEnvironment( ConfigurableEnvironment aEnv, String aKey, String aValue )
            throws SdiException
    {
        Map<String, Object> map = ConfigUtils.getOrCreatePropertySource( aEnv, TEST_PROPERTY_SOURCE_NAME );

        myLog.debug( "replacing property " + aKey + " in the environment. New value: " + aValue  );
        map.remove( aKey );
        map.put( aKey, aValue );
    }

    /**
     * Outputs the environment to log output
     * @param aEnv
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
