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

import java.io.ByteArrayOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * Implements a SSH command executor.
 * <p>
 * The implementation is uses the SSH library http://www.jcraft.com/jsch/. The code was adapted from
 * the example in http://www.jcraft.com/jsch/examples/Exec.java.html
 * <p>
 *
 * @version 1.0 (13.12.2014)
 * @author  Heri
 */
@Component
public class SshExecutor
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( SshExecutor.class );

    @Autowired
    private Environment myEnv;

    private Session mySession;

    public void executeCmd( String aCommand ) throws SdiException
    {
        if ( mySession == null )
        {
            throw new SdiException( "SSH session not initialized.",
                                    SdiException.EXIT_CODE_SSH_ERROR );
        } // if mySession == null

        if ( !mySession.isConnected() )
        {
            throw new SdiException( "SSH session not connected.",
                                    SdiException.EXIT_CODE_SSH_ERROR );
        }

        Channel channel = null;

        try
        {
            channel = mySession.openChannel( "exec" );
            myLog.debug( "executing SSH command " + aCommand );
            ( (ChannelExec) channel ).setCommand( aCommand );

            ByteArrayOutputStream osOut = new ByteArrayOutputStream();
            ByteArrayOutputStream osErr = new ByteArrayOutputStream();

            try
            {
                channel.setInputStream( null );
                channel.setOutputStream(osOut, true );
                ( (ChannelExec) channel ).setErrStream( osErr, true );

                myLog.debug( "connecting the channel" );
                channel.connect();

                while ( true )
                {
                    if ( channel.isClosed() )
                    {
                        break;
                    }
                    try
                    {
                        myLog.debug( "going to sleep 50 ms" );
                        Thread.sleep( 50 );
                    }
                    catch ( Exception ee )
                    {
                    }
                }
            }
            finally
            {
                String out = new String( osOut.toByteArray() );
                if ( StringUtils.hasText( out ) )
                {
                    myLog.info( "Captured SSH out:\n" + out );
                } // if StringUtils.hasText( out )

                String err = new String( osErr.toByteArray() );
                if ( StringUtils.hasText( err ) )
                {
                    myLog.warn( "Captured SSH err:\n" + err );
                } // if StringUtils.hasText( out )
            }


        }
        catch ( Throwable t )
        {
            throw new SdiException( "SSH session not connected.",
                                    t,
                                    SdiException.EXIT_CODE_SSH_ERROR );
        }
        finally
        {
            if ( channel != null )
            {
                int exitStatus = channel.getExitStatus();
                myLog.debug( "SSH exit status: " + exitStatus );

                myLog.debug( "disconnecting the channel" );
                channel.disconnect();

                if ( exitStatus != 0 )
                {
                    throw new SdiException( "SSH not successful. SSH-Exitstatus: " + exitStatus,
                                            SdiException.EXIT_CODE_SSH_ERROR );
                } // if exitStatus
            } // if channel != null
        }
    }

    public void init() throws SdiException
    {
        String hostname = myEnv.getProperty( SdiMainProperties.KEY_TARGET_HOST );
        String username = myEnv.getProperty( SdiMainProperties.KEY_SSH_USER );
        String password = myEnv.getProperty( SdiMainProperties.KEY_SSH_PASSWORD );
        int port = ConfigUtils.getIntProperty( myEnv, SdiMainProperties.KEY_SSH_PORT );
        boolean checkCertificate = ConfigUtils.getBooleanProperty( myEnv, SdiMainProperties.KEY_SSH_CHECK_CERTIFICATE, true );

        myLog.debug( "Initializing SSH connection with parameters: "
                     + "\n    hostname        : " + hostname
                     + "\n    username        : " + username
//                     + "\n    password: " + password
                     + "\n    port            : " + port
                     + "\n    checkCertificate: " + checkCertificate
                     );
        JSch jsch = new JSch();
        try
        {
            mySession = jsch.getSession( username, hostname, port );
            mySession.setPassword( password );

            if ( !checkCertificate )
            {
                mySession.setConfig("StrictHostKeyChecking", "no");
            } // if !myCheckCertificate

            mySession.connect( 30000 );
        }
        catch ( JSchException t )
        {
            mySession = null;
            throw new SdiException( "Problems connecting to SSH server",
                                    t,
                                    SdiException.EXIT_CODE_FTP_ERROR );
        }
    }

    public void close() throws SdiException
    {
        if ( mySession != null )
        {
            mySession.disconnect();
        } // if mySession != null
    }

}
