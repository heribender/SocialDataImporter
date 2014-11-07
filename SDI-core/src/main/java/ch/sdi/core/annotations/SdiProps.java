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

package ch.sdi.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Interfaces and classes with this annotations describe property resources.<p>
 *
 * They are subject to the generic startup mechanisme which searches for user overloaded
 * property files. See ConfigHelper<p>
 *
 * The value parameter may indicate the name of the resource, without the extension ".properties".
 * If the parameter is empty, the class name is used as follows:
 *
 * <ul>
 *     <li> if class name ends with "Properties" this suffix will be truncated and replaced by ".properties"</li>
 *     <li> any other class name is used as is and suffixed with ".properties"</li>
 * </ul>
 *
 * @version 1.0 (04.11.2014)
 * @author  Heri
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SdiProps
{
    /**
     * The value may indicate the name of the resource, without the extension ".properties".
     * If empty, the class name is used.<p>
     *
     * @return the name of the property resource, if any, without extension ".properties"
     */
    String value() default "";

}
