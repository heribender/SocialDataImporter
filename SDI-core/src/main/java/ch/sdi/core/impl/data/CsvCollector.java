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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.data.converter.ConverterFactory;
import ch.sdi.core.impl.data.filter.CollectFilter;
import ch.sdi.core.impl.data.filter.FilterCommentedLine;
import ch.sdi.core.impl.data.filter.FilterFactory;
import ch.sdi.core.impl.data.filter.RawDataFilterString;
import ch.sdi.core.impl.parser.CsvParser;
import ch.sdi.core.intf.CollectorResult;
import ch.sdi.core.intf.FieldConverter;
import ch.sdi.core.intf.InputCollector;
import ch.sdi.core.intf.SdiMainProperties;
import ch.sdi.report.ReportMsg;


/**
 * Collector for CSV files.
 * <p>
 * The parsing is done by the CsvParser.
 * <p>
 *
 * @version 1.0 (08.11.2014)
 * @author  Heri
 */
@Component
public class CsvCollector implements InputCollector
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CsvCollector.class );
    @Autowired
    private CsvParser myParser;
    @Autowired
    private ConfigurableEnvironment myEnv;
    @Autowired
    private InputCollectorFactory myInputCollectorFactory;
    @Autowired
    private ConverterFactory myConverterFactory;
    @Autowired
    private FilterFactory myFilterFactory;

    private Collection<String> myFieldnames;
    private String myDelimiter;
    private String myEncoding;
    private List<RawDataFilterString> myLineFilters;
    private List<CollectFilter<?>> myCollectFilters;
    private boolean myHeaderRow;
    private int mySkip;
    private String myInputFileName;

    /**
     * Constructor
     *
     */
    public CsvCollector()
    {
        super();
    }

    /**
     * Executes the CSV parsing.
     *
     * @see ch.sdi.core.intf.InputCollector#execute()
     */
    @Override
    public CollectorResult execute() throws SdiException
    {
        init();

        InputStream is = openInputStream();

        List<List<String>> parsed = myParser.parse( is, myDelimiter, myEncoding, myLineFilters );

        myFieldnames = evaluateFieldNames( parsed, myHeaderRow );
        myLog.info( new ReportMsg( ReportMsg.ReportType.COLLECTOR, "Fieldnames", myFieldnames ) );

        int toSkip = myHeaderRow ? (mySkip + 1) : 0;
        myLog.debug( "Skipping first " + toSkip + " rows" );
        if ( parsed.size() < toSkip )
        {
            throw new SdiException( "No data found in CSV file "+ myInputFileName,
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        } // if parsed.size() < toSkip

        Collection<Collection<Object>> myRows = new ArrayList<Collection<Object>>();
        Collection<Collection<Object>> myRowsFiltered = new ArrayList<Collection<Object>>();

        List<FieldConverter<?>> converters = myConverterFactory.getFieldConverters( myFieldnames );

        ROW_LOOP:
        for ( int i = toSkip; i < parsed.size(); i++ )
        {
            List<String> row = parsed.get( i );
            myLog.trace( "row " + (i-toSkip) + ": " + row );

            try
            {
                Collection<Object> converted = convertFields( row, converters );

                for ( CollectFilter<?> filter : myCollectFilters )
                {
                    if ( filter.isFiltered( Dataset.create( myFieldnames, converted ) ) )
                    {
                        myLog.debug( "Given row is filtered: " + row );
                        myRowsFiltered.add( converted );
                        continue ROW_LOOP;
                    }
                }

                myRows.add( converted );
            }
            catch ( Exception t )
            {
                myLog.error( "row " + (i-toSkip) + " cannot be converted" );
                throw t;
            }
        }

        myLog.info( new ReportMsg( ReportMsg.ReportType.COLLECTOR, "Rows", myRows ) );
        myLog.info( new ReportMsg( ReportMsg.ReportType.POSTPARSE_FILTER, "Filtered datasets",
                                   myRowsFiltered ) );


        return new CollectorResult() {

            @Override
            public Collection<String> getFieldnames()
            {
                return myFieldnames;
            }

            @Override
            public Collection<Collection<Object>> getRows()
            {
                return myRows;
            }

        };

    }

    /**
     * @return
     * @throws SdiException
     */
    private InputStream openInputStream() throws SdiException
    {
        File file = new File( myInputFileName );

        if ( myLog.isDebugEnabled() )
        {
            try
            {
                myLog.debug( "Loading CSV file " + file.getCanonicalPath() );
            }
            catch ( IOException t1 )
            {
                myLog.warn( "Problems with resolving canonical path of filet " + myInputFileName );
            }
        } // if myLog.isDebugEnabled()

        InputStream is;
        try
        {
            is = new FileInputStream( file );
        }
        catch ( FileNotFoundException t )
        {
            throw new SdiException( "File not found "+ myInputFileName, t,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }
        return is;
    }

    /**
     * Initializes the CSVCollector by config values
     *
     * @throws SdiException
     */
    private void init() throws SdiException
    {
        myDelimiter = ConfigUtils.getStringProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_DELIMITER );
        myEncoding = Charset.defaultCharset().name();
        myEncoding = myEnv.getProperty( SdiMainProperties.KEY_COLLECT_CSV_ENCODING, myEncoding );
        myLineFilters = new ArrayList<>();
        Collection<String> set = ConfigUtils.getPropertyNamesStartingWith( myEnv,
                                      SdiMainProperties.KEY_COLLECT_CSV_COMMENT_CHARS_PREFIX );
        for ( String key : set )
        {
            FilterCommentedLine filter = new FilterCommentedLine();
            filter.init( myEnv, myEnv.getProperty( key ) );
            myLineFilters.add( filter );
        }

        myCollectFilters = new ArrayList<>();
        set = ConfigUtils.getPropertyNamesStartingWith( myEnv, CollectFilter.KEY_PREFIX_FILTER );
        for ( String value : set )
        {
            myCollectFilters.add( myFilterFactory.getFilter( myEnv.getProperty( value ) ) );
        }



        myHeaderRow = ConfigUtils.getBooleanProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW, false );
        mySkip = ConfigUtils.getIntProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, 0 );
        myInputFileName = ConfigUtils.getStringProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME );

        if ( myLog.isDebugEnabled() )
        {
            StringBuilder sb = new StringBuilder( "Starting a CSV collection." );
            sb.append( "\n    Configuration properties:" )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_DELIMITER ).append( " = " ).append( myDelimiter )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW ).append( " = " ).append( myHeaderRow )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER ).append( " = " ).append( mySkip )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_FILENAME ).append( " = " ).append( myInputFileName )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_COMMENT_CHARS_PREFIX ).append( " = " ).append( myLineFilters );

            myLog.debug( sb.toString()  );

        } // if myLog.isDebugEnabled()
    }

    /**
     * Converts each field using the configured converter
     * <p>
     *
     * @param aRow
     * @return
     * @throws SdiException
     */
    private Collection<Object> convertFields( List<String> aRow, List<FieldConverter<?>> aConverters )
            throws SdiException
    {
        Collection<Object> result = new ArrayList<Object>();

        if ( aRow.size() != aConverters.size() )
        {
            throw new SdiException( "Mismatch! Number of read fields: " + aRow.size()
                                    + "; number of fieldnames: " + aConverters.size(),
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        } // if aRow.size() != aConverters.size()

        for ( int i = 0; i < aConverters.size(); i++ )
        {
            Object o =  aConverters.get( i ).convert( aRow.get( i ) );
            if ( o == null )
            {
                o = new NullField();
            } // if o == null
            result.add( o );
        }

        return result;
    }

    /**
     * Evaluates the field names. If the CSV provides the field names by a header row (see configuration)
     * the values found in the first item of the given list are interpreted as fieldnames.
     * Otherwise the configured fieldnames are looked up (sdi.collect.csv.fieldnames)
     *
     *
     * @param aParsed
     * @param aHeaderRow
     * @return
     * @throws SdiException
     */
    private List<String> evaluateFieldNames( List<List<String>> aParsed, boolean aHeaderRow ) throws SdiException
    {
        List<String> result = null;

        if ( aHeaderRow )
        {
            if ( aParsed.size() == 0 )
            {
                throw new SdiException( "Expected header row not present",
                                        SdiException.EXIT_CODE_PARSE_ERROR );
            } // if aParsed.size() == 0

            result = aParsed.get( 0 );
            myLog.debug( "CSV Field names extracted from first csv row: " + result );
            return result;
        } // if aHeaderRow

        String fieldNames = ConfigUtils.getStringProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FIELD_NAMES );
        myLog.debug( "CSV Fieldnames read from configuration: " + fieldNames );
        return Arrays.asList( fieldNames.split( "," ) );
    }

}
