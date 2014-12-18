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

import ch.sdi.core.impl.data.Person;
import ch.sdi.plugins.oxwall.sql.entity.OxProfileData;


/**
 * Specialized ProfileQuestion class for number values
 *
 * @version 1.0 (06.12.2014)
 * @author  Heri
 */
public class OxProfileQuestionNumber extends OxProfileQuestion
{

    /**
     * Constructor
     *
     * @param aName
     *        the name of the question (as configured in a ox.target.qn.xxxx property)
     * @param aPersonKey
     *        the normalized person key as it is used later in the target phase
     */
    public OxProfileQuestionNumber( String aName, String aPersonKey )
    {
        super( aName, aPersonKey );
    }

    /**
     * @see ch.sdi.plugins.oxwall.profile.OxProfileQuestion#fillValues(ch.sdi.plugins.oxwall.sql.entity.OxProfileData, ch.sdi.core.impl.data.Person)
     */
    @Override
    public void fillValues( OxProfileData aProfileDataEntity, Person<?> aPerson )
    {
        super.fillValues( aProfileDataEntity, aPerson );
        aProfileDataEntity.setIntValue( getLongValue( aPerson ) );
    }

    /**
     * @param aPerson
     * @return
     */
    protected long getLongValue( Person<?> aPerson )
    {
        return aPerson.getNumberProperty( myPersonKey ).longValue();
    }

}
