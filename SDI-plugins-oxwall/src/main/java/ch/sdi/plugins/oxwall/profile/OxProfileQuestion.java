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
 * Entity for holding oxwalls profile question names (which can be customized)
 *
 * @version 1.0 (05.12.2014)
 * @author  Heri
 */
public abstract class OxProfileQuestion
{
    private OxQuestionType myType;  // TODO: still used?
    /** the question name */
    protected String myName;
    /** the key in the Person class to retrieve the value */
    protected String myPersonKey;


    /**
     * Constructor
     *
     * @param aType
     * @param aName
     * @param aPersonKey
     */
    public OxProfileQuestion( OxQuestionType aType, String aName, String aPersonKey )
    {
        super();
        myType = aType;
        myName = aName;
        myPersonKey = aPersonKey;
    }

    /**
     * @return type
     */
    public OxQuestionType getType()
    {
        return myType;
    }

    /**
     * @param  aType
     *         type to set
     */
    public void setType( OxQuestionType aType )
    {
        myType = aType;
    }

    /**
     * @return value
     */
    public String getName()
    {
        return myName;
    }

    /**
     * @param  aName
     *         value to set
     */
    public void setName( String aName )
    {
        myName = aName;
    }

    public void fillValues( OxProfileData aProfileDataEntity, Person<?> aPerson )
    {
        aProfileDataEntity.setQuestionName( myName );
    }

    public void init( Environment aEnvironment ) throws SdiException
    {
        // provided for overriding if necessary
    };

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    Type      : " ).append( myType );
        sb.append( "\n    Name      : " ).append( myName );
        sb.append( "\n    PersonKey : " ).append( myPersonKey );
        return sb.toString();
    }



}
