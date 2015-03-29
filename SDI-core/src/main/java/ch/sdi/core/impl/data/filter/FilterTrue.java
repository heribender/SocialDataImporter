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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sdi.core.annotations.SdiFilter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Dataset;


/**
 * Filters the dataset if the configured criteria is true.
 * <p>
 * Note that the filter is annotated with Scope=Prototype to ensure that a new instance of the
 * filter is created each time a ctxt.getBean(() (or one of its variant) is called. The reason is that
 * there might be more than one filter of the same class which distinguish in parametrization.
 * <p>
 *
 * @version 1.0 (24.01.2015)
 * @author  Heri
 */
@SdiFilter( "trueField" )
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FilterTrue extends FilterOnBooleanField
{
    /**
     * @see ch.sdi.core.impl.data.filter.CollectFilter#isFiltered(ch.sdi.core.impl.data.Dataset)
     */
    @Override
    public boolean isFiltered( Dataset aDataset ) throws SdiException
    {
        Boolean value = getFieldValue( aDataset );

        if ( value == null )
        {
            return false;
        } // if value == null

        return value;
    }

}
