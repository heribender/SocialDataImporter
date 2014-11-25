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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigHelper;
import ch.sdi.core.impl.data.converter.ConverterFactory;
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
    private Environment myEnv;
    @Autowired
    private InputCollectorFactory myInputCollectorFactory;
    @Autowired
    private ConverterFactory myConverterFactory;



    private Collection<String> myFieldnames;
    Collection<Collection<Object>> myRows;

    /**
     * Constructor
     *
     */
    public CsvCollector()
    {
        super();
    }

    /**
     * @throws SdiException
     * @see ch.sdi.core.intf.InputCollector#execute()
     */
    @Override
    public CollectorResult execute() throws SdiException
    {
        String delimiter = ConfigHelper.getStringProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_DELIMITER );
        boolean headerRow = ConfigHelper.getBooleanProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW, false );
        int skip = ConfigHelper.getIntProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER, 0 );
        String fileName =  ConfigHelper.getStringProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FILENAME );

        if ( myLog.isDebugEnabled() )
        {
            StringBuilder sb = new StringBuilder( "Starting a CSV collection." );
            sb.append( "\n    Configuration properties:" )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_DELIMITER ).append( " = " ).append( delimiter )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_HEADER_ROW ).append( " = " ).append( headerRow )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_SKIP_AFTER_HEADER ).append( " = " ).append( skip )
              .append( "\n       " ).append( SdiMainProperties.KEY_COLLECT_CSV_FILENAME ).append( " = " ).append( fileName );

            myLog.debug( sb.toString()  );

        } // if myLog.isDebugEnabled()

        File file = new File( fileName );

        if ( myLog.isDebugEnabled() )
        {
            try
            {
                myLog.debug( "Loading CSV file " + file.getCanonicalPath() );
            }
            catch ( IOException t1 )
            {
                myLog.warn( "Problems with resolving canonical path of filet " + fileName );
            }
        } // if myLog.isDebugEnabled()

        InputStream is;
        try
        {
            is = new FileInputStream( file );
        }
        catch ( FileNotFoundException t )
        {
            throw new SdiException( "File not found "+ fileName, t,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }

        List<List<String>> parsed = null;

        try
        {
            parsed = myParser.parse( is, delimiter );
        }
        catch ( IOException t )
        {
            throw new SdiException( "File not found "+ fileName, t,
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        }

        myFieldnames = evaluateFieldNames( parsed, headerRow );
        myLog.info( new ReportMsg( ReportMsg.ReportType.COLLECTOR, "Fieldnames", myFieldnames ) );

        int toSkip = 0;

        if ( headerRow )
        {
            toSkip = skip + 1;
        } // if headerRow

        myLog.debug( "Skipping first " + toSkip + " rows" );

        if ( parsed.size() < toSkip )
        {
            throw new SdiException( "No data found in CSV file "+ fileName,
                                    SdiException.EXIT_CODE_PARSE_ERROR );
        } // if parsed.size() < toSkip

        myRows = new ArrayList<Collection<Object>>();

        List<FieldConverter<?>> converters = myConverterFactory.getFieldConverters( myFieldnames );

        for ( int i = toSkip; i < parsed.size(); i++ )
        {
            List<String> row = parsed.get( i );
            myLog.trace( "row " + (i-toSkip) + ": " + row );

            try
            {
                myRows.add( convertFields( row, converters ) );
            }
            catch ( Exception t )
            {
                myLog.error( "row " + (i-toSkip) + " cannot be converted" );
                throw t;
            }
        }

        myLog.info( new ReportMsg( ReportMsg.ReportType.COLLECTOR, "Rows", myRows ) );

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

        String fieldNames = ConfigHelper.getStringProperty( myEnv, SdiMainProperties.KEY_COLLECT_CSV_FIELD_NAMES );
        myLog.debug( "CSV Fieldnames read from configuration: " + fieldNames );
        return Arrays.asList( fieldNames.split( "," ) );
    }

}
