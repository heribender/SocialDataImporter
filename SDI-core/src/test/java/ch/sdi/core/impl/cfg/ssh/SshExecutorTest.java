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

package ch.sdi.core.impl.cfg.ssh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.cfg.ConversionServiceProvider;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * TODO
 *
 * @version 1.0 (13.12.2014)
 * @author Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SshExecutor.class,
                               ConversionServiceProvider.class })
public class SshExecutorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( SshExecutorTest.class );

    @Autowired
    private ConfigurableEnvironment  myEnv;
    @Autowired
    private SshExecutor myClassUnderTest;
    @Autowired
    private ConversionService  myConversionService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        ConfigUtils.setConversionService( myConversionService );

        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_TARGET_HOST, "192.168.99.1" );
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_SSH_USER, "heri" );
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_SSH_PASSWORD, "heri" );
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_SSH_PORT, "22" );
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_SSH_CHECK_CERTIFICATE, "false" );

        myClassUnderTest.init();
    }

    @After
    public void tearDown() throws Throwable
    {
        myClassUnderTest.close();
    }

    @Test
    public void testChmod() throws Throwable
    {
        String command1 = "chmod 666 /var/www/oxwall-1.6.0.zip";
//        myClassUnderTest.executeCmd( command1 );
    }

    @Test( expected=SdiException.class )
    public void testChown() throws Throwable
    {
        String command1 = "chown www-data:www-data /var/www/oxwall-1.6.0.zip";
        myClassUnderTest.executeCmd( command1 );
    }

}
