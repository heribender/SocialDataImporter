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


package ch.sdi.core.impl.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.impl.data.converter.ConverterFactory;
import ch.sdi.core.intf.InputCollector;


/**
 * Factory which provides the appropriate collector.
 * <p>
 *
 * @version 1.0 (08.11.2014)
 * @author  Heri
 */
@Component
public final class InputCollectorFactory
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( InputCollectorFactory.class );
    @Autowired
    private ApplicationContext myAppCtxt;
    @Autowired
    private ConverterFactory myConverterFactory;

    /**
     * @param aCollectorType
     * @return
     * @throws SdiException
     */
    public InputCollector getCollector( CollectorType aCollectorType ) throws SdiException
    {
        myLog.debug( "Looking for an input collector for type " + aCollectorType );

        switch ( aCollectorType )
        {
            case CSV:
                return evaluateCSVParser();
            default:
                throw new SdiException( "Unhandled collectorType: " + aCollectorType, SdiException.EXIT_CODE_CONFIG_ERROR );
        }
    }

    /**
     * @return
     */
    private InputCollector evaluateCSVParser()
    {
        InputCollector collector =  myAppCtxt.getBean( CsvCollector.class );
        myLog.debug( "Found collector: " + collector );
        return collector;
    }


}
