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
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;


/**
 * Creates a mail content
 * <p>
 *
 * @version 1.0 (02.11.2014)
 * @author Heri
 */
@Component
public class MailCreator
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( MailCreator.class );

    @Autowired
    private ConfigurableEnvironment  myEnv;
    @Autowired
    private MailTextResolver myMailTextResolver;

    /**
     * Creates a new simple mail class and fills the subject and the body for the given person
     * @param aPerson the person for whom the mail is addressed
     *
     * @return a mail instance
     * @throws SdiException on any problem
     */
    public Email createMailFor( Person<?> aPerson ) throws SdiException
    {
        Email email = new SimpleEmail();

        try
        {
            email.addTo( aPerson.getEMail() );
            String subject = myMailTextResolver.getResolvedSubject( aPerson );
            myLog.debug( "resolved subject: " + subject);
            email.setSubject( subject );
            String body = myMailTextResolver.getResolvedBody( aPerson );
            myLog.debug( "resolved body: " + body );
            email.setMsg( body );
        }
        catch ( EmailException t )
        {
            throw new SdiException( "Problems setting up mail for " + aPerson.getEMail(),
                                    t,
                                    SdiException.EXIT_CODE_MAIL_ERROR );
        }

        return email;
    }

    public void init() throws SdiException
    {
        myMailTextResolver.init();
    }


}
