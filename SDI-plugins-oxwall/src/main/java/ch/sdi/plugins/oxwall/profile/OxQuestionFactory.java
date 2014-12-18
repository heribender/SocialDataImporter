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


package ch.sdi.plugins.oxwall.profile;

import java.lang.reflect.Constructor;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.util.ClassUtil;


/**
 * Factory for instatiating custom ProfileQuestion implementations.
 * <p>
 * These are the configured ox.target.qn.xxxx properties of type "custom" which must have three
 * parameters in the value part.
 * <p>
 *
 * @version 1.0 (06.12.2014)
 * @author  Heri
 */
@Component
public class OxQuestionFactory
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( OxQuestionFactory.class );

    @Autowired
    private Environment myEnv;

    /**
     * Instantiates a custom oxwall ProfileQuestion type.
     * <p>
     * see properties ox.target.qn.xxxx). For example:
     * <pre>
     *     ox.target.qn.person.gender=custom:gendermap:sex
     * </pre>
     * <p>
     * @param aBeanName
     *        the name of the implementing bean (in fact its the name parameter of the OxCustomQuestion
     *        annotation). This corresponds to the second parameter of the property value.
     * @param aQuestionName
     *        the name of the question. This corresponds to the third parameter of the property value.
     * @param aPersonKey
     *        The key in the person PropertySource under which the corresponding value will be stored.
     * @return
     * @throws SdiException
     */
    public OxProfileQuestion getCustomQuestion( String aBeanName, String aQuestionName, String aPersonKey )
            throws SdiException
    {
        Collection<Class<OxProfileQuestion>> candidates
            = ClassUtil.findCandidatesByAnnotation( OxProfileQuestion.class,
                                                    OxCustomQuestion.class,
                                                    "ch.sdi.plugins.oxwall" );

        for ( Class<OxProfileQuestion> clazz : candidates )
        {
            myLog.trace( "found candidate for custom question: " + clazz.getName() );
            OxCustomQuestion ann = clazz.getAnnotation( OxCustomQuestion.class );

            if ( !ann.value().endsWith( aBeanName ) )
            {
                myLog.trace( "candidate has not the desired bean name (" + aBeanName
                             + ") but: " + ann.value() );
                continue;
            } // if !ann.value().endsWith( "beanName" )

            myLog.trace( "found custom question class with beanName(" + aBeanName
                             + "): " + clazz.getName() );

            try
            {
                Constructor<OxProfileQuestion> constructor =
                        clazz.getConstructor( new Class<?>[] { String.class, String.class } );
                return constructor.newInstance( new Object[] { aQuestionName, aPersonKey } );

            }
            catch ( Throwable t )
            {
                throw new SdiException( "missing desired constructor found custom question class with"
                        + " beanName(" + aBeanName + "): " + clazz.getName(),
                        SdiException.EXIT_CODE_CONFIG_ERROR );
            }

        }

        throw new SdiException( "No suitable custom question found. BeanName: " + aBeanName,
                                SdiException.EXIT_CODE_CONFIG_ERROR );
    }

}
