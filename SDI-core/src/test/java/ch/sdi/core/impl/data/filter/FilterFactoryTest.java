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


package ch.sdi.core.impl.data.filter;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.annotations.SdiFilter;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Dataset;
import ch.sdi.core.impl.data.filter.FilterFactoryTest.TestFilter;
import ch.sdi.core.impl.data.filter.FilterFactoryTest.TestFilter2;


/**
 * Testcase
 *
 * @version 1.0 (30.01.2015)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={FilterFactory.class,
                               FilterTrue.class,
                               TestFilter.class,
                               TestFilter2.class })
public class FilterFactoryTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( FilterFactoryTest.class );
    @Autowired
    private FilterFactory myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.filter.FilterFactory#getFilter(java.lang.String)}.
     */
    @Test
    public void testGetFilter() throws Throwable
    {
        String params;
        CollectFilter<?> actual;

        params = "trueField:deny";
        myLog.debug( "Fetching filter for params: " + params );
        actual = myClassUnderTest.getFilter( params );
        myLog.debug( "Received filter: " + actual );
        Assert.assertNotNull( actual );

        params = "testField:deny:moreParams:oneMore";
        myLog.debug( "Fetching filter for params: " + params );
        actual = myClassUnderTest.getFilter( params );
        myLog.debug( "Received filter: " + actual );
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual instanceof TestFilter );
        TestFilter testFilter = (TestFilter) actual;
        Assert.assertEquals( "moreParams:oneMore", testFilter.myAdditionalParams );
    }

    @Test(expected=SdiException.class)
    public void testGetFilterTooLessParams() throws Throwable
    {
        String params;
        params = "trueField";
        myLog.debug( "Fetching filter for params: " + params );
        try
        {
            myClassUnderTest.getFilter( params );
        }
        catch ( SdiException t )
        {
            myLog.debug( "Exception received: " + t.getMessage() );
            throw t;
        }
    }

    @Test(expected=SdiException.class)
    public void testGetFilterNoBean() throws Throwable
    {
        String params;
        params = "blablaFilter:bla";
        myLog.debug( "Fetching filter for params: " + params );
        try
        {
            myClassUnderTest.getFilter( params );
        }
        catch ( SdiException t )
        {
            myLog.debug( "Exception received: " + t.getMessage() );
            throw t;
        }
    }

    @Test(expected=SdiException.class)
    public void testGetFilterWrongType() throws Throwable
    {
        String params;
        params = "testDatasetFilter:bla";
        myLog.debug( "Fetching filter for params: " + params );
        try
        {
            myClassUnderTest.getFilter( params );
        }
        catch ( SdiException t )
        {
            myLog.debug( "Exception received: " + t.getMessage() );
            throw t;
        }
    }



    @SdiFilter( "testField" )
    @Component
    static class TestFilter extends CollectFilter<String>
    {

        String myAdditionalParams;


        /**
         * @see ch.sdi.core.impl.data.filter.CollectFilter#isFiltered(ch.sdi.core.impl.data.Dataset)
         */
        @Override
        public boolean isFiltered( Dataset aDataset ) throws SdiException
        {
            return false;
        }

        /**
         * @see ch.sdi.core.impl.data.filter.CollectFilter#getFieldValue(ch.sdi.core.impl.data.Dataset)
         */
        @Override
        protected String getFieldValue( Dataset aDataset )
        {
            return null;
        }

        /**
         * @see ch.sdi.core.impl.data.filter.CollectFilter#init(org.springframework.core.env.Environment, java.lang.String, java.lang.String)
         */
        @Override
        public CollectFilter<String> init( String aFieldname, String aParameters )
                throws SdiException
        {
            myAdditionalParams = aParameters;
            return super.init( aFieldname, aParameters );
        }

    }

    @SdiFilter( "testDatasetFilter" )
    @Component
    static class TestFilter2 extends Date
    {
        private static final long serialVersionUID = 1L;
    }

}
