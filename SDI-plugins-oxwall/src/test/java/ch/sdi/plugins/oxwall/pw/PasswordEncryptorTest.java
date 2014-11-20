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

package ch.sdi.plugins.oxwall.pw;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
 *
 * @version 1.0 (01.11.2014)
 * @author Heri
 */
public class PasswordEncryptorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( PasswordEncryptorTest.class );

    private PasswordEncryptor myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
//        logTest();

        myLog.debug( "Creating class under test" );
        myClassUnderTest = new PasswordEncryptor( "53442de4b15f3" );
        long stampFromDB = 1414797228L; // <- unix timestamp (seconds since 1970)
        Date joinStamp = new Date( stampFromDB * 1000 );
        myLog.error( joinStamp );
    }

    /**
     *
     */
    private void logTest()
    {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));        myLog.trace( "Trace: logtest" );
        myLog.debug( "Debug: logtest" );
        myLog.info( "info: logtest" );
        myLog.warn( "Warn: logtest" );
        myLog.error( "Error: logtest" );
        myLog.fatal( "Fatal: logtest" );
    }

    /**
     * Test method for {@link ch.sdi.plugins.oxwall.pw.PasswordEncryptor#encrypt(java.lang.String)}.
     */
    @Test
    public void testEncrypt() throws Throwable
    {
        String expected = "4cd5bed8e904fd9fe005a7099e0bf3abe02884b7c04886b69beddb8d778d4216";
        String input = "heri";
        String actual;

        myLog.info( "encrypting password " + input );
        actual = myClassUnderTest.encrypt( input );
        myLog.debug( "Received: " + actual );
        myLog.debug( "Expected: " + expected );
        Assert.assertEquals( expected, actual );
    }

}