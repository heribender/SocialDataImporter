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

import java.util.Date;



/**
 * Utilities for the oxwall platform.
 * <p>
 *
 * @version 1.0 (04.12.2014)
 * @author  Heri
 */
public class OxUtils
{

    /**
     * Converts the given date to the timestamp representation used in oxwall tables (seconds since 1970)
     * <p>
     * @param aDate
     * @return the unix timestamp (seconds since 1970) or zero if given date is <code>null</code>.
     */
    public static long dateToLong( Date aDate )
    {
        if ( aDate == null )
        {
            return 0L;
        } // if aDate = null

        return aDate.getTime() / 1000;
    }
}
