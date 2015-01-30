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

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.sdi.core.impl.data.Dataset;


/**
 * Testcase
 *
 * @version 1.0 (30.01.2015)
 * @author  Heri
 */
public class FilterTrueTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( FilterTrueTest.class );

    private FilterTrue myClassUnderTest;
    private List<String> myKeys;
    private List<Object> myValues;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        myClassUnderTest = new FilterTrue();
        myKeys = Arrays.asList( new String[] { "key1", "key2" } );
        myValues = Arrays.asList( new Object[] { Boolean.TRUE, Boolean.FALSE } );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.filter.FilterTrue#isFiltered(ch.sdi.core.impl.data.Dataset)}.
     */
    @Test
    public void testIsFiltered() throws Throwable
    {
        String key;
        Boolean actual;
        Dataset dataset = Dataset.create( myKeys, myValues );

        key = "key1";
        myLog.debug( "Testing FilterTrue for dataset: " + dataset + " and key" + key );
        myClassUnderTest.init( key, null );
        actual = myClassUnderTest.isFiltered( dataset );
        myLog.debug( "Received: " + actual );
        Assert.assertNotNull( actual );
        Assert.assertTrue( actual );

        key = "key2";
        myLog.debug( "Testing FilterTrue for dataset: " + dataset + " and key" + key );
        myClassUnderTest.init( key, null );
        actual = myClassUnderTest.isFiltered( dataset );
        myLog.debug( "Received: " + actual );
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual );

    }

}
