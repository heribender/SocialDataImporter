package ch.sdi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

import ch.sdi.core.impl.data.ParserMapping;
import ch.sdi.core.intf.ParserMappingProperties;

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
    private ParserMapping myParseMapping;
    @Autowired
    private ConfigurableEnvironment  env;

    private static AnnotationConfigApplicationContext mySpringContext;

    public static void main( String[] args )
    {
        myLog.debug( "main starting" );

        mySpringContext = new AnnotationConfigApplicationContext(SocialDataImporter.class.getPackage().getName());

        try
        {
            mySpringContext.getBean(SocialDataImporter.class).init(args);
        }
        finally
        {
            mySpringContext.close();
        }
    }

    public void init( String[] args )
    {
        myLog.debug( "in init()" );

        MutablePropertySources propertySources = env.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource( args ));

        myLog.debug( "Username-Key before override: " + myParseMapping.getUsernameKey() );

        if ( mySpringContext.containsBean( ParserMappingProperties.BEAN_NAME ) )
        {
            ParserMappingProperties userProps = mySpringContext.getBean( ParserMappingProperties.BEAN_NAME, ParserMappingProperties.class );
            userProps.override();
        }

        myLog.debug( "Username-Key after override: " + myParseMapping.getUsernameKey() );







    }

}
