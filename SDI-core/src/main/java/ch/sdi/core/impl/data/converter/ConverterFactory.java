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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiConverter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.FieldConverter;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * TODO
 *
 * @version 1.0 (09.11.2014)
 * @author  Heri
 */
@Component
public class ConverterFactory
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterFactory.class );

    @Autowired
    private ApplicationContext myAppCtxt;

    /**
     * @param aFieldnames
     * @return
     * @throws SdiException
     */
    public List<FieldConverter<?>> getFieldConverters( Collection<String> aFieldnames ) throws SdiException
    {
        List<FieldConverter<?>> result = new ArrayList<FieldConverter<?>>();

        for ( String fieldname : aFieldnames )
        {
            FieldConverter<?> converter = getConverterFor( fieldname );
            myLog.trace( "Converter for field " + fieldname + ": " + converter.getClass().getSimpleName() );
            result.add( converter );
        }

        return result;
    }

    public FieldConverter<?> getConverterFor( String aFieldname ) throws SdiException
    {
        Assert.hasLength( aFieldname, "Parameter fieldname is empty" );

        myLog.debug( "Looking up converter for field " + aFieldname );

        Environment env = myAppCtxt.getEnvironment();
        String converterName = env.getProperty( SdiMainProperties.KEY_PREFIX_CONVERTER + aFieldname );

        if ( !StringUtils.hasText( converterName ) )
        {
            return new ConverterString();
        } // if !StringUtils.hasText( converterName )

        myLog.debug( "Looking up converter with name " + converterName );

        Map<String, Object>  beans = myAppCtxt.getBeansWithAnnotation( SdiConverter.class );

        for ( Object bean : beans.values() )
        {
            if ( !( bean instanceof FieldConverter ) )
            {
                throw new SdiException( "Bean annotated with @SdiConverter which is not a FieldConverter, but: "
                        + bean.getClass().getName(),
                        SdiException.EXIT_CODE_CONFIG_ERROR );
            } // if !bean instanceof FieldConverter

            FieldConverter<?> converter = (FieldConverter<?>) bean;

            SdiConverter annotation = bean.getClass().getAnnotation( SdiConverter.class );
            String value = annotation.value();

            if ( converterName.equals( value ) )
            {
                return converter.init( env, aFieldname );
            } // if converterName.equals( value )
        }

        throw new SdiException( "Converter " + converterName + " for field '" + aFieldname + "' not found",
                                SdiException.EXIT_CODE_CONFIG_ERROR );
    }



}
