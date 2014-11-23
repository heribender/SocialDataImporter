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


package ch.sdi.core.ftp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.net.SocketClient;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;


/**
 * Interceptor of FTP commands and responses for writing them to a Log4j2 logger.
 * <p>
 * This listener creates an internal PrintWriter which can be accessed by getPrintWriter() and which can
 * be passed to the constructor of an org.apache.commons.net.PrintCommandListener.PrintCommandListener().
 * All lines written to this internal PrintWriter are redirected to the given logger (TRACE-level).
 * <p>
 * CAUTION: this class is not thread safe.
 * <p>
 *
 * @version 1.0 (22.11.2014)
 * @author  Heri
 */
public class PrintCommandToLoggerListener
{
    private PrintWriter myPrintWriter;
    private PrintCommandToLoggerWriter myStringWriter;

    public PrintCommandToLoggerListener( Logger aLogger )
    {
        myStringWriter = new PrintCommandToLoggerWriter( aLogger );
        myPrintWriter = new PrintWriter( myStringWriter );
    }

    /**
     * @return myPrintWriter
     */
    public PrintWriter getPrintWriter()
    {
        return myPrintWriter;
    }

    /**
     * Writes the rest of the internal buffer to the log, regardless if there was a line termination or
     * not.
     */
    public void flushRest()
    {
        myStringWriter.flushRest();
    }

    static class PrintCommandToLoggerWriter extends StringWriter
    {
        private Logger myLog;
        /**
         * If not null, this internal buffer keeps not yet logged characters (because there wasn't any
         * line termination found in the last supplied content). The buffer will be written the next
         * time a line termination will be supplied. This ensures that only whole lines are written
         * to log as one statement, exactly as if the content would be written to the console.
         */
        private StringBuilder myBuffer;

        /**
         * Constructor
         *
         * @param aLog
         */
        public PrintCommandToLoggerWriter( Logger aLog )
        {
            super();
            myLog = aLog;
        }

        /**
         * @see java.io.StringWriter#write(int)
         */
        @Override
        public void write( int aInt )
        {
            getStringBuilder().append( aInt );
        }

        /**
         * @see java.io.StringWriter#write(char[], int, int)
         */
        @Override
        public void write( char[] cbuf, int off, int len )
        {
            // this entry check is a copy from base class:
            if ( ( off < 0 ) || ( off > cbuf.length ) || ( len < 0 ) || ( ( off + len ) > cbuf.length )
                    || ( ( off + len ) < 0 ) )
            {
                throw new IndexOutOfBoundsException();
            }
            else if ( len == 0 )
            {
                return;
            }

            String str = new String( cbuf, off, len );
            sendToLog( str );
        }

        /**
         * @see java.io.StringWriter#write(java.lang.String)
         */
        @Override
        public void write( String aStr )
        {
            sendToLog( aStr );
        }

        /**
         * @see java.io.StringWriter#write(java.lang.String, int, int)
         */
        @Override
        public void write( String aStr, int aOff, int aLen )
        {
            sendToLog( aStr.substring( aOff, aOff + aLen ) );
        }

        /**
         * @see java.io.StringWriter#append(java.lang.CharSequence)
         */
        @Override
        public StringWriter append( CharSequence aCsq )
        {
            sendToLog( aCsq.toString() );
            return this;
        }

        /**
         * @see java.io.StringWriter#append(java.lang.CharSequence, int, int)
         */
        @Override
        public StringWriter append( CharSequence aCsq, int aStart, int aEnd )
        {
            sendToLog( aCsq.toString().substring( aStart, aStart + aEnd ) );
            return this;
        }

        /**
         * @see java.io.StringWriter#append(char)
         */
        @Override
        public StringWriter append( char aChar )
        {
            sendToLog( new String( new char[] { aChar } ) );
            return this;
        }

        /**
         * @return buffer
         */
        public StringBuilder getStringBuilder()
        {
            if ( myBuffer == null )
            {
                myBuffer = new StringBuilder();
            } // if myBuffer == null

            return myBuffer;
        }

        /**
         * @param aString
         */
        private void sendToLog( String aString )
        {
            String str = aString;
            if ( !StringUtils.hasText( str ) )
            {
                if ( SocketClient.NETASCII_EOL.equals( str ) )
                {
                    // sometimes there is only a line termination supplied
                    writeLineToLog();
                    return;
                } // if SocketClient.NETASCII_EOL.equals( str )

                myLog.warn( "given log message has not text" );
                return;
            } // if !StringUtils.hasText( str )

            str = str.replaceAll( SocketClient.NETASCII_EOL, "\n" );

            int pos = str.indexOf( "\n" );
            while (pos >= 0)
            {
                // remove leading line terminations:
                while ( pos == 0 )
                {
                    str = str.length() > 1 ? str.substring( 1 ) : "";
                    pos = str.indexOf( "\n" );
                }

                if ( StringUtils.hasText( str ) )
                {
                    getStringBuilder().append( str.substring(0,pos));
                    writeLineToLog();
                } // if StringUtils.hasText( str )

                str = str.length() > (pos+1) ? str.substring( pos+1 ) : "";
                pos = str.indexOf( "\n" );

            }

            if ( StringUtils.hasText( str ) )
            {
                getStringBuilder().append( str );
            } // if StringUtils.hasText( str )
        }

        /**
         *
         */
        private void writeLineToLog()
        {
            myLog.trace( "FTP: " + getStringBuilder().toString() );
            myBuffer = null;
        }

        public void flushRest()
        {
            if ( myBuffer != null )
            {
                String s = myBuffer.toString().trim();
                if ( StringUtils.hasText( s ) )
                {
                    sendToLog( s + "\n" );
                } // if StringUtils.hasText( s )
            } // if myBuffer == null


        }

        /**
         * @see java.io.StringWriter#close()
         */
        @Override
        public void close() throws IOException
        {
            flushRest();
            super.close();
        }

    }

}

