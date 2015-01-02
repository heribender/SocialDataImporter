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


package ch.sdi.core.exc;

import ch.sdi.core.impl.data.Person;


/**
 * Special exception for indicating that the person to be processed is already present in the target
 * platform.
 *
 * @version 1.0 (24.11.2014)
 * @author  Heri
 */
public class SdiDuplicatePersonException extends SdiException
{
    private static final long serialVersionUID = 1L;
    private Person<?> myPerson;

    /**
     * Constructor
     *
     * @param aExitCode
     */
    public SdiDuplicatePersonException( Person<?> aPerson )
    {
        super( EXIT_CODE_NO_ERROR );
        myPerson = aPerson;
    }

    /**
     * Constructor
     *
     * @param aMessage
     * @param aExitCode
     */
    public SdiDuplicatePersonException( String aMessage, Person<?> aPerson )
    {
        super( aMessage, EXIT_CODE_NO_ERROR );
        myPerson = aPerson;
    }


    /**
     * @return person
     */
    public Person<?> getPerson()
    {
        return myPerson;
    }

}
