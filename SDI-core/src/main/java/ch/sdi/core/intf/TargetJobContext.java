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

import java.util.Collection;

import ch.sdi.core.exc.SdiDuplicatePersonException;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;


/**
 * Basic interface for target job implementations.
 * <p>
 * The TargetExecutor calls implementatins of this class as follows:
 *
 * <pre>
 *     prepare()
 *     getJobs()
 *     for each job:
 *         job.init()
 *     for each person:
 *         preparePerson()
 *         for each job:
 *             job.execute()
 *         finalizePerson()
 *     for each job:
 *         job.close()
 *     release()
 *  </pre>
 * <p>
 * Note that the custom target job must be inherited from CustomTargetJobContext in order that spring
 * can identify it uniquely and does not confuse it with the DefaultTargetJobContext.
 * (see AutoWired members in TargetExecutor).
 * <p>
 *
 * @version 1.0 (24.11.2014)
 * @author  Heri
 */
public interface TargetJobContext
{
    /**
     * The target job context may initialize itself globally
     * <p>
     * @throws SdiException
     *         On any problem
     */
    public void prepare() throws SdiException;

    /**
     * Returns the jobs which has to be executed in which order.
     * <p>
     * The TargetExecutor calls this method
     * @return
     * @throws SdiException
     *         On any problem
     */
    public Collection<? extends TargetJob> getJobs() throws SdiException;

    /**
     * Called before each single job is called for this person
     * <p>
     * @param aPerson
     *        the person which will be processed next
     * @throws SdiException
     *         On any problem
     * @throws SdiDuplicatePersonException
     *         If the given person is already member in the target platform. The tartet executor will
     *         not process further this person and report the person as DuplicatePersons
     */
    public void preparePerson( Person<?> aPerson ) throws SdiException;

    /**
     * Called after all jobs on this person have been processed, or if an execption during this
     * processing occured
     * @param aPerson
     *        the person to be processed
     * @param aException
     *        if an exception occured in a job execution the exception is passed to this method
     * @throws SdiException
     *         On any problem
     */
    public void finalizePerson( Person<?> aPerson, SdiException aException ) throws SdiException;

    /**
     * Called after all persons have been processed. The target job context may close all its
     * resources.
     * <p>
     * @param aException
     *        if an exception occured in a job execution the exception is passed to this method
     * @throws SdiException
     *         On any problem
     */
    public void release( SdiException aException ) throws SdiException;

}
