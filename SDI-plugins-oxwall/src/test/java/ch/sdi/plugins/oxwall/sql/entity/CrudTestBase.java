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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ejb.HibernateEntityManager;

import ch.sdi.plugins.oxwall.job.EntityManagerProvider;


/**
 * TODO
 *
 * @version 1.0 (29.11.2014)
 * @author  Heri
 */

public class CrudTestBase<T>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( CrudTestBase.class );

//    @PersistenceContext(unitName="test") ??? this is never loaded, regardless if executed with SpringTestRunner, etc.
    protected  HibernateEntityManager em;
    private EntityTransaction myTransaction;

    private Class<T> myClass;

    protected CrudTestBase( Class<T> aClass )
    {
        super();
        myClass = aClass;
    }

    /**
     * @throws java.lang.Exception
     */
    public  void setUp() throws Exception
    {
        // only a try out what the oxwall time stamp in DB means:
        long stampFromDB = 1396986027L; // <- unix timestamp (seconds since 1970)
        Date joinStamp = new Date( stampFromDB * 1000 );
        myLog.error( "Join-Stamp: " + joinStamp );

        initPersistence();
    }

    public void tearDown() throws Exception
    {
        closeEntityMangaer();
    }

    /**
     * @param aEntity
     * @return
     */
    protected T findById( Serializable aId )
    {
        return em.find( myClass, aId );
//        return (T) mySession.get( myClass, aId );
    }

    /**
     * @param aEntity
     * @return
     */
    protected T persist( T aEntity )
    {
        em.persist( aEntity );
        return aEntity;
    }

    protected void delete( T aEntity )
    {
        em.remove( aEntity );
    }

    /**
     * @param aClass
     */
    protected void initPersistence()
    {
        em = EntityManagerProvider.getEntityManager( "oxwall" );
    }

    /**
     *
     */
    protected void closeEntityMangaer()
    {
        if ( em != null )
        {
            em.close();
        } // if em != null
    }

    /**
     *
     */
    public void startTransaction()
    {
        myLog.debug( "starting transaction" );
        myTransaction = em.getTransaction();
        myTransaction.begin();
    }

    /**
     *
     */
    public void commitTransaction()
    {
        // TODO: investigate in MySQL transaction management since the commit or rollback currently
        // has no effect (the user is already persisted after persist() ).
        if ( myTransaction != null )
        {
            myLog.debug( "going to commit" );
            myTransaction.commit();
        }
        else
        {
            myLog.warn( "in commitTransaction, but transaction is null" );
        } // if..else myTransacion != null
    }

}
