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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;


/**
 * TODO
 *
 * @version 1.0 (07.11.2014)
 * @author  Heri
 */
public class ClassUtil
{

    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( ClassUtil.class );

    /**
     * Constructor
     *
     */
    public ClassUtil()
    {
        super();
    }

    /**
     * Lists all types in the given package (recursive) which are annotated by the given annotation.
     * <p>
     * All types which match the criteria are returned, no further checks (interface, abstract, embedded, etc.
     * are performed.
     * <p>
     *
     * @param aAnnotation
     *        the desired annotation type
     * @param aRoot
     *        the package name where to start the search. Must not be empty. And not start
     *        with 'org.springframework' (cannot parse springs library itself).
     * @return a list of found types
     */
    public static Collection<? extends Class<?>> findCandidatesByAnnotation(
                                                             Class<? extends Annotation> aAnnotation,
                                                             String aRoot )
    {
        if ( !StringUtils.hasText( aRoot ) )
        {
            throw new IllegalArgumentException( "aRoot must not be empty (cannot parse spring library classes)" );

        } // if StringUtils.hasText( aRoot )

        if ( aRoot.startsWith( "org.springframework" ) )
        {
            throw new IllegalArgumentException( "cannot parse spring library classes" );

        } // if StringUtils.hasText( aRoot )

        List<Class<?>> result = new ArrayList<Class<?>>();

        MyClassScanner scanner = new MyClassScanner();

        scanner.addIncludeFilter( new AnnotationTypeFilter( aAnnotation ) );
        Set<BeanDefinition> canditates = scanner.findCandidateComponents( aRoot );
        for ( BeanDefinition beanDefinition : canditates )
        {
            try
            {
                String classname = beanDefinition.getBeanClassName();
                Class<?> clazz = Class.forName( classname );
                result.add( clazz );
            }
            catch ( ClassNotFoundException t )
            {
                myLog.error( "Springs type scanner returns a class name whose class cannot be evaluated!!!", t );
            }
        }

        return result;
    }


    static class MyClassScanner extends ClassPathScanningCandidateComponentProvider
    {
        /**
         * Constructor
         */
        public MyClassScanner()
        {
            super( false );
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)
        {
            return true;
        }
    };


}
