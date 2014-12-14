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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StreamUtils;

import ch.sdi.core.TestUtils;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * Tests the FtpExecutor with anonymous login, username/password without SLL and with implicite SSL by
 * uploading two files (test resources) to the target "./../testTarget"
 * <p>
 * The test opens an embedded FTPServer as counterpart for our FTPClient. For SSL the server needs a
 * certificate which it finds in src\test\Resources\keystore.jks (password is "password").
 * <p>
 *
 * @version 1.0 (22.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={FtpExecutor.class })
public class FtpExecutorTest implements ApplicationContextAware
{

    /** */
    private static final String TEST_TARGET_DIR_LOCAL = "./../testTarget/";
    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( FtpExecutorTest.class );
    @Autowired
    private ConfigurableEnvironment  myEnv;
    private FtpExecutor myClassUnderTest;
    private static String myTargetDirLocal;
    private static List<Authority> myFtpAuthorities;
    private FtpServerFactory myServerFactory;
    private static ApplicationContext myCtx = null;

    @Override
    public void setApplicationContext( ApplicationContext aCtx ) throws BeansException
    {
        myCtx = aCtx;
    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpStatic() throws Exception
    {
        myTargetDirLocal = new File( TEST_TARGET_DIR_LOCAL ).getCanonicalPath() + File.separator;
        myFtpAuthorities = new ArrayList<Authority>();
        myFtpAuthorities.add(new WritePermission());
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void setTearDownStatic() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        myClassUnderTest = myCtx.getBean( FtpExecutor.class );
        myServerFactory = new FtpServerFactory();
    }

    /**
     * Test method for {@link ch.sdi.core.ftp.FtpExecutor#executeUpload(java.io.InputStream, java.lang.String)}.
     */
    @Test
    public void testInitBySpring() throws Throwable
    {
        myLog.debug( "Testing self-initialize by Spring context" );

        String targetDir = myTargetDirLocal;
        cleanTargetDir( targetDir );
        Map<String, InputStream> filesToUpload = createFileUploadMap( targetDir );

        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_FTP_CMD_LINE,
                                    "-A localhost"  );

        registerFtpUser( "anonymous",
                         System.getProperty( "user.name" ) + "@" + InetAddress.getLocalHost().getHostName() );

        FtpServer server = startFtpServer();
        try
        {
            // omit call to init in order to auto initialize by spring context
            myClassUnderTest.connectAndLogin();
            myClassUnderTest.uploadFiles( filesToUpload );
            myClassUnderTest.logoutAndDisconnect();
            assertFilesUploaded( createFileUploadMap( targetDir ) );
        }
        finally
        {
            if ( server != null )
            {
                myLog.debug( "stopping the embedded FTP server" );
                server.stop();
            } // if myServer != null
        }
    }

    /**
     * Test method for {@link ch.sdi.core.ftp.FtpExecutor#executeUpload(java.io.InputStream, java.lang.String)}.
     */
    @Test
    public void testUploadAnonymous() throws Throwable
    {
        myLog.debug( "Testing Anonymous login" );

        String targetDir = myTargetDirLocal;
        cleanTargetDir( targetDir );
        Map<String, InputStream> filesToUpload = createFileUploadMap( targetDir );

        List<String> args = new ArrayList<String>();
        args.add( "-bla" ); // invalid option should be ignored
        args.add( "-A" ); // anonymous
        args.add( "localhost" );

        registerFtpUser( "anonymous",
                         System.getProperty( "user.name" ) + "@" + InetAddress.getLocalHost().getHostName() );

        FtpServer server = startFtpServer();
        try
        {
            myClassUnderTest.init( args.toArray( new String[args.size()] ) );
            myClassUnderTest.connectAndLogin();
            myClassUnderTest.uploadFiles( filesToUpload );
            myClassUnderTest.logoutAndDisconnect();
            assertFilesUploaded( createFileUploadMap( targetDir ) );
        }
        finally
        {
            if ( server != null )
            {
                myLog.debug( "stopping the embedded FTP server" );
                server.stop();
            } // if myServer != null
        }
    }

    /**
     * Test method for {@link ch.sdi.core.ftp.FtpExecutor#executeUpload(java.io.InputStream, java.lang.String)}.
     */
    @Test
    public void testUploadLogin() throws Throwable
    {
        myLog.debug( "Testing normal login" );

        String targetDir = myTargetDirLocal;
        cleanTargetDir( targetDir );
        Map<String, InputStream> filesToUpload = createFileUploadMap( targetDir );

        List<String> args = new ArrayList<String>();
        args.add( "localhost" );
        args.add( "heri" ); // user
        args.add( "heri" ); // pw

        registerFtpUser( "heri", "heri" );

        FtpServer server = startFtpServer();
        try
        {
            myClassUnderTest.init( args.toArray( new String[args.size()] ) );
            myClassUnderTest.connectAndLogin();
            myClassUnderTest.uploadFiles( filesToUpload );
            myClassUnderTest.logoutAndDisconnect();
            assertFilesUploaded( createFileUploadMap( targetDir ) );
        }
        finally
        {
            if ( server != null )
            {
                myLog.debug( "stopping the embedded FTP server" );
                server.stop();
            } // if myServer != null
        }
    }

    /**
     * Test method for {@link ch.sdi.core.ftp.FtpExecutor#executeUpload(java.io.InputStream, java.lang.String)}.
     */
    @Test
    public void testUploadLoginSSLImplicite() throws Throwable
    {
        myLog.debug( "Testing SSL login (implicite)" );

        String targetDir = myTargetDirLocal;
        cleanTargetDir( targetDir );
        Map<String, InputStream> filesToUpload = createFileUploadMap( targetDir );

        List<String> args = new ArrayList<String>();
        args.add( "-p" );
        args.add( "false" );  // activate implicite SSL
        args.add( "localhost" );
        args.add( "heri" ); // user
        args.add( "heri" ); // pw

        ListenerFactory listenerFactory = new ListenerFactory();

        // define SSL configuration
        SslConfigurationFactory ssl = new SslConfigurationFactory();
        ssl.setKeystoreFile(new File("keystore.jks"));  // this is in core/test/resources and contains one
                                                        // selfsigned certificate
        ssl.setKeystorePassword("password");
        listenerFactory.setSslConfiguration(ssl.createSslConfiguration());
        listenerFactory.setImplicitSsl(false);

        // replace the default listener
        Listener listenerOrg = myServerFactory.getListener( "default" );
        try
        {
            myServerFactory.addListener("default", listenerFactory.createListener());

            registerFtpUser( "heri", "heri" );

            FtpServer server = startFtpServer();
            try
            {
                myClassUnderTest.init( args.toArray( new String[args.size()] ) );
                myClassUnderTest.connectAndLogin();
                myClassUnderTest.uploadFiles( filesToUpload );
                myClassUnderTest.logoutAndDisconnect();
                assertFilesUploaded( createFileUploadMap( targetDir ) );
            }
            finally
            {
                if ( server != null )
                {
                    myLog.debug( "stopping the embedded FTP server" );
                    server.stop();
                } // if myServer != null
            }
        }
        finally
        {
            myServerFactory.addListener("default", listenerOrg );
        }
    }

    /**
     * @param aCreateFileUploadMap
     */
    private void assertFilesUploaded( Map<String, InputStream> aCreateFileUploadMap )
    {
        for ( String targetFileName : aCreateFileUploadMap.keySet() )
        {
            assertFileUpdloaded( targetFileName, aCreateFileUploadMap.get( targetFileName ) );
        }
    }

    /**
     * @param aTargetFileName
     * @param aInputStream
     */
    private void assertFileUpdloaded( String aTargetFileName, InputStream aInputStream )
    {
        File target = new File( aTargetFileName );
        Assert.assertTrue( target.exists() );
        byte[] input = copyStreamToByteArray( aInputStream );
        byte[] output = null;
        try
        {
            output = copyStreamToByteArray( new FileInputStream( target ) );
        }
        catch ( FileNotFoundException t )
        {
            myLog.error( "Should not occur", t );
            Assert.fail( "FileNotFoundException while opening file " + target.getPath() );
        }
        Assert.assertArrayEquals( input, output );


    }

    /**
     * @param aInputStream
     * @return
     */
    private byte[] copyStreamToByteArray( InputStream aInputStream )
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            StreamUtils.copy( aInputStream, baos );
        }
        catch ( IOException t )
        {
            Assert.fail( "IOException while copying stream. " + t );
        }
        byte[] input = baos.toByteArray();
        return input;
    }

    /**
     * @param aTargetDir
     */
    private void cleanTargetDir( String aTargetDir )
    {
        File[] list = new File( aTargetDir ).listFiles();
        for ( File file : list )
        {
            file.delete();
        }

    }

    /**
     * @param aUsername
     * @param aPassword
     * @throws FtpException
     */
    private void registerFtpUser( String aUsername, String aPassword ) throws FtpException
    {
        BaseUser user = new BaseUser();
        user.setName( aUsername );
        user.setPassword( aPassword );
        user.setAuthorities(myFtpAuthorities);
        UserManager userManager = myServerFactory.getUserManager();
        userManager.save(user);
    }

    /**
     * @return
     * @throws FtpException
     */
    private FtpServer startFtpServer() throws FtpException
    {
        FtpServer result;
        result = myServerFactory.createServer();

        myLog.debug( "starting an embedded FTP server" );
        result.start();
        return result;
    }

    /**
     * @param aTargetDir
     * @return
     */
    private Map<String, InputStream> createFileUploadMap( String aTargetDir )
    {
        Map<String,InputStream> filesToUpload = new TreeMap<String,InputStream>();

        filesToUpload.put( aTargetDir + "sdimain_test.properties",
                           ClassLoader.getSystemResourceAsStream( "sdimain.properties" ) );
        filesToUpload.put( aTargetDir + "log4j2.xml",
                           ClassLoader.getSystemResourceAsStream( "log4j2.xml" ) );
        return filesToUpload;
    }

}
