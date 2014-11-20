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

import java.util.Map;

import org.apache.logging.log4j.message.Message;


/**
 * TODO
 *
 * @version 1.0 (19.11.2014)
 * @author Heri
 */
public class ReportMsg implements Message
{

    public enum ReportType
    {
        START, APPEND, STOP
    };

    private final ReportType myType;
    private final String myTable;
    private final Map<String, String> myArgs;

    public ReportMsg( ReportType type, String table )
    {
        this( type, table, null );
    }

    public ReportMsg( ReportType type, String table, Map<String, String> aArgs )
    {
        this.myType = type;
        this.myTable = table;
        this.myArgs = aArgs;
    }

    @Override
    public String getFormattedMessage() {
        switch (myType) {
            case START:
              return myTable + " " + myType;
            case APPEND:
              return myTable + " " + myType;
            case STOP:
                return myTable + " " + myType;
            default:
               return "Unhandled type: "  + myType;
        }
    }

    public String getMessageFormat()
    {
        return myType + " " + myTable;
    }

    private String formatCols( Map<String, String> cols )
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for ( Map.Entry<String, String> entry : cols.entrySet() )
        {
            if ( !first )
            {
                sb.append( ", " );
            }
            sb.append( entry.getKey() ).append( "=" ).append( entry.getValue() );
            first = false;
        }
        return sb.toString();
    }

    /**
     * @see org.apache.logging.log4j.message.Message#getFormat()
     */
    @Override
    public String getFormat()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.logging.log4j.message.Message#getParameters()
     */
    @Override
    public Object[] getParameters()
    {
        return new Object[] { myArgs };
    }

    /**
     * @see org.apache.logging.log4j.message.Message#getThrowable()
     */
    @Override
    public Throwable getThrowable()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
