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


package ch.sdi.plugins.oxwall.profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * For annotating custom profile question resolvers.
 * <p>
 * The custom question bean must fullfil following requirements:
 * <ul>
 *   <li> reside in or below package ch.sdi.plugins.oxwall and have a public </li>
 *   <li> extend base class OxProfileQuestion </li>
 *   <li> have a public constructor with two String parameters </li>
 * </ul>
 * The value (required) must equal the one used in the corresponding configuration value
 * "ox.target.qn.xxx"
 * <p>
 *
 * @version 1.0 (06.12.2014)
 * @author  Heri
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OxCustomQuestion
{
    /**
     * The value may indicate the name of the custom question bean, as configured in the corresponding
     * ox.target.qn.-property.<p>
     *
     * @return the name of the bean
     */
    String value();
}
