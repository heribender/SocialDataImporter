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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.CollectorResult;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * This input transformer transforms the raw input into a normalized collection of PropertiesPerson
 * <p>
 * At start of execution it reads the ignored fields from the configuration (<code>sdi.normalize.ignoreField</code>)
 * and applies these filters during execution.
 * <p>
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
    private Environment myEnv;
    @Autowired
    private FieldnameNormalizer myFieldnameNormalizer;
    private Collection<String> myIgnoredFields;

    /**
     * @see ch.sdi.core.impl.data.InputTransformer#execute(ch.sdi.core.intf.CollectorResult)
     */
    @Override
    public Collection<? extends Person<?>> execute( CollectorResult aCollectorResult ) throws SdiException
    {
        Collection<String> filteredRawFieldNames = getFilteredRawFieldNames( aCollectorResult.getFieldnames() );
        Map<String,String> normalizedMap = myFieldnameNormalizer.normalize( filteredRawFieldNames );

        Collection<Person<?>> result = new ArrayList<Person<?>>();

        Collection<Dataset> rows = aCollectorResult.getRows();
        for ( Dataset row : rows )
        {
            result.add( transformRow( row, normalizedMap ) );
        }

        return result;
    }

    /**
     * Reads the ignored fields into member myIgnoredFields and returns a collection of all raw field
     * names which are not filtered (sdi.normalize.ignoreField).
     * <p>
     * @param aFieldnames
     *        the collected fieldnames
     * @return
     */
    private Collection<String> getFilteredRawFieldNames( Collection<String> aFieldnames )
    {
        String value = myEnv.getProperty( SdiMainProperties.KEY_NORMALIZE_IGNORE_FIELD );

        if ( !StringUtils.hasText( value ) )
        {
            myLog.debug( "no normalize field filters configured" );
            myIgnoredFields = new ArrayList<>();
            return aFieldnames;
        } // if !StringUtils.hasText( value )

        myIgnoredFields = Arrays.asList( value.split( "," ) );

        return aFieldnames.stream()
            .filter( f -> !myIgnoredFields.contains( f ) )
            .collect( Collectors.toCollection( (Supplier<List<String>>) ArrayList::new ) );
    }

    /**
     * Transforms the collected Dataset into a PropertiesPerson. Ignored raw field names are not
     * processed.
     * <p>
     * @param aRow
     *        the dataset to be converted
     * @param aNormalizedFieldNames
     *        a mapping between raw field names and normalized field names
     * @throws SdiException
     */
    private PropertiesPerson transformRow( Dataset aRow, Map<String,String> aNormalizedFieldNames )
            throws SdiException
    {
        Properties props = new Properties();

        for ( String rawKey : aRow.keySet() )
        {
            if ( myIgnoredFields.contains( rawKey ) )
            {
                continue;
            }

            String normalized = aNormalizedFieldNames.get( rawKey );

            if ( !StringUtils.hasText( normalized ) )
            {
                throw new SdiException( "No normalized field name found for raw field name: " + rawKey,
                                        SdiException.EXIT_CODE_PARSE_ERROR );
            }

            props.put( normalized, aRow.get( rawKey ) );
        }

        // TODO: make it configurable which field can be used as primary key which will be the name of the
        // embedded PropertySource and must be unique.
        String mail = props.getProperty( PersonKey.PERSON_EMAIL.getKeyName() );
        if ( !StringUtils.hasText( mail ) )
        {
            throw new SdiException( "Person has no mail address or the configuration of the field mapping"
                                    + " is not correct. "
                                    + "\n    Fieldnames: " + aNormalizedFieldNames
                                    + "\n    Fields:     " + aRow,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if !StringUtils.hasText( name )

        PropertiesPerson result = new PropertiesPerson( mail, props );

        return result;

    }

}
