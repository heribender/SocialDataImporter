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



package ch.sdi.core.intf;

import ch.sdi.core.annotations.SdiProps;
import ch.sdi.core.impl.cfg.ConfigHelper;
import ch.sdi.core.intf.cfg.SdiProperties;


/**
 * Configuration interface for the ParserMapping.properties.<p>
 *
 * The expected property names are defined in {@link ch.sdi.core.impl.data.Person}.<p>
 *
 * Because this interface is annotated with @SdiProps it is subject to the automatic mechanisme for
 * overloading default properties with user defined properties (see {@link ch.sdi.core.annotations.SdiProps}).
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@SdiProps()
public interface ParserMappingProperties extends SdiProperties
{

    public static final String RESOURCE_NAME =
            ConfigHelper.makePropertyResourceName( ParserMappingProperties.class );

}
