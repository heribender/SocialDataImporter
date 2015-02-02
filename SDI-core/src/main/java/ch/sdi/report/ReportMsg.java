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

import org.apache.logging.log4j.message.Message;
import org.springframework.util.StringUtils;


/**
 * Special log4j message implementation for sampling report items.
 * <p>
 *
 * @version 1.0 (19.11.2014)
 * @author Heri
 */
public class ReportMsg implements Message
{
    private static final long serialVersionUID = 1L;

    public enum ReportType
    {
        COLLECTOR,
        COLLECTOR_PROBLEM,
        PREPARSE_FILTER,
        POSTPARSE_FILTER,
        TARGET,
        TARGET_PROBLEM,
        MAIL_TARGET,
        SQL_TARGET,
        FTP_TARGET
    };

    private final ReportType myType;
    private final String[] myKeys;
    private final Object[] myValues;
    private final Throwable[] myThrowables;

    public ReportMsg( ReportType type, String aKey, Object aValue )
    {
        this( type, new String[] { aKey }, new Object[] { aValue }, null );
    }

    private ReportMsg( ReportType type, String[] aKeys, Object[] aValues, Throwable[] aThrowables )
    {
        this.myType = type;
        this.myKeys = aKeys;
        this.myValues = aValues;
        this.myThrowables = aThrowables;
    }

    @Override
    public String getFormattedMessage()
    {
        switch ( myType )
        {
            case COLLECTOR:
            case COLLECTOR_PROBLEM:
            case TARGET:
            case TARGET_PROBLEM:
            case MAIL_TARGET:
            case SQL_TARGET:
            case FTP_TARGET:
                return getMessageFormat();
            default:
                return "Unhandled type: " + myType;
        }
    }

    public String getMessageFormat()
    {
        return myType
                + "; keys: [" + StringUtils.arrayToCommaDelimitedString( myKeys )
                + "], values: [" + StringUtils.arrayToCommaDelimitedString( myValues )
                + "], Throwables: [" + renderThrowables( myThrowables ) + "]"
                ;
    }

    /**
     * @param aThrowables
     * @return
     */
    private String renderThrowables( Throwable[] aThrowables )
    {
        StringBuilder sb = new StringBuilder();

        if ( myThrowables != null )
        {
            for ( Throwable t : aThrowables )
            {
                sb.append( t.getClass().getSimpleName() ).append( ": " ).append( t.getMessage() ).append( ", " );
            }
        } // if myThrowables != null
        return sb.toString();
    }

    /**
     * @see org.apache.logging.log4j.message.Message#getFormat()
     */
    @Override
    public String getFormat()
    {
        return null;
    }

    /**
     * @see org.apache.logging.log4j.message.Message#getParameters()
     */
    @Override
    public Object[] getParameters()
    {
        return new Object[] { myKeys, myValues };
    }

    /**
     * @see org.apache.logging.log4j.message.Message#getThrowable()
     */
    @Override
    public Throwable getThrowable()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return getMessageFormat();
    }
}
