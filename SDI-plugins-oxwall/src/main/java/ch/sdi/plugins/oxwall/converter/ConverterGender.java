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


package ch.sdi.plugins.oxwall.converter;

import java.util.HashMap;
import java.util.Map;

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
 * TODO
 *
 * @version 1.0 (29.11.2014)
 * @author  Heri
 */
@SdiConverter( ConverterGender.CONVERTER_NAME )
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConverterGender implements FieldConverter<OxGender>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterGender.class );

    public static final String CONVERTER_NAME = "toGender";

    private Map<String,OxGender> myGenderMapping;

    /**
     * @see ch.sdi.core.intf.FieldConverter#init(org.springframework.core.env.Environment, java.lang.String)
     */
    @Override
    public FieldConverter<OxGender> init( Environment aEnv, String aFieldname ) throws SdiException
    {
        myGenderMapping = new HashMap<String,OxGender>();

        for ( OxGender gender : OxGender.values() )
        {
            String pattern = aEnv.getProperty( SdiMainProperties.KEY_PREFIX_CONVERTER
                                               + CONVERTER_NAME + "." + gender );
            if ( StringUtils.hasText( pattern ) )
            {
                pattern = pattern.trim();
                myLog.trace( "Gender mapping found for " + gender + ": " + pattern );
                myGenderMapping.put( pattern, gender );
            } // if StringUtils.hasText( pattern )
        }

        return this;
    }

    /**
     * @see ch.sdi.core.intf.FieldConverter#convert(java.lang.String)
     */
    @Override
    public OxGender convert( String aValue ) throws SdiException
    {
        OxGender result = myGenderMapping.get( aValue );

        if ( result != null )
        {
            return result;
        } // if result != null

        if ( StringUtils.hasText( aValue ) )
        {
            myLog.warn( "Unknown gender pattern received: " + aValue );
        } // if StringUtils.hasText( aValue )

        return OxGender.dontcare;
    }

}
