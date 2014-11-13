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
 * TODO
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
     * @param aFieldnames
     * @return
     * @throws SdiException
     */
    public Collection<String> normalize( Collection<String> aFieldnames ) throws SdiException
    {

        Map<String,String> replaceMap = new HashMap<String,String>();

        for ( PersonKey key : PersonKey.values() )
        {
            String keyName = InputCollectorMappingProperties.KEY_PREFIX + key.getKeyName();

            String configuredMapping = myEnv.getProperty( keyName );

            if ( !StringUtils.hasText( configuredMapping ) )
            {
                continue;
            } // if StringUtils.hasText( configuredMapping )

            configuredMapping = configuredMapping.trim();

            myLog.debug( "Found input collector mapping: " + keyName + " -> " + configuredMapping );

            replaceMap.put( configuredMapping, key.getKeyName() );

        }

        Collection<String> result = new ArrayList<String>();

        for ( String collectedFieldName : aFieldnames )
        {
            String normalized = collectedFieldName;

            if ( replaceMap.containsKey( collectedFieldName ) )
            {
                normalized = replaceMap.remove( collectedFieldName );
                myLog.debug( "Normalizing collected fieldname " + collectedFieldName
                             + " by " + normalized );
            }

            result.add( normalized );
        }

        if ( !replaceMap.isEmpty() )
        {
            throw new SdiException( "configured input collector mapping not known: "
                                    + replaceMap.values(),
                                    SdiException.EXIT_CODE_CONFIG_ERROR );

        } // if !replaceMap.isEmpty()

        myLog.debug( "Normalized field names: " + result );

        return result;
    }

}
