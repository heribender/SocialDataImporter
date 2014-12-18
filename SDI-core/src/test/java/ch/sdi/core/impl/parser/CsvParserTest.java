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

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import ch.sdi.core.exc.SdiException;


/**
 * Textcase for the CSV parsing
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
public class CsvParserTest
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CsvParserTest.class );

    private CsvParser myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        myLog.debug( "Creating class under test" );
        myClassUnderTest = new CsvParser();
    }

    @Test
    public void testParse() throws Throwable
    {
        String testFileName = "CSV_testdata_withHeader0.csv";

        InputStream is;
        List<List<String>>actual;
        List<String> list;
        String expected;

        myLog.debug( "Testing No charset" );
        is = ClassLoader.getSystemResourceAsStream( testFileName );
        actual = myClassUnderTest.parse( is, ";", null  );
        myLog.debug( "Received: " + actual );

        myLog.debug( "Testing plainvanilla" );
        is = ClassLoader.getSystemResourceAsStream( testFileName );
        actual = myClassUnderTest.parse( is, ";", "UTF-8");
        myLog.debug( "Received: " + actual );
        assertEquals( 3, actual.size() );
        assertEquals( "[[Screenname, Name, Middlename, Prename, email, birthday, entrydate], [Bobby, Smith, C., Bob, bob@gmail.com, 7.3.57, 2005-05-23 17:30], [Sandy, Hübscher, , Sandra, sandra@lamp.vm, 09.12.98, 2007-06-13 07:12]]", actual.toString() );

        myLog.debug( "Wrong charset" );
        is = ClassLoader.getSystemResourceAsStream( testFileName );
        actual = myClassUnderTest.parse( is, ";", "ISO-8859-1" );
        myLog.debug( "Received: " + actual );
        list = actual.get( 2 );
        String wrongString = list.get( 1 );
        expected = "H\u00c3\u00bcbscher";
        assertEquals( expected, wrongString );

        myLog.debug( "Wrong delimiter" );
        is = ClassLoader.getSystemResourceAsStream( testFileName );
        actual = myClassUnderTest.parse( is, " ", "UTF-8" );
        myLog.debug( "Received: " + actual );
        list = actual.get( 0 );
        assertEquals( 1, list.size() );
    }

    @Test
    public void testParseEmptyFields() throws Throwable
    {
        String testFileName = "CSV_testdata_noHeader_empty_fields.csv";

        InputStream is;
        List<List<String>>actual;
        List<String> list;

        myLog.debug( "Testing empty fields" );
        is = ClassLoader.getSystemResourceAsStream( testFileName );
        actual = myClassUnderTest.parse( is, ";", "UTF-8" );
        myLog.debug( "Received: " + actual );
        assertEquals( 2, actual.size() );
        assertEquals( "[[Bobby, Smith, C., Bob, bob@gmail.com, 7.3.57, ], [Sandy, Hübscher, , Sandra, sandra@lamp.vm, , ]]", actual.toString() );
        list = actual.get( 0 );
        assertEquals( 7, list.size() );
        assertEquals( "", list.get( 6 ) );
        list = actual.get( 1 );
        assertEquals( 7, list.size() );
        assertEquals( "", list.get( 2 ) );
        assertEquals( "", list.get( 5 ) );
        assertEquals( "", list.get( 6 ) );


    }

    @Test( expected=SdiException.class )
    public void testNullDelimiter() throws Throwable
    {
        InputStream is;
        myLog.debug( "Testing no delimter set" );
        is = ClassLoader.getSystemResourceAsStream( "testdata1.csv" );
        myClassUnderTest.parse( is, null, null );
    }



}
