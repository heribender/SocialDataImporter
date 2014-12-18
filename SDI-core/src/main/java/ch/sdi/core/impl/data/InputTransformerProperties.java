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


package ch.sdi.core.impl.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.CollectorResult;


/**
 * TODO
 *
 * @version 1.0 (08.11.2014)
 * @author  Heri
 */
@Component
public class InputTransformerProperties implements InputTransformer
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( InputTransformerProperties.class );
    @Autowired
    private FieldnameNormalizer myFieldnameNormalizer;

    /**
     * @throws SdiException
     * @see ch.sdi.core.impl.data.InputTransformer#execute(ch.sdi.core.intf.CollectorResult)
     */
    @Override
    public Collection<? extends Person<?>> execute( CollectorResult aCollectorResult ) throws SdiException
    {
        Collection<String> fieldNames = myFieldnameNormalizer.normalize( aCollectorResult.getFieldnames() );

        Collection<Person<?>> result = new ArrayList<Person<?>>();

        Collection<Collection<Object>> rows = aCollectorResult.getRows();
        for ( Collection<Object> row : rows )
        {
            result.add( transformRow( row, fieldNames ) );
        }

        return result;
    }

    /**
     * @param aRow
     * @param aFieldNames
     * @throws SdiException
     */
    private PropertiesPerson transformRow( Collection<Object> aRow, Collection<String> aFieldNames ) throws SdiException
    {
        Properties props = new Properties();

        Iterator<Object> iter = aRow.iterator();
        int fieldPos = 0;
        for ( String field : aFieldNames )
        {
            if ( !iter.hasNext() )
            {
                throw new SdiException( "Mismatch between number of fieldnames and number of fields "
                                        + "in the row. Pos: " + fieldPos,
                                        SdiException.EXIT_CODE_PARSE_ERROR );
            } // if !iter.hasNext()

            Object o = iter.next();

            props.put( field, o );

            fieldPos++;
        }

        if ( iter.hasNext() )
        {
            throw new SdiException( "More fields in the row than in the fieldname list",
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        } // if !iter.hasNext()

        // TODO: make it configurable which field can be used as primary key which will be the name of the
        // embedded PropertySource and must be unique (TODO: verify if this is even needed).
        String name = props.getProperty( PersonKey.PERSON_EMAIL.getKeyName() );
        if ( !StringUtils.hasText( name ) )
        {
            throw new SdiException( "Person has no mail address or the configuration of the field mapping"
                                    + " is not correct. "
                                    + "\n    Fieldnames: " + aFieldNames
                                    + "\n    Fields:     " + aRow,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if !StringUtils.hasText( name )

        PropertiesPerson result = new PropertiesPerson( name, props );

        return result;

    }

}
