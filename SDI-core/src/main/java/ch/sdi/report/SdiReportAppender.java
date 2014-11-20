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

package ch.sdi.report;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;
import org.springframework.context.ApplicationContext;

import ch.sdi.core.util.ApplicationContextProvider;


/**
 * TODO
 *
 * @version 1.0 (20.11.2014)
 * @author Heri
 */
@Plugin( name = "SdiReportAppender", category = "Core", elementType = "appender", printObject = true )
public class SdiReportAppender extends AbstractAppender
{

    private static final long serialVersionUID = 1L;

    private SdiReporter myReporter;

    @PluginFactory
    public static SdiReportAppender createAppender( @PluginAttribute( "name" ) String name,
                                                    @PluginAttribute( "ignoreExceptions" ) boolean ignoreExceptions,
                                                    @PluginElement( "Layout" ) Layout<?> layout,
                                                    @PluginElement( "Filters" ) Filter filter )
    {

        if ( name == null )
        {
            LOGGER.error( "No name provided for StubAppender" );
            return null;
        }

        return new SdiReportAppender( name, null, null );
    }

    /**
     * Constructor
     *
     * @param aName
     * @param aFilter
     * @param aLayout
     */
    public SdiReportAppender( String aName, Filter aFilter, Layout<? extends Serializable> aLayout )
    {
        super( aName, aFilter, aLayout );
    }

    /**
     * @see org.apache.logging.log4j.core.Appender#append(org.apache.logging.log4j.core.LogEvent)
     */
    @Override
    public void append( LogEvent aEvent )
    {
        Message m = aEvent.getMessage();

//        LOGGER.error( "Received Event: " + m.getClass().getSimpleName() );
        if ( ! ( m instanceof ReportMsg ) )
        {
            return;
        } // if ! ( m instanceof ReportMsg )

        SdiReporter reporter = getReporter();

        if ( reporter == null )
        {
            LOGGER.error( "Bean SdiReporter is null" );
            return;
        } // if reporter == null


        myReporter.add( (ReportMsg) m );
    }

    private SdiReporter getReporter()
    {
        if ( myReporter != null )
        {
            return myReporter;
        }

        ApplicationContext ctxt = ApplicationContextProvider.getApplicationContext();

        if ( ctxt == null )
        {
            LOGGER.error( "ApplicationContext is null. Maybe too early? Or we are in a unit test" );
            return null;
        } // if ctxt == null

        myReporter = ctxt.getBean( SdiReporter.class );

        if ( myReporter == null )
        {
            LOGGER.error( "Bean SdiReporter not found" );
        } // if myReporter == null

        return myReporter;
    }

}
