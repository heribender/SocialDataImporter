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


package ch.sdi.plugins.oxwall.job;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.ftp.FtpExecutor;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.cfg.ssh.SshExecutor;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.intf.FtpJob;
import ch.sdi.core.intf.SdiMainProperties;
import ch.sdi.plugins.oxwall.OxTargetJobContext;
import ch.sdi.report.ReportMsg;
import ch.sdi.report.ReportMsg.ReportType;


/**
 * TODO
 *
 * @version 1.0 (24.11.2014)
 * @author  Heri
 */
@Component
public class OxFtpJob implements FtpJob
{
    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxFtpJob.class );
    @Autowired
    private Environment myEnv;
    @Autowired
    private FtpExecutor myFtpExecutor;
    @Autowired
    private SshExecutor mySshExecutor;
    private boolean myDryRun;
    private String myTargetDir;
    private String[] myCmdLineArgs;

    /**
     * Constructor
     *
     */
    public OxFtpJob()
    {
        super();
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#execute(ch.sdi.core.impl.data.Person)
     */
    @Override
    public void execute( Person<?> aPerson ) throws SdiException
    {
        Long avatarHash = aPerson.getProperty( OxTargetJobContext.KEY_AVATAR_HASH, Long.class );
        if ( avatarHash == null )
        {
            myLog.debug( "person has no avatar hash. No FTP file upload" );
            return;
        }

        Long userId = aPerson.getProperty( OxTargetJobContext.KEY_PERSON_USER_ID, Long.class );
        if ( userId == null )
        {
            throw new SdiException( "There is no userId in given person",
                                    SdiException.EXIT_CODE_UNKNOWN_ERROR );
        } // if condition

        Object o = aPerson.getProperty( OxTargetJobContext.KEY_PERSON_PREPARED_AVATAR_FILES );
        if ( o == null )
        {
            throw new SdiException( "There are no prepared avatar images in given person",
                                    SdiException.EXIT_CODE_UNKNOWN_ERROR );
        } // if condition

        @SuppressWarnings( "unchecked" )
        Map<String, InputStream> preparedImageStreams = (Map<String, InputStream>) o;

        String suffix = "" + userId + "_" + avatarHash + ".jpg";
        Map<String, InputStream> filesToUpload = new HashMap<String, InputStream>();
        String targetDir = myTargetDir + "ow_userfiles/plugins/base/avatars/";

        for ( String prefix : preparedImageStreams.keySet() )
        {
            String filename = targetDir + prefix + suffix;
            filesToUpload.put( filename, preparedImageStreams.get( prefix ) );
        }

        if ( myDryRun )
        {
            myLog.debug( "DryRun is set. No FTP action is performed" );
            // TODO: save local in output dir
        }
        else
        {
            myFtpExecutor.uploadFiles( filesToUpload );

            for ( String filename : filesToUpload.keySet() )
            {
                String command = "chmod 644 " + filename ;
                mySshExecutor.executeCmd( command );

            }

        } // if..else myDryRun

        ReportMsg msg = new ReportMsg( ReportType.FTP_TARGET,
                                       aPerson.getEMail(),
                                       filesToUpload.keySet().toArray() );
        myLog.info( msg );
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#init()
     */
    @Override
    public void init() throws SdiException
    {
        myDryRun = ConfigUtils.getBooleanProperty( myEnv, SdiMainProperties.KEY_DRYRUN, false );

        myTargetDir = ConfigUtils.getStringProperty( myEnv, SdiMainProperties.KEY_FTP_DEST_DIR );
        if ( !StringUtils.hasText( myTargetDir ) )
        {
            throw new SdiException( "Property " + SdiMainProperties.KEY_FTP_DEST_DIR + " not configured",
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if !StringUtils.hasText( targetDir )
        myTargetDir = myTargetDir.trim();

        String commandLine = ConfigUtils.getStringProperty( myEnv, SdiMainProperties.KEY_FTP_CMD_LINE );
        if ( !StringUtils.hasText( commandLine ) )
        {
            throw new SdiException( "Property " + SdiMainProperties.KEY_FTP_CMD_LINE + " not configured",
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if !StringUtils.hasText( commandLine )
        myCmdLineArgs = StringUtils.delimitedListToStringArray( commandLine, " " );

        myFtpExecutor.init( myCmdLineArgs );
        try
        {
            myFtpExecutor.connectAndLogin();
        }
        catch ( IOException t )
        {
            throw new SdiException( "Problems connecting to FTP server",
                                    SdiException.EXIT_CODE_FTP_ERROR );
        }

        mySshExecutor.init();
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#close()
     */
    @Override
    public void close() throws SdiException
    {
        myFtpExecutor.logoutAndDisconnect();
        mySshExecutor.close();
    }

}
