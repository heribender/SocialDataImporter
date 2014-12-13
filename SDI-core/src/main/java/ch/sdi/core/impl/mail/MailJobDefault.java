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


package ch.sdi.core.impl.mail;

import org.apache.commons.mail.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.intf.MailJob;
import ch.sdi.report.ReportMsg;
import ch.sdi.report.ReportMsg.ReportType;


/**
 * TODO
 *
 * @version 1.0 (24.11.2014)
 * @author  Heri
 */
@Component
public class MailJobDefault implements MailJob
{
    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailJobDefault.class );
    @Autowired
    private Environment myEnv;
    @Autowired
    private MailCreator myMailCreator;
    @Autowired
    private MailSenderDefault myMailSender;

    /**
     * Constructor
     *
     */
    public MailJobDefault()
    {
        super();
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#execute(ch.sdi.core.impl.data.Person)
     */
    @Override
    public void execute( Person<?> aPerson ) throws SdiException
    {
        Email email = myMailCreator.createMailFor( aPerson );
        myMailSender.sendMail( email );

        ReportMsg msg = new ReportMsg( ReportType.FTP_TARGET,
                                       aPerson.getEMail(),
                                       new MailWrap( email ) );
        myLog.info( msg );
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#init()
     */
    @Override
    public void init() throws SdiException
    {
        myMailCreator.init();
        myMailSender.init();
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#close()
     */
    @Override
    public void close() throws SdiException
    {
        // TODO Auto-generated method stub

    }

    class MailWrap
    {
        Email myMail;

        /**
         * Constructor
         *
         * @param aMail
         */
        public MailWrap( Email aMail )
        {
            super();
            myMail = aMail;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder( super.toString() );
            sb.append( "\n    Receiver     : " ).append( myMail.getToAddresses() );
            sb.append( "\n    Subject : " ).append( myMail.getSubject() );
            return sb.toString();

        }
    }
}
