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

package ch.sdi.core.impl.cfg;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiProps;
import ch.sdi.core.exc.SdiException;


/**
 * Provides some static methods for dealing with the configuration.
 * <p>
 *
 * @version 1.0 (04.11.2014)
 * @author Heri
 */
@Component
public class ConfigUtils
{

    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( ConfigUtils.class );
    public static final String PROP_SOURCE_NAME_CMD_LINE = "CommandLineArguments";
    private static final String PROP_SOURCE_NAME_DYNAMIC = "DynamicProperties";
    public static final String KEY_PROP_OUTPUT_DIR = "dynamic.outputDir.file";

    // must be manually injected since static!
    private static ConversionService  myConversionService;

    /**
     * Converts the given classname into a properties file name according following rules:
     *
     * <ul>
     *     <li> if class is annotated with SdiProps the value of the annotation is used, suffixed by
     *          ".properties" (unless already present).</li>
     *     <li>if annotation is missing or no value configured:</li>
     *     <ul>
     *         <li> if class name ends with "Properties" this suffix will be truncated and replaced by
     *         ".properties"</li>
     *         <li> any other class name is used as is and suffixed with ".properties"</li>
     *     </ul>
     * </ul>
     *
     */
    public static String makePropertyResourceName( Class<?> aClass )
    {
        String found = null;

        SdiProps ann = aClass.getAnnotation( SdiProps.class );

        if ( ann != null )
        {
            found = ann.value();
        } // if ann != null

        if ( StringUtils.hasText( found ) )
        {
            if ( found.endsWith( ".properties" ) )
            {
                return found;
            } // if condition

            return found + ".properties";
        }

        String classname = aClass.getSimpleName();

        if ( classname.endsWith( "Properties" ) || classname.endsWith( "properties" ) )
        {
            return classname.substring( 0, classname.length() - "Properties".length() ) + ".properties";
        } // if condition

        return classname + ".properties";

    }

    /**
     * Tries to read the value from the given environment and to convert it to an int.
     * <p>
     * If the converstion fails, an excption is thrown.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @param aDefault
     * @return
     * @throws SdiException
     */
    public static int getIntProperty( Environment aEnv, String aKey ) throws SdiException
    {

        try
        {
            checkConversionServiceInitialized();
            return myConversionService.convert( aEnv.getRequiredProperty( aKey ), Integer.class );
        }
        catch ( Throwable t )
        {
            throw new SdiException( "No integer value found for property "+ aKey,
                                    t,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }
    }

    /**
     * @throws SdiException
     *
     */
    private static void checkConversionServiceInitialized() throws SdiException
    {
        if ( myConversionService == null )
        {
            myLog.warn( "ConversionService not initialized" );
            // Maybe forgotten in a unit test? Note: Without this check a forgotten injection in a unit
            // test would hardly be noticed if you get - e.g. - a int property with a default value
            throw new SdiException( "ConversionService not initialized", SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if myConversionService == null

    }

    /**
     * Tries to read the value from the given environment and to convert it to an int.
     * <p>
     * If the converstion fails, the default value is returned.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @param aDefault
     * @return
     */
    public static int getIntProperty( Environment aEnv, String aKey, int aDefault )
    {
        try
        {
            checkConversionServiceInitialized();
            return myConversionService.convert( aEnv.getRequiredProperty( aKey ), Integer.class );
        }
        catch ( Throwable t )
        {
            return aDefault;
        }
    }

    /**
     * Tries to read the value from the given environment.
     * <p>
     * If the converstion fails, an excption is thrown.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @return
     * @throws SdiException
     */
    public static String getStringProperty( Environment aEnv,
                                            String aKey ) throws SdiException
    {
        try
        {
            return aEnv.getRequiredProperty( aKey );
        }
        catch ( Throwable t )
        {
            throw new SdiException( "No value found for property "+ aKey,
                                    t,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }
    }

    /**
     * Tries to read the value from the given environment and to convert it to a boolean.
     * <p>
     * If the conversion fails, an excption is thrown.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @return
     * @throws SdiException
     */
    public static boolean getBooleanProperty( Environment aEnv,
                                              String aKey ) throws SdiException
    {
        try
        {
            checkConversionServiceInitialized();
            return myConversionService.convert( aEnv.getRequiredProperty( aKey ), Boolean.class );
        }
        catch ( Throwable t )
        {
            throw new SdiException( "No boolean value found for property "+ aKey,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }
    }

    /**
     * Tries to read the value from the given environment and to convert it to a boolean.
     * <p>
     * If the converstion fails, the default value is returned.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @param aDefault
     * @return
     */
    public static boolean getBooleanProperty( Environment aEnv,
                                              String aKey,
                                              boolean aDefault )
    {
        try
        {
            return getBooleanProperty( aEnv, aKey );
        }
        catch ( Throwable t )
        {
            return aDefault;
        }
    }

    /**
     * @param aEnv
     * @param aKey
     * @param aValue
     * @throws SdiException
     */
    public static void addToEnvironment( ConfigurableEnvironment aEnv,
                                         String aKey,
                                         Object aValue )
            throws SdiException
    {
        Map<String, Object> map = getOrCreatePropertySource( aEnv, PROP_SOURCE_NAME_DYNAMIC );

        myLog.debug( "setting property " + aKey + " = " + aValue + " into the environment" );
        map.remove( aKey );
        map.put( aKey, aValue );
    }

    /**
     * Tries to retrieve the named PropertySource from the environment. If not found it creates a
     * new one.
     * <p>
     *
     * @param aEnv
     * @param aPropertySourceName
     * @return the embedded property map
     * @throws SdiException if the found property source is not instance of PropertiesPropertySource
     */
    public static Map<String, Object> getOrCreatePropertySource( ConfigurableEnvironment aEnv,
                                                                 String aPropertySourceName ) throws SdiException
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
            if ( !( ps instanceof PropertiesPropertySource ) )
            {
                throw new SdiException( "Found property source is not instance of PropertiesPropertySource "
                                        + " but: " + ps.getClass().getName(),
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            } // if !( ps instanceof PropertiesPropertySource )
            Assert.assertTrue( ps instanceof PropertiesPropertySource );
            pps = (PropertiesPropertySource) ps;
        }

        Map<String,Object> map = pps.getSource();
        return map;
    }


    /**
     * Injects springs conversion service
     * <p>
     * @param  aConversionService
     *         myConversionService to set
     */
    public static void setConversionService( ConversionService aConversionService )
    {
        myConversionService = aConversionService;
    }

    /**
     * Assembles a set of property names starting with the given prefix.
     * <p>
     * The resulting set is sorted according the natural ordering of the full property name.
     * <p>
     * @param aEnv
     * @param aKeyPrefix
     * @return
     */
    public static Collection<String> getPropertyNamesStartingWith( ConfigurableEnvironment aEnv,
                                                                   String aKeyPrefix )
    {
        return getAllPropertyNames( aEnv ).stream()
            .filter( key -> key.startsWith( aKeyPrefix ) )
            .collect( Collectors.toCollection( (Supplier<Set<String>>) TreeSet::new ) );
    }

    /**
     * Returns a collection of all property names in the environment.
     * <p>
     * @param aEnv
     * @return
     */
    public static Collection<String> getAllPropertyNames( ConfigurableEnvironment aEnv )
    {
        Collection<String> result = new HashSet<>();
        aEnv.getPropertySources().forEach( ps -> result.addAll( getAllPropertyNames( ps ) ) );
        return result;
    }

    /**
     * Returns a collection of all property names in the given PropertySource.
     * <p>
     * @param aPropSource
     * @return
     */
    public static Collection<String> getAllPropertyNames( PropertySource<?> aPropSource )
    {
        Collection<String> result = new HashSet<>();

        if ( aPropSource instanceof CompositePropertySource)
        {
            CompositePropertySource cps = (CompositePropertySource) aPropSource;
            cps.getPropertySources().forEach( ps -> result.addAll( getAllPropertyNames( ps ) ) );
            return result;
        }

        if ( aPropSource instanceof EnumerablePropertySource<?> )
        {
            EnumerablePropertySource<?> ps = (EnumerablePropertySource<?>) aPropSource;
            Arrays.asList( ps.getPropertyNames() ).forEach( key -> result.add( key ) );
            return result;
        }

        // note: Most descendants of PropertySource are EnumerablePropertySource. There are some
        // few others like JndiPropertySource or StubPropertySource
        myLog.debug( "Given PropertySource is instanceof " + aPropSource.getClass().getName()
                     + " and cannot be iterated" );

        return result;
    }

}
