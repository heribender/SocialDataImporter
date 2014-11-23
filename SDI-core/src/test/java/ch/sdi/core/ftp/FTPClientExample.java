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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.util.TrustManagerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;


/**
 * The base of this example is copied from
 * (http://commons.apache.org/proper/commons-net/examples/ftp/FTPClientExample.java) and adapted for
 * using it embedded.<p>
 *
 * This is an example program demonstrating how to use the FTPClient class.
 * This program connects to an FTP server and retrieves the specified
 * file. If the -s flag is used, it stores the local file at the FTP server.
 * Just so you can see what's happening, all reply strings are printed.
 * If the -b flag is used, a binary transfer is assumed (default is ASCII).
 * See below for further options.
 */
public final class FTPClientExample
{

    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( FTPClientExample.class );

    private boolean myStoreFile = false;
    private boolean myBinaryTransfer = false;
    private boolean myListFiles = false;
    private boolean myListNames = false;
    private boolean myHidden = false;
    private boolean myLocalActive = false;
    private boolean myUseEpsvWithIPv4 = false;
    private boolean myFeat = false;
    private boolean myPrintHash = false;
    private boolean myMlst = false;
    private boolean myMlsd = false;
    private boolean myLenient = false;
    private long myKeepAliveTimeout = -1;
    private int myControlKeepAliveReplyTimeout = -1;
    private String myProtocol = null; // SSL protocol
    private String myDoCommand = null;
    private String myTrustmgr = null;
    private String myProxyHost = null;
    private int myProxyPort = 80;
    private String myProxyUser = null;
    private String myProxyPassword = null;
    private String myUsername = null;
    private String myPassword = null;
    private String myRemote;
    private String myLocal;
    private int myPort;
    private String myServer;
    private FTPClient myFtp;
    private PrintCommandToLoggerListener myPrintCommandToLoggerListener;

    public static final String USAGE =
            "Usage: ftp [options] <hostname> <username> <password> [<remote file> [<local file>]]\n"
                    + "\nDefault behavior is to download a file and use ASCII transfer mode.\n"
                    + "\t-a - use local active mode (default is local passive)\n"
                    + "\t-A - anonymous login (omit username and password parameters)\n"
                    + "\t-b - use binary transfer mode\n"
                    + "\t-c cmd - issue arbitrary command (remote is used as a parameter if provided) \n"
                    + "\t-d - list directory details using MLSD (remote is used as the pathname if provided)\n"
                    + "\t-e - use EPSV with IPv4 (default false)\n"
                    + "\t-f - issue FEAT command (remote and local files are ignored)\n"
                    + "\t-h - list hidden files (applies to -l and -n only)\n"
                    + "\t-k secs - use keep-alive timer (setControlKeepAliveTimeout)\n"
                    + "\t-l - list files using LIST (remote is used as the pathname if provided)\n"
                    + "\t     Files are listed twice: first in raw mode, then as the formatted parsed data.\n"
                    + "\t-L - use lenient future dates (server dates may be up to 1 day into future)\n"
                    + "\t-n - list file names using NLST (remote is used as the pathname if provided)\n"
                    + "\t-p true|false|protocol[,true|false] - use FTPSClient with the specified protocol and/or isImplicit setting\n"
                    + "\t-s - store file on server (upload)\n"
                    + "\t-t - list file details using MLST (remote is used as the pathname if provided)\n"
                    + "\t-w msec - wait time for keep-alive reply (setControlKeepAliveReplyTimeout)\n"
                    + "\t-T  all|valid|none - use one of the built-in TrustManager implementations (none = JVM default)\n"
                    + "\t-PrH server[:port] - HTTP Proxy host and optional port[80] \n"
                    + "\t-PrU user - HTTP Proxy server username\n" + "\t-PrP password - HTTP Proxy server password\n"
                    + "\t-# - add hash display during transfers\n";


    public static void main( String[] aArgs ) throws UnknownHostException
    {
        List<String> args = new ArrayList<String>( Arrays.asList( aArgs ) );

        args.add( "-s" ); // store file on sesrver
        args.add( "-b" ); // binary transfer mode
        args.add( "-#" );

        args.add( "192.168.99.1" );
        args.add( "heri" ); // user
        args.add( "heri" ); // pw
        args.add( "/var/www/log4j2.xml" );

        URL url = ClassLoader.getSystemResource( "sdimain_test.properties" );
// URL url = ClassLoader.getSystemResource( "log4j2.xml" );
        args.add( url.getFile() );

        FTPClientExample example = new FTPClientExample();
        try
        {
            example.init( args.toArray( new String[args.size()] ) );
            example.run();
        }
        catch ( Throwable t )
        {
            myLog.error( "Exception caught", t );
            myLog.info( USAGE );
            System.exit( 1 );
        }

    } // end main

    /**
     * @param aArgs
     */
    private void init( String[] aArgs ) throws Throwable
    {
        int base = 0;
        int minParams = 5; // listings require 3 params
        for ( base = 0; base < aArgs.length; base++ )
        {
            if ( aArgs[base].equals( "-s" ) )
            {
                myStoreFile = true;
            }
            else if ( aArgs[base].equals( "-a" ) )
            {
                myLocalActive = true;
            }
            else if ( aArgs[base].equals( "-A" ) )
            {
                myPassword = System.getProperty( "user.name" ) + "@" + InetAddress.getLocalHost().getHostName();
                myUsername = "anonymous";
            }
            else if ( aArgs[base].equals( "-b" ) )
            {
                myBinaryTransfer = true;
            }
            else if ( aArgs[base].equals( "-c" ) )
            {
                myDoCommand = aArgs[++base];
                minParams = 3;
            }
            else if ( aArgs[base].equals( "-d" ) )
            {
                myMlsd = true;
                minParams = 3;
            }
            else if ( aArgs[base].equals( "-e" ) )
            {
                myUseEpsvWithIPv4 = true;
            }
            else if ( aArgs[base].equals( "-f" ) )
            {
                myFeat = true;
                minParams = 3;
            }
            else if ( aArgs[base].equals( "-h" ) )
            {
                myHidden = true;
            }
            else if ( aArgs[base].equals( "-k" ) )
            {
                myKeepAliveTimeout = Long.parseLong( aArgs[++base] );
            }
            else if ( aArgs[base].equals( "-l" ) )
            {
                myListFiles = true;
                minParams = 3;
            }
            else if ( aArgs[base].equals( "-L" ) )
            {
                myLenient = true;
            }
            else if ( aArgs[base].equals( "-n" ) )
            {
                myListNames = true;
                minParams = 3;
            }
            else if ( aArgs[base].equals( "-p" ) )
            {
                myProtocol = aArgs[++base];
            }
            else if ( aArgs[base].equals( "-t" ) )
            {
                myMlst = true;
                minParams = 3;
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
            else if ( aArgs[base].equals( "-#" ) )
            {
                myPrintHash = true;
            }
            else
            {
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
            throw new Exception( "Error: Too less params" );
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

        myRemote = null;
        if ( aArgs.length - base > 0 )
        {
            myRemote = aArgs[base++];
        }

        myLocal = null;
        if ( aArgs.length - base > 0 )
        {
            myLocal = aArgs[base++];
        }

    }

    /**
    *
    */
    private void run() throws Throwable
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
                myFtp = new FTPClient();
            }
        }
        else
        {
            FTPSClient ftps;
            if ( myProtocol.equals( "true" ) )
            {
                ftps = new FTPSClient( true );
            }
            else if ( myProtocol.equals( "false" ) )
            {
                ftps = new FTPSClient( false );
            }
            else
            {
                String prot[] = myProtocol.split( "," );
                if ( prot.length == 1 )
                { // Just protocol
                    ftps = new FTPSClient( myProtocol );
                }
                else
                { // protocol,true|false
                    ftps = new FTPSClient( prot[0], Boolean.parseBoolean( prot[1] ) );
                }
            }
            myFtp = ftps;
            if ( "all".equals( myTrustmgr ) )
            {
                ftps.setTrustManager( TrustManagerUtils.getAcceptAllTrustManager() );
            }
            else if ( "valid".equals( myTrustmgr ) )
            {
                ftps.setTrustManager( TrustManagerUtils.getValidateServerCertificateTrustManager() );
            }
            else if ( "none".equals( myTrustmgr ) )
            {
                ftps.setTrustManager( null );
            }
        }

        if ( myPrintHash )
        {
            myFtp.setCopyStreamListener( createListener() );
        }
        if ( myKeepAliveTimeout >= 0 )
        {
            myFtp.setControlKeepAliveTimeout( myKeepAliveTimeout );
        }
        if ( myControlKeepAliveReplyTimeout >= 0 )
        {
            myFtp.setControlKeepAliveReplyTimeout( myControlKeepAliveReplyTimeout );
        }
        myFtp.setListHiddenFiles( myHidden );

        // intercept commands and write it to our logger
        myPrintCommandToLoggerListener = new PrintCommandToLoggerListener( myLog );
        PrintWriter writer = myPrintCommandToLoggerListener.getPrintWriter();
        myFtp.addProtocolCommandListener( new PrintCommandListener( writer, true, '\n', true ) );
//        myFtp.addProtocolCommandListener( new PrintCommandListener( new PrintWriter( System.out ), true ) );

        try
        {
            int reply;
            if ( myPort > 0 )
            {
                myFtp.connect( myServer, myPort );
            }
            else
            {
                myFtp.connect( myServer );
            }

            myLog.debug( "Connected to " + myServer + " on " + ( myPort > 0 ? myPort : myFtp.getDefaultPort() ) );

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = myFtp.getReplyCode();

            if ( !FTPReply.isPositiveCompletion( reply ) )
            {
                myFtp.disconnect();
                throw new Exception( "FTP server refused connection." );
            }
        }
        catch ( IOException e )
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
            throw new Exception( "Could not connect to server.", e );
        }

        try
        {
            if ( !myFtp.login( myUsername, myPassword ) )
            {
                myFtp.logout();
                throw createFtpException( "Problems on login" );
            }

            myLog.debug( "Remote system is " + myFtp.getSystemType() );

            if ( myBinaryTransfer )
            {
                myFtp.setFileType( FTP.BINARY_FILE_TYPE );
            }
            else
            {
                // in theory this should not be necessary as servers should default to ASCII
                // but they don't all do so - see NET-500
                myFtp.setFileType( FTP.ASCII_FILE_TYPE );
            }

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            if ( myLocalActive )
            {
                myFtp.enterLocalActiveMode();
            }
            else
            {
                myFtp.enterLocalPassiveMode();
            }

            myFtp.setUseEPSVwithIPv4( myUseEpsvWithIPv4 );

            if ( myStoreFile )
            {
                InputStream input;

                input = new FileInputStream( myLocal );

                myFtp.storeFile( myRemote, input );

                input.close();
            }
            else if ( myListFiles )
            {
                if ( myLenient )
                {
                    FTPClientConfig config = new FTPClientConfig();
                    config.setLenientFutureDates( true );
                    myFtp.configure( config );
                }

                for ( FTPFile f : myFtp.listFiles( myRemote ) )
                {
                    // TODO:
                    myLog.debug( f.getRawListing() );
                    myLog.debug( f.toFormattedString() );
                }
            }
            else if ( myMlsd )
            {
                // TODO
                for ( FTPFile f : myFtp.mlistDir( myRemote ) )
                {
                    myLog.debug( f.getRawListing() );
                    myLog.debug( f.toFormattedString() );
                }
            }
            else if ( myMlst )
            {
                FTPFile f = myFtp.mlistFile( myRemote );
                if ( f != null )
                {
                    // TODO
                    myLog.debug( f.toFormattedString() );
                }
            }
            else if ( myListNames )
            {
                for ( String s : myFtp.listNames( myRemote ) )
                {
                    // TODO:
                    myLog.debug( s );
                }
            }
            else if ( myFeat )
            {
                // boolean feature check
                if ( myRemote != null )
                { // See if the command is present
                    if ( myFtp.hasFeature( myRemote ) )
                    {
                        // TODO
                        myLog.debug( "Has feature: " + myRemote );
                    }
                    else
                    {
                        if ( FTPReply.isPositiveCompletion( myFtp.getReplyCode() ) )
                        {
                            // TODO
                            myLog.debug( "FEAT " + myRemote + " was not detected" );
                        }
                        else
                        {
                            throw createFtpException( "Command failed" );
                        }
                    }

                    // Strings feature check
                    String[] features = myFtp.featureValues( myRemote );
                    if ( features != null )
                    {
                        for ( String f : features )
                        {
                            // TODO
                            myLog.debug( "FEAT " + myRemote + "=" + f + "." );
                        }
                    }
                    else
                    {
                        if ( FTPReply.isPositiveCompletion( myFtp.getReplyCode() ) )
                        {
                            // TODO
                            myLog.warn( "FEAT " + myRemote + " is not present" );
                        }
                        else
                        {
                            throw createFtpException( "Command failed" );
                        }
                    }
                }
                else
                {
                    if ( myFtp.features() )
                    {
                        // Command listener has already printed the output
                    }
                    else
                    {
                        throw createFtpException( "Command failed" );
                    }
                }
            }
            else if ( myDoCommand != null )
            {
                if ( myFtp.doCommand( myDoCommand, myRemote ) )
                {
                    // Command listener has already printed the output
                }
                else
                {
                    throw createFtpException( "Command failed" );
                }
            }
            else
            {
                OutputStream output;

                output = new FileOutputStream( myLocal );

                myFtp.retrieveFile( myRemote, output );

                output.close();
            }

            myFtp.noop(); // check that control connection is working OK

            myFtp.logout();
        }
        catch ( FTPConnectionClosedException e )
        {
            throw createFtpException( "Server closed connection." );
        }
        catch ( IOException e )
        {
            throw createFtpException( "IOException caught" );
        }
        finally
        {
            myPrintCommandToLoggerListener.flushRest();

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

    private CopyStreamListener createListener()
    {
        return new CopyStreamListener()
        {

            private long megsTotal = 0;

            @Override
            public void bytesTransferred( CopyStreamEvent event )
            {
                bytesTransferred( event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize() );
            }

            @Override
            public void bytesTransferred( long totalBytesTransferred, int bytesTransferred, long streamSize )
            {
                // ??? what does this algo? Print a # for each transferred MB?
                long megs = totalBytesTransferred / 1000000;
                for ( long l = megsTotal; l < megs; l++ )
                {
                    myLog.warn( "#" );
                }
                megsTotal = megs;
            }
        };
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
