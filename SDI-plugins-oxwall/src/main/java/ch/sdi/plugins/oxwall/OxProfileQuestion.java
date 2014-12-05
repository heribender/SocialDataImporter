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


package ch.sdi.plugins.oxwall;



/**
 * Entity for holding oxwalls profile question names (which can be customized)
 *
 * @version 1.0 (05.12.2014)
 * @author  Heri
 */
public class OxProfileQuestion
{
    private OxQuestionType myType;
    /** the question name */
    private String myName;

    /**
     * @return type
     */
    public OxQuestionType getType()
    {
        return myType;
    }

    /**
     * @param  aType
     *         type to set
     */
    public void setType( OxQuestionType aType )
    {
        myType = aType;
    }

    /**
     * @return value
     */
    public String getValue()
    {
        return myName;
    }

    /**
     * @param  aValue
     *         value to set
     */
    public void setValue( String aValue )
    {
        myName = aValue;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    Type : " ).append( myType );
        sb.append( "\n    Name : " ).append( myName );
        return sb.toString();
    }



}
