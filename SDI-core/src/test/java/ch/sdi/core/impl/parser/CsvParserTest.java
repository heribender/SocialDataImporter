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

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
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
        InputStream is;
        List<List<String>>actual;
        List<String> list;
        String expected;

        myLog.debug( "Testing No charset" );
        is = ClassLoader.getSystemResourceAsStream( "testdata1.csv" );
        myClassUnderTest.setCharset( null );
        actual = myClassUnderTest.parse( is, ";"  );
        myLog.debug( "Received: " + actual );

        myLog.debug( "Testing plainvanilla" );
        is = ClassLoader.getSystemResourceAsStream( "testdata1.csv" );
        myClassUnderTest.setCharset( "UTF-8" );
        actual = myClassUnderTest.parse( is, ";" );
        myLog.debug( "Received: " + actual );
        assertEquals( 3, actual.size() );
        assertEquals( "[[Screenname, Name, Middlename, Prename, email], [Bobby, Smith, C., Bob, bob@lamp.vm], [Sandy, Hübscher, , Sandra, sandra@lamp.vm]]", actual.toString() );

        myLog.debug( "Wrong charset" );
        is = ClassLoader.getSystemResourceAsStream( "testdata1.csv" );
        myClassUnderTest.setCharset( "ISO-8859-1" );
        actual = myClassUnderTest.parse( is, ";" );
        myLog.debug( "Received: " + actual );
        list = actual.get( 2 );
        String wrongString = list.get( 1 );
        expected = "H\u00c3\u00bcbscher";
        assertEquals( expected, wrongString );

        myLog.debug( "Wrong delimiter" );
        is = ClassLoader.getSystemResourceAsStream( "testdata1.csv" );
        myClassUnderTest.setCharset( "UTF-8" );
        actual = myClassUnderTest.parse( is, " " );
        myLog.debug( "Received: " + actual );
        list = actual.get( 0 );
        assertEquals( 1, list.size() );
    }

    @Test( expected=NullPointerException.class )
    public void testNullDelimiter() throws Throwable
    {
        InputStream is;
        myLog.debug( "Testing no delimter set" );
        is = ClassLoader.getSystemResourceAsStream( "testdata1.csv" );
        myClassUnderTest.setCharset( null );
        myClassUnderTest.parse( is, null );
    }



}
