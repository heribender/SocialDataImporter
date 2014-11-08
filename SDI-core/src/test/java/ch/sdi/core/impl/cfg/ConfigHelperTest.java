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

import ch.sdi.core.intf.InputCollectorMappingProperties;
import ch.sdi.core.intf.cfg.SdiProperties;


/**
 * TODO
 *
 * @version 1.0 (04.11.2014)
 * @author  Heri
 */
public class ConfigHelperTest
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConfigHelperTest.class );
//    private ConfigHelper myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
//        myClassUnderTest = new ConfigHelper();
    }

    /**
     * Test method for {@link ch.sdi.core.impl.cfg.ConfigHelper#makePropertyResourceName(Class)}.
     */
    @Test
    public void testMakePropertyResourceName()
    {
        String actual = ConfigHelper.makePropertyResourceName( InputCollectorMappingProperties.class );
        myLog.debug( "Received: " + actual );
        Assert.assertEquals( "InputCollectorMapping.properties", actual );

        actual = ConfigHelper.makePropertyResourceName( NonStandardNamedPropertiesDerivation.class );
        myLog.debug( "Received: " + actual );
        Assert.assertEquals( "NonStandardNamedPropertiesDerivation.properties", actual );
    }

    public interface NonStandardNamedPropertiesDerivation extends SdiProperties {}
}
