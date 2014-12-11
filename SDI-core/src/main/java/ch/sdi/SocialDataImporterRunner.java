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

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.cfg.ConfigUtils;
import ch.sdi.core.impl.data.InputCollectorExecutor;
import ch.sdi.core.impl.data.InputTransformer;
import ch.sdi.core.impl.data.Person;
import ch.sdi.core.impl.data.PersonKey;
import ch.sdi.core.target.TargetExecutor;
import ch.sdi.report.SdiReporter;


/**
 * TODO
 *
 * @version 1.0 (10.11.2014)
 * @author  Heri
 */
@Component
public class SocialDataImporterRunner
{

    /** logger for this class */
    private static Logger myLog = LogManager.getLogger( SocialDataImporterRunner.class );

    @Autowired
    private ConfigurableEnvironment  myEnv;
    @Autowired
    private InputCollectorExecutor myCollectorExecutor;
    @Autowired
    private InputTransformer  myInputTransformer;
    @Autowired
    private UserPropertyOverloader  myUserPropertyOverloader;
    @Autowired
    private TargetExecutor  myTargetExecutor;
    @Autowired
    private SdiReporter  mySdiReporter;
    @Autowired
    private ConversionService  myConversionService;



    /**
     * Constructor
     *
     */
    public SocialDataImporterRunner()
    {
        super();
    }

    public void run( String[] args ) throws SdiException
    {
        if ( myConversionService == null )
        {
            throw new SdiException( "ConversionService not injected",
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        } // if myConversionService == null

        ConfigUtils.setMyConversionService( myConversionService );

        myUserPropertyOverloader.overrideByUserProperties();

        myLog.debug( "adding command line arguments to the environment: " );  // TODO: debug out args

        MutablePropertySources propertySources = myEnv.getPropertySources();
        propertySources.addFirst(
                   new SimpleCommandLinePropertySource( ConfigUtils.PROP_SOURCE_NAME_CMD_LINE, args ));

        if ( myLog.isDebugEnabled() )
        {
            StringBuilder sb = new StringBuilder( "After self-configuration, and after user property "
                    + "override. " );
            sb.append( myLog.isTraceEnabled() ? "Loaded properties: " : "Loaded property sources: " );

            MutablePropertySources mps = myEnv.getPropertySources();
            for ( PropertySource<?> propertySource : mps )
            {
                sb.append( "\n    PropertySource: " ).append( propertySource.getName() );

                if ( myLog.isTraceEnabled() )
                {
                    sb.append( "\n        " ).append( "" + propertySource.getSource() );
                } // if myLog.isTraceEnabled()
            }

            if ( myLog.isTraceEnabled() )
            {
                myLog.trace( sb.toString() );
            } // if myLog.isTraceEnabled()
            else if ( myLog.isDebugEnabled() )
            {
                myLog.debug( sb.toString() );
            }
        } // if myLog.isDebugEnabled()


        myLog.trace( "inputcollector.thing.alternateName: " + myEnv.getProperty( "inputcollector.thing.alternateName" ) );

        try
        {
            PersonKey.initCustomKeys( myEnv );

            mySdiReporter.reset();

            Collection<? extends Person<?>> inputPersons = myCollectorExecutor.execute();

            if ( myLog.isDebugEnabled() )
            {
                myLog.debug( "collected persons: " + inputPersons );
            } // if myLog.isDebugEnabled()

            myTargetExecutor.execute( inputPersons );

        }
        finally
        {
            String report = mySdiReporter.getReport();
            myLog.debug( "Generated report:\n" + report );
            // TODO: Write it to a file
        }

    }

}
