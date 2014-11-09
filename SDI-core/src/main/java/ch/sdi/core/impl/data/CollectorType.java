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


/**
 * Types of the collectors
 *
 * @version 1.0 (08.11.2014)
 * @author  Heri
 */
public enum CollectorType
{

    /** Collector which can parse a CSV file */
    CSV;

    /**
     * If not found, an IllegalArgumentException is thrown
     * <p>
     * @param aValue
     * @return the found CollectorType which matches the given value
     */
    public static CollectorType parse( String aValue )
    {
        for ( CollectorType ct : CollectorType.values() )
        {
            if ( ct.name().equalsIgnoreCase( aValue ) )
            {
                return ct;
            } // if ct.name().equalsIgnoreCase( aValue )
        }

        throw new IllegalArgumentException( "Unknown CollectorType: " + aValue );
    }
}
