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
 * Base interface for a custom SQL jobs.
 *
 * @version 1.0 (23.11.2014)
 * @author  Heri
 */
public interface SqlJob extends TargetJob
{
    /**
     * Check if the person is already present in the target platform.
     * <p>
     * @param aPerson
     * @return <code>true</code> if the given person is already present
     * @throws SdiException on any problem
     */
    public boolean isAlreadyPresent( Person<?> aPerson ) throws SdiException;

    /**
     * Starts a transaction
     */
    public void startTransaction();

    /**
     * Commits a transaction
     */
    public void commitTransaction();

    /**
     * Transaction rollback
     */
    public void rollbackTransaction();


}
