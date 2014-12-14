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

import java.util.Properties;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ejb.HibernateEntityManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.sdi.core.TestUtils;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.cfg.ConversionServiceProvider;
import ch.sdi.core.impl.data.PersonKey;
import ch.sdi.core.impl.data.PropertiesPerson;
import ch.sdi.plugins.oxwall.OxTargetConfiguration;
import ch.sdi.plugins.oxwall.profile.OxQuestionFactory;
import ch.sdi.plugins.oxwall.sql.entity.OxUser;


/**
 * TODO
 *
 * @version 1.0 (30.11.2014)
 * @author  Heri
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={OxSqlJob.class,
                               OxQuestionFactory.class,
                               ConversionServiceProvider.class })
public class OxSqlJobTest
{

    /** */
    private static final String USER_EMAIL = "testUser@oxwall.test";
    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxSqlJobTest.class );
    @Autowired
    private OxSqlJob myClassUnderTest;
    @Autowired
    private ConfigurableEnvironment  myEnv;
    @Autowired
    private ConversionService  myConversionService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
//        int value = 1;
//        for ( OxGender gender : OxGender.values() )
//        {
//            String key = OxSqlJob.KEY_PREFIX_GENDER + gender;
//            TestUtils.addToEnvironment( myEnv, key, "" + value );
//            value++;
//        }

        ConfigUtils.setConversionService( myConversionService );

        TestUtils.addToEnvironment( myEnv, OxTargetConfiguration.KEY_USER_EMAIL_VERIFY, "false" );
        myClassUnderTest.init();
//        provideNewEntityManager();

    }

    @After
    public void tearDown() throws Exception
    {
        myClassUnderTest.close();
    }

    /**
     *
     */
    private void provideNewEntityManager()
    {
        HibernateEntityManager em = Whitebox.getInternalState( myClassUnderTest,
                                                               HibernateEntityManager.class );
        if ( em != null )
        {
            em.close();
        } // if em != null
        EntityManager newEm = EntityManagerProvider.getEntityManager( "oxwall" );
        Assert.assertNotNull( newEm );
        Assert.assertTrue( em != newEm );
        Whitebox.setInternalState( myClassUnderTest, "myEntityManager", newEm );
    }

    /**
     *
     */
    private void addTestUser()
    {
        HibernateEntityManager em = Whitebox.getInternalState( myClassUnderTest,
                                                               HibernateEntityManager.class );
        Assert.assertNotNull( em );
        em.getTransaction().begin();

        try
        {
            OxUser newUser = new OxUser();
            newUser.setUsername( "Heri22" );
            newUser.setAccountType( "accountType" );
            newUser.setActivityStamp( 1396986027L );
            newUser.setEmail( USER_EMAIL );
            newUser.setJoinIp( 1234L );
            newUser.setJoinStamp( 1396980000L );
            newUser.setPassword( "asdflkj" );

            em.persist( newUser );
            myLog.debug( "Persisted user: " + newUser );

            em.getTransaction().commit();;

        }
        catch ( Throwable t )
        {
            em.getTransaction().rollback();
            myLog.error( "Exception caught: ", t );
            throw t;
        }

    }

    /**
     * Test method for {@link ch.sdi.plugins.oxwall.job.OxSqlJob#isAlreadyPresent(ch.sdi.core.impl.data.Person)}.
     * @throws Throwable
     */
    @Test
    public void testIsAlreadyPresent() throws Throwable
    {
        myLog.debug( "Testing a non present person" );
        addTestUser();
        provideNewEntityManager();

        Properties props = new Properties();
        props.put( PersonKey.PERSON_EMAIL.getKeyName(), "bla" + USER_EMAIL );
        PropertiesPerson person = new PropertiesPerson( USER_EMAIL, props );

        boolean received = myClassUnderTest.isAlreadyPresent( person );
        Assert.assertFalse( received );

        myLog.debug( "Testing a present person" );
        person.setEMail( USER_EMAIL );
        received = myClassUnderTest.isAlreadyPresent( person );
        Assert.assertTrue( received );



    }

}
