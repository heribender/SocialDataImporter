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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.impl.data.PropertiesPerson;
import ch.sdi.core.intf.MailProperties;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * TODO
 *
 * @version 1.0 (11.12.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MailTextResolver.class})
public class MailTextResolverTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailTextResolverTest.class );
    @Autowired
    private ConfigurableEnvironment myEnv;
    @Autowired
    private MailTextResolver myClassUnderTest;
    private Person<?> myPerson;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_TARGET_NAME,
                "MySocialSite"  );
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_BODY,
                "${person.givenName}: You are now a member of our site ${sdi.target.name}"  );
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_SUBJECT,
                "Hello ${person.givenName}"  );

        myPerson = new PropertiesPerson( "test", new Properties() );
        myPerson.setGivenname( "Bobby"  );

        myClassUnderTest.init();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        TestUtils.removeAllFromEnvironment( myEnv );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.mail.MailTextResolver#getResolvedBody(ch.sdi.core.impl.data.Person)}.
     */
    @Test
    public void testGetResolvedBody() throws Throwable
    {
        String received = myClassUnderTest.getResolvedBody( myPerson );
        myLog.debug( "Received: " + received );
        Assert.assertEquals( "Bobby: You are now a member of our site MySocialSite", received );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.mail.MailTextResolver#getResolvedBody(ch.sdi.core.impl.data.Person)}.
     */
    @Test( expected = SdiException.class )
    public void testGetResolvedBodyUnresolvedProp() throws Throwable
    {
        TestUtils.removeFromEnvironment( myEnv, SdiMainProperties.KEY_TARGET_NAME );
        String received = myClassUnderTest.getResolvedBody( myPerson );
        myLog.debug( "Received: " + received );
        Assert.assertEquals( "You are now a member of our site MySocialSite", received );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.mail.MailTextResolver#getResolvedSubject(ch.sdi.core.impl.data.Person)}.
     */
    @Test
    public void testGetResolvedSubject() throws Throwable
    {
        String received = myClassUnderTest.getResolvedSubject( myPerson );
        myLog.debug( "Received: " + received );
        Assert.assertEquals( "Hello Bobby", received );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.mail.MailTextResolver#getResolvedBody(ch.sdi.core.impl.data.Person)}.
     */
    @Test( expected = SdiException.class )
    public void testGetResolvedBodyNoCharset() throws Throwable
    {
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_BODY_TEMPLATE,
                "template.txt"  );
        myClassUnderTest.init();
    }

    /**
     * Test method for {@link ch.sdi.core.impl.mail.MailTextResolver#getResolvedBody(ch.sdi.core.impl.data.Person)}.
     */
    @Test( expected = SdiException.class )
    public void testGetResolvedBodyInvalidCharset() throws Throwable
    {
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_BODY_TEMPLATE,
                "template.txt"  );
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_BODY_TEMPLATE_CHARSET,
                "invalid"  );
        myClassUnderTest.init();
    }

    /**
     * Test method for {@link ch.sdi.core.impl.mail.MailTextResolver#getResolvedSubject(ch.sdi.core.impl.data.Person)}.
     */
    @Test( expected = SdiException.class )
    public void testGetResolvedSubjectByTemplateNotFound() throws Throwable
    {
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_BODY_TEMPLATE,
                "template.txt"  );
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_BODY_TEMPLATE_CHARSET,
                "UTF-8"  );
        myClassUnderTest.init();
    }

    /**
     * Test method for {@link ch.sdi.core.impl.mail.MailTextResolver#getResolvedSubject(ch.sdi.core.impl.data.Person)}.
     */
    @Test
    public void testGetResolvedSubjectByTemplate() throws Throwable
    {
        File file = new File( "template.txt" );
        Assert.assertTrue( file.createNewFile() );
        try
        {
            String body = "Hallo ${person.givenName}. Wünschen viel Spass mit unserer Site ${sdi.target.name}";
            OutputStream os = new FileOutputStream( file );
            os.write( body.getBytes( Charset.forName( "UTF-8" ) ) );
            os.close();
            TestUtils.addToEnvironment( myEnv, MailProperties.KEY_BODY_TEMPLATE, file.getName() );
            TestUtils.addToEnvironment( myEnv, MailProperties.KEY_BODY_TEMPLATE_CHARSET,
                    "UTF-8"  );
            myClassUnderTest.init();
            String received = myClassUnderTest.getResolvedBody( myPerson );
            myLog.debug( "Received: " + received );
            Assert.assertEquals( "Hallo Bobby. Wünschen viel Spass mit unserer Site MySocialSite",
                                 received );
        }
        finally
        {
            file.delete();
        }
    }

}
