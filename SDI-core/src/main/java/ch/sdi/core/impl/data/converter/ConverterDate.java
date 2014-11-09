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
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiConverter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.cfg.SdiProperties;
import ch.sdi.core.intf.data.FieldConverter;


/**
 * TODO
 *
 * @version 1.0 (09.11.2014)
 * @author  Heri
 */
@SdiConverter( ConverterDate.CONVERTER_NAME )
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
        String pattern = aEnv.getProperty( SdiProperties.KEY_PREFIX_CONVERTER
                                          + CONVERTER_NAME + "." + aFieldname + PATTERN_SUFFIX );
        if ( !StringUtils.hasText( pattern ) )
        {
            myLog.trace( "No pattern found for field " + aFieldname + ". Looking for a generic pattern" );
            pattern = aEnv.getProperty( SdiProperties.KEY_PREFIX_CONVERTER
                                       + CONVERTER_NAME + PATTERN_SUFFIX  );
        } // if !StringUtils.hasText( pattern )

        myLog.debug( "found pattern for ConverterDate: " + pattern );
        try
        {
            // Clone the result
            ConverterDate result = (ConverterDate) this.clone();
            result.setDatePattern( pattern );
            return result;
        }
        catch ( CloneNotSupportedException t )
        {
            throw new SdiException( t, SdiException.EXIT_CODE_UNKNOWN_ERROR );
        }
    }

    /**
     * @see ch.sdi.core.intf.data.FieldConverter#convert(java.lang.String)
     */
    @Override
    public Date convert( String aValue ) throws SdiException
    {
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
