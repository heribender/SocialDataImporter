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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.CollectorResult;
import ch.sdi.core.intf.InputCollector;
import ch.sdi.core.intf.SdiMainProperties;


/**
 * Executes the input collecting phase.
 * <p>
 * The collecting type must be configured by sdi.collect.type (one of CollectorType enums)
 * <p>
 *
 * @version 1.0 (08.11.2014)
 * @author Heri
 */
@Component
public class InputCollectorExecutor
{
    @Autowired
    private Environment  myEnv;
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

    /**
     * Performs the input collecting.
     *
     * @return a list of collected persons with normalized field names
     * @throws SdiException on any problem
     */
    public Collection<? extends Person<?>> execute() throws SdiException
    {
        CollectorType ct = CollectorType.parse( myEnv.getProperty( SdiMainProperties.KEY_COLLECT_TYPE ) );
        InputCollector collector = myInputCollectorFactory.getCollector( ct );
        CollectorResult collectorResult = collector.execute();
        Collection<? extends Person<?>> result = myInputTransformer.execute( collectorResult );
        return result;
    }
}
