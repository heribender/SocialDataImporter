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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.UserPropertyOverloader;
import ch.sdi.core.TestUtils;
import ch.sdi.core.intf.InputCollectorMappingProperties;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * TODO
 *
 * @version 1.0 (07.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={UserPropertyOverloader.class})
public class UserPropertyOverloaderTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( UserPropertyOverloaderTest.class );
    @Autowired
    private ConfigurableEnvironment  env;
    @Autowired
    private UserPropertyOverloader myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        TestUtils.addToEnvironment( env, "hello", "world" );
    }

    /**
     * Test method for {@link ch.sdi.UserPropertyOverloader#overrideByUserProperties()}.
     */
    @Test
    public void testOverrideByUserProperties()
    {
        myLog.debug( "Calling productive code" );
        myClassUnderTest.overrideByUserProperties();
        // Note: user.InputCollectorMapping.properties should have been found on the classpath and the
        // content should have been entered into the environment
        String name = SdiMainProperties.USER_OVERRIDE_PREFIX
                + ConfigUtils.makePropertyResourceName( InputCollectorMappingProperties.class );
        Assert.assertNotNull( env.getPropertySources().get( name ) );
        Assert.assertEquals( "universe", env.getProperty( "hello" ) );
        Assert.assertEquals( "Screenname", env.getProperty( "inputcollector.thing.alternateName" ) );
    }

}
