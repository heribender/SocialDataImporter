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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.intf.FtpJob;


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
        // TODO Auto-generated method stub

    }

}
