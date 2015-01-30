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


package ch.sdi.core.impl.data;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.cfg.ConversionServiceProvider;
import ch.sdi.core.impl.data.converter.ConverterDate;
import ch.sdi.core.impl.data.converter.ConverterFactory;
import ch.sdi.core.impl.data.converter.ConverterJpgFromHexDump;
import ch.sdi.core.impl.data.filter.FilterFactory;
import ch.sdi.core.impl.parser.CsvParser;
import ch.sdi.core.intf.CollectorResult;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * Testcase
 *
 * @version 1.0 (09.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CsvCollector.class,
                               CsvParser.class,
                               InputCollectorFactory.class,
                               ConverterFactory.class,
                               ConverterDate.class,
                               ConverterJpgFromHexDump.class,
                               ConversionServiceProvider.class,
                               FilterFactory.class })
public class CsvCollectorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CsvCollectorTest.class );
    @Autowired
    private ConfigurableEnvironment myEnv;
    @Autowired
    private CsvCollector myClassUnderTest;
    @Autowired
    private ConversionService  myConversionService;



    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        ConfigUtils.setConversionService( myConversionService );

        TestUtils.addToEnvironment( myEnv, "sdi.converter.avatar", ConverterJpgFromHexDump.CONVERTER_NAME  );
        TestUtils.addToEnvironment( myEnv, "sdi.converter.birthday", ConverterDate.CONVERTER_NAME  );
        TestUtils.addToEnvironment( myEnv, "sdi.converter." + ConverterDate.CONVERTER_NAME
                                    + ".birthday.pattern", "dd.MM.yy"  );
        TestUtils.addToEnvironment( myEnv, "sdi.converter.entrydate",
                                    ConverterDate.CONVERTER_NAME  );
        TestUtils.addToEnvironment( myEnv, "sdi.converter." + ConverterDate.CONVERTER_NAME
                                    + ".entrydate.pattern", "yyyy-MM-dd mm:ss"  );

    }


    /**
     * @param aFilename
     * @return
     */
    private File toFile( String aFilename )
    {
        URL url = ClassLoader.getSystemResource( aFilename );
        File file = new File( url.getPath() );
        return file;
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.CsvCollector#execute()}.
     */
    @Test
    public void testExecute() throws Throwable
    {
        myLog.debug( "delimiter not yet set in environment" );
        testException();

        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_DELIMITER, ";" );

        myLog.debug( "filename not yet set in environment" );
        testException();

        myLog.debug( "wrong filename in environment" );
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME, "blabla" );
        testException();

        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                    toFile( "CSV_testdata_noHeader.csv" ).getCanonicalPath() );

        myLog.debug( "fieldnames not yet set in environment" );
        testException();

        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FIELD_NAMES,
                                    "Screenname,Name,Middlename,Prename,email,birthday,entrydate" );

        myLog.debug( "fieldnames now set in environment" );
        testSuccess( 7, 2 );

        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "2" );
        myLog.debug( "skip >= rowsize. Ignored because headerRow is false" );
        testSuccess( 7, 2 );


        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW, "true" );
        myLog.debug( "skip >= rowsize. HeaderRow=true -> Exception" );
        testException();

        // CSV_testdata_withHeader0.csv:
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "0" );
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                    toFile( "CSV_testdata_withHeader0.csv" ).getCanonicalPath() );
        TestUtils.removeFromEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FIELD_NAMES );

        myLog.debug( "CSV_testdata_withHeader0.csv success" );
        testSuccess( 7, 2 );

        // CSV_testdata_withHeader2.csv:
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "2" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                    toFile( "CSV_testdata_withHeader2.csv" ).getCanonicalPath() );
        myLog.debug( "CSV_testdata_withHeader2.csv success" );
        testSuccess( 7, 2 );

        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "5" );
        myLog.debug( "skip >= rowsize. HeaderRow=true -> Exception" );
        testException();

        // CSV-File with too many entries
        TestUtils.addToEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FIELD_NAMES,
                "Screenname,Name,Middlename,Prename,email,birthday" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "0" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW, "false" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                        toFile( "CSV_testdata_noHeader.csv" ).getCanonicalPath() );
        myLog.debug( "CSV-File with too many entries -> Exception" );
        testException();

        // empty field content
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FIELD_NAMES,
                "Screenname,Name,Middlename,Prename,email,birthday,entrydate" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "0" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW, "false" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                        toFile( "CSV_testdata_noHeader_empty_fields.csv" ).getCanonicalPath() );
        myLog.debug( "loading avatar picture (jpg hex dump)" );
        testSuccess( 7, 2 );

        // avatar
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW, "true" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW, "true" );
        TestUtils.replaceInEnvironment( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                        toFile( "CSV_testdata_withHeader0_Avatar.csv" ).getCanonicalPath() );
        myLog.debug( "empty field content -> Empty String or null (if date)" );
        testSuccess( 8, 2 );

    }

    /**
     *
     */
    private void testException()
    {
        try
        {
            myClassUnderTest.execute();
            Assert.fail( "Exception expected" );
        }
        catch ( SdiException t )
        {
            myLog.debug( "Expected exception received. Message: " + t.getMessage() );
        }
    }

    /**
    *
    */
   private void testSuccess( int aExpectedFieldNum, int aExpectedRowNum ) throws Throwable
   {
       CollectorResult actual = myClassUnderTest.execute();
       myLog.debug( "Received: " + actual );
       Assert.assertNotNull( actual );
       Collection<String> receivedFieldnames = actual.getFieldnames();
       myLog.debug( "Received field names: " + receivedFieldnames );
       Assert.assertNotNull( receivedFieldnames );
       Assert.assertEquals( aExpectedFieldNum, receivedFieldnames.size() );

       Collection<Collection<Object>> receivedRows = actual.getRows();
       myLog.debug( "Received rows: " + receivedRows );
       Assert.assertNotNull( receivedRows );
       Assert.assertEquals( aExpectedRowNum, receivedRows.size() );
   }

}
