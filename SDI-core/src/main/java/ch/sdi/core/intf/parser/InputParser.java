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



package ch.sdi.core.intf.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;



/**
 * TODO
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
public interface InputParser
{

    /**
     * Parses the given input stream.
     * <p>
     * If the member Charset is set, it will be used. Otherwise the system default character set is used.
     * <p>
     *
     * @param aInputStream
     *        must not be null
     * @param aDelimiter
     *        must not be null
     * @return a list which contains a list for each found line. The inner list contains the found values
     *         in each line.
     * @throws IOException
     */
    public abstract List<List<String>> parse( InputStream aInputStream, String aDelimiter ) throws IOException;

}
