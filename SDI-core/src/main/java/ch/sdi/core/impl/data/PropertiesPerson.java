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



package ch.sdi.core.impl.data;

import java.util.Map;
import java.util.Properties;


/**
 * A person implementation which takes a java.util.properties as data source.
 * <p>
 *
 * @version 1.0 (07.11.2014)
 * @author  Heri
 */
public class PropertiesPerson extends Person<Properties>
{

    /**
     * Constructor
     *
     * @param aName
     * @param aSource
     */
    public PropertiesPerson( String aName, Properties aSource )
    {
        super( aName, (Map<Object, Object>) aSource );
    }

}
