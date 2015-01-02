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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiConverter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.FieldConverter;


/**
 * Converts a JPG image to a BufferedImage.
 * <p>
 * The input string is expected to be a hex dump of a valid jpg image.
 *
 * @version 1.0 (16.11.2014)
 * @author  Heri
 */
@SdiConverter( ConverterJpgFromHexDump.CONVERTER_NAME )
@Component
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConverterJpgFromHexDump extends ConverterImage
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterJpgFromHexDump.class );
    public static final String CONVERTER_NAME = "toJpgFromHexDump";

    /**
     * @see ch.sdi.core.intf.FieldConverter#init(org.springframework.core.env.Environment, java.lang.String)
     */
    @Override
    public FieldConverter<BufferedImage> init( Environment aEnv, String aFieldname ) throws SdiException
    {
        return this;
    }


    /**
     * @see ch.sdi.core.intf.FieldConverter#convert(java.lang.String)
     */
    @Override
    public BufferedImage convert( String aValue ) throws SdiException
    {
        if ( !StringUtils.hasText( aValue ) )
        {
            myLog.debug( "Given value is null" );
            return null;
        }

        byte[] binary;

        try
        {
            binary = Hex.decodeHex( aValue.toCharArray() );
        }
        catch ( DecoderException t )
        {
            throw new SdiException( "Cannot convert input value to binary array: " + aValue, t,
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        }

        ByteArrayInputStream bais = new ByteArrayInputStream( binary );

        return super.convert( bais );
    }

}
