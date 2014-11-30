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

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
 *
 * @version 1.0 (16.11.2014)
 * @author Heri
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes={OxUser.class, UserTest.class })
public class OxUserTest extends CrudTestBase<OxUser>
{

    /**
     * Constructor
     *
     * @param aClass
     */
    public OxUserTest()
    {
        super( OxUser.class );
    }


    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxUserTest.class );

    /**
     * @throws java.lang.Exception
     */
    @Override
    @Before
    public  void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }


    @Test
    public void testCreate()
    {
        super.startTransaction();

        OxUser newUser = new OxUser();
        newUser.setUsername( "Heri22" );
        newUser.setAccountType( "accountType" );
        newUser.setActivityStamp( 1396986027L );
        newUser.setEmail( "heri22@lamp.vm" );
        newUser.setJoinIp( 1234L );
        newUser.setJoinStamp( 1396980000L );
        newUser.setPassword( "asdflkj" );

        persist( newUser );
        myLog.debug( "Persisted user: " + newUser );

        super.commitTransaction();

        myLog.debug( "UserId after commit: " + newUser.getId() );
        Assert.assertTrue( newUser.getId() != 0 );

        myLog.debug( "detaching newUser in order to fetch a new instance from EntityManager" );
        em.detach( newUser );
        OxUser user = findById( newUser.getId() );
        Assert.assertNotNull( user );
        myLog.debug( "Retrieved user: " + user );
        Assert.assertTrue( newUser != user );
        user.setPassword( "aaaaaaaa" );
        persist( user );
        em.detach( user );

        myLog.debug( "finding user by email" );
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<OxUser> criteria = cb.createQuery(OxUser.class);
        Root<OxUser> root = criteria.from(OxUser.class);
        ParameterExpression<String> emailParam = cb.parameter(String.class);
        criteria.select(root).where(cb.equal( root.get("email"), emailParam ));

        TypedQuery<OxUser> query = em.createQuery(criteria);
        query.setParameter( emailParam, "heri22@lamp.vm" );
        List<OxUser> results = query.getResultList();
        myLog.debug( "results: " + results );
        Assert.assertEquals( 1, results.size() );


        myLog.debug( "finding user by email and emailVerify=true (no data)" );
        criteria = cb.createQuery(OxUser.class);
        root = criteria.from(OxUser.class);
        ParameterExpression<Boolean> emailVerifyParam = cb.parameter(Boolean.class);
        criteria.select(root).where( cb.and( cb.equal( root.get("email"), emailParam ) ),
                                             cb.equal( root.get( "emailVerify" ), emailVerifyParam ) );

        query = em.createQuery(criteria);
        query.setParameter( emailParam, "heri22@lamp.vm" );
        query.setParameter( emailVerifyParam, true ); // -> no such row
        results = query.getResultList();
        Assert.assertEquals( 0, results.size() );

        myLog.debug( "finding user by email and emailVerify=false (1 row)" );
        query.setParameter( emailVerifyParam, false );
        results = query.getResultList();
        Assert.assertEquals( 1, results.size() );
    }

}
