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

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ejb.HibernateEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.intf.SqlJob;
import ch.sdi.plugins.oxwall.sql.OxUser;


/**
 * TODO
 *
 * @version 1.0 (24.11.2014)
 * @author  Heri
 */
@Component
public class OxSqlJob implements SqlJob
{
    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxSqlJob.class );
    @Autowired
    private Environment myEnv;

//    @PersistenceContext(unitName="test") ??? this is never loaded, regardless if executed with SpringTestRunner, etc.
    protected  HibernateEntityManager em;

    /**
     * Constructor
     *
     */
    public OxSqlJob()
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

    /**
     * @see ch.sdi.core.intf.SqlJob#isAlreadyPresent(ch.sdi.core.impl.data.Person)
     */
    @Override
    public boolean isAlreadyPresent( Person<?> aPerson ) throws SdiException
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<OxUser> criteria = cb.createQuery(OxUser.class);
        Root<OxUser> root = criteria.from(OxUser.class);
        ParameterExpression<String> emailParam = cb.parameter(String.class);
        criteria.select(root).where(cb.equal( root.get("email"), emailParam ));

        TypedQuery<OxUser> query = em.createQuery(criteria);
        query.setParameter( emailParam, aPerson.getEMail() );
        List<OxUser> results = query.getResultList();

        if ( results.size() > 0 )
        {
            myLog.debug( "given Person is already present: " + results.get( 0 ) );
            return true;
        } // if results.size() > 0

        return false;
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#initPersistence()
     */
    @Override
    public void initPersistence()
    {
        em = EntityManagerProvider.getEntityManager( "oxwall" );
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#closePersistence()
     */
    @Override
    public void closePersistence()
    {
        if ( em != null )
        {
            em.close();
        } // if em != null
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#startTransaction()
     */
    @Override
    public void startTransaction()
    {
        em.getTransaction().begin();
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#commitTransaction()
     */
    @Override
    public void commitTransaction()
    {
        if ( em != null )
        {
            em.getTransaction().commit();
        } // if em != null
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#rollbackTransaction()
     */
    @Override
    public void rollbackTransaction()
    {
        if ( em != null )
        {
            em.getTransaction().rollback();
        } // if em != null
    }

}
