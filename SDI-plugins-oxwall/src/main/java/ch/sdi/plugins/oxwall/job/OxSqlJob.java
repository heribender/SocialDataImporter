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
import java.util.List;

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
import ch.sdi.core.impl.data.converter.ConverterNumberList;
import ch.sdi.core.intf.SdiMainProperties;
import ch.sdi.core.intf.SqlJob;
import ch.sdi.plugins.oxwall.OxTargetConfiguration;
import ch.sdi.plugins.oxwall.OxTargetJobContext;
import ch.sdi.plugins.oxwall.OxUtils;
import ch.sdi.plugins.oxwall.profile.OxProfileQuestion;
import ch.sdi.plugins.oxwall.profile.OxProfileQuestionDate;
import ch.sdi.plugins.oxwall.profile.OxProfileQuestionNumber;
import ch.sdi.plugins.oxwall.profile.OxProfileQuestionString;
import ch.sdi.plugins.oxwall.profile.OxQuestionFactory;
import ch.sdi.plugins.oxwall.profile.OxQuestionType;
import ch.sdi.plugins.oxwall.sql.entity.OxAvatar;
import ch.sdi.plugins.oxwall.sql.entity.OxProfileData;
import ch.sdi.plugins.oxwall.sql.entity.OxUser;
import ch.sdi.plugins.oxwall.sql.entity.OxUserGroupMembership;
import ch.sdi.plugins.oxwall.sql.entity.OxUserRole;
import ch.sdi.report.ReportMsg;
import ch.sdi.report.ReportMsg.ReportType;


/**
 * SQL job implementation for the oxwall platform.
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
    public static final String KEY_DEFAULT_GROUPS = "ox.target.defaultGroups";
    public static final String KEY_DEFAULT_ROLES = "ox.target.defaultRoles";
    public static final String KEY_GROUP_PRIVACY = "ox.target.groups.privacy";


    @Autowired
    private Environment myEnv;
    @Autowired
    private OxQuestionFactory myQuestionFactory;

    // does not work: @PersistenceContext(unitName="oxwall")
    protected  HibernateEntityManager myEntityManager;
    private boolean myDryRun;
    private long myDummyId = 1;
    private List<OxProfileQuestion> myProfileQuestions;
    private List<Long> myDefaultGroups;
    private List<Long> myDefaultRoles;
    private String myGroupPrivacy;
    private String myUserAccountType;
    private Integer myJoinIp;
    private boolean myEmailVerify;

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

        myLog.debug( "creating new user entity" );
        OxUser user = new OxUser();
        user.setUsername( aPerson.getStringProperty( PersonKey.THING_ALTERNATENAME.getKeyName() ) );
        user.setAccountType( myUserAccountType );
        user.setActivityStamp( OxUtils.dateToLong( new Date() ) );
        user.setEmail( aPerson.getEMail() );
        user.setJoinIp( myJoinIp.longValue() );
        user.setJoinStamp( OxUtils.dateToLong( new Date() ) );
        user.setPassword( aPerson.getStringProperty( OxTargetJobContext.KEY_ENCRYPTED_PASSWORD ));
        user.setEmailVerify( myEmailVerify );
        saveEntity( dbEntities, user );

        aPerson.setProperty( OxTargetJobContext.KEY_PERSON_USER_ID, user.getId() );;

        List<Long> roles = resolveRoles( aPerson );
        if ( roles.size() > 0 )
        {
            myLog.debug( "creating group membership entities" );
            for ( Long role : roles )
            {
                OxUserRole roleEntity = new OxUserRole();
                roleEntity.setUserId( user.getId() );
                roleEntity.setRoleId( role );
                saveEntity( dbEntities, roleEntity );
            }
        } // if groups.size() > 0

        myLog.debug( "creating profile question entities" );
        for ( OxProfileQuestion question : myProfileQuestions )
        {
            if ( !question.hasValue( aPerson ) )
            {
                continue;
            } // if !question.hasValue( aPerson )

            OxProfileData profileData = new OxProfileData();
            profileData.setUserId( user.getId() );
            question.fillValues( profileData, aPerson );
            saveEntity( dbEntities, profileData );
        }

        Long avatarHash = aPerson.getProperty( OxTargetJobContext.KEY_AVATAR_HASH, Long.class );
        if ( avatarHash != null )
        {
            myLog.debug( "creating avatar entities" );
            OxAvatar avatar = new OxAvatar();
            avatar.setUserId( user.getId() );
            avatar.setHash( avatarHash );
            saveEntity( dbEntities, avatar );
        } // if StringUtils.hasText( avatarHash )

        List<Long> groups = resolveMembership( aPerson );
        if ( groups.size() > 0 )
        {
            myLog.debug( "creating group membership entities" );
            for ( Long group : groups )
            {
                OxUserGroupMembership groupEntity = new OxUserGroupMembership();
                groupEntity.setGroupId( group );
                groupEntity.setUserId( user.getId() );
                groupEntity.setTimeStamp( OxUtils.dateToLong( new Date() ) );
                groupEntity.setPrivacy( myGroupPrivacy );
                saveEntity( dbEntities, groupEntity );
            }
        } // if groups.size() > 0

        ReportMsg msg = new ReportMsg( ReportType.SQL_TARGET,
                                       aPerson.getEMail(),
                                       dbEntities.toArray() );
        myLog.info( msg );
    }

    /**
     * @param aPerson
     * @return
     */
    private List<Long> resolveRoles( Person<?> aPerson )
    {
        List<Long> result = new ArrayList<Long>();  // TODO: add roles per user, as with group membership
        myLog.debug( "collected roles of person: " + result );
        result.addAll( myDefaultRoles );
        myLog.debug( "resolved roles of person: " + result );
        return result;
    }

    /**
     * @param aPerson
     * @return
     * @throws SdiException
     */
    private List<Long> resolveMembership( Person<?> aPerson ) throws SdiException
    {
        List<Long> result = aPerson.getLongListProperty( PersonKey.PERSON_MEMBEROF.getKeyName() );
        myLog.debug( "collected groups of person: " + result );
        result.addAll( myDefaultGroups );
        myLog.debug( "resolved groups of person: " + result );
        return result;
    }

    /**
     * @param dbEntities
     * @param aEntity
     * @throws SdiException
     */
    private void saveEntity( List<Object> dbEntities, Object aEntity ) throws SdiException
    {
        if ( myDryRun )
        {
            myLog.trace( "DryRun: Going to save entity: " + aEntity );

            try
            {
                MethodUtils.invokeExactMethod( aEntity, "setId", new Object[] { Long.valueOf( myDummyId ) } );
            }
            catch ( Throwable t )
            {
                throw new SdiException( "Entity has no 'setId( Long )' method", t, SdiException.EXIT_CODE_UNKNOWN_ERROR );
            }

            myDummyId++;
        }
        else
        {
            myLog.trace( "Going to save entity: " + aEntity );
            myEntityManager.persist( aEntity );
        } // if..else myDryRun

        dbEntities.add( aEntity );
    }

    /**
     * @see ch.sdi.core.intf.SqlJob#isAlreadyPresent(ch.sdi.core.impl.data.Person)
     */
    @Override
    public boolean isAlreadyPresent( Person<?> aPerson ) throws SdiException
    {
        if ( myDryRun )
        {
            myLog.debug( "DryRun is active. Not checking for duplicate person" );
            return false;
        } // if myDryRun

        CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();

        CriteriaQuery<OxUser> criteria = cb.createQuery(OxUser.class);
        Root<OxUser> root = criteria.from(OxUser.class);
        ParameterExpression<String> mailParam = cb.parameter(String.class);
        criteria.select(root).where(cb.equal( root.get("email"), mailParam ));

        TypedQuery<OxUser> queryEMail = myEntityManager.createQuery(criteria);

        queryEMail.setParameter( mailParam, aPerson.getEMail() );
        List<OxUser> results = queryEMail.getResultList();

        if ( results.size() > 0 )
        {
            myLog.debug( "given Person is already present: " + results.get( 0 ) );
            return true;
        } // if results.size() > 0

        return false;
    }

    /**
     * Checks if the given hash is present in the ow_base_avatar table
     *
     * @param aHash
     * @return
     */
    public boolean isAvatarHashPresent( Long aHash )
    {
        if ( myDryRun )
        {
            myLog.debug( "DryRun is active. Not checking for duplicate avatar hash" );
            return false;
        } // if myDryRun

        CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();

        CriteriaQuery<OxAvatar> criteria = cb.createQuery(OxAvatar.class);
        Root<OxAvatar> root = criteria.from(OxAvatar.class);
        ParameterExpression<Long> avatarParam = cb.parameter(Long.class);
        avatarParam = cb.parameter(Long.class);
        criteria.select(root).where(cb.equal( root.get("hash"), avatarParam ));
        TypedQuery<OxAvatar> query = myEntityManager.createQuery(criteria);
        query.setParameter( avatarParam, aHash );
        List<OxAvatar> results = query.getResultList();

        if ( results.size() > 0 )
        {
            myLog.debug( "given avatar hash is already present: " + aHash );
            return true;
        } // if results.size() > 0

        return false;
    }

    /**
     * @see ch.sdi.core.intf.TargetJob#init()
     */
    @Override
    public void init() throws SdiException
    {
        myDryRun = ConfigUtils.getBooleanProperty( myEnv, SdiMainProperties.KEY_DRYRUN, false );
        myGroupPrivacy = myEnv.getProperty( KEY_GROUP_PRIVACY, "everybody" );
        myUserAccountType = myEnv.getProperty( OxTargetConfiguration.KEY_USER_ACCOUNT_TYPE );
        myJoinIp = ConfigUtils.getIntProperty( myEnv, OxTargetConfiguration.KEY_USER_JOINIP, 12345 );
        myEmailVerify = ConfigUtils.getBooleanProperty( myEnv, OxTargetConfiguration.KEY_USER_EMAIL_VERIFY );


        myEntityManager = EntityManagerProvider.getEntityManager( "oxwall" );

        if ( myEntityManager == null )
        {
            throw new SdiException( "Problems initializing EntityManager",
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if em == null

        initProfileQuestions();
        initDefaultGroups();
        initDefaultRoles();

    }

    /**
     *
     */
    private void initDefaultRoles() throws SdiException
    {
        String configured = myEnv.getProperty( KEY_DEFAULT_ROLES, "" );
        myDefaultRoles = ConverterNumberList.toLongList( configured, "," );
        myLog.debug( "Configured default roles: " + myDefaultRoles );

    }

    /**
     * @throws SdiException
     */
    private void initDefaultGroups() throws SdiException
    {
        String configured = myEnv.getProperty( KEY_DEFAULT_GROUPS, "" );
        myDefaultGroups = ConverterNumberList.toLongList( configured, "," );
        myLog.debug( "Configured default groups: " + myDefaultGroups );
    }

    /**
     * Checks the configuration if there are profile questions configured and populates the
     * myProfileQuestions member with the found configurations.
     * <p>
     * @throws SdiException
     */
    private void initProfileQuestions() throws SdiException
    {
        myProfileQuestions = new ArrayList<OxProfileQuestion>();

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
            if ( values.length < 2 )
            {
                throw new SdiException( "Profile question not configured correctly: " + configured,
                                        SdiException.EXIT_CODE_CONFIG_ERROR );
            }

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

            OxProfileQuestion question = null;

            switch ( type )
            {
                case text:
                    question = new OxProfileQuestionString( values[1], personKey );
                    break;
                case number:
                    question = new OxProfileQuestionNumber( values[1], personKey );
                    break;
                case date:
                    question = new OxProfileQuestionDate( values[1], personKey );
                    break;
                case custom:
                    if ( values.length < 3 )
                    {
                        throw new SdiException( "Profile question not configured correctly: " + values,
                                                SdiException.EXIT_CODE_CONFIG_ERROR );
                    }

                    question = myQuestionFactory.getCustomQuestion( values[1], values[2], personKey );
                    break;
                default:
                    throw new SdiException( "unhandled question type: " + type,
                                            SdiException.EXIT_CODE_UNKNOWN_ERROR );
            }

            question.init( myEnv );
            myLog.debug( "Adding profile question for key '" + personKey + "': " + question );
            myProfileQuestions.add( question );

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
            if ( myEntityManager.getTransaction().isActive() )
            {
                myEntityManager.getTransaction().commit();
            } // if myEntityManager.getTransaction().isActive()
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
            if ( myEntityManager.getTransaction().isActive() )
            {
                myEntityManager.getTransaction().rollback();
            } // if myEntityManager.getTransaction().isActive()
        } // if em != null
    }

}
