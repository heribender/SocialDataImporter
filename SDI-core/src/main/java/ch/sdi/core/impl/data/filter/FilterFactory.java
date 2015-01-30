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


package ch.sdi.core.impl.data.filter;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiFilter;
import ch.sdi.core.exc.SdiException;


/**
 * Factory for instantiating the configured CollectFilters
 *
 * <p>
 *
 * @version 1.0 (30.01.2015)
 * @author  Heri
 */
@Component
public class FilterFactory
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( FilterFactory.class );


    @Autowired
    private ApplicationContext myAppCtxt;

    /**
     * Instantiates the filter described by the given param.
     * <p>
     * The param is expected to be:
     * <pre>
     *     &lt;filterName>:&lt;fieldname>[:additionalParams]
     * </pre>
     * The number of additional parameters is dependant of the particular filter type.
     * <p>
     * @param aParams
     * @return the initialized filter
     * @throws SdiException on any problem
     */
    public CollectFilter<?> getFilter( String aParams ) throws SdiException
    {
        if ( !StringUtils.hasText( aParams ) )
        {
            throw new SdiException( "Given param is empty!",
                                    SdiException.EXIT_CODE_CONFIG_ERROR );

        }

        String[] values = aParams.split( ":", 3 );

        if ( values.length < 2 )
        {
            throw new SdiException( "Given param does not contain at least 2 parts: " + aParams,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }

        String filterName = values[0];
        String fieldName = values[1];
        String params = values.length == 3 ? values[2] : null;

        myLog.debug( "Looking up filter " + filterName + " for field " + fieldName );

        Map<String, Object>  beans = myAppCtxt.getBeansWithAnnotation( SdiFilter.class );

        myLog.trace( "Found candidates for filters: " + beans.keySet() );

        for ( Object bean : beans.values() )
        {
            SdiFilter annotation = bean.getClass().getAnnotation( SdiFilter.class );
            String value = annotation.value();

            if ( filterName.equals( value ) )
            {
                if ( !( bean instanceof CollectFilter ) )
                {
                    throw new SdiException( "Found Bean annotated with @SdiFilter which is not a CollectFilter, but: "
                            + bean.getClass().getName(),
                            SdiException.EXIT_CODE_CONFIG_ERROR );
                }

                CollectFilter<?> filter = (CollectFilter<?>) bean;

                return filter.init( fieldName, params );
            }
        }

        throw new SdiException( "No filter found for filtername " + filterName,
                                SdiException.EXIT_CODE_CONFIG_ERROR );
    }


}
