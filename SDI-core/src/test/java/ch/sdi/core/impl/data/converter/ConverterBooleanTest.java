/**
 * Copyright (c) 2015 by the original author or authors.
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


package ch.sdi.core.impl.data.converter;

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
import ch.sdi.core.impl.cfg.ConversionServiceProvider;
import ch.sdi.core.intf.FieldConverter;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * Testcase
 *
 * @version 1.0 (23.01.2015)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ ConverterBoolean.class,
                                ConversionServiceProvider.class })
public class ConverterBooleanTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterBooleanTest.class );
    @Autowired
    private ConverterBoolean myClassUnderTest;
    @Autowired
    private ConfigurableEnvironment myEnv;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        myClassUnderTest.init( myEnv, null );
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
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterBoolean#init(org.springframework.core.env.Environment, java.lang.String)}.
     */
    @Test
    public void testInit() throws Throwable
    {
        myLog.debug( "testing init success" );
        FieldConverter<Boolean> received = myClassUnderTest.init( myEnv, null );
        Assert.assertTrue( myClassUnderTest == received );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterBoolean#convert(java.lang.String)}.
     */
    @Test(expected=SdiException.class)
    public void testConvertFail() throws Throwable
    {

        myLog.debug( "testing 'ja' fail" );
        Boolean received = myClassUnderTest.convert( "ja" );
        Assert.assertTrue( received );
        myLog.debug( "testing 'nein' fail" );
        received = myClassUnderTest.convert( "nein" );
        Assert.assertFalse( received );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterBoolean#convert(java.lang.String)}.
     */
    @Test
    public void testConvertSuccess() throws Throwable
    {
        String[] trueValues = new String[] { "true ", " on", "yes", "1" };
        for ( String value : trueValues )
        {
            myLog.debug( "testing 'true' success for built in value " + value );
            Boolean received = myClassUnderTest.convert( value );
            Assert.assertTrue( received );
            received = myClassUnderTest.convert( value.toUpperCase() );
            Assert.assertTrue( received );
        }

        String[] falseValues = new String[] { "false", "off", " no", "0 " };
        for ( String value : falseValues )
        {
            myLog.debug( "testing 'false' success for built in value " + value );
            Boolean received = myClassUnderTest.convert( value );
            Assert.assertFalse( received );
            received = myClassUnderTest.convert( value.toUpperCase() );
            Assert.assertFalse( received );
        }

    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterBoolean#convert(java.lang.String)}.
     */
    @Test
    public void testConvertSuccessSpecial() throws Throwable
    {
        TestUtils.addToEnvironment( myEnv,
                                    SdiMainProperties.KEY_PREFIX_CONVERTER
                                    + ConverterBoolean.CONVERTER_NAME + "."
                                    + ConverterBoolean.KEY_TRUE_VALUES, "ja, OK " );
        TestUtils.addToEnvironment( myEnv,
                                    SdiMainProperties.KEY_PREFIX_CONVERTER
                                    + ConverterBoolean.CONVERTER_NAME + "."
                                    + ConverterBoolean.KEY_FALSE_VALUES, " nein, nok, " );
        myClassUnderTest.init( myEnv, null );

        String[] trueValues = new String[] { "ja ", " ok" };
        for ( String value : trueValues )
        {
            myLog.debug( "testing 'true' success for built in value " + value );
            Boolean received = myClassUnderTest.convert( value );
            Assert.assertTrue( received );
            received = myClassUnderTest.convert( value.toUpperCase() );
            Assert.assertTrue( received );
        }

        String[] falseValues = new String[] { "nein", "nok" };
        for ( String value : falseValues )
        {
            myLog.debug( "testing 'false' success for built in value " + value );
            Boolean received = myClassUnderTest.convert( value );
            Assert.assertFalse( received );
            received = myClassUnderTest.convert( value.toUpperCase() );
            Assert.assertFalse( received );
        }

    }

}
