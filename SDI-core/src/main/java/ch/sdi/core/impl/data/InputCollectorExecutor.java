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

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.cfg.SdiMainProperties;
import ch.sdi.core.intf.data.InputCollector;


/**
 * TODO
 *
 * @version 1.0 (08.11.2014)
 * @author Heri
 */
@Component
public class InputCollectorExecutor
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( InputCollectorExecutor.class );

    @Autowired
    private ConfigurableEnvironment  myEnv;
    @Autowired
    private InputTransformer  myInputTransformer;
    @Autowired
    private InputCollectorFactory  myInputCollectorFactory;

    /**
     * Constructor
     *
     */
    public InputCollectorExecutor()
    {
        super();
    }

    public Collection<? extends Person<?>> execute() throws SdiException
    {
        Collection<? extends Person<?>> result = null;

        CollectorType ct = CollectorType.parse( myEnv.getProperty( SdiMainProperties.KEY_CSV_TYPE ) );

        InputCollector collector = myInputCollectorFactory.getCollector( ct );
        collector.execute();

        // TODO: Collect

        // TODO: Transform

        return result;
    }
}
