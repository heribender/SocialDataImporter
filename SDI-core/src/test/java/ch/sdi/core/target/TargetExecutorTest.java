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


package ch.sdi.core.target;

import java.io.File;
import java.lang.reflect.Method;

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
import ch.sdi.core.intf.SdiMainProperties;


/**
 * Testcase
 *
 * @version 1.0 (02.12.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ TargetExecutor.class,
                                DefaultTargetJobContext.class })
public class TargetExecutorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( TargetExecutorTest.class );
    @Autowired
    private TargetExecutor myClassUnderTest;
    @Autowired
    private ConfigurableEnvironment  myEnv;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_OUTPUT_DIR,
                "./output"  );
    }

    @Test
    public void testPrepareOutputDirWithSlash() throws Throwable
    {
        try
        {
            myLog.debug( "Test for outputDir ./output/" );
            TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_FTP_DEST_DIR,
                    "./output/"  );

            Method method = Whitebox.getMethod( TargetExecutor.class, "prepareOutputDir", new Class<?>[0] );
            Assert.assertNotNull( method );
            Object o = method.invoke( myClassUnderTest, new Object[0] );
            Assert.assertNotNull( o );
            Assert.assertTrue( o instanceof File );
            File received = (File) o;
            Assert.assertTrue( received.exists() );
            received.delete();
            File parent = received.getParentFile();
            parent.delete();
        }
        catch ( Throwable t )
        {
            myLog.error( "Exception caught: ", t );
            Assert.fail( t.getMessage() );
        }

    }

    @Test
    public void testPrepareOutputDirNoSlash() throws Throwable
    {
        try
        {
            myLog.debug( "Test for outputDir ./output" );
            TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_FTP_DEST_DIR,
                    "./output/"  );

            Method method = Whitebox.getMethod( TargetExecutor.class, "prepareOutputDir", new Class<?>[0] );
            Assert.assertNotNull( method );
            Object o = method.invoke( myClassUnderTest, new Object[0] );
            Assert.assertNotNull( o );
            Assert.assertTrue( o instanceof File );
            File received = (File) o;
            Assert.assertTrue( received.exists() );
            received.delete();
            File parent = received.getParentFile();
            parent.delete();
        }
        catch ( Throwable t )
        {
            myLog.error( "Exception caught: ", t );
            Assert.fail( t.getMessage() );
        }

    }

    @Test
    public void testPrepareOutputDirExistingRoot() throws Throwable
    {
        try
        {
            myLog.debug( "Test for outputDir ./output" );
            TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_FTP_DEST_DIR,
                    "./output/"  );
            new File( "./output" ).mkdirs();

            Method method = Whitebox.getMethod( TargetExecutor.class, "prepareOutputDir", new Class<?>[0] );
            Assert.assertNotNull( method );
            Object o = method.invoke( myClassUnderTest, new Object[0] );
            Assert.assertNotNull( o );
            Assert.assertTrue( o instanceof File );
            File received = (File) o;
            Assert.assertTrue( received.exists() );
            received.delete();
            File parent = received.getParentFile();
            parent.delete();
        }
        catch ( Throwable t )
        {
            myLog.error( "Exception caught: ", t );
            Assert.fail( t.getMessage() );
        }

    }

}
