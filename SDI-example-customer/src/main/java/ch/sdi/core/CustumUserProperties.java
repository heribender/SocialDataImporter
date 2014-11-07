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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import ch.sdi.core.impl.cfg.ConfigHelper;
import ch.sdi.core.intf.ParserMappingProperties;
import ch.sdi.core.intf.cfg.SdiProperties;


/**
 * Ensures that the custom.properties is examined before all other ResourcePropertySource
 * <p>
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@Component
public class CustumUserProperties implements ParserMappingProperties
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CustumUserProperties.class );
    /** */
    private static final String CUSTOM_PROPERTIES_FILE_NAME =
            SdiProperties.USER_OVERRIDE_PREFIX +
            ConfigHelper.makePropertyResourceName( ParserMappingProperties.class );

    @Autowired
    private ConfigurableEnvironment  env;

    // TODO: check if this is needed:
//    @Bean(name=ParserMappingProperties.BEAN_NAME)
//    public static ParserMappingProperties instance()
//    {
//        return new CustomUserProperties();
//    }

    /**
     * Constructor
     *
     */
    public CustumUserProperties()
    {
        super();
    }

    /**
     * @see ch.sdi.core.intf.ParserMappingProperties#override()
     */
    public void override()
    {
        MutablePropertySources propertySources = env.getPropertySources();
        myLog.debug( propertySources );
        PropertySource<?> mine = null;
        PropertySource<?> firstOther = null;
        for ( PropertySource<?> propertySource : propertySources )
        {
            if ( propertySource.getName().contains( CUSTOM_PROPERTIES_FILE_NAME ) )
            {
                mine = propertySource;
            } // if propertySource.getName().contains( NENA1_PROPERTIES_FILE_NAME )
            else if ( firstOther == null && propertySource instanceof ResourcePropertySource )
            {
                firstOther = propertySource;
            } // if propertySource instanceof ResourcePropertySource

            if ( mine != null && firstOther != null )
            {
                break;
            } // if mine != null && firstOther != null

        }

        if ( mine == null )
        {
            myLog.warn( CUSTOM_PROPERTIES_FILE_NAME + " could not be loaded as PropertySource" );
            return;
        } // if is == null

        // ensure that our property source is before all other ResourcePropertySource's
        if ( firstOther != null )
        {
            propertySources.remove( mine.getName() );
            propertySources.addBefore( firstOther.getName(), mine );
        } // if firstOther != null
    }

}
