package ch.sdi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;

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

    private static AnnotationConfigApplicationContext mySpringContext;

    public static void main( String[] args )
    {
        myLog.debug( "main starting" );

        mySpringContext =
                new AnnotationConfigApplicationContext(SocialDataImporter.class.getPackage().getName());

        try
        {
            mySpringContext.getBean(SocialDataImporterRunner.class).run(args);
        }
        catch ( SdiException t )
        {
            myLog.error( "Exception caught. Terminating with exitcode " + t.getExitCode(), t );
            System.exit( t.getExitCode() );
        }
        catch ( Throwable t )
        {
            myLog.error( "Exception caught: ", t );
            System.exit( 1 );
        }
        finally
        {
            mySpringContext.close();
        }
    }

}
