/**
 * Copyright (c) 2015 by the original author or authors.
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


package ch.sdi.core.impl.data.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.sdi.core.annotations.SdiFilter;
import ch.sdi.core.exc.SdiException;


/**
 * Filters commented lines.
 * <p>
 * This converter requires one parameter: the the character(s) which mark a line as commented out.
 * <p>
 * Note that the filter is annotated with Scope=Prototype to ensure that a new instance of the
 * filter is created each time a ctxt.getBean(() (or one of its variant) is called. The reason is that
 * there might be more than one filter of the same class which distinguish in parametrization.
 * <p>
 * @version 1.0 (24.01.2015)
 * @author  Heri
 */
@SdiFilter( "commentedLine" )
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FilterCommentedLine extends RawDataFilterString
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( FilterCommentedLine.class );

    protected String myCommentChars;

    /**
     * @see ch.sdi.core.impl.data.filter.CollectFilter#init(org.springframework.core.env.Environment, java.lang.String)
     */
    @Override
    public RawDataFilter<String> init( Environment aEnv, String aParameters ) throws SdiException
    {
        if ( !StringUtils.hasText( aParameters ) )
        {
            throw new SdiException( "Character(s) expected for this filter",
                                    SdiException.EXIT_CODE_CONFIG_ERROR );

        }

        myCommentChars = aParameters;

        return this;
    }

    /**
     * @see ch.sdi.core.impl.data.filter.CollectFilter#isFiltered(java.lang.Object)
     */
    @Override
    public boolean isFiltered( String aCriteria ) throws SdiException
    {
        if ( !StringUtils.hasText( aCriteria ) )
        {
            myLog.debug( "Given line is empty" );
            return false;
        }

        return aCriteria.startsWith( myCommentChars );
    }

    @Override
    public String toString()
    {
        String result = super.toString();

        result += "; CommentChars: " + myCommentChars;

        return result;
    }

}
