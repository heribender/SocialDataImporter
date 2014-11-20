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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


/**
 * TODO
 *
 * @version 1.0 (20.11.2014)
 * @author  Heri
 */
@Component
public class SdiReporter
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( SdiReporter.class );

    private List<ReportMsg> myMessages = new ArrayList<ReportMsg>();

    /**
     * Constructor
     *
     */
    public SdiReporter()
    {
        super();
    }

    public void reset()
    {
        myLog.info( "resetting " + this.getClass().getSimpleName() );
        myMessages = new ArrayList<ReportMsg>();
    }

    public void add( ReportMsg aMsg )
    {
        myLog.trace( "adding a message" );
        myMessages.add( aMsg );
    }

    public String getReport()
    {
        myLog.debug( "Rendering the report" );
        // TODO
        return "TODO";
    }
}
