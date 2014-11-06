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



package ch.sdi.core.impl.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.sdi.core.intf.parser.InputParser;


/**
 * TODO
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
public class CsvParser implements InputParser
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CsvParser.class );

//    public static final String HEADING_KEY = "heading";
    public static final String TAB = "\u0009";

    private String myCharset;

    /**
     * @see ch.sdi.core.impl.parser.Test#parse(java.io.InputStream, java.lang.String)
     */
    @Override
    public List<List<String>> parse( InputStream aInputStream, String aDelimiter ) throws IOException
    {
        if ( aDelimiter == null )
        {
            throw new NullPointerException( "Delimiter not set" );
        } // if myDelimiter == null

        BufferedReader br = new BufferedReader( myCharset == null
                                                ? new InputStreamReader( aInputStream )
                                                : new InputStreamReader( aInputStream, myCharset ) );

        List<List<String>> result = new ArrayList<List<String>>();

        int lineNo = 0;
        String line;
        while ( ( line = br.readLine() ) != null )
        {
            lineNo++;
            myLog.debug( "Parsing line " + lineNo + ": " + line );

            List<String> list = new ArrayList<String>();
            Scanner sc = new Scanner( line );
            try
            {
                sc.useDelimiter( aDelimiter );
                while ( sc.hasNext() )
                {
                    list.add( sc.next() );
                }

                result.add( list );
            }
            finally
            {
                sc.close();
            }
        }

        return result;
    }

    /**
     * @return charset
     */
    public String getCharset()
    {
        return myCharset;
    }

    /**
     * @param  aCharset
     *         charset to set
     */
    public void setCharset( String aCharset )
    {
        myCharset = aCharset;
    }

}
