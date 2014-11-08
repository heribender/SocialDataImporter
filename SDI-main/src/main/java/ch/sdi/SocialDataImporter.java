package ch.sdi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

import ch.sdi.core.impl.cfg.ConfigHelper;
import ch.sdi.core.impl.data.InputTransformer;

/**
 * Main class of the SocialDataImporter application
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@Component
public class SocialDataImporter
{

    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( SocialDataImporter.class );

    @Autowired
    private ConfigurableEnvironment  myEnv;
    @Autowired
    private InputTransformer  myInputTransformer;


    private static AnnotationConfigApplicationContext mySpringContext;

    public static void main( String[] args )
    {
        myLog.debug( "main starting" );

        mySpringContext =
                new AnnotationConfigApplicationContext(SocialDataImporter.class.getPackage().getName());

        try
        {
            mySpringContext.getBean(SocialDataImporter.class).run(args);
        }
        finally
        {
            mySpringContext.close();
        }
    }

    public void run( String[] args )
    {
        myLog.debug( "adding command line arguments to the environment: " );  // TODO: debug out args

        MutablePropertySources propertySources = myEnv.getPropertySources();
        propertySources.addFirst(
                   new SimpleCommandLinePropertySource( ConfigHelper.PROP_SOURCE_NAME_CMD_LINE, args ));


        myLog.trace( "inputcollector.usernamekey: " + myEnv.getProperty( "inputcollector.usernamekey" ) );








    }

}