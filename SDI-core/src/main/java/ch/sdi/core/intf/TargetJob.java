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

package ch.sdi.core.intf;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;


/**
 * Base interface for all custom target jobs.
 *
 * @version 1.0 (15.11.2014)
 * @author Heri
 */
public interface TargetJob
{

    /**
     * Initializes the job. Is invoked once before all persons will be executed. Resources which are
     * valid for all persons can be allocated here.
     * <p>
     * @throws SdiException on any problem
     */
    public void init() throws SdiException;

    /**
     * Closes the job. Is invoked once after all persons have been executed. Resources can be freed.
     * <p>
     * @throws SdiException on any problem
     */
    public void close() throws SdiException;

    /**
     * Executes the concrete job for the given person
     * <p>
     *
     * @param aPerson
     *        must not be <code>null</code>
     * @throws SdiException
     *         on any problem
     */
    void execute( Person<?> aPerson ) throws SdiException;

}
