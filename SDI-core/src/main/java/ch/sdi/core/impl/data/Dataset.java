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


package ch.sdi.core.impl.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import ch.sdi.core.exc.SdiException;


/**
 * Implements a simple HashMap<String,Object>
 * <p>
 *
 * @version 1.0 (24.01.2015)
 * @author  Heri
 */
public class Dataset extends LinkedHashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /**
     * Convertes the given collections into a Dataset
     * <p>
     * Both collections must not be <code>null</code> and have the same size
     * <p>
     * @param aKeys
     * @param aValues
     * @return an initialized dataset
     */
    public static Dataset create( Collection<String> aKeys, Collection<Object> aValues )
        throws SdiException
    {
        if ( aKeys == null )
        {
            throw new SdiException( "Given keys is null!",
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        }

        if ( aValues == null )
        {
            throw new SdiException( "Given values is null!",
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        }

        if ( aKeys.size() != aValues.size() )
        {
            throw new SdiException( "Given collections are not the same in size. Keys: " + aKeys.size()
                                    + "; Values: " + aValues.size(),
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        }

        Dataset result = new Dataset();

        Iterator<String> keys = aKeys.iterator();
        Iterator<Object> values = aValues.iterator();

        while ( keys.hasNext() )
        {
            result.put( keys.next(), values.next() );
        }

        return result;
    }

}
