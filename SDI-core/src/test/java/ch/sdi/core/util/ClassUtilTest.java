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

package ch.sdi.core.util;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Component;

import ch.sdi.core.annotations.SdiProps;


/**
 * Test methods for class ClssUtil
 *
 * @version 1.0 (07.11.2014)
 * @author Heri
 */
public class ClassUtilTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ClassUtilTest.class );

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * Test method for {@link ch.sdi.core.util.ClassUtil#findCandidatesByAnnotation(java.lang.Class, java.lang.String)}.
     */
    @Test
    public void testFindCandidatesByAnnotation()
    {
        myLog.debug( "we parse all classes which are below the top level package" );
        String pack = this.getClass().getPackage().getName();
        myLog.debug( "found package: " + pack );
        pack = pack.replace( '.', '/' );
        String root = pack.split( "/" )[0];

        Collection<? extends Class<?>> received = ClassUtil.findCandidatesByAnnotation( SdiProps.class, root );

        Assert.assertNotNull( received );
        Assert.assertTrue( received.size() > 0 );
        received.forEach( c -> myLog.debug( "Found class with annotation @SdiProps: " + c.getName() ) );

    }

    /**
     * Test method for {@link ch.sdi.core.util.ClassUtil#findCandidatesByAnnotation(java.lang.Class, java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testFindCandidatesByAnnotationEmptyRoot()
    {
        myLog.debug( "now from the root package -> should throw" );
        ClassUtil.findCandidatesByAnnotation( Component.class, "" );
    }

    /**
     * Test method for {@link ch.sdi.core.util.ClassUtil#findCandidatesByAnnotation(java.lang.Class, java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testFindCandidatesByAnnotationSpringFramework()
    {
        myLog.debug( "now from the package 'org.springframework' -> should throw" );
        ClassUtil.findCandidatesByAnnotation( Component.class, "org.springframework" );
    }

}
