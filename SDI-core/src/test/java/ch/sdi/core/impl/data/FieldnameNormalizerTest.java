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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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


/**
 * Testcase
 *
 * @version 1.0 (02.01.2015)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={FieldnameNormalizer.class })
public class FieldnameNormalizerTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( FieldnameNormalizerTest.class );
    @Autowired
    private ConfigurableEnvironment myEnv;
    @Autowired
    private FieldnameNormalizer myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
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
     * Test method for {@link ch.sdi.core.impl.data.FieldnameNormalizer#normalize(java.util.Collection)}.
     */
    @Test
    public void testNormalize() throws Throwable
    {
        myLog.debug( "testing custom field Screenname normalized to thing.alternateName" );
        TestUtils.addToEnvironment( myEnv, "inputcollector.thing.alternateName", "Screenname" );
        List<String> fieldnames = new ArrayList<>();
        fieldnames.add( "Screenname" );
        Collection<String> received = myClassUnderTest.normalize( fieldnames );
        myLog.debug( "received: " + received );
        Assert.assertNotNull( received );
        Assert.assertEquals( 1, received.size() );
        Assert.assertEquals( "thing.alternateName", received.stream().findFirst().get() );

    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.FieldnameNormalizer#normalize(java.util.Collection)}.
     */
    @Test
    public void testAlreadyNormalized() throws Throwable
    {
        myLog.debug( "testing field thing.alternateName is already normalized" );
        List<String> fieldnames = new ArrayList<>();
        fieldnames.add( "thing.alternateName" );
        Collection<String> received = myClassUnderTest.normalize( fieldnames );
        myLog.debug( "received: " + received );
        Assert.assertNotNull( received );
        Assert.assertEquals( 1, received.size() );
        Assert.assertEquals( "thing.alternateName", received.stream().findFirst().get() );

    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.FieldnameNormalizer#normalize(java.util.Collection)}.
     */
    @Test(expected=SdiException.class)
    public void testNormalizeWrongConfigured() throws Throwable
    {
        myLog.debug( "testing field Screenname with wrong configured normalized key" );
        TestUtils.addToEnvironment( myEnv, "inputcollector.thing.blabla", "Screenname" );
        List<String> fieldnames = new ArrayList<>();
        fieldnames.add( "Screenname" );
        try
        {
            myClassUnderTest.normalize( fieldnames );
        }
        catch ( SdiException t )
        {
            myLog.info( "Expected Exception received: " + t.getMessage() );
            throw t;
        }
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.FieldnameNormalizer#normalize(java.util.Collection)}.
     */
    @Test(expected=SdiException.class)
    public void testNormalizeNotConfigured() throws Throwable
    {
        myLog.debug( "testing field Screenname not configured" );
        List<String> fieldnames = new ArrayList<>();
        fieldnames.add( "Screenname" );
        try
        {
            myClassUnderTest.normalize( fieldnames );
        }
        catch ( SdiException t )
        {
            myLog.info( "Expected Exception received: " + t.getMessage() );
            throw t;
        }
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.FieldnameNormalizer#normalize(java.util.Collection)}.
     */
    @Test(expected=SdiException.class)
    public void testNormalizeNotCovered() throws Throwable
    {
        myLog.debug( "testing given fieldnames do not cover all configured mappings" );
        TestUtils.addToEnvironment( myEnv, "inputcollector.thing.alternateName", "Screenname" );
        try
        {
            myClassUnderTest.normalize( new ArrayList<>() );
        }
        catch ( SdiException t )
        {
            myLog.info( "Expected Exception received: " + t.getMessage() );
            throw t;
        }
    }

}
