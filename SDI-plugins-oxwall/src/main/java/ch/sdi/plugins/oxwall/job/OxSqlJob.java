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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ejb.HibernateEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.impl.data.PersonKey;
import ch.sdi.core.impl.data.converter.ConverterGender;
import ch.sdi.core.intf.SdiMainProperties;
import ch.sdi.core.intf.SqlJob;
import ch.sdi.plugins.oxwall.OxProfileQuestion;
import ch.sdi.plugins.oxwall.OxQuestionType;
import ch.sdi.plugins.oxwall.OxTargetConfiguration;
import ch.sdi.plugins.oxwall.OxTargetJobContext;
import ch.sdi.plugins.oxwall.OxUtils;
import ch.sdi.plugins.oxwall.sql.OxProfileData;
import ch.sdi.plugins.oxwall.sql.OxUser;
import ch.sdi.report.ReportMsg;
import ch.sdi.report.ReportMsg.ReportType;


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
    public static final String KEY_PREFIX_PROFILE_QUESTION = "ox.target.qn.";
    public static final String KEY_PREFIX_GENDER = "ox.target.sex.";
    public static final String KEY_DEFAULT_GROUPS = "ox.target.defaultGroups";

    @Autowired
    private Environment myEnv;

    // does not work: @PersistenceContext(unitName="oxwall")
    protected  HibernateEntityManager myEntityManager;
    private boolean myDryRun;
    private long myDummyId = 1;
    private Map<String,OxProfileQuestion> myProfileQuestions;
    private Optional<Long> myDefaultGroup = Optional.empty();
    private Map<ConverterGender.Gender,Number> myGenderMap;
    private TypedQuery<OxUser> myQueryEMail;
    private ParameterExpression<String> myEMailParam;

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
        List<Object> dbEntities = new ArrayList<Object>();

        OxUser user = new OxUser();
        user.setUsername( aPerson.getStringProperty( PersonKey.THING_ALTERNATENAME.getKeyName() ) );
        user.setAccountType( myEnv.getProperty( OxTargetConfiguration.KEY_USER_ACCOUNT_TYPE ) );
        user.setActivityStamp( OxUtils.dateToLong( new Date() ) );
        user.setEmail( aPerson.getEMail() );
        user.setJoinIp( 1234L ); // see comment in OxUser
        user.setJoinStamp( OxUtils.dateToLong( new Date() ) );
        user.setPassword( aPerson.getStringProperty( OxTargetJobContext.KEY_ENCRYPTED_PASSWORD ));
        saveEntity( dbEntities, user );

        for ( String personKey : myProfileQuestions.keySet() )
        {
            OxProfileQuestion question = myProfileQuestions.get( personKey );
            OxProfileData profileData = new OxProfileData();
            profileData.setQuestionName( question.getValue() );
            profileData.setUserId( user.getId() );
            switch ( question.getType() )
            {
                case text:
                    profileData.setTextValue( aPerson.getStringProperty( personKey ) );
                    break;
                case number:
                    Long value;
                    if ( personKey.equalsIgnoreCase( PersonKey.PERSON_GENDER.getKeyName() ) )
                    {
                        value = myGenderMap.get( aPerson.getProperty( personKey,
                                                                      ConverterGender.Gender.class ) )
                                                                      .longValue();
                    }
                    else
                    {
                        value = aPerson.getNumberProperty( personKey ).longValue();
                    }
                    profileData.setIntValue( value );
                    break;
                case date:
                    profileData.setDateValue( aPerson.getDateProperty( personKey ) );
                    break;
            }

            saveEntity( dbEntities, profileData );
        }


        ReportMsg msg = new ReportMsg( ReportType.SQL_TARGET,
                                       aPerson.getEMail(),
                                       dbEntities.toArray() );
        myLog.info( msg );
//        throw new SdiException( "TODO: remove: ",
//                                SdiException.EXIT_CODE_CONFIG_ERROR );
    }

    /**
     * @param dbEntities
     * @param user
     * @throws SdiException
     */
    private void saveEntity( List<Object> dbEntities, Object user ) throws SdiException
    {
        if ( myDryRun )
        {
            try
            {
                MethodUtils.invokeExactMethod( user, "setId", new Object[] { Long.valueOf( myDummyId ) } );
            }
            catch ( Throwable t )
            {
                throw new SdiException( "Entity has no 'setId( Long )' method", t, SdiException.EXIT_CODE_UNKNOWN_ERROR );
            }

            myDummyId++;
        }
        else
        {
            myEntityManager.persist( user );
        } // if..else myDryRun

        dbEntities.add( user );
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#isAlreadyPresent(ch.sdi.core.impl.data.Person)
     */
    @Override
    public boolean isAlreadyPresent( Person<?> aPerson ) throws SdiException
    {
        myQueryEMail.setParameter( myEMailParam, aPerson.getEMail() );
        List<OxUser> results = myQueryEMail.getResultList();

        if ( results.size() > 0 )
        {
            myLog.debug( "given Person is already present: " + results.get( 0 ) );
            return true;
        } // if results.size() > 0

        return false;
    }

    /**
     *
     */
    private void initAlreadyPresentQuery()
    {
        CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();

        CriteriaQuery<OxUser> criteria = cb.createQuery(OxUser.class);
        Root<OxUser> root = criteria.from(OxUser.class);
        myEMailParam = cb.parameter(String.class);
        criteria.select(root).where(cb.equal( root.get("email"), myEMailParam ));

        myQueryEMail = myEntityManager.createQuery(criteria);
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#init()
     */
    @Override
    public void init() throws SdiException
    {
        myDryRun = ConfigUtils.getBooleanProperty( myEnv, SdiMainProperties.KEY_DRYRUN, false );

        myEntityManager = EntityManagerProvider.getEntityManager( "oxwall" );

        if ( myEntityManager == null )
        {
            throw new SdiException( "Problems initializing EntityManager",
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if em == null

        initAlreadyPresentQuery();
        initProfileQuestions();
        initGenderMap();
        initDefaultGroups();

    }

    /**
     * @throws SdiException
     */
    private void initDefaultGroups() throws SdiException
    {
        String configured = myEnv.getProperty( KEY_DEFAULT_GROUPS );
        if ( StringUtils.hasText( configured ) )
        {
            try
            {
                myDefaultGroup = Optional.of( Long.parseLong( configured ) );
            }
            catch ( NumberFormatException t )
            {
                throw new SdiException( "DefaultGroup configuration " + configured + " cannot be converted to number",
                                        t,
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            }

        } // if !StringUtils.hasText( configured )
    }

    /**
     * @throws SdiException
     */
    private void initGenderMap() throws SdiException
    {
        myGenderMap = new HashMap<ConverterGender.Gender,Number>();

        for ( ConverterGender.Gender gender : ConverterGender.Gender.values() )
        {
            String key = KEY_PREFIX_GENDER + gender;
            String configured = myEnv.getProperty( key );

            if ( !StringUtils.hasText( configured ) )
            {
                throw new SdiException( "Gender mapping not configured: " + key,
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            } // if !StringUtils.hasText( configured )

            try
            {
                myGenderMap.put( gender, Long.parseLong( configured ) );
            }
            catch ( NumberFormatException t )
            {
                throw new SdiException( "Gender mapping " + configured + " cannot be converted to number",
                                        t,
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            }

        }
    }

    /**
     * @throws SdiException
     */
    private void initProfileQuestions() throws SdiException
    {
        myProfileQuestions = new HashMap<String,OxProfileQuestion>();

        for ( String personKey : PersonKey.getKeyNames() )
        {
            String key = KEY_PREFIX_PROFILE_QUESTION + personKey;
            myLog.trace( "Looking up profile question configuration " + key );
            String configured = myEnv.getProperty( key );

            if ( !StringUtils.hasText( configured ) )
            {
                continue;
            } // if !StringUtils.hasText( configured )

            myLog.trace( "Found profile question configuration " + configured );

            String[] values = configured.trim().split( ":" );
            if ( values.length != 2 )
            {
                throw new SdiException( "Profile question not configured correctly: " + configured,
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            } // if values.length != 2

            OxQuestionType type;
            try
            {
                type = OxQuestionType.valueOf( values[0] );
            }
            catch ( IllegalArgumentException t )
            {
                throw new SdiException( "Profile question not configured correctly: " + configured + "; "
                                        + "type cannot be resolved",
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            }

            OxProfileQuestion question = new OxProfileQuestion();
            question.setType( type );
            question.setValue( values[1] );
            myLog.debug( "Adding profile question for key '" + personKey + "': " + question );
            myProfileQuestions.put( personKey, question );

        }
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#close()
     */
    @Override
    public void close() throws SdiException
    {
        if ( myEntityManager != null )
        {
            myEntityManager.close();
        } // if em != null
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#startTransaction()
     */
    @Override
    public void startTransaction()
    {
        myEntityManager.getTransaction().begin();
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#commitTransaction()
     */
    @Override
    public void commitTransaction()
    {
        if ( myEntityManager != null )
        {
            myEntityManager.getTransaction().commit();
        } // if em != null
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#rollbackTransaction()
     */
    @Override
    public void rollbackTransaction()
    {
        if ( myEntityManager != null )
        {
            myEntityManager.getTransaction().rollback();
        } // if em != null
    }

}
