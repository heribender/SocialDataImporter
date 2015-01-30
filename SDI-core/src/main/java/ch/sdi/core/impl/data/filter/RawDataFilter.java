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

import org.springframework.core.env.Environment;

import ch.sdi.core.exc.SdiException;


/**
 * Base class of a specialized filter which is applied on the raw data before they are parsed by the
 * collector.
 * <p>
 *
 * @version 1.0 (24.01.2015)
 * @author  Heri
 */
public abstract class RawDataFilter<T>
{
    /**
     * Each concrete filter is responsible for initializing itself
     * <p>
     *
     * @param aEnv
     * @param aFieldname the field for which the converter is responsible
     * @return the initialized filter (fluent API)
     * @throws SdiException if the initialization fails
     */
    public RawDataFilter<T> init( Environment aEnv, String aParameters ) throws SdiException
    {
        return this;
    }

    /**
     * Applies the filter.
     *
     * @param aCriteria
     *        the criteria to be examined
     * @return <code>true</code> if the dataset should be filtered out.
     *
     * @throws SdiException
     */
    public abstract boolean isFiltered( T aCriteria ) throws SdiException;

}
