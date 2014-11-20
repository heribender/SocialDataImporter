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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
 *
 * @version 1.0 (16.11.2014)
 * @author Heri
 */
public class UserTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( UserTest.class );


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testSave()
    {
        SessionFactory factory;
        try
        {
            Configuration configuration = new Configuration()
                .addAnnotatedClass(User.class)
                .configure();
//            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
//                .applySettings(configuration.getProperties());
//            factory = configuration.buildSessionFactory(builder.build());
            factory = configuration.buildSessionFactory();
        }
        catch ( Throwable ex )
        {
            myLog.error( "Problems creating session factory", ex );
            throw ex;
        }

        Session session = factory.openSession();

        Transaction tx = null;
        Integer id = null;
        try
        {
            tx = session.beginTransaction();
            User user = new User();
            user.setUsername( "Heri1" );
            user.setAccountType( "accountType" );
            user.setActivityStamp( 1234L );
            user.setEmail( "heri2@lamp.vm" );
            user.setJoinIp( 1234L );
            user.setJoinStamp( 1234L );
            user.setPassword( "asdflkj" );
            id = (Integer) session.save( user );
            tx.commit();
        }
        catch ( HibernateException e )
        {
            if ( tx != null )
                tx.rollback();
            myLog.error( e.getMessage() );
        }
        finally
        {
            session.close();
        }

        myLog.debug( "Received ID: " + id );

    }

}
