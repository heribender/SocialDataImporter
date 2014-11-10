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
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.converter.ConverterDate;
import ch.sdi.core.impl.data.converter.ConverterFactory;
import ch.sdi.core.impl.parser.CsvParser;
import ch.sdi.core.intf.cfg.SdiMainProperties;
import ch.sdi.core.intf.data.CollectorResult;


/**
 * TODO
 *
 * @version 1.0 (09.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CsvCollector.class,
                               CsvParser.class,
                               CsvCollector.class,
                               InputCollectorFactory.class,
                               ConverterFactory.class,
                               ConverterDate.class })
public class CsvCollectorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CsvCollectorTest.class );
    @Autowired
    private ConfigurableEnvironment  env;
    @Autowired
    private CsvCollector myClassUnderTest;



    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        TestUtils.addToEnvironment( env, "sdi.converter.birthday", ConverterDate.CONVERTER_NAME  );
        TestUtils.addToEnvironment( env, "sdi.converter." + ConverterDate.CONVERTER_NAME
                                    + ".birthday.pattern", "dd.MM.yy"  );
        TestUtils.addToEnvironment( env, "sdi.converter.entrydate",
                                    ConverterDate.CONVERTER_NAME  );
        TestUtils.addToEnvironment( env, "sdi.converter." + ConverterDate.CONVERTER_NAME
                                    + ".entrydate.pattern", "yyyy-MM-dd mm:ss"  );

    }

    /**
     * @param aFilename
     * @return
     */
    private File locateFile( String aFilename )
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

        TestUtils.addToEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_DELIMITER, ";" );

        myLog.debug( "filename not yet set in environment" );
        testException();

        myLog.debug( "wrong filename in environment" );
        TestUtils.addToEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_FILENAME, "blabla" );
        testException();

        TestUtils.addToEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                    locateFile( "CSV_testdata_noHeader.csv" ).getCanonicalPath() );

        myLog.debug( "fieldnames not yet set in environment" );
        testException();

        TestUtils.addToEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_FIELD_NAMES,
                                    "Screenname,Name,Middlename,Prename,email,birthday,entrydate" );

        myLog.debug( "fieldnames now set in environment" );
        testSuccess();

        TestUtils.addToEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "2" );
        myLog.debug( "skip >= rowsize. Ignored because headerRow is false" );
        testSuccess();


        TestUtils.addToEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW, "true" );
        myLog.debug( "skip >= rowsize. HeaderRow=true -> Exception" );
        testException();

        // CSV_testdata_withHeader0.csv:
        TestUtils.replaceInEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "0" );
        TestUtils.addToEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                    locateFile( "CSV_testdata_withHeader0.csv" ).getCanonicalPath() );
        TestUtils.removeFromEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_FIELD_NAMES );

        myLog.debug( "CSV_testdata_withHeader0.csv success" );
        testSuccess();

        // CSV_testdata_withHeader2.csv:
        TestUtils.replaceInEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "2" );
        TestUtils.addToEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_FILENAME,
                                    locateFile( "CSV_testdata_withHeader2.csv" ).getCanonicalPath() );
        myLog.debug( "CSV_testdata_withHeader2.csv success" );
        testSuccess();

        TestUtils.replaceInEnvironment( env, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, "5" );
        myLog.debug( "skip >= rowsize. HeaderRow=true -> Exception" );
        testException();
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
   private void testSuccess() throws Throwable
   {
       CollectorResult actual = myClassUnderTest.execute();
       myLog.debug( "Received: " + actual );
       Assert.assertNotNull( actual );
       Collection<String> receivedFieldnames = actual.getFieldnames();
       myLog.debug( "Received field names: " + receivedFieldnames );
       Assert.assertNotNull( receivedFieldnames );
       Assert.assertEquals( 7, receivedFieldnames.size() );

       Collection<Collection<Object>> receivedRows = actual.getRows();
       myLog.debug( "Received rows: " + receivedRows );
       Assert.assertNotNull( receivedRows );
       Assert.assertEquals( 2, receivedRows.size() );
   }

}