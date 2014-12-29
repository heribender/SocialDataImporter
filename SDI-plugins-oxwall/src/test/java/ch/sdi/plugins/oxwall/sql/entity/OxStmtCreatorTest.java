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


package ch.sdi.plugins.oxwall.sql.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
 *
 * @version 1.0 (28.12.2014)
 * @author  Heri
 */
public class OxStmtCreatorTest
{


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxStmtCreatorTest.class );
    private OxStmtCreator myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * Test method for {@link ch.sdi.plugins.oxwall.sql.entity.OxStmtCreator#createInsertStatement(java.lang.Object)}.
     */
    @Test
    public void testCreateInsertStatement()
    {
        OxUser newUser = new OxUser();
        newUser.setUsername( "Heri22" );
        newUser.setAccountType( "accountType" );
        newUser.setActivityStamp( 1396986027L );
        newUser.setEmail( "heri22@lamp.vm" );
        newUser.setJoinIp( 1234L );
        newUser.setJoinStamp( 1396980000L );
        newUser.setPassword( "asdflkj" );

        String received = OxStmtCreator.createInsertStatement( newUser );
        myLog.debug( "Received: " + received );
        Assert.assertNotNull( received );
        Assert.assertEquals( "INSERT INTO ow_base_user ( email, username, password, joinStamp, "
                             + "activityStamp, accountType, emailVerify, joinIp ) "
                             + "VALUES ('heri22@lamp.vm','Heri22','asdflkj',1396980000, "
                             + "1396986027,'accountType',1,1234)",
                             received );

    }

}
