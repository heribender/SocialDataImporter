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


package ch.sdi.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import ch.sdi.core.impl.data.Dataset;
import ch.sdi.core.impl.data.Person;
import ch.sdi.report.ReportMsg.ReportType;


/**
 * Global singleton for collecting ReportMsg items in order to render a report at the end of the
 * data import.
 * <p>
 *
 * @version 1.0 (20.11.2014)
 * @author  Heri
 */
@Component
public class SdiReporter
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( SdiReporter.class );

    private List<ReportMsg> myMessages = new ArrayList<ReportMsg>();

    /**
     * Constructor
     *
     */
    public SdiReporter()
    {
        super();
    }

    /**
     * Clears all ReportMsg from internal memory
     */
    public void reset()
    {
        myLog.info( "resetting " + this.getClass().getSimpleName() );
        myMessages = new ArrayList<ReportMsg>();
    }

    /**
     * Adds given ReportMsg to the internal memory
     * <p>
     * @param aMsg
     */
    public void add( ReportMsg aMsg )
    {
        // Since wie are already in a call from a logger there would be an error entry if we call
        // the logger again ("Recursive call to appender "). So log this asynchroneously:
        CompletableFuture
            .supplyAsync(() -> "adding a message" )
            .thenAcceptAsync( myLog::trace );
        myMessages.add( aMsg );
    }

    /**
     * Renders the report
     * <p>
     * @return
     */
    public String getReport()
    {
        myLog.debug( "Rendering the report" );
        StringBuilder result = new StringBuilder();

        DateFormat df = new SimpleDateFormat( "dd.MM.yyyy, HH:mm:ss" );

        appendTitle( result, "Import " + df.format( new Date() ), "*" );
        result.append( "\n" );

        appendSimpleEntry( result,
                           "Source:",
                           findSimpleStringEntry( ReportType.COLLECTOR_CFG, "InputSource" ) );
        result.append( "\n" );

        Collection<ReportMsg> preparsedFiltered = findOfTypes( ReportType.PREPARSE_FILTER );
        appendSummary( result, preparsedFiltered );

        Collection<ReportMsg> parsed = findOfTypes( ReportType.COLLECTOR );
        appendSummary( result, parsed );

        Collection<ReportMsg> parsedProblems = findOfTypes( ReportType.COLLECTOR_PROBLEM );
        appendSummary( result, parsedProblems );

        Collection<ReportMsg> postparsedFiltered = findOfTypes( ReportType.POSTPARSE_FILTER );
        appendSummary( result, postparsedFiltered );

        Collection<ReportMsg> skippedNoEmail = findOfTypes( ReportType.SKIPPED_NO_EMAIL );
        appendSummary( result, skippedNoEmail );

        Collection<ReportMsg> processed = findOfTypes( ReportType.TARGET );
        appendSummary( result, processed );

        Collection<ReportMsg> processFailed = findOfTypes( ReportType.TARGET_PROBLEM );
        appendSummary( result, processFailed );

        result.append( "\n" );
        appendProcessedPersons( result, processed );

        result.append( "\n" );
        appendFailedPersons( result, processFailed );

        result.append( "\n" );
        appendSkippedNoEmail( result, skippedNoEmail );

        result.append( "\n" );
        appendFiltered( result, postparsedFiltered );

        result.append( "\n" );
        appendTitle( result, "All collected report messages (unformatted)", "-" );
        result.append( "" + myMessages );

        return result.toString();
    }

    /**
     * @param aResult
     * @param aFiltered
     */
    private void appendFiltered( StringBuilder aSb, Collection<ReportMsg> aFiltered )
    {
        appendTitle( aSb, "Filtered while collecting the raw data", "-" );

        aFiltered.stream()
            .filter( msg ->
            (
                msg.getKey().equals( "Skpped Persons (no mail address)" ) )
                &&
                msg.getValue() instanceof Collection
            )
            .map( msg -> Collection.class.cast( msg.getValue() ) )
            .forEach( list -> appendDatasetList( aSb, list ) );
    }

    /**
     * @param aSb
     * @param aList
     * @return
     */
    private void appendDatasetList( StringBuilder aSb, Collection<?> aList )
    {
        aList.stream()
        .filter( p -> p instanceof Dataset )
        .map( p -> Dataset.class.cast( p ) )
        .forEach
        ( p -> appendDataset( aSb, p ) );
    }

    /**
     * @param aSb
     * @param aP
     * @return
     */
    private void appendDataset( StringBuilder aSb, Dataset aDataset )
    {
        aSb.append( "    " )
        .append( aDataset )
        .append( "\n" );
    }

    /**
     * @param aResult
     * @param aSkippedNoEmail
     */
    private void appendSkippedNoEmail( StringBuilder aSb, Collection<ReportMsg> aSkippedNoEmail )
    {
        appendTitle( aSb, "Skipped because there is no email address", "-" );

        aSkippedNoEmail.stream()
            .filter( msg ->
            (
                msg.getKey().equals( "Skpped Persons (no mail address)" ) )
                &&
                msg.getValue() instanceof Collection
            )
            .map( msg -> Collection.class.cast( msg.getValue() ) )
            .forEach( list -> appendPersonList( aSb, list ) );
    }

    /**
     * @param aResult
     * @param aProcessFailed
     */
    private void appendFailedPersons( StringBuilder aSb, Collection<ReportMsg> aProcessFailed )
    {
        appendTitle( aSb, "Failed during processing", "-" );

        aProcessFailed.stream()
            .filter( msg ->
            (
                msg.getKey().equals( "FailedPersons" ) )
                &&
                msg.getValue() instanceof Collection
            )
            .map( msg -> Collection.class.cast( msg.getValue() ) )
            .forEach( list -> appendPersonList( aSb, list ) );
    }

    /**
     * @param aResult
     * @param aProcessed
     */
    private void appendProcessedPersons( StringBuilder aSb, Collection<ReportMsg> aProcessed )
    {
        appendTitle( aSb, "Processed Persons", "-" );

        aProcessed.stream()
            .filter( msg ->
            (
                msg.getKey().equals( "ProcessedPersons" ) )
                &&
                msg.getValue() instanceof Collection
            )
            .map( msg -> Collection.class.cast( msg.getValue() ) )
            .forEach( list -> appendPersonList( aSb, list ) );

        aSb.append( "\n" );
        appendTitle( aSb, "Persons already in target platform (duplicate)", "-" );

        aProcessed.stream()
        .filter( msg ->
        (
            msg.getKey().equals( "DuplicatePersons" ) )
            &&
            msg.getValue() instanceof Collection
        )
        .map( msg -> Collection.class.cast( msg.getValue() ) )
        .forEach( list -> appendPersonList( aSb, list ) );

    }

    private void appendPersonList( StringBuilder aSb, Collection<?> list )
    {
        list.stream()
        .filter( p -> p instanceof Person )
        .map( p -> Person.class.cast( p ) )
        .forEach
        ( p -> appendSimplePerson( aSb, p ) );
    }

    private void appendSimplePerson( StringBuilder aSb, Person<?> p )
    {
        aSb.append( "    " )
        .append( p.getEMail() )
        .append( " (" )
        .append( p.getGivenname() )
        .append( " " )
        .append( p.getFamilyName() )
        .append( ")\n" );
    }

    /**
     * @param aResult
     * @param aMessages
     */
    private void appendSummary( StringBuilder aSb, Collection<ReportMsg> aMessages )
    {
        aMessages.forEach( msg ->
        {
            Object value = msg.getValue();

            if ( value instanceof Collection )
            {
                value = ((Collection<?>) value).size();
            }

            appendSimpleEntry( aSb,
                               msg.getKey(),
                               value );
        } );
    }

    /**
     *
     */
    private void appendSimpleEntry( StringBuilder aSb, String aName, Object aValue )
    {
        aSb.append( aName ).append(  ": " ).append( aValue ).append( "\n" );
    }

    /**
     * @param aResult
     * @param aTitle
     * @param aUnderlineChar
     */
    private void appendTitle( StringBuilder aSb, String aTitle, String aUnderlineChar )
    {
        aSb.append( aTitle ).append( "\n" );
        for ( int i = 0; i < aTitle.length(); i++ )
        {
            aSb.append( aUnderlineChar );
        }
        aSb.append( "\n" );
    }

    private String findSimpleStringEntry( ReportType aReportType, String aKey )
    {
        return findSimpleEntry( aReportType, aKey, String.class );
    }

    /**
     * @param aCollector
     * @param aString
     * @return
     */
    private <T> T findSimpleEntry( ReportType aReportType, String aKey, Class<T> aClass )
    {
        Collection<ReportMsg> messages = findOfTypes( aReportType );
        for ( ReportMsg msg : messages )
        {
            if ( msg.getKey().equals( aKey ) )
            {
                Object o = msg.getValue();

                if ( aClass.isInstance( o ) )
                {
                    return aClass.cast( o );

                } // if aClass.isInstance( obj )

                myLog.warn( "Entry found for report type " + aReportType + " and key " + aKey
                            + ", but not of desired type! Entry: " + o.getClass().getName()
                            + "; expected: " + aClass.getName() );
                return null;

            } // if key.equals( aKey )
        }

        myLog.warn( "No entry found for report type " + aReportType + " and key " + aKey );
        return null;
    }

    /**
     * @param aReportType
     * @return
     */
    private Collection<ReportMsg> findOfTypes( ReportType aReportType )
    {
        return myMessages.stream()
            .filter( msg -> msg.getType() == aReportType )
            .collect( Collectors.toSet() );
    }

}
