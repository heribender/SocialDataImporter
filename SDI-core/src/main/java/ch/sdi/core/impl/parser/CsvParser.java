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
import org.springframework.stereotype.Component;

import ch.sdi.core.intf.parser.InputParser;


/**
 * TODO
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
@Component
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

                // Note: if the line is terminated by the delimiter (last entry not present, the last entry
                // will not appear in the scanned enumeration. Check for this special case:
                if ( line.endsWith( aDelimiter ) )
                {
                    list.add( "" );
                } // if line.endsWith( aDelimiter )

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
