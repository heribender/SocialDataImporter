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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Dataset;


/**
 * Base class of specialized filters which are applied after having parsed the raw data of one person
 * during collection phase (before the field names get normalized).
 * <p>
 * Datasets which are filtered by this class are no more processed further (i.e. they are not imported
 * to the target platform).
 * <p>
 * Each filter requires at least one parameter: the name of the field which is examined.
 * <p>
 *
 * @version 1.0 (24.01.2015)
 * @author  Heri
 */
public abstract class CollectFilter<T>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CollectFilter.class );

    public static final String KEY_PREFIX_FILTER = "sdi.collect.filter.";

    private String myFieldName;

    /**
     * Each concrete filter is responsible for initializing itself
     * <p>
     *
     * @param aFieldname the field which is examined when applying this filter
     * @param aParameters optional additional parameters. May be <code>null</code>
     * @return the initialized filter (fluent API)
     * @throws SdiException if the initialization fails
     */
    public CollectFilter<T> init( String aFieldname, String aParameters )
            throws SdiException
    {
        if ( !StringUtils.hasText( aFieldname ) )
        {
            throw new SdiException( "Fieldname expected for this filter",
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }

        myFieldName = aFieldname;

        return this;
    }


    /**
     * Applies the filter.
     *
     * @param aDataset
     * @return <code>true</code> if the dataset should be filtered out.
     *
     * @throws SdiException
     */
    public abstract boolean isFiltered( Dataset aDataset ) throws SdiException;

    /**
     * Typesafe extraction of the configured field (property FieldName)
     * <p>
     * @param aDataset
     *        dataset to be examined
     * @param aClass
     *        desired class of the field value
     * @return
     */
    protected T getFieldValue( Dataset aDataset, Class<T> aClass  )
    {
        Object value = aDataset.get( myFieldName );

        if ( value == null )
        {
            myLog.debug( "dataset does not contain a field named " + myFieldName );
            return null;
        }

        if ( !( aClass.isInstance( value ) ) )
        {
            myLog.warn( "value of field " + myFieldName + " is not instance of "
                        + aClass.getSimpleName() + ", but: " + value.getClass().getName() );
            return null;
        }

        return aClass.cast( value );
    }

    protected abstract T getFieldValue( Dataset aDataset );


    @Override
    public String toString()
    {
        String result = super.toString();

        result += "; FieldName: " + myFieldName;

        return result;
    }

}
