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


package ch.sdi.plugins.oxwall;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.impl.mail.MailJobDefault;
import ch.sdi.plugins.oxwall.job.OxFtpJob;
import ch.sdi.plugins.oxwall.job.OxSqlJob;
import ch.sdi.plugins.oxwall.profile.OxQuestionFactory;
import ch.sdi.plugins.oxwall.pw.OxPasswordEncryptor;


/**
 * TODO
 *
 * @version 1.0 (01.12.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ OxTargetJobContext.class,
                                OxPasswordEncryptor.class,
                                MailJobDefault.class,
                                OxFtpJob.class,
                                OxSqlJob.class,
                                OxQuestionFactory.class })
public class OxTargetJobContextTest
{
    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxTargetJobContextTest.class );
    @Autowired
    private OxTargetJobContext myClassUnderTest;
    @Autowired
    private ConfigurableEnvironment  myEnv;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void test()
    {

    }
}
