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

package ch.sdi.core.impl.mail;

import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.mail.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.impl.data.PropertiesPerson;


/**
 * TODO
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MailCreator.class})
public class MailCreatorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailCreatorTest.class );

    @Autowired
    private ConfigurableEnvironment  env;
    @Autowired
    private MailCreator myClassUnderTest;
    public static final String MAIL_WEB_DE = "sdi-test@web.de";

    private Properties myPersonProps;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        TestUtils.debugPropertySources( env );

//        myImportItemProps = new MockPropertySource( "ImportItem" )
//                .withProperty( Person.KEY_THING_ALTERNATENAME, "Bobby" )
//                .withProperty( Person.KEY_PERSON_GIVENNAME, "Robert" )
//                .withProperty( Person.KEY_PERSON_ADDITIONALNAME, "S." )
//                .withProperty( Person.KEY_PERSON_FAMILYNAME, "Smith" )
//                .withProperty( Person.KEY_PERSON_EMAIL, MAIL_WEB_DE );
//        env.getPropertySources().addLast( myImportItemProps );

        myPersonProps = new Properties();
        myPersonProps.setProperty( Person.KEY_THING_ALTERNATENAME, "Bobby" );
        myPersonProps.setProperty( Person.KEY_PERSON_GIVENNAME, "Robert" );
        myPersonProps.setProperty( Person.KEY_PERSON_ADDITIONALNAME, "S." );
        myPersonProps.setProperty( Person.KEY_PERSON_FAMILYNAME, "Smith" );
        myPersonProps.setProperty( Person.KEY_PERSON_EMAIL, MAIL_WEB_DE );
        myPersonProps.setProperty( Person.KEY_PERSON_GENDER, "m" );
        myPersonProps.put( Person.KEY_PERSON_BIRTHDATE, new SimpleDateFormat( "yyyy-MM-dd" ).parse( "1998-12-09" ) );
    }

    @Test
    public void testCreateMailFor() throws Throwable
    {
        PropertiesPerson person = new PropertiesPerson( "person", myPersonProps );
        Email email = myClassUnderTest.createMailFor( person );
        myLog.debug( "Created Mail: " + email );

//        fail( "Not yet implemented" );
    }

}
