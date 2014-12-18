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


package ch.sdi.plugins.oxwall.profile;

import org.springframework.core.env.Environment;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.plugins.oxwall.sql.entity.OxProfileData;



/**
 * Entity for holding oxwalls profile question names (which can be customized).
 * <p>
 * Oxwall stores the answers of custom profile questions in a DB table which holds the value in one of
 * three columns, depending on the data type of the profile question: text, number or date (see entity
 * OxProfileData).
 * <p>
 * The question names and its types can be configured in target.properties (see
 * property ox.target.qn.xxxx)
 * <p>
 * Custom type implementations must be implemented within or under the package ch.sdi.plugins.oxwall in
 * order to be found by the generic instantiatin mechanisme (see OxQuestionFactory).
 * <p>
 *
 * @version 1.0 (05.12.2014)
 * @author  Heri
 */
public abstract class OxProfileQuestion
{
    /** the question name */
    protected String myName;
    /** the key in the Person class to retrieve the value */
    protected String myPersonKey;


    /**
     * Constructor
     *
     * @param aName
     *        the name of the question (as configured in a ox.target.qn.xxxx property)
     * @param aPersonKey
     *        the normalized person key as it is used later in the target phase
     */
    public OxProfileQuestion( String aName, String aPersonKey )
    {
        super();
        myName = aName;
        myPersonKey = aPersonKey;
    }

    /**
     * @return value
     *         the name of the question (as configured in a ox.target.qn.xxxx property)
     */
    public String getName()
    {
        return myName;
    }

    /**
     * @param  aName
     *         the name of the question (as configured in a ox.target.qn.xxxx property)
     */
    public void setName( String aName )
    {
        myName = aName;
    }

    /**
     * Updates the ProfileDataEntity with our profile data information.
     * <p>
     * This base implementation assigns the QuestionName field. For assigning the type dependant
     * value this method must be overridden by type specialized classes.
     * <p>
     *
     * @param aProfileDataEntity
     *        the entity to be updated
     * @param aPerson
     *        the person entity where the value can be retrieved from
     */
    public void fillValues( OxProfileData aProfileDataEntity, Person<?> aPerson )
    {
        aProfileDataEntity.setQuestionName( myName );
    }

    /**
     * Can be overridden to perform initialization work
     * <p>
     * @param aEnvironment
     * @throws SdiException
     */
    public void init( Environment aEnvironment ) throws SdiException
    {
        // provided for overriding if necessary
    };

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    Name      : " ).append( myName );
        sb.append( "\n    PersonKey : " ).append( myPersonKey );
        return sb.toString();
    }



}
