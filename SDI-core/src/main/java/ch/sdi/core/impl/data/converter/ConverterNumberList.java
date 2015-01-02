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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiConverter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.FieldConverter;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * FieldConverter derivation which can convert string number lists to a List of Number instances.
 * <p>
 * The delimiter of the string number list can be configured. If not configured, the delimiter defaults
 * to '/'.
 *
 * @version 1.0 (29.11.2014)
 * @author  Heri
 */
@SdiConverter( ConverterNumberList.CONVERTER_NAME )
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConverterNumberList implements FieldConverter<List<Number>>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterNumberList.class );

    public static final String CONVERTER_NAME = "toNumberList";
    public static final String DELIMITER_SUFFIX = ".delimiter";

    private String myDelimiter;

    /**
     * @see ch.sdi.core.intf.FieldConverter#init(org.springframework.core.env.Environment, java.lang.String)
     */
    @Override
    public FieldConverter<List<Number>> init( Environment aEnv, String aFieldname ) throws SdiException
    {
        String delimiter = aEnv.getProperty( SdiMainProperties.KEY_PREFIX_CONVERTER
                                           + CONVERTER_NAME + "." + aFieldname + DELIMITER_SUFFIX );
         if ( !StringUtils.hasText( delimiter ) )
         {
             myLog.trace( "No delimiter found for field " + aFieldname + ". Looking for a generic delimiter" );
             delimiter = aEnv.getProperty( SdiMainProperties.KEY_PREFIX_CONVERTER
                                        + CONVERTER_NAME + DELIMITER_SUFFIX  );
         } // if !StringUtils.hasText( pattern )

         if ( !StringUtils.hasText( delimiter ) )
         {
             myLog.warn( "No delimiter found for field " + aFieldname + ". Setting to default '/'" );
             delimiter = "/";
         }
         else
         {
             delimiter = delimiter.trim();
             myLog.debug( "found delimiter for ConverterNumberList: " + delimiter );
         }

         setDelimiter( delimiter );
         return this;
    }

    /**
     * @see ch.sdi.core.intf.FieldConverter#convert(java.lang.String)
     */
    @Override
    public List<Number> convert( String aValue ) throws SdiException
    {
        if ( !StringUtils.hasText( aValue ) )
        {
            myLog.debug( "Given value is null" );
            return null;
        }

        return toList( aValue, myDelimiter );
    }

    public static List<Long> toLongList( String aStringArray,
                                         String aDelimiter ) throws SdiException
    {
        List<Long> result = new ArrayList<Long>();

        List<Number> list = toList( aStringArray, aDelimiter );
        list.stream().forEach( n -> result.add( n.longValue() ) );

        return result;
    }

    /**
     * @param aStringArray
     * @return
     * @throws SdiException
     */
    public static List<Number> toList( String aStringArray, String aDelimiter ) throws SdiException
    {
        List<Number> result = new ArrayList<Number>();

        if ( !StringUtils.hasText( aStringArray ) )
        {
            return result;
        }

        String[] items = aStringArray.trim().split( aDelimiter );

        for ( String item : items )
        {
            try
            {
                result.add( Long.parseLong( item.trim() ) );
            }
            catch ( NumberFormatException t )
            {
                throw new SdiException( "Item " + item + " cannot be converted to number",
                                        t,
                                        SdiException.EXIT_CODE_PARSE_ERROR );
            }
        }
        return result;
    }


    /**
     * @return delimiter
     */
    public String getDelimiter()
    {
        return myDelimiter;
    }


    /**
     * @param  aDelimiter
     *         delimiter to set
     */
    public void setDelimiter( String aDelimiter )
    {
        myDelimiter = aDelimiter;
    }

}
