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


package ch.sdi.core.ftp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * TODO
 *
 * @version 1.0 (22.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={FtpExecutor.class })
public class FtpExecutorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( FtpExecutorTest.class );
    @Autowired
    private ConfigurableEnvironment  myEnv;
    @Autowired
    private FtpExecutor myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * Test method for {@link ch.sdi.core.ftp.FtpExecutor#executeUpload(java.io.InputStream, java.lang.String)}.
     */
    @Test
    public void testUpload() throws Throwable
    {
        Map<String,InputStream> filesToUpload = new TreeMap<String,InputStream>();

        String targetDir = "/var/www/";

        filesToUpload.put( targetDir + "sdimain_test.properties",
                           ClassLoader.getSystemResourceAsStream( "sdimain_test.properties" ) );
        filesToUpload.put( targetDir + "log4j2.xml",
                           ClassLoader.getSystemResourceAsStream( "log4j2.xml" ) );

        List<String> args = new ArrayList<String>();

        args.add( "192.168.99.1" );
        args.add( "heri" ); // user
        args.add( "heri" ); // pw

        myClassUnderTest.init( args.toArray( new String[args.size()] ) );
        myClassUnderTest.connectAndLogin();
        myClassUnderTest.uploadFiles( filesToUpload );
        myClassUnderTest.logoutAndDisconnect();

    }

}
