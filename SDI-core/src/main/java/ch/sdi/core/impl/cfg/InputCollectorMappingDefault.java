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

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ch.sdi.core.intf.InputCollectorMappingProperties;


/**
 * Configuration class for loading the default InputCollectorMapping.properties file.<p>
 *
 * Because the implemented interface is annotated with @SdiProps it is subject to the automatic mechanisme for
 * overloading default properties with user defined properties (see {@link ch.sdi.core.annotations.SdiProps}).
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@Configuration
@PropertySource("classpath:/" + "InputCollectorMapping.properties" )
// more than one @PropertySources(value = {@PropertySource("classpath:/datasource.properties")})
public class InputCollectorMappingDefault implements InputCollectorMappingProperties
{
}
