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

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.MailProperties;


/**
 * Testcase
 *
 * @version 1.0 (12.12.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MailSenderDefault.class})
public class MailSenderDefaultTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailSenderDefaultTest.class );
    @Autowired
    private ConfigurableEnvironment myEnv;
    @Autowired
    private MailSenderDefault myClassUnderTest;
    public static final String MAIL_WEB_DE = "sdi-test@web.de";



    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        configureLampVm();
    }

    /**
     * @throws SdiException
     */
    private void configureLampVm() throws SdiException
    {
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_HOST,
                "192.168.99.1" );
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_PORT,
                "25" );
//        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_PORT_SSL,
//                "25" );
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_SMTP_USER,
                "heri" );
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_SMTP_PASSWORD,
                "heri" );
//        mySslOnConnect = ConfigUtils.getBooleanProperty( myEnv, MailProperties.KEY_SSL_ON_CONNECT, false );
//        myStartTlsRequired = ConfigUtils.getBooleanProperty( myEnv,
//                                                             MailProperties.KEY_START_TLS_REQUIRED, false );
        TestUtils.addToEnvironment( myEnv, MailProperties.KEY_SENDER_ADDRESS,
                "heribender@web.de" );

        myClassUnderTest.init();
    }

    /**
     * Test method for {@link ch.sdi.core.impl.mail.MailSenderDefault#sendMail(org.apache.commons.mail.Email)}.
     */
    @Ignore( "TODO: Provide a mocked mail sink " )
    @Test
    public void testSendMail() throws Throwable
    {
        Email email = new SimpleEmail();
        email.addTo( "heri@lamp.vm" );
        email.setSubject( "Testmail" );
        email.setMsg( "Dies ist ein Testmail" );
        myLog.debug( "Test: sending simple mail: " + email );

        myClassUnderTest.sendMail( email );


    }

}
