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


package ch.sdi.core.exc;


/**
 * Common exception, transports an exit code of the application
 *
 * @version 1.0 (08.11.2014)
 * @author  Heri
 */
public class SdiException extends Exception
{
    private static final long serialVersionUID = 1L;

    public static final int EXIT_CODE_UNKNOWN_ERROR = 1;
    public static final int EXIT_CODE_PARSE_ERROR = 2;
    public static final int EXIT_CODE_CONFIG_ERROR = 3;
    public static final int EXIT_CODE_MAIL_ERROR = 4;
    public static final int EXIT_CODE_IMPORT_ERROR = 5;
    public static final int EXIT_CODE_FTP_ERROR = 6;

    private int myExitCode;

    /**
     * Constructor
     *
     */
    public SdiException( int aExitCode )
    {
        super();
    }

    /**
     * Constructor
     *
     * @param aMessage
     */
    public SdiException( String aMessage, int aExitCode )
    {
        super( aMessage );
        myExitCode = aExitCode;
    }

    /**
     * Constructor
     *
     * @param aCause
     */
    public SdiException( Throwable aCause, int aExitCode )
    {
        super( aCause );
        myExitCode = aExitCode;
    }

    /**
     * Constructor
     *
     * @param aMessage
     * @param aCause
     */
    public SdiException( String aMessage, Throwable aCause, int aExitCode )
    {
        super( aMessage, aCause );
        myExitCode = aExitCode;
    }


    /**
     * @return exitCode
     */
    public int getExitCode()
    {
        return myExitCode;
    }

    @Override
    public String toString()
    {
        String result = super.toString();

        result += "; ExitCode: " + myExitCode;

        return result;
    }


}
