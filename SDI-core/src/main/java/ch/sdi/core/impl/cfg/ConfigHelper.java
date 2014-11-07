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
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import ch.sdi.core.annotations.SdiProps;
import ch.sdi.core.intf.cfg.SdiProperties;


/**
 * TODO
 *
 * @version 1.0 (04.11.2014)
 * @author Heri
 */
@Component
public class ConfigHelper
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConfigHelper.class );

    /**
     * Converts the given classname into a properties file name according following rules:
     *
     * <ul>
     *     <li> if class name ends with "Properties" this suffix will be truncated and replaced by
     *     ".properties"</li>
     *     <li> any other class name is used as is and suffixed with ".properties"</li>
     * </ul>
     *
     */
    public static String makePropertyResourceName( Class<? extends SdiProperties> aClass )
    {
        String classname = aClass.getSimpleName();

        if ( classname.endsWith( "Properties" ) || classname.endsWith( "properties" ) )
        {
            return classname.substring( 0, classname.length() - "Properties".length() ) + ".properties";
        } // if condition

        return classname + ".properties";

    }

    public void overrideByUserProperties()
    {
        List<Class<?>> candidates = findCandidates();

        for ( Class<?> clazz : candidates )
        {
            myLog.debug( "examinating property class " + clazz.getName() );
        }

        // TODO: implemen overriding
    }

    /**
     * @return
     */
    private List<Class<?>> findCandidates()
    {
        List<Class<?>> result = new ArrayList<Class<?>>();

        // we parse all classes which are below the top level package:
        String pack = this.getClass().getPackage().getName();
        myLog.debug( "found package: " + pack );
        pack = pack.replace( '.', '/' );
        String root = pack.split( "/" )[0];

        result.addAll( findCandidatesByAnnotation( SdiProps.class, root ) );

        return result;
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
     *        the package name where to start the search.
     * @return a list of found types
     */
    private Collection<? extends Class<?>> findCandidatesByAnnotation( Class<? extends Annotation> aAnnotation,
                                                                       String aRoot )
    {
        List<Class<?>> result = new ArrayList<Class<?>>();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider( false )
        {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)
            {
                return true;
            }
        };

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

    /**
     * Tries to read the value from the given environment and to convert it to an int.
     * <p>
     * If the converstion fails, the default value is returned.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @param aDefault
     * @return
     */
    public static int getIntProperty( Environment aEnv, String aKey, int aDefault )
    {
        try
        {
            return Integer.valueOf( aEnv.getProperty( aKey ) ).intValue();
        }
        catch ( Exception t )
        {
            return aDefault;
        }
    }

    /**
     * Tries to read the value from the given environment and to convert it to a boolean.
     * <p>
     * If the converstion fails, the default value is returned.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @param aDefault
     * @return
     */
    public static boolean getBooleanProperty( ConfigurableEnvironment aEnv,
                                              String aKey,
                                              boolean aDefault )
    {
        try
        {
            return Boolean.valueOf( aEnv.getProperty( aKey ) ).booleanValue();
        }
        catch ( Exception t )
        {
            return aDefault;
        }
    }


}
