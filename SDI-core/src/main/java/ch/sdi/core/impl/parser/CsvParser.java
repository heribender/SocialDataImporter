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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.filter.RawDataFilterString;


/**
 * A Parser for CSV files.
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
@Component
public class CsvParser
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CsvParser.class );

    /**
     * Parses the given input stream.
     * <p>
     *
     * @param aInputStream
     *        must not be null
     * @param aDelimiter
     *        must not be null
     * @param aEncoding
     *        The encoding to be used. If null or empty, the systems default encoding is used.
     * @return a list which contains a list for each found person. The inner list contains the found
     *         values for this person. The number and the order must correspond to the configured field
     *         name list (see
     *         in each line.
     * @throws SdiException
     */
    public List<List<String>> parse( InputStream aInputStream,
                                     String aDelimiter,
                                     String aEncoding ) throws SdiException
    {
        return parse( aInputStream, aDelimiter, aEncoding, null );
    }

    /**
     * Parses the given input stream.
     * <p>
     *
     * @param aInputStream
     *        must not be null
     * @param aDelimiter
     *        must not be null
     * @param aEncoding
     *        The encoding to be used. If null or empty, the systems default encoding is used.
     * @return a list which contains a list for each found person. The inner list contains the found
     *         values for this person. The number and the order must correspond to the configured field
     *         name list (see
     *         in each line.
     * @throws SdiException
     */
    public List<List<String>> parse( InputStream aInputStream,
                                     String aDelimiter,
                                     String aEncoding,
                                     List<RawDataFilterString> aFilters ) throws SdiException
    {
        if ( !StringUtils.hasLength( aDelimiter ) )
        {
            throw new SdiException( "Delimiter not set", SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if myDelimiter == null

        try
        {
            myLog.debug( "Using encoding " + aEncoding );

            BufferedReader br = new BufferedReader( !StringUtils.hasText( aEncoding )
                                                    ? new InputStreamReader( aInputStream )
                                                    : new InputStreamReader( aInputStream, aEncoding ) );
            List<List<String>> result = new ArrayList<List<String>>();

            int lineNo = 0;
            String line;
            LineLoop:
            while ( ( line = br.readLine() ) != null )
            {
                lineNo++;

                if ( aFilters != null )
                {
                    for ( RawDataFilterString filter : aFilters )
                    {
                        if ( filter.isFiltered( line ) )
                        {
                            myLog.debug( "Skipping commented line: " + line );
                            continue LineLoop;
                        }
                    }
                }

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
                }
                finally
                {
                    sc.close();
                }

                result.add( list );
            }

            return result;
        }
        catch ( Throwable t )
        {
            throw new SdiException( "Problems while parsing CSV file",
                                    t,
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        }
    }

}
