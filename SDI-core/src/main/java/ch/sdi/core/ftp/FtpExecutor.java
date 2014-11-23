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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;


/**
 * The implementation of this class is inspired by the official example
 * (http://commons.apache.org/proper/commons-net/examples/ftp/FTPClientExample.java).
 * <p>
 * The available options of the original were stripped off in order only to use it as file uploader in
 * binary mode (stripped off options see below).
 * <p>
 * Also, the file(s) to upload are not given as arguments but as separate Map<sourceStream,destFileName>
 * in executeUpload(). So it's possible to transfer more than one file in one session.
 * <p>
 * The remaining valid "command line" arguments (given as array of String to the init() method):
 * <pre>
 *     Usage: [options] &lt;hostname> &lt;username> &lt;password>
 * </pre>
 * Options:
 * <ul>
 *   <li>-a - use local active mode (default is local passive)</li>
 *   <li>-A - anonymous login (omit username and password parameters)</li>
 *   <li>-e - use EPSV with IPv4 (default false)</li>
 *   <li>-k secs - use keep-alive timer (setControlKeepAliveTimeout)</li>
 *   <li>-p true|false|protocol[,true|false] - use FTPSClient with the specified protocol and/or isImplicit setting</li>
 *   <li>-w msec - wait time for keep-alive reply (setControlKeepAliveReplyTimeout)</li>
 *   <li>-T  all|valid|none - use one of the built-in TrustManager implementations (none = JVM default)</li>
 *   <li>-PrH server[:port] - HTTP Proxy host and optional port[80] </li>
 *   <li>-PrU user - HTTP Proxy server username</li>
 *   <li>-PrP password - HTTP Proxy server password</li>
 * </ul>
 * Removed options (compared to the apache original):
 * <ul>
 *   <li>-# - add hash display during transfers</li>
 *   <li>-b - use binary transfer mode</li>
 *   <li>-c cmd - issue arbitrary command (remote is used as a parameter if provided)</li>
 *   <li>-d - list directory details using MLSD (remote is used as the pathname if provided)</li>
 *   <li>-f - issue FEAT command (remote and local files are ignored)</li>
 *   <li>-h - list hidden files (applies to -l and -n only)</li>
 *   <li>-l - list files using LIST (remote is used as the pathname if provided)</li>
 *   <li>-L - use lenient future dates (server dates may be up to 1 day into future)</li>
 *   <li>-n - list file names using NLST (remote is used as the pathname if provided)</li>
 *   <li>-s - store file on server (upload)</li>
 *   <li>-t - list file details using MLST (remote is used as the pathname if provided)</li>
 * </ul>
 *
 * @version 1.0 (22.11.2014)
 * @author Heri
 */
@Component
public class FtpExecutor
{
    public static final String USAGE =
            "Usage: [options] <hostname> <username> <password>\n"
                    + "\nDefault behavior is to upload file(s) and use binary transfer mode.\n"
                    + "\t-a - use local active mode (default is local passive)\n"
                    + "\t-A - anonymous login (omit username and password parameters)\n"
                    + "\t-e - use EPSV with IPv4 (default false)\n"
                    + "\t-k secs - use keep-alive timer (setControlKeepAliveTimeout)\n"
                    + "\t-p true|false|protocol[,true|false] - use FTPSClient with the specified protocol and/or isImplicit setting\n"
                    + "\t-w msec - wait time for keep-alive reply (setControlKeepAliveReplyTimeout)\n"
                    + "\t-T  all|valid|none - use one of the built-in TrustManager implementations (none = JVM default)\n"
                    + "\t-PrH server[:port] - HTTP Proxy host and optional port[80] \n"
                    + "\t-PrU user - HTTP Proxy server username\n" + "\t-PrP password - HTTP Proxy server password\n"
                    ;

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( FtpExecutor.class );

    private boolean myLocalActive = false;
    private boolean myUseEpsvWithIPv4 = false;
    private long myKeepAliveTimeout = -1;
    private int myControlKeepAliveReplyTimeout = -1;
    private String myProtocol = null; // SSL protocol
    private String myTrustmgr = null;
    private String myProxyHost = null;
    private int myProxyPort = 80;
    private String myProxyUser = null;
    private String myProxyPassword = null;
    private String myUsername = null;
    private String myPassword = null;
    private int myPort;
    private String myServer;
    private FTPClient myFtp;
    private PrintCommandToLoggerListener myPrintCommandToLoggerListener;

    /**
     * @param aArgs
     */
    public void init( String[] aArgs ) throws SdiException
    {
        try
        {
            myLog.debug( "analyzing given arguments: "
                         + StringUtils.arrayToCommaDelimitedString( aArgs ) );
            int base = 0;
            int minParams = 3; // listings require 3 params
            for ( base = 0; base < aArgs.length; base++ )
            {
                if ( aArgs[base].equals( "-a" ) )
                {
                    myLocalActive = true;
                }
                else if ( aArgs[base].equals( "-A" ) )
                {
                    myPassword = System.getProperty( "user.name" ) + "@" + InetAddress.getLocalHost().getHostName();
                    myUsername = "anonymous";
                }
                else if ( aArgs[base].equals( "-e" ) )
                {
                    myUseEpsvWithIPv4 = true;
                }
                else if ( aArgs[base].equals( "-k" ) )
                {
                    myKeepAliveTimeout = Long.parseLong( aArgs[++base] );
                }
                else if ( aArgs[base].equals( "-p" ) )
                {
                    myProtocol = aArgs[++base];
                }
                else if ( aArgs[base].equals( "-w" ) )
                {
                    myControlKeepAliveReplyTimeout = Integer.parseInt( aArgs[++base] );
                }
                else if ( aArgs[base].equals( "-T" ) )
                {
                    myTrustmgr = aArgs[++base];
                }
                else if ( aArgs[base].equals( "-PrH" ) )
                {
                    myProxyHost = aArgs[++base];
                    String parts[] = myProxyHost.split( ":" );
                    if ( parts.length == 2 )
                    {
                        myProxyHost = parts[0];
                        myProxyPort = Integer.parseInt( parts[1] );
                    }
                }
                else if ( aArgs[base].equals( "-PrU" ) )
                {
                    myProxyUser = aArgs[++base];
                }
                else if ( aArgs[base].equals( "-PrP" ) )
                {
                    myProxyPassword = aArgs[++base];
                }
                else
                {
                    if ( aArgs[base].startsWith( "-" ) )
                    {
                        myLog.warn( "unhandled argument: " + aArgs[base] );
                        continue;
                    } // if aArgs[base].startsWith( "-" )

                    break;
                }
            }

            int remain = aArgs.length - base;
            if ( myUsername != null )
            {
                minParams -= 2;
            }
            if ( remain < minParams ) // server, user, pass, remote, local [protocol]
            {
                throw createFtpException( "Error: Too less params" );
            }

            myServer = aArgs[base++];
            myPort = 0;
            String parts[] = myServer.split( ":" );
            if ( parts.length == 2 )
            {
                myServer = parts[0];
                myPort = Integer.parseInt( parts[1] );
            }
            if ( myUsername == null )
            {
                myUsername = aArgs[base++];
                myPassword = aArgs[base++];
            }

        }
        catch ( Throwable t )
        {
            myLog.error( "Exception caught: ", t );
            myLog.info( USAGE );

            if ( t instanceof SdiException )
            {
                throw (SdiException) t;
            } // if t instanceof SdiException

            throw createFtpException( "inititalization problems", t ) ;
        }

    }

    /**
     * @throws SdiException
     * @throws IOException
     */
    public void connectAndLogin() throws SdiException, IOException
    {
        try
        {
            if ( myProtocol == null )
            {
                if ( myProxyHost != null )
                {
                    myLog.debug( "Using HTTP proxy server: " + myProxyHost );
                    myFtp = new FTPHTTPClient( myProxyHost, myProxyPort, myProxyUser, myProxyPassword );
                }
                else
                {
                    myLog.debug( "Using simple FTPClient" );
                    myFtp = new FTPClient();
                }
            }
            else
            {
                FTPSClient ftps;
                if ( myProtocol.equals( "true" ) )
                {
                    myLog.debug( "Using FTPSClient with implicite SSL" );
                    ftps = new FTPSClient( true );
                }
                else if ( myProtocol.equals( "false" ) )
                {
                    myLog.debug( "Using FTPSClient with no implicite SSL" );
                    ftps = new FTPSClient( false );
                }
                else
                {
                    String prot[] = myProtocol.split( "," );
                    if ( prot.length == 1 )
                    { // Just protocol
                        myLog.debug( "Using FTPSClient with protocol " + myProtocol );
                        ftps = new FTPSClient( myProtocol );
                    }
                    else
                    { // protocol,true|false
                        myLog.debug( "Using FTPSClient with " + prot[0] + " and " + prot[1] );
                        ftps = new FTPSClient( prot[0], Boolean.parseBoolean( prot[1] ) );
                    }
                }
                myFtp = ftps;
                if ( "all".equals( myTrustmgr ) )
                {
                    myLog.debug( "Using AcceptAllTrustManager" );
                    ftps.setTrustManager( TrustManagerUtils.getAcceptAllTrustManager() );
                }
                else if ( "valid".equals( myTrustmgr ) )
                {
                    myLog.debug( "Using ValidateServerCertificateTrustManager" );
                    ftps.setTrustManager( TrustManagerUtils.getValidateServerCertificateTrustManager() );
                }
                else if ( "none".equals( myTrustmgr ) )
                {
                    myLog.debug( "Setting TrustManager to null" );
                    ftps.setTrustManager( null );
                }
                else
                {
                    myLog.debug( "Using no TrustManager at all" );
                }
            }

            if ( myKeepAliveTimeout >= 0 )
            {
                myLog.debug( "Setting KeepAliveTimeout to " + myKeepAliveTimeout );
                myFtp.setControlKeepAliveTimeout( myKeepAliveTimeout );
            }
            if ( myControlKeepAliveReplyTimeout >= 0 )
            {
                myLog.debug( "Setting ControlKeepAliveReplyTimeout to " + myControlKeepAliveReplyTimeout );
                myFtp.setControlKeepAliveReplyTimeout( myControlKeepAliveReplyTimeout );
            }

            // intercept commands and write it to our logger
            myPrintCommandToLoggerListener = new PrintCommandToLoggerListener( myLog );
            PrintWriter writer = myPrintCommandToLoggerListener.getPrintWriter();
            myFtp.addProtocolCommandListener( new PrintCommandListener( writer, true, '\n', true ) );
//            myFtp.addProtocolCommandListener( new PrintCommandListener( new PrintWriter( System.out ), true ) );

            try
            {
                int reply;
                if ( myPort > 0 )
                {
                    myLog.debug( "Connecting to " + myServer + ":" + myPort );
                    myFtp.connect( myServer, myPort );
                }
                else
                {
                    myLog.debug( "Connecting to " + myServer + " (default port)" );
                    myFtp.connect( myServer );
                }

                myLog.debug( "Connected to " + myServer + " on " + ( myPort > 0 ? myPort : myFtp.getDefaultPort() ) );

                // After connection attempt, you should check the reply code to verify success.
                reply = myFtp.getReplyCode();

                if ( !FTPReply.isPositiveCompletion( reply ) )
                {
                    myFtp.disconnect();
                    throw createFtpException( "FTP server refused connection." );
                }
            }
            catch ( IOException e )
            {
                throw createFtpException( "Could not connect to server.", e );
            }

            if ( !myFtp.login( myUsername, myPassword ) )
            {
                myFtp.logout();
                throw createFtpException( "Problems on login" );
            }

            myLog.debug( "Remote system is " + myFtp.getSystemType() );
            myFtp.setFileType( FTP.BINARY_FILE_TYPE );

            // Use passive mode as default because most of us are behind firewalls these days.
            if ( myLocalActive )
            {
                myFtp.enterLocalActiveMode();
            }
            else
            {
                myFtp.enterLocalPassiveMode();
            }

            myFtp.setUseEPSVwithIPv4( myUseEpsvWithIPv4 );
        }
        finally
        {
            myPrintCommandToLoggerListener.flushRest();
        }
    }

    /**
     * @throws SdiException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void uploadFiles( Map<String,InputStream> aFilesToUpload ) throws SdiException
    {
        for ( String target : aFilesToUpload.keySet() )
        {
            try
            {
                myLog.debug( "Uploading a file to " + target );
                myFtp.storeFile( target, aFilesToUpload.get( target ) );
                myFtp.noop(); // check that control connection is working OK
            }
            catch ( FTPConnectionClosedException e )
            {
                throw createFtpException( "Server closed connection.", e );
            }
            catch ( IOException e )
            {
                throw createFtpException( "IOException caught", e );
            }
            finally
            {
                myPrintCommandToLoggerListener.flushRest();
            }
        }
    }

    /**
     * @throws SdiException
     */
    public void logoutAndDisconnect() throws SdiException
    {
        try
        {
            myFtp.logout();
        }
        catch ( FTPConnectionClosedException e )
        {
            throw createFtpException( "Server closed connection.", e );
        }
        catch ( IOException e )
        {
            throw createFtpException( "IOException caught", e );
        }
        finally
        {

            if ( myFtp.isConnected() )
            {
                try
                {
                    myFtp.disconnect();
                }
                catch ( IOException f )
                {
                    // do nothing
                }
            }
        }
    }

    private SdiException createFtpException( String aMessage )
    {
        return createFtpException( aMessage, null );

    }

    private SdiException createFtpException( String aMessage, Throwable aThrowable )
    {
        StringBuilder sb = new StringBuilder( aMessage );
        if ( myFtp != null )
        {
            sb.append( "ReplyCode: " ).append( myFtp.getReplyCode() );
            sb.append( "\n    Reply-Message:" );
            sb.append( "\n        " ).append( StringUtils.collectionToDelimitedString( Arrays.asList( myFtp
                    .getReplyStrings() ), "\n        " ) );
        } // if myFtp != null

        return new SdiException( sb.toString(), aThrowable, SdiException.EXIT_CODE_FTP_ERROR );

    }


}
