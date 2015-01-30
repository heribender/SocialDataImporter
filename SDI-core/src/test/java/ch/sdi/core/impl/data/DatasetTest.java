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


package ch.sdi.core.impl.data;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.sdi.core.exc.SdiException;


/**
 * Testcase
 *
 * @version 1.0 (30.01.2015)
 * @author  Heri
 */
public class DatasetTest
{
    /** logger for this class */
    private Logger myLog = LogManager.getLogger( DatasetTest.class );
    @Autowired
    private Dataset myClassUnderTest;
    private List<String> myKeys;
    private List<Object> myValues;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        myKeys = Arrays.asList( new String[] { "key1", "key2" } );
        myValues = Arrays.asList( new Object[] { "value1", "value2" } );
    }

    @Test
    public void test() throws Throwable
    {
        Dataset actual;

        myLog.debug( "Creating Dataset: " + myKeys + " and " + myValues );
        actual = Dataset.create( myKeys, myValues );
        myLog.debug( "Received Dataset: " + actual );
        Assert.assertNotNull( actual );
        Assert.assertEquals( 2, actual.size() );
        Assert.assertEquals( "value1", actual.get( "key1" ) );
        Assert.assertEquals( "value2", actual.get( "key2" ) );
    }

    @Test(expected=SdiException.class)
    public void testKeysNull() throws Throwable
    {
        try
        {
            myLog.debug( "Creating Dataset: " + null + " and " + myValues );
            Dataset.create( null, myValues );
        }
        catch ( SdiException t )
        {
            myLog.debug( "Exception received: " + t.getMessage() );
            throw t;
        }
    }

    @Test(expected=SdiException.class)
    public void testValuesNull() throws Throwable
    {
        try
        {
            myLog.debug( "Creating Dataset: " + myKeys + " and " + null );
            Dataset.create( myKeys, null );
        }
        catch ( SdiException t )
        {
            myLog.debug( "Exception received: " + t.getMessage() );
            throw t;
        }
    }

    @Test(expected=SdiException.class)
    public void testKeySizeBigger() throws Throwable
    {
        try
        {
            myKeys = Arrays.asList( new String[] { "key1", "key2", "Key3" } );
            myLog.debug( "Creating Dataset: " + myKeys + " and " + myValues );
            Dataset.create( myKeys, myValues );
        }
        catch ( SdiException t )
        {
            myLog.debug( "Exception received: " + t.getMessage() );
            throw t;
        }
    }

    @Test(expected=SdiException.class)
    public void testValueSizeBigger() throws Throwable
    {
        try
        {
            myValues = Arrays.asList( new Object[] { "value1", "value2", "Value3" } );
            myLog.debug( "Creating Dataset: " + myKeys + " and " + myValues );
            Dataset.create( myKeys, myValues );
        }
        catch ( SdiException t )
        {
            myLog.debug( "Exception received: " + t.getMessage() );
            throw t;
        }
    }

}
