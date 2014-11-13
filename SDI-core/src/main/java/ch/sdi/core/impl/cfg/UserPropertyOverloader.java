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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiProps;
import ch.sdi.core.intf.cfg.SdiMainProperties;
import ch.sdi.core.util.ClassUtil;


/**
 * Overloads the already loaded properties with properties found in property files on the classpath
 * whose filenam follows this pattern "user.<name>.properties", where name is one of the property
 * file names of the configuration classes which are annotated with @SdiProps (see {@link SdiProps}
 * for more information).<p>
 *
 *
 * @version 1.0 (07.11.2014)
 * @author  Heri
 */
@Component
public class UserPropertyOverloader
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( UserPropertyOverloader.class );
    @Autowired
    private ConfigurableEnvironment  env;

    /**
     * Constructor
     *
     */
    public UserPropertyOverloader()
    {
        super();
    }

    public void overrideByUserProperties()
    {
        List<Class<?>> candidates = findCandidates();

        for ( Class<?> clazz : candidates )
        {
            myLog.debug( "candidate for user property overloading: " + clazz.getName() );
            String fileName = SdiMainProperties.USER_OVERRIDE_PREFIX +
                    ConfigHelper.makePropertyResourceName( clazz );
            InputStream is = this.getClass().getResourceAsStream( "/" + fileName );

            if ( is == null )
            {
                myLog.debug( "Skipping non existing user overloading property file: " + fileName  );
                continue;
            } // if is == null

            myLog.debug( "Found overloading property file: " + fileName  );
            Properties props = new Properties();
            try
            {
                props.load( is );
            }
            catch ( IOException t )
            {
                myLog.error( "Problems loading property file " + fileName );
            }

            props.stringPropertyNames().stream()
//                .filter(key ->
//                {
//                    if ( key instanceof String )
//                    {
//                        return true;
//                    }
//
//                    myLog.warn( "key is not instance of String, but " + key.getClass().getName() );
//                    return false;
//                })
                .map( key ->
                {
                    String origValue = env.getProperty( key );
                    String result = "Key " + key + ": ";
                    return ( origValue == null || origValue.isEmpty() )
                            ? result + "No default value found. Adding new value to environment: \""
                                     + props.getProperty( key ) + "\""
                            : result + "Overriding default value \"" + origValue + "\" with new value: \""
                                     + props.getProperty( key ) + "\"";
                })
                .forEach( msg -> myLog.debug( msg ) ) ;

            String name = clazz.getSimpleName();
            PropertySource<?> ps = new PropertiesPropertySource( name, props );
            MutablePropertySources mps = env.getPropertySources();
            if ( mps.get( ConfigHelper.PROP_SOURCE_NAME_CMD_LINE ) != null )
            {
                mps.addAfter( ConfigHelper.PROP_SOURCE_NAME_CMD_LINE, ps );
            }
            else
            {
                mps.addFirst( ps );
            }
            myLog.debug( "PropertySources after adding overloading: " + mps );
        }

    }

    /**
     * @return
     */
    private List<Class<?>> findCandidates()
    {
        List<Class<?>> result = new ArrayList<Class<?>>();

        // we parse all classes which are below our top level package:
        String pack = this.getClass().getPackage().getName();
        myLog.debug( "found package: " + pack );
        pack = pack.replace( '.', '/' );
        String defaultRoot = pack.split( "/" )[0];

        result.addAll( ClassUtil.findCandidatesByAnnotation( SdiProps.class, defaultRoot ) );

        String newRoot = env.getProperty( SdiMainProperties.KEY_SDI_PROPERTIESOVERRIDE_INCLUDEROOT );
        if ( StringUtils.hasText( newRoot ) )
        {
            String[] newRoots = newRoot.split( "," );

            for ( int i = 0; i < newRoots.length; i++ )
            {
                if ( newRoots[i].equals( defaultRoot ) )
                {
                    continue;
                }

                result.addAll( ClassUtil.findCandidatesByAnnotation( SdiProps.class, newRoots[i] ) );
            }
        }

        return result;
    }

}
