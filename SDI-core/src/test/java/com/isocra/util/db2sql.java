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

/**
 * Copyright Isocra Ltd 2004
 * You can use, modify and freely distribute this file as long as you credit Isocra Ltd.
 * There is no explicit or implied guarantee of functionality associated with this file, use it at your own risk.
 */

package com.isocra.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * This class connects to a database and dumps all the tables and contents out to stdout in the form of
 * a set of SQL executable statements
 */
public class db2sql
{

    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( db2sql.class );

    /** Dump the whole database to an SQL string */
    public static String dumpDB( Properties props )
    {
        String driverClassName = props.getProperty( "driver.class" );
        String driverURL = props.getProperty( "driver.url" );
        // Default to not having a quote character
        String columnNameQuote = props.getProperty( "columnName.quoteChar", "" );
        DatabaseMetaData dbMetaData = null;
        Connection dbConn = null;
        try
        {
            Class.forName( driverClassName );
            dbConn = DriverManager.getConnection( driverURL, props );
            dbMetaData = dbConn.getMetaData();
        }
        catch ( Exception e )
        {
            myLog.error( "Unable to connect to database: ", e );
            return null;
        }

        try
        {
            StringBuffer result = new StringBuffer();
            String catalog = props.getProperty( "catalog" );
            String schema = props.getProperty( "schemaPattern" );
            String tables = props.getProperty( "tableName" );
            ResultSet rs = dbMetaData.getTables( catalog, schema, tables, null );
            if ( !rs.next() )
            {
                myLog.warn( "Unable to find any tables matching: catalog=" + catalog + " schema=" + schema
                        + " tables=" + tables );
                rs.close();
            }
            else
            {
                // Right, we have some tables, so we can go to work.
                // the details we have are
                // TABLE_CAT String => table catalog (may be null)
                // TABLE_SCHEM String => table schema (may be null)
                // TABLE_NAME String => table name
                // TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE",
// "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
                // REMARKS String => explanatory comment on the table
                // TYPE_CAT String => the types catalog (may be null)
                // TYPE_SCHEM String => the types schema (may be null)
                // TYPE_NAME String => type name (may be null)
                // SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may
// be null)
                // REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are
// "SYSTEM", "USER", "DERIVED". (may be null)
                // We will ignore the schema and stuff, because people might want to import it somewhere else
                // We will also ignore any tables that aren't of type TABLE for now.
                // We use a do-while because we've already caled rs.next to see if there are any rows
                do
                {
                    String tableName = rs.getString( "TABLE_NAME" );
                    String tableType = rs.getString( "TABLE_TYPE" );
                    if ( "TABLE".equalsIgnoreCase( tableType ) )
                    {
                        result.append( "\n\n-- " + tableName );
                        result.append( "\nCREATE TABLE " + tableName + " (\n" );
                        ResultSet tableMetaData = dbMetaData.getColumns( null, null, tableName, "%" );
                        boolean firstLine = true;
                        while ( tableMetaData.next() )
                        {
                            if ( firstLine )
                            {
                                firstLine = false;
                            }
                            else
                            {
                                // If we're not the first line, then finish the previous line with a comma
                                result.append( ",\n" );
                            }
                            String columnName = tableMetaData.getString( "COLUMN_NAME" );
                            String columnType = tableMetaData.getString( "TYPE_NAME" );
                            // WARNING: this may give daft answers for some types on some databases (eg JDBC-ODBC link)
                            int columnSize = tableMetaData.getInt( "COLUMN_SIZE" );
                            String nullable = tableMetaData.getString( "IS_NULLABLE" );
                            String nullString = "NULL";
                            if ( "NO".equalsIgnoreCase( nullable ) )
                            {
                                nullString = "NOT NULL";
                            }
                            result.append( "    " + columnNameQuote + columnName + columnNameQuote + " " + columnType
                                    + " (" + columnSize + ")" + " " + nullString );
                        }
                        tableMetaData.close();

                        // Now we need to put the primary key constraint
                        try
                        {
                            ResultSet primaryKeys = dbMetaData.getPrimaryKeys( catalog, schema, tableName );
                            // What we might get:
                            // TABLE_CAT String => table catalog (may be null)
                            // TABLE_SCHEM String => table schema (may be null)
                            // TABLE_NAME String => table name
                            // COLUMN_NAME String => column name
                            // KEY_SEQ short => sequence number within primary key
                            // PK_NAME String => primary key name (may be null)
                            String primaryKeyName = null;
                            StringBuffer primaryKeyColumns = new StringBuffer();
                            while ( primaryKeys.next() )
                            {
                                String thisKeyName = primaryKeys.getString( "PK_NAME" );
                                if ( ( thisKeyName != null && primaryKeyName == null )
                                        || ( thisKeyName == null && primaryKeyName != null )
                                        || ( thisKeyName != null && !thisKeyName.equals( primaryKeyName ) )
                                        || ( primaryKeyName != null && !primaryKeyName.equals( thisKeyName ) ) )
                                {
                                    // the keynames aren't the same, so output all that we have so far (if anything)
                                    // and start a new primary key entry
                                    if ( primaryKeyColumns.length() > 0 )
                                    {
                                        // There's something to output
                                        result.append( ",\n    PRIMARY KEY " );
                                        if ( primaryKeyName != null )
                                        {
                                            result.append( primaryKeyName );
                                        }
                                        result.append( "(" + primaryKeyColumns.toString() + ")" );
                                    }
                                    // Start again with the new name
                                    primaryKeyColumns = new StringBuffer();
                                    primaryKeyName = thisKeyName;
                                }
                                // Now append the column
                                if ( primaryKeyColumns.length() > 0 )
                                {
                                    primaryKeyColumns.append( ", " );
                                }
                                primaryKeyColumns.append( primaryKeys.getString( "COLUMN_NAME" ) );
                            }
                            if ( primaryKeyColumns.length() > 0 )
                            {
                                // There's something to output
                                result.append( ",\n    PRIMARY KEY " );
                                if ( primaryKeyName != null )
                                {
                                    result.append( primaryKeyName );
                                }
                                result.append( " (" + primaryKeyColumns.toString() + ")" );
                            }
                        }
                        catch ( SQLException e )
                        {
                            // NB you will get this exception with the JDBC-ODBC link because it says
                            // [Microsoft][ODBC Driver Manager] Driver does not support this function
                            myLog.warn( "Unable to get primary keys for table " + tableName + " because ", e );
                        }

                        result.append( "\n);\n" );

                        // Right, we have a table, so we can go and dump it
                        dumpTable( dbConn, result, tableName );
                    }
                } while ( rs.next() );
                rs.close();
            }
            dbConn.close();
            return result.toString();
        }
        catch ( SQLException e )
        {
            myLog.warn( e.getMessage(), e );
        }
        return null;
    }

    /** dump this particular table to the string buffer */
    private static void dumpTable( Connection dbConn, StringBuffer result, String tableName )
    {
        try
        {
            // First we output the create table stuff
            PreparedStatement stmt = dbConn.prepareStatement( "SELECT * FROM " + tableName );
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Now we can output the actual data
            result.append( "\n\n-- Data for " + tableName + "\n" );
            while ( rs.next() )
            {
                result.append( "INSERT INTO " + tableName + " VALUES (" );
                for ( int i = 0; i < columnCount; i++ )
                {
                    if ( i > 0 )
                    {
                        result.append( ", " );
                    }
                    Object value = rs.getObject( i + 1 );
                    if ( value == null )
                    {
                        result.append( "NULL" );
                    }
                    else
                    {
                        String outputValue = value.toString();
                        outputValue = outputValue.replaceAll( "'", "\\'" );
                        result.append( "'" + outputValue + "'" );
                    }
                }
                result.append( ");\n" );
            }
            rs.close();
            stmt.close();
        }
        catch ( SQLException e )
        {
            myLog.error( "Unable to dump table " + tableName + " because: ", e );
        }
    }

    /** Main method takes arguments for connection to JDBC etc. */
    public static void main( String[] args )
    {

//        if ( args.length != 1 )
//        {
//            System.err.println( "usage: db2sql <property file>" );
//        }
        // Right so there's one argument, we assume it's a property file
        // so lets open it
        Properties props = new Properties();
        try
        {
//            props.load( new FileInputStream( args[0] ) );
            props.setProperty( "driver.class", "com.mysql.jdbc.Driver"  );
            props.setProperty( "driver.url", "jdbc:mysql://192.168.99.1/test"  );
            props.setProperty( "user", "root"  );
            props.setProperty( "password", "admin"  );

//            <property name="hibernate.connection.driver_class" value="com.mysql.jdbc.Driver" />
//            <property name="hibernate.connection.url" value="jdbc:mysql://192.168.99.1/oxwall" />
//            <property name="hibernate.connection.username" value="root" />
//            <property name="hibernate.connection.password" value="admin" />

//            String driverClassName = props.getProperty( "driver.class" );
//            String driverURL = props.getProperty( "driver.url" );
//            String columnNameQuote = props.getProperty( "columnName.quoteChar", "" );
//            String catalog = props.getProperty( "catalog" );
//            String schema = props.getProperty( "schemaPattern" );
//            String tables = props.getProperty( "tableName" );


            String result = dumpDB( props );
            myLog.debug( result );
        }
        catch ( Throwable e )
        {
            myLog.error( "Throwable caught: " + e.getMessage(), e );
        }

    }
}
