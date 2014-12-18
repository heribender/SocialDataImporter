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


package ch.sdi.core.impl.data.converter;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Testcase for ConverterDate
 *
 * @version 1.0 (11.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ ConverterDate.class })
public class ConverterDateTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterDateTest.class );
    @Autowired
    private ConverterDate myClassUnderTest;
    @Autowired
    private ApplicationContext myAppCtxt;


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {

    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterDate#convert(java.lang.String)}.
     */
    @Test
    public void testPrototype() throws Throwable
    {
        myClassUnderTest = myAppCtxt.getBean( ConverterDate.class );
        ConverterDate newInstance = myAppCtxt.getBean( ConverterDate.class );
        Assert.assertTrue( myClassUnderTest != newInstance );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterDate#convert(java.lang.String)}.
     */
    @Test
    public void testConvert() throws Throwable
    {
        long expected = -404614800000L;
        myClassUnderTest.setDatePattern( "dd.MM.yy" );
        Date actual = myClassUnderTest.convert( "7.3.57" );
        long time = actual.getTime();
        myLog.debug( "actual: " + actual );
        Assert.assertNotNull( actual );
        myLog.debug( "actual.time: " + time );
        Assert.assertEquals( expected, actual.getTime() );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterDate#convert(java.lang.String)}.
     */
    @Test
    public void testConvertAsBean() throws Throwable
    {
        myClassUnderTest = myAppCtxt.getBean( ConverterDate.class );
        long expected = -404614800000L;
        myClassUnderTest.setDatePattern( "dd.MM.yy" );
        Date actual = myClassUnderTest.convert( "07.03.57" );
        long time = actual.getTime();
        myLog.debug( "actual: " + actual );
        Assert.assertNotNull( actual );
        myLog.debug( "actual.time: " + time );
        Assert.assertEquals( expected, actual.getTime() );
    }

}
