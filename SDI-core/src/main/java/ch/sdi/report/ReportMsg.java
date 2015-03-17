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


/**
 * Special log4j message implementation for sampling report items.
 * <p>
 * Embeds a type (one of the ReportType enum), a key of type String and a value of type Object.
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
        COLLECTOR_CFG,
        COLLECTOR_PROBLEM,
        PREPARSE_FILTER,
        POSTPARSE_FILTER,
        SKIPPED_NO_EMAIL,
        TARGET,
        TARGET_PROBLEM,
        MAIL_TARGET,
        SQL_TARGET,
        FTP_TARGET
    };

    private final ReportType myType;
    private final String myKey;
    private final Object myValue;

    public ReportMsg( ReportType type, String aKey, Object aValue )
    {
        if ( type == null )
        {
            throw new NullPointerException( "type must not be null" );
        }

        if ( aKey == null )
        {
            throw new NullPointerException( "aKey must not be null" );
        }

        if ( aValue == null )
        {
            throw new NullPointerException( "aValue must not be null" );
        }

        this.myType = type;
        this.myKey = aKey;
        this.myValue = aValue;
    }

    @Override
    public String getFormattedMessage()
    {
        switch ( myType )
        {
            case COLLECTOR:
            case COLLECTOR_PROBLEM:
            case PREPARSE_FILTER:
            case POSTPARSE_FILTER:
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
                + "; key: " + myKey + ","
                + "value: " + myValue;
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
        return new Object[] { myKey, myValue };
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


    /**
     * @return type
     */
    public ReportType getType()
    {
        return myType;
    }


    /**
     * @return keys
     */
    public String getKey()
    {
        return myKey;
    }


    /**
     * @return values
     */
    public Object getValue()
    {
        return myValue;
    }

}
