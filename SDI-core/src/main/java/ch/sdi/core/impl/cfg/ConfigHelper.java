/**
 * Copyright (c) 2014 Nena1.ch. All rights reserved.
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
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
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import ch.sdi.core.annotations.SdiProps;


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

    public void overrideByUserProperties()
    {
        List<Class<?>> candidates = findCandidates();

        for ( Class<?> clazz : candidates )
        {
            myLog.debug( "examinating property class " + clazz.getName() );
        }

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


}
