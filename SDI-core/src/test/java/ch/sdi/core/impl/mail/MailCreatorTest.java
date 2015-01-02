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
import java.util.List;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.impl.data.PersonKey;
import ch.sdi.core.impl.data.PropertiesPerson;
import ch.sdi.core.intf.MailProperties;


/**
 * Testcase
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MailCreator.class,
                               MailTextResolver.class })
public class MailCreatorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailCreatorTest.class );

    /** */
    static final String THIS_IS_THE_SUBJECT = "This is the subject";
    /** */
    static final String THIS_IS_THE_BODY = "This is the body";


    @Autowired
    private ConfigurableEnvironment myEnv;
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
        myPersonProps = new Properties();
        myPersonProps.setProperty( PersonKey.THING_ALTERNATENAME.getKeyName(), "Bobby" );
        myPersonProps.setProperty( PersonKey.PERSON_GIVENNAME.getKeyName(), "Robert" );
        myPersonProps.setProperty( PersonKey.PERSON_ADDITIONALNAME.getKeyName(), "S." );
        myPersonProps.setProperty( PersonKey.PERSON_FAMILYNAME.getKeyName(), "Smith" );
        myPersonProps.setProperty( PersonKey.PERSON_EMAIL.getKeyName(), MAIL_WEB_DE );
        myPersonProps.setProperty( PersonKey.PERSON_GENDER.getKeyName(), "m" );
        myPersonProps.put( PersonKey.PERSON_BIRTHDATE.getKeyName(), new SimpleDateFormat( "yyyy-MM-dd" ).parse( "1998-12-09" ) );

        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_SUBJECT, "Test-Subject" );

        MailTextResolverDummy dummy = new MailTextResolverDummy();
        Whitebox.setInternalState( myClassUnderTest, "myMailTextResolver", dummy );

        TestUtils.debugPropertySources(  myEnv );

    }

    @Test
    public void testCreateMailFor() throws Throwable
    {
        PropertiesPerson person = new PropertiesPerson( "person", myPersonProps );
        Email email = myClassUnderTest.createMailFor( person );
        myLog.debug( "Created Mail: " + email );
        Assert.assertNotNull( email );
        Assert.assertEquals( THIS_IS_THE_SUBJECT, email.getSubject() );
        Object content = Whitebox.getInternalState( email, "content" );
        Assert.assertNotNull( content );
        Assert.assertEquals( THIS_IS_THE_BODY, content );
        List<InternetAddress> addresses = email.getToAddresses();
        Assert.assertNotNull( addresses );
        Assert.assertEquals( 1, addresses.size() );

    }

    static class MailTextResolverDummy extends MailTextResolver
    {

        /** logger for this class */
        private Logger myLog = LogManager.getLogger( MailCreatorTest.MailTextResolverDummy.class );
        /**
         * @see ch.sdi.core.impl.mail.MailTextResolver#init()
         */
        @Override
        public void init() throws SdiException
        {
            myLog.debug( "init, doing nothing" );
        }

        /**
         * @see ch.sdi.core.impl.mail.MailTextResolver#getResolvedBody(ch.sdi.core.impl.data.Person)
         */
        @Override
        public String getResolvedBody( Person<?> aPerson ) throws SdiException
        {
            return THIS_IS_THE_BODY;
        }

        /**
         * @see ch.sdi.core.impl.mail.MailTextResolver#getResolvedSubject(ch.sdi.core.impl.data.Person)
         */
        @Override
        public String getResolvedSubject( Person<?> aPerson ) throws SdiException
        {
            return THIS_IS_THE_SUBJECT;
        }


    }
}
