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
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import ch.sdi.core.impl.cfg.ConfigHelper;
import ch.sdi.core.intf.InputCollectorMappingProperties;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * Example how to completely replace the default properties which would be loaded by
 * InputCollectorMappingDefault if this class would not be present on the classpath (see
 * annotation @ConditionalOnMissingClass)
 * <p>
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@Component
@PropertySource("classpath:/CustomInputCollectorMapping.properties")
public class CustomInputCollectorMappingProperties implements InputCollectorMappingProperties
{
    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CustomInputCollectorMappingProperties.class );
    /** */
    public static final String CUSTOM_PROPERTIES_FILE_NAME =
            SdiMainProperties.USER_OVERRIDE_PREFIX +
            ConfigHelper.makePropertyResourceName( InputCollectorMappingProperties.class );

   /**
     * Constructor
     *
     */
    public CustomInputCollectorMappingProperties()
    {
        super();
        myLog.debug( this.getClass().getSimpleName() + " created" );
    }

}
