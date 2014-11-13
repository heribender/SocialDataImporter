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

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ch.sdi.core.intf.InputCollectorMappingProperties;


/**
 * Configuration class for loading the default InputCollectorMapping.properties file.<p>
 *
 * If you implement your own configuration class which implements {@link InputCollectorMappingProperties} and
 * provide it on the classpath defaults are not loaded. In this case you have to define all needed
 * properties for this configuration artifact.<p>
 *
 * Another way of overloading default properties is providing a same name properties file prefixed with
 * "user.". In this case the defaults are loaded and then overwritten by the properties found in your
 * user.xxx.properties file.<p>
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@ConditionalOnMissingClass(name="ch.sdi.core.intf.cfg.SdiProperties.InputCollectorMappingProperties" )
@Configuration
@PropertySource("classpath:/" + "InputCollectorMapping.properties" )
// more than one @PropertySources(value = {@PropertySource("classpath:/datasource.properties")})
public class InputCollectorMappingDefault implements InputCollectorMappingProperties
{
}
