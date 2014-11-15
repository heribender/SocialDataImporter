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

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.annotations.SdiConverter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.converter.ConverterFactoryTest.AnnotherConverter;
import ch.sdi.core.intf.FieldConverter;


/**
 * TODO
 *
 * @version 1.0 (09.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ConverterFactory.class,
                               ConverterDate.class,
                               AnnotherConverter.class })
public class ConverterFactoryTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterFactoryTest.class );
    @Autowired
    private ConfigurableEnvironment  env;
    @Autowired
    private ConverterFactory myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        TestUtils.addToEnvironment( env, "sdi.converter.dateField",
                                         ConverterDate.CONVERTER_NAME  );
        TestUtils.addToEnvironment( env, "sdi.converter.anotherDateField",
                                         ConverterDate.CONVERTER_NAME  );
        TestUtils.addToEnvironment( env, "sdi.converter." + ConverterDate.CONVERTER_NAME + ".pattern",
                                         "yyyy-MM-dd"  );
        TestUtils.addToEnvironment( env, "sdi.converter." + ConverterDate.CONVERTER_NAME + ".dateField.pattern",
                                         "yyyy-MM-dd hh:mm:ss"  );
        TestUtils.addToEnvironment( env, "sdi.converter.custom",
                                         "customConverter"  );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterFactory#getConverterFor(java.lang.String)}.
     */
    @Test
    public void testGetConverterFor() throws Throwable
    {
        FieldConverter<?> actual;
        ConverterDate convDate;
        String expected;

        myLog.debug( "Fetching converter for dateField" );
        expected = "yyyy-MM-dd hh:mm:ss";
        actual = myClassUnderTest.getConverterFor( "dateField" );
        myLog.debug( "Received converter: " + actual );
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual instanceof ConverterDate );
        convDate = (ConverterDate) actual;
        Assert.assertEquals( expected, convDate.getDatePattern() );

        myLog.debug( "Fetching converter for anotherDateField" );
        expected = "yyyy-MM-dd";
        actual = myClassUnderTest.getConverterFor( "anotherDateField" );
        myLog.debug( "Received converter: " + actual );
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual instanceof ConverterDate );
        convDate = (ConverterDate) actual;
        Assert.assertEquals( expected, convDate.getDatePattern() );

        myLog.debug( "Fetching a non declared field converter -> ConverterString" );
        actual = myClassUnderTest.getConverterFor( "blabla" );
        myLog.debug( "Received converter: " + actual );
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual instanceof ConverterString );

    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterFactory#getConverterFor(java.lang.String)}.
     */
    @Test
    public void testNewInstance() throws Throwable
    {
        FieldConverter<?> actual1;
        FieldConverter<?> actual2;

        myLog.debug( "Fetching converter for dateField (1)" );
        actual1 = myClassUnderTest.getConverterFor( "dateField" );
        myLog.debug( "Received converter (1): " + actual1 );
        Assert.assertNotNull( actual1 );

        myLog.debug( "Fetching converter for dateField (2)" );
        actual2 = myClassUnderTest.getConverterFor( "dateField" );
        myLog.debug( "Received converter (2): " + actual2 );
        Assert.assertNotNull( actual2 );

        Assert.assertTrue( actual1 != actual2 );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterFactory#getConverterFor(java.lang.String)}.
     */
    @Test
    public void testGetConverterForCustomType() throws Throwable
    {
        myLog.debug( "testing 'customConverter'" );
        FieldConverter<?> actual = myClassUnderTest.getConverterFor( "custom" );
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual instanceof AnnotherConverter );
    }

    @SdiConverter( "customConverter" )
    static class AnnotherConverter implements FieldConverter<InputStream>
    {
        /**
         * Constructor
         *
         */
        public AnnotherConverter()
        {
            super();
        }


        /**
         * @see ch.sdi.core.intf.FieldConverter#init(org.springframework.core.env.Environment, java.lang.String)
         */
        @Override
        public AnnotherConverter init( Environment aEnv, String aFieldname ) throws SdiException
        {
            return this;
        }

        /**
         * @see ch.sdi.core.intf.FieldConverter#convert(java.lang.String)
         */
        @Override
        public InputStream convert( String aValue ) throws SdiException
        {
            return null;
        }

    }
}
