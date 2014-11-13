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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiConverter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.cfg.SdiMainProperties;
import ch.sdi.core.intf.data.FieldConverter;


/**
 * Converter for converting parsed strings into a Date object.
 * <p>
 * This converter needs a parameter pattern to be configured in the environment:
 * <pre>
 *    sdi.converter.toDate.pattern =
 *    or
 *    sdi.converter.toDate.birthday.pattern =
 * </pre>
 * The latter variant is the pattern for the field birthday, whereas the first variant is injected
 * if the field does not specify a pattern.
 * <p>
 * Note that the converter is annotated with Scope=Prototype to ensure that a new instance of the
 * converter is created each time a ctxt.getBean(() (or one of its variant) is called. The reason is that
 * different field in the same parsed input might need different convert patterns.
 * <p>
 *
 * @version 1.0 (09.11.2014)
 * @author  Heri
 */
@SdiConverter( ConverterDate.CONVERTER_NAME )
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConverterDate implements FieldConverter<Date>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterDate.class );

    public static final String CONVERTER_NAME = "toDate";
    public static final String PATTERN_SUFFIX = ".pattern";

    private String myDatePattern;

    /**
     * Constructor
     *
     */
    public ConverterDate()
    {
        super();
    }

    /**
     * @see ch.sdi.core.intf.data.FieldConverter#init(org.springframework.core.env.Environment, java.lang.String)
     */
    @Override
    public ConverterDate init( Environment aEnv, String aFieldname ) throws SdiException
    {
        String pattern = aEnv.getProperty( SdiMainProperties.KEY_PREFIX_CONVERTER
                                          + CONVERTER_NAME + "." + aFieldname + PATTERN_SUFFIX );
        if ( !StringUtils.hasText( pattern ) )
        {
            myLog.trace( "No pattern found for field " + aFieldname + ". Looking for a generic pattern" );
            pattern = aEnv.getProperty( SdiMainProperties.KEY_PREFIX_CONVERTER
                                       + CONVERTER_NAME + PATTERN_SUFFIX  );
        } // if !StringUtils.hasText( pattern )

        pattern = pattern.trim();

        myLog.debug( "found pattern for ConverterDate: " + pattern );
        setDatePattern( pattern );
        return this;
    }

    /**
     * @see ch.sdi.core.intf.data.FieldConverter#convert(java.lang.String)
     */
    @Override
    public Date convert( String aValue ) throws SdiException
    {
        if ( !StringUtils.hasText( aValue ) )
        {
            // TODO: make it configurable if this field is mandatory or not!
            return null;
        }

        DateFormat df;
        if ( StringUtils.hasText( myDatePattern ) )
        {
            df = new SimpleDateFormat( myDatePattern );
        }
        else
        {
            df = new SimpleDateFormat();
        } // if..else StringUtils.hasText( myPattern )

        Date result;

        try
        {
            result = df.parse( aValue );
        }
        catch ( ParseException t )
        {
            throw new SdiException( "Problems converting date: " + aValue
                                    + "; myDatePattern: " + myDatePattern,
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        }

        return result;
    }


    /**
     * @return datePattern
     */
    public String getDatePattern()
    {
        return myDatePattern;
    }


    /**
     * @param  aDatePattern
     *         datePattern to set
     */
    public void setDatePattern( String aDatePattern )
    {
        myDatePattern = aDatePattern;
    }

}