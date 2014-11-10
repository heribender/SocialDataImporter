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

package ch.sdi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ch.sdi.core.exc.SdiException;


/**
 * This testrunner has the same code as the SocialDataImporter from project SDI-main. You can use this
 * entry point for running the application within eclipse and have the log4j
 * configuration of the core test.<p>
 *
 * @version 1.0 (07.11.2014)
 * @author  Heri
 */
public class SocialDataImporterTestRunner
{

    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( SocialDataImporterTestRunner.class );

    private static AnnotationConfigApplicationContext mySpringContext;

    public static void main( String[] args )
    {
        myLog.info( "main starting" );

        mySpringContext =
                new AnnotationConfigApplicationContext(SocialDataImporterTestRunner.class.getPackage().getName());

        try
        {
//            Object o = mySpringContext.getBean(SdiMainProperties.class);
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
