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


package ch.sdi.plugins.oxwall.converter;

/**
 * The oxwall specific gender enumeration, mapped to the default value used in the oxwall DB.
 * <p>
 * The default value can be overridden by configuring it in target.properties
 *
 * @version 1.0 (18.12.2014)
 * @author  Heri
 */
public enum OxGender
{
    male( 1 ),
    female( 2 ),
    dontcare( 4 );

    private int myDefaultValue;

    /**
     * Constructor
     *
     * @param aDefaultValue
     */
    private OxGender( int aDefaultValue )
    {
        myDefaultValue = aDefaultValue;
    }

    /**
     * The default value as it is used in the oxwall DB
     * @return
     */
    public int getDefaultValue()
    {
        return myDefaultValue;
    }


}