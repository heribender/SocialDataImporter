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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.InputCollectorMappingProperties;


/**
 * Normalizes person fieldnames (of collecting phase ) to the internal representation
 * <p>
 *
 * @version 1.0 (13.11.2014)
 * @author  Heri
 */
@Component
public class FieldnameNormalizer
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( FieldnameNormalizer.class );
    @Autowired
    private Environment  myEnv;

    /**
     * Normalizes person fieldnames (of collecting phase ) to the internal representation by looking
     * up the respective configuration.
     * <p>
     * Example: During collection phase the username field is called "Screenname" (e.g. in CSV file). In
     * order to map this key name to the internal representation "thing.alternateName" there must be a
     * configured key/value pair "inputcollector.thing.alternateName=Screenname".
     * <p>
     * If the used field name has already the normalized form no configuration is necessary and it is
     * returned as is.
     * <p>
     *
     * @param aFieldnames the fieldnames used in collection phase. Must not be <code>null</code>
     * @return the normalized representation of the input field names in form of a map:
     *         <pre>
     *             rawFieldName=NormalizedFieldName
     *         </pre>
     * @throws SdiException on any problem
     */
    public Map<String,String> normalize( Collection<String> aFieldnames ) throws SdiException
    {
        Map<String,String> configuredReplaceMap = new HashMap<String,String>();

        for ( String key : PersonKey.getKeyNames() )
        {
            String keyName = InputCollectorMappingProperties.KEY_PREFIX + key;

            String configuredMapping = myEnv.getProperty( keyName );

            if ( !StringUtils.hasText( configuredMapping ) )
            {
                continue;
            } // if StringUtils.hasText( configuredMapping )

            configuredMapping = configuredMapping.trim();

            myLog.debug( "Found input collector mapping: " + keyName + " -> " + configuredMapping );

            configuredReplaceMap.put( configuredMapping, key );

        }

        Map<String,String> result = new HashMap<>();

        for ( String collectedFieldName : aFieldnames )
        {
            String normalized = null;

            if ( configuredReplaceMap.containsKey( collectedFieldName ) )
            {
                normalized = configuredReplaceMap.remove( collectedFieldName );
                myLog.debug( "Normalizing collected fieldname " + collectedFieldName
                             + " by " + normalized );
            }
            else
            {
                if ( !PersonKey.getKeyNames().contains( collectedFieldName ) )
                {
                    throw new SdiException( "Given fieldname \"" + collectedFieldName + "\" not configured for "
                            + "normalizing",
                            SdiException.EXIT_CODE_CONFIG_ERROR );
                } // if condition

                myLog.debug( "Given fieldname " + collectedFieldName + " already normalized" );
                normalized = collectedFieldName;
            }

            result.put( collectedFieldName, normalized );
        }

        if ( !configuredReplaceMap.isEmpty() )
        {
            myLog.warn( "At least one configured input collector mapping not contained in"
                                    + " given input list: "
                                    + configuredReplaceMap.values() );

        } // if !replaceMap.isEmpty()

        myLog.debug( "Normalized field names: " + result );

        return result;
    }

}
