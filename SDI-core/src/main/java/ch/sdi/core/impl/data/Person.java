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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        return "" + getProperty( PersonKey.THING_ALTERNATENAME.getKeyName() );
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
        return "" + getProperty( PersonKey.PERSON_GIVENNAME.getKeyName() );
    }

    /**
     * @param  aGivenname
     *         givenname to set
     */
    @SuppressWarnings( "unchecked" )
    public void setGivenname( String aGivenname )
    {
        ((Map) getSource() ).put( PersonKey.PERSON_GIVENNAME.getKeyName(), aGivenname );
    }

    /**
     * @return middlename
     */
    public String getMiddlename()
    {
        return "" + getProperty( PersonKey.PERSON_ADDITIONALNAME.getKeyName() );
    }

    /**
     * @param  aMiddlename
     *         middlename to set
     */
    @SuppressWarnings( "unchecked" )
    public void setMiddlename( String aMiddlename )
    {
        ((Map) getSource() ).put( PersonKey.PERSON_ADDITIONALNAME.getKeyName(), aMiddlename );
    }

    /**
     * @return familyName
     */
    public String getFamilyName()
    {
        return "" + getProperty( PersonKey.PERSON_FAMILYNAME.getKeyName() );
    }

    /**
     * @param  aFamilyName
     *         familyName to set
     */
    @SuppressWarnings( "unchecked" )
    public void setFamilyName( String aFamilyName )
    {
        ((Map) getSource() ).put( PersonKey.PERSON_FAMILYNAME.getKeyName(), aFamilyName );
    }

    /**
     * @return eMail
     */
    public String getEMail()
    {
        return "" + getProperty( PersonKey.PERSON_EMAIL.getKeyName() );
    }

    /**
     * @param  aEMail
     *         eMail to set
     */
    @SuppressWarnings( "unchecked" )
    public void setEMail( String aEMail )
    {
        ((Map) getSource() ).put( PersonKey.PERSON_EMAIL.getKeyName(), aEMail );
    }

    /**
     * @return Gender
     */
    public String getGender()
    {
        return "" + getProperty( PersonKey.PERSON_GENDER.getKeyName() );
    }

    /**
     * @param  aValue
     *         Gender
     */
    @SuppressWarnings( "unchecked" )
    public void setGender( String aValue )
    {
        ((Map) getSource() ).put( PersonKey.PERSON_BIRTHDATE.getKeyName(), aValue );
    }

    /**
     * @return Gender
     */
    public Date getBirthdate()
    {
        Object o = getProperty( PersonKey.PERSON_BIRTHDATE.getKeyName() );

        if ( o == null )
        {
            return null;
        } // if o == null

        if ( o instanceof Date )
        {
            return (Date) o;
        } // if o instanceof Date

        if ( o instanceof String )
        {
            try
            {
                return new SimpleDateFormat().parse( "" + o );
            }
            catch ( ParseException t )
            {
                myLog.error( "Failed to parse birthday " + o, t );
                return null;
            }
        } // if o instanceof String

        myLog.warn( "Unknown value type in property " + PersonKey.PERSON_BIRTHDATE.getKeyName() + ": " + o.getClass().getName() );

        return null;
    }

    /**
     * @param  aValue
     *         Gender
     */
    @SuppressWarnings( "unchecked" )
    public void setBirthdate( Date aValue )
    {
        ((Map) getSource() ).put( PersonKey.PERSON_BIRTHDATE.getKeyName(), aValue );
    }

    // TODO: add property for PersonKey.THING_IMAGE

    /**
     * @see org.springframework.core.env.PropertySource#getProperty(java.lang.String)
     */
    @Override
    public Object getProperty( String aName )
    {
        return getSource().get( aName );
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
