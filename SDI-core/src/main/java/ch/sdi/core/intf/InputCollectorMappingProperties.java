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

package ch.sdi.core.intf;

import ch.sdi.core.annotations.SdiProps;
import ch.sdi.core.impl.cfg.ConfigHelper;


/**
 * Configuration interface for the InputCollectorMapping.properties.<p>
 *
 * The expected property names are defined in {@link ch.sdi.core.impl.data.Person}.<p>
 *
 * Because this interface is annotated with @SdiProps it is subject to the automatic mechanisme for
 * overloading default properties with user defined properties (see {@link ch.sdi.core.annotations.SdiProps}).
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@SdiProps()
public interface InputCollectorMappingProperties extends SdiProperties
{

    public static final String RESOURCE_NAME =
            ConfigHelper.makePropertyResourceName( InputCollectorMappingProperties.class );
    public static final String KEY_PREFIX = "inputcollector.";

}
