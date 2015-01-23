/**
 * Copyright (c) 2015 by the original author or authors.
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

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiConverter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.FieldConverter;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * Converter for converting parsed strings into a Boolean object.
 * <p>
 * This converter has optional parameters;
 * <pre>
 *    sdi.converter.toBoolean.trueValues =
 *    or
 *    sdi.converter.toBoolean.falseValues =
 * </pre>
 * Both are comma separated lists of additional true/false values.
 * <p>
 * The converter uses springs ConversionService for the built in values:
 * True values: true, on, yes, 1
 * False values: false, off, no, 0
 * <p>
 * The parsed additional parameters are truncated and converted to lower case. This is also done for the
 * values passed to the convert method.
 *
 * @version 1.0 (23.01.2015)
 * @author  Heri
 */
@SdiConverter( ConverterBoolean.CONVERTER_NAME )
@Component
public class ConverterBoolean implements FieldConverter<Boolean>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterBoolean.class );

    public static final String CONVERTER_NAME = "toBoolean";
    public static final String KEY_TRUE_VALUES = "trueValues";
    public static final String KEY_FALSE_VALUES = "falseValues";

    @Autowired
    private ConversionService  myConversionService;
    private static final Set<String> trueValues = new HashSet<String>(4);
    private static final Set<String> falseValues = new HashSet<String>(4);

    /**
     * @see ch.sdi.core.intf.FieldConverter#init(org.springframework.core.env.Environment, java.lang.String)
     */
    @Override
    public FieldConverter<Boolean> init( Environment aEnv, String aFieldname ) throws SdiException
    {
        resolveConfiguredValues( aEnv, trueValues, KEY_TRUE_VALUES );
        resolveConfiguredValues( aEnv, falseValues, KEY_FALSE_VALUES );
        return this;
    }

    /**
     * Clears the given list and fills it with found configured values according the given key
     * <p>
     * @param aEnv
     * @param aValuesToFill
     * @param aKey
     *        either "trueValues" or "falseValues"
     */
    private void resolveConfiguredValues( Environment aEnv,
                                          Set<String> aValuesToFill,
                                          String aKey )
    {
        aValuesToFill.clear();
        String key = SdiMainProperties.KEY_PREFIX_CONVERTER + CONVERTER_NAME + "." + aKey;
        String values = aEnv.getProperty( key );
        if ( !StringUtils.hasText( values ) )
        {
            myLog.debug( "No configured values found for key " + key );
            return;
        } // if !StringUtils.hasText( values )

        String[] split = values.split( "," );
        for ( String value : split )
        {
            if ( !StringUtils.hasText( value ) )
            {
                continue;
            } // if !StringUtils.hasText( value )

            value = value.trim().toLowerCase();
            myLog.debug( "Found configured value (aKey): " + value );
            aValuesToFill.add( value );
        }
    }

    /**
     * @see ch.sdi.core.intf.FieldConverter#convert(java.lang.String)
     */
    @Override
    public Boolean convert( String aValue ) throws SdiException
    {
        if ( !StringUtils.hasText( aValue ) )
        {
            myLog.debug( "Given value is null" );
            return null;
        }

        String value = aValue.trim().toLowerCase();

        if ( trueValues.contains( value ) )
        {
            return Boolean.TRUE;
        } // if

        if ( falseValues.contains( value ) )
        {
            return Boolean.FALSE;
        } // if

        try
        {
            return myConversionService.convert( value, Boolean.class );
        }
        catch ( Throwable t )
        {
            throw new SdiException( "Problems converting boolean value " + aValue, t, SdiException.EXIT_CODE_CONFIG_ERROR );
        }
    }

}
