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


package ch.sdi.core.target;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;


/**
 * TODO
 *
 * @version 1.0 (15.11.2014)
 * @author  Heri
 */
@Component
public class TargetExecutor
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( TargetExecutor.class );
    @Autowired
    private Environment  myEnv;

    public void execute( Collection<? extends Person<?>> aPersons) throws SdiException
    {
        Collection<Person<?>> failedPersons = new ArrayList<Person<?>>();
        aPersons.stream()
            .filter(p ->
            {
                // returns the
                return !handlePerson( p );
            } )
//            .collect( (Collector<?, A, R>) failedPersons );
//            .map( p ->
//            {
//                return ( "origValue == null || origValue.isEmpty() )
//                        ? result + "No default value found. Adding new value to environment: \""
//                                 + props.getProperty( key ) + "\""
//                        : result + "Overriding default value \"" + origValue + "\" with new value: \""
//                                 + props.getProperty( key ) + "\"";
//            })
            .forEach( p -> failedPersons.add( p ) ) ;

        if ( failedPersons.isEmpty() )
        {
            myLog.info( "All data successfully handled for target" );
            return;
        } // if failedPersons.isEmpty()

        throw new SdiException( "" + failedPersons.size() + " could not have been processed",
                                SdiException.EXIT_CODE_IMPORT_ERROR );
    }

    /**
     * @param aPerson
     * @return
     */
    private boolean handlePerson( Person<?> aPerson )
    {
        myLog.debug( "processing person " + aPerson.getFamilyName() );

        return true;
    }

}
