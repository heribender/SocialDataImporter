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

package ch.sdi.core.impl.cfg.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;


/**
 * TODO
 *
 * @version 1.0 (13.12.2014)
 * @author Heri
 */
public class SshExecutorTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( SshExecutorTest.class );

    private String myHostname;
    private String myUsername;
    private String myPassword;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        myHostname = "192.168.99.1";
        myUsername = "heri";
        myPassword = "heri";
    }

// @Test
    public void testGanymed()
    {

        try
        {
            /* Create a connection instance */

            Connection conn = new Connection( myHostname );

            /* Now connect */

            conn.connect();

            /*
             * Authenticate.
             * If you get an IOException saying something like
             * "Authentication method password not supported by the server at this stage."
             * then please check the FAQ.
             */

            boolean isAuthenticated = conn.authenticateWithPassword( myUsername, myPassword );

            if ( isAuthenticated == false )
                throw new IOException( "Authentication failed." );

            /* Create a session */

            Session sess = conn.openSession();

            sess.execCommand( "uname -a && date && uptime && who" );

            System.out.println( "Here is some information about the remote host:" );

            /*
             * This basic example does not handle stderr, which is sometimes dangerous
             * (please read the FAQ).
             */

            InputStream stdout = new StreamGobbler( sess.getStdout() );

            BufferedReader br = new BufferedReader( new InputStreamReader( stdout ) );

            while ( true )
            {
                String line = br.readLine();
                if ( line == null )
                    break;
                System.out.println( line );
            }
            br.close();

            /* Show exit status, if available (otherwise "null") */

            System.out.println( "ExitCode: " + sess.getExitStatus() );

            /* Close this session */

            sess.close();

            /* Close the connection */

            conn.close();

        }
        catch ( IOException e )
        {
            e.printStackTrace( System.err );
            System.exit( 2 );
        }
    }

    @Test
    public void testSchj()
    {
        try
        {
            JSch jsch = new JSch();

            com.jcraft.jsch.Session session = jsch.getSession( myUsername, myHostname, 22 );
            session.setPassword( myPassword );

            UserInfo ui = new MyUserInfo()
            {

                @Override
                public void showMessage( String message )
                {
                    myLog.debug( "showMessage: " + message );
                }

                @Override
                public boolean promptYesNo( String message )
                {
                    myLog.debug( "promptYesNo: " + message );
                    return true;
                }

            };

            session.setUserInfo( ui );

            // It must not be recommended, but if you want to skip host-key check,
            // invoke following,
            // session.setConfig("StrictHostKeyChecking", "no");

            // session.connect();
            session.connect( 30000 ); // making a connection with timeout.

// Channel channel = session.openChannel( "shell" );
//
// // Enable agent-forwarding.
// // ((ChannelShell)channel).setAgentForwarding(true);
//
// channel.setInputStream( System.in );
// /*
// * // a hack for MS-DOS prompt on Windows.
// * channel.setInputStream(new FilterInputStream(System.in){
// * public int read(byte[] b, int off, int len)throws IOException{
// * return in.read(b, off, (len>1024?1024:len));
// * }
// * });
// */
//
// channel.setOutputStream( System.out );
//
// /*
// * // Choose the pty-type "vt102".
// * ((ChannelShell)channel).setPtyType("vt102");
// */
//
// /*
// * // Set environment variable "LANG" as "ja_JP.eucJP".
// * ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
// */
//
// // channel.connect();
// channel.connect( 3 * 1000 );
// channel.disconnect();

            String command = "chmod 664 /var/www/oxwall/.gitignore";
            Channel channel = session.openChannel( "exec" );
            ( (ChannelExec) channel ).setCommand( command );
            // channel.setInputStream(System.in);
            channel.setInputStream( null );

            // channel.setOutputStream(System.out);

            // FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            // ((ChannelExec)channel).setErrStream(fos);
            ( (ChannelExec) channel ).setErrStream( System.err );

            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while ( true )
            {
                while ( in.available() > 0 )
                {
                    int i = in.read( tmp, 0, 1024 );
                    if ( i < 0 )
                        break;
                    System.out.print( new String( tmp, 0, i ) );
                }
                if ( channel.isClosed() )
                {
                    if ( in.available() > 0 )
                        continue;
                    System.out.println( "exit-status: " + channel.getExitStatus() );
                    break;
                }
                try
                {
                    Thread.sleep( 1000 );
                }
                catch ( Exception ee )
                {
                }
            }
            channel.disconnect();
        }
        catch ( Exception e )
        {
            System.out.println( e );
        }

    }

    public static abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive
    {

        @Override
        public String getPassword()
        {
            return null;
        }

        @Override
        public boolean promptYesNo( String str )
        {
            return false;
        }

        @Override
        public String getPassphrase()
        {
            return null;
        }

        @Override
        public boolean promptPassphrase( String message )
        {
            return false;
        }

        @Override
        public boolean promptPassword( String message )
        {
            return false;
        }

        @Override
        public void showMessage( String message )
        {
        }

        @Override
        public String[] promptKeyboardInteractive( String destination,
                                                   String name,
                                                   String instruction,
                                                   String[] prompt,
                                                   boolean[] echo )
        {
            return null;
        }
    }
}
