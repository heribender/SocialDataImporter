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


package ch.sdi.core.impl.data.converter;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.annotations.SdiConverter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.FieldConverter;


/**
 * Default converter for all collected values which do not have a special converter configured. In fact
 * this converter only convertes given null values into an empty string. It mainly is provided for
 * convenience (in order not to have null checks in iterations over the collected fields)
 * <p>
 *
 * @version 1.0 (09.11.2014)
 * @author  Heri
 */
@SdiConverter( "default" )
@Component
public class ConverterString implements FieldConverter<String>
{
    /**
     * @see ch.sdi.core.intf.FieldConverter#convert(java.lang.String)
     */
    @Override
    public String convert( String aValue ) throws SdiException
    {
        return aValue == null ? "" : aValue;
    }

    /**
     * @see ch.sdi.core.intf.FieldConverter#init(org.springframework.core.env.Environment, java.lang.String)
     */
    @Override
    public FieldConverter<String> init( Environment aEnv, String aFieldname ) throws SdiException
    {
        return this;
    }

}
