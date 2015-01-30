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

package ch.sdi.core.impl.cfg;


import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.ftp.FtpExecutor;
import ch.sdi.core.intf.InputCollectorMappingProperties;
import ch.sdi.core.intf.SdiProperties;


/**
 * Testcase for
 *
 * @version 1.0 (04.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={FtpExecutor.class })
public class ConfigUtilsTest
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConfigUtilsTest.class );
    @Autowired
    private ConfigurableEnvironment myEnv;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
//        myClassUnderTest = new ConfigHelper();
    }

    @After
    public void tearDownUp() throws Exception
    {
        TestUtils.removeAllFromEnvironment( myEnv );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.cfg.ConfigUtils#makePropertyResourceName(Class)}.
     */
    @Test
    public void testMakePropertyResourceName()
    {
        String actual = ConfigUtils.makePropertyResourceName( InputCollectorMappingProperties.class );
        myLog.debug( "Received: " + actual );
        Assert.assertEquals( "InputCollectorMapping.properties", actual );

        actual = ConfigUtils.makePropertyResourceName( NonStandardNamedPropertiesDerivation.class );
        myLog.debug( "Received: " + actual );
        Assert.assertEquals( "NonStandardNamedPropertiesDerivation.properties", actual );
    }

    public interface NonStandardNamedPropertiesDerivation extends SdiProperties {}

    /**
     * Test method for {@link ch.sdi.core.impl.cfg.ConfigUtils#getPropertyNamesStartingWith(Class)}.
     */
    @Test
    public void testGetPropertyNamesStartingWith()
    {
        Properties props1 = new Properties();
        PropertiesPropertySource pps1 = new PropertiesPropertySource( "Props1", props1 );
        props1.put( "sdi.collect.comment.2", "//" );
        props1.put( "sdi.collect.comment.1", "#" );
        props1.put( "key", "value" );

        Properties props2 = new Properties();
        PropertiesPropertySource pps2 = new PropertiesPropertySource( "Props2", props2 );
        props1.put( "sdi.collect.comment.2", ";" );
        props1.put( "sdi.collect.comment.1", "?" );

        MutablePropertySources mps = myEnv.getPropertySources();
        mps.addFirst( pps1 );
        mps.addLast( pps2 );

        Collection<String> received = ConfigUtils.getPropertyNamesStartingWith( myEnv,
                                                                                "sdi.collect.comment." );
        Assert.assertNotNull( received );
        myLog.debug( "received: " + received );
        Assert.assertEquals( 2, received.size() );
        // assert also if the retrieved keys are sorted:
        int i = 1;
        for ( Iterator<String> iterator = received.iterator(); iterator.hasNext(); )
        {
            String key = iterator.next();
            Assert.assertEquals( "sdi.collect.comment." + i, key );
            i++;
        }

        myLog.debug( "testing composite property source" );
        CompositePropertySource cps = new CompositePropertySource( "CompositeTest" );
        cps.addPropertySource( pps1 );
        cps.addPropertySource( pps2 );

        TestUtils.removeAllFromEnvironment( myEnv );
        mps = myEnv.getPropertySources();
        mps.addFirst( pps1 );

        received = ConfigUtils.getPropertyNamesStartingWith( myEnv,
                                                             "sdi.collect.comment." );
        Assert.assertNotNull( received );
        myLog.debug( "received: " + received );
        Assert.assertEquals( 2, received.size() );
        for ( Iterator<String> iterator = received.iterator(); iterator.hasNext(); )
        {
            String key = iterator.next();
            Assert.assertTrue( key.startsWith( "sdi.collect.comment." ) );
        }


    }


}
