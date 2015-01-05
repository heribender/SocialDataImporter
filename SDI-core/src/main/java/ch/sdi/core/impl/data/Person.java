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

package ch.sdi.core.impl.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.EnumerablePropertySource;


/**
 * An abstract data holder class for a person.<p>
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
@SuppressWarnings( "rawtypes" )
abstract public class Person<T extends Map> extends EnumerablePropertySource<Map<String, Object>>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( Person.class );

    /**
     * @return username
     */
    public String getUsername()
    {
        return getStringProperty( PersonKey.THING_ALTERNATENAME.getKeyName() );
    }

    /**
     * @param  aUsername
     *         username to set
     */
    @SuppressWarnings( "unchecked" )
    public void setUsername( String aUsername )
    {
        ((Map) getSource() ).put( PersonKey.THING_ALTERNATENAME.getKeyName(), aUsername );
    }

    /**
     * @return givenname
     */
    public String getGivenname()
    {
        return getStringProperty( PersonKey.PERSON_GIVENNAME.getKeyName() );
    }

    /**
     * @param  aGivenname
     *         givenname to set
     */
    public void setGivenname( String aGivenname )
    {
        getSource().put( PersonKey.PERSON_GIVENNAME.getKeyName(), aGivenname );
    }

    /**
     * @return middlename
     */
    public String getMiddlename()
    {
        return getStringProperty( PersonKey.PERSON_ADDITIONALNAME.getKeyName() );
    }

    /**
     * @param  aMiddlename
     *         middlename to set
     */
    public void setMiddlename( String aMiddlename )
    {
        getSource().put( PersonKey.PERSON_ADDITIONALNAME.getKeyName(), aMiddlename );
    }

    /**
     * @return familyName
     */
    public String getFamilyName()
    {
        return getStringProperty( PersonKey.PERSON_FAMILYNAME.getKeyName() );
    }

    /**
     * @param  aFamilyName
     *         familyName to set
     */
    public void setFamilyName( String aFamilyName )
    {
        getSource().put( PersonKey.PERSON_FAMILYNAME.getKeyName(), aFamilyName );
    }

    /**
     * @return eMail
     */
    public String getEMail()
    {
        return getStringProperty( PersonKey.PERSON_EMAIL.getKeyName() );
    }

    /**
     * @param  aEMail
     *         eMail to set
     */
    public void setEMail( String aEMail )
    {
        getSource().put( PersonKey.PERSON_EMAIL.getKeyName(), aEMail );
    }

    /**
     * @return Gender
     */
    public String getGender()
    {
        return getStringProperty( PersonKey.PERSON_GENDER.getKeyName() );
    }

    /**
     * @param  aValue
     *         Gender
     */
    public void setGender( String aValue )
    {
        getSource().put( PersonKey.PERSON_BIRTHDATE.getKeyName(), aValue );
    }

    /**
     * @return Gender
     */
    public Date getBirthdate()
    {
        return getDateProperty( PersonKey.PERSON_BIRTHDATE.getKeyName() );
    }

    /**
     * @param  aValue
     *         Gender
     */
    public void setBirthdate( Date aValue )
    {
        getSource().put( PersonKey.PERSON_BIRTHDATE.getKeyName(), aValue );
    }

    /**
     * @see org.springframework.core.env.PropertySource#getProperty(java.lang.String)
     */
    @Override
    public Object getProperty( String aKey )
    {
        return getProperty( aKey, Object.class );
    }

    public String getStringProperty( String aKey )
    {
        return getProperty( aKey, String.class );
    }

    public Number getNumberProperty( String aKey )
    {
        return getProperty( aKey, Number.class );
    }

    public Date getDateProperty( String aKey )
    {
        return getProperty( aKey, Date.class );
    }

    public List<Long> getLongListProperty( String aKey )
    {
        List<Long> result = new ArrayList<Long>();
        List<?> list = getProperty( aKey, List.class );
        if ( list != null )
        {
            list.forEach( item -> result.add( (Long) item ) );
        } // if list != null
        return result;
    }


    @SuppressWarnings( "unchecked" )
    public <R> R getProperty( String aKey, Class<R> aClass )
    {
        Object o = getSource().get( aKey );

        if ( o instanceof NullField || o == null )
        {
            return null;
        } // if o instanceof NullField

        if ( aClass.isAssignableFrom( o.getClass() ) )
        {
            return (R) o;
        } // if aClass.isAssignableFrom( o.getClass() )

        throw new IllegalStateException( "Unexpected value type: " + o.getClass()
                                         + "; expected: " + aClass.getSimpleName() );
    }

    public void setProperty( String aKey, Object aValue )
    {
        getSource().put( aKey, aValue );
    }

    /**
     * @see org.springframework.core.env.EnumerablePropertySource#getPropertyNames()
     */
    @Override
    public String[] getPropertyNames()
    {
        Map<String, Object> map = getSource();
        return map.keySet().toArray( new String[0] );
    }

    /**
     * Constructor
     *
     * @param aName
     * @param aSource
     */
    @SuppressWarnings( "unchecked" )
    public <F extends Map> Person( String aName, F aSource )
    {
        super( aName, aSource );
    }


}
