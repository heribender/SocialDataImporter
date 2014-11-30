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


package ch.sdi.plugins.oxwall.sql;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
 *
 * @version 1.0 (30.11.2014)
 * @author  Heri
 */
public class OxProfileDataTest extends CrudTestBase<OxProfileData>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxProfileDataTest.class );

    /**
     * Constructor
     *
     * @param aClass
     */
    public OxProfileDataTest()
    {
        super( OxProfileData.class );
    }

    /**
     * @see ch.sdi.plugins.oxwall.sql.CrudTestBase#setUp()
     */
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Test
    public void testCreate()
    {
        myLog.debug( "Inserting Text question " );
        super.startTransaction();
        OxProfileData data = new OxProfileData();
        data.setQuestionName( "Testquestion" );
        data.setUserId( 1234L );
        data.setTextValue( "TestAnswer" );
        persist( data );
        myLog.debug( "Persisted data: " + data );
        super.commitTransaction();
        Assert.assertTrue( data.getId() != 0 );

        myLog.debug( "Inserting int question " );
        super.startTransaction();
        data = new OxProfileData();
        data.setQuestionName( "TestquestionInt" );
        data.setUserId( 1234L );
        data.setIntValue( 23456L );
        persist( data );
        myLog.debug( "Persisted data: " + data );
        super.commitTransaction();
        Assert.assertTrue( data.getId() != 0 );

        myLog.debug( "Inserting Date question " );
        super.startTransaction();
        data = new OxProfileData();
        data.setQuestionName( "TestquestionDate" );
        data.setUserId( 1234L );
        data.setDateValue( new Date() );
        persist( data );
        myLog.debug( "Persisted data: " + data );
        super.commitTransaction();
        Assert.assertTrue( data.getId() != 0 );

    }

}
