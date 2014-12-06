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

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.impl.data.converter.OxGender;


/**
 * TODO
 *
 * @version 1.0 (06.12.2014)
 * @author  Heri
 */
@OxCustomQuestion( OxProfileQuestionGender.QUESTION_BEAN_NAME )
public class OxProfileQuestionGender extends OxProfileQuestionNumber
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxProfileQuestionGender.class );
    public static final String QUESTION_BEAN_NAME = "gendermap";
    public static final String KEY_PREFIX_GENDER = "ox.target.sex.";

    private Map<OxGender,Number> myGenderMap;

    /**
     * Constructor
     *
     * @param aName
     * @param aPersonKey
     */
    public OxProfileQuestionGender( String aName, String aPersonKey )
    {
        super( aName, aPersonKey );
    }

    /**
     * @see ch.sdi.plugins.oxwall.profile.OxProfileQuestionNumber#getLongValue(ch.sdi.core.impl.data.Person)
     */
    @Override
    protected long getLongValue( Person<?> aPerson )
    {
        return myGenderMap.get( aPerson.getProperty( myPersonKey,
                                                     OxGender.class ) ).longValue();
    }

    /**
     * @see ch.sdi.plugins.oxwall.profile.OxProfileQuestion#init(org.springframework.core.env.Environment)
     */
    @Override
    public void init( Environment aEnvironment ) throws SdiException
    {
        myGenderMap = new HashMap<OxGender,Number>();

        for ( OxGender gender : OxGender.values() )
        {
            String key = KEY_PREFIX_GENDER + gender;
            String configured = aEnvironment.getProperty( key );

            Long value;

            if ( !StringUtils.hasText( configured ) )
            {
                myLog.warn( "Gender mapping not configured: " + key + "; using default value" );
                value = Long.valueOf( gender.getDefaultValue() );
            }
            else
            {
                try
                {
                    value = Long.parseLong( configured );
                }
                catch ( NumberFormatException t )
                {
                    throw new SdiException( "Gender mapping " + configured + " cannot be converted to number",
                                            t,
                                            SdiException.EXIT_CODE_CONFIG_ERROR );
                }
            }

            myGenderMap.put( gender, value );

        }
    }


}
