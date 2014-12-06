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


package ch.sdi.core.impl.data.converter;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
 *
 * @version 1.0 (06.12.2014)
 * @author  Heri
 */
public class ConverterNumberListTest
{
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterNumberList#toLongList(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testToLongList() throws Throwable
    {
        String aStringArray = "1, 2, 3, 4";
        List<Long> received = ConverterNumberList.toLongList( aStringArray, "," );
        Assert.assertNotNull( received );
        Assert.assertEquals( 4, received.size() );

        aStringArray = "";
        received = ConverterNumberList.toLongList( aStringArray, "," );
        Assert.assertNotNull( received );
        Assert.assertEquals( 0, received.size() );
    }

    /**
     * Test method for {@link ch.sdi.core.impl.data.converter.ConverterNumberList#toList(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testToList() throws Throwable
    {
        String aStringArray = "1, 2, 3, 4";
        List<Number> received = ConverterNumberList.toList( aStringArray, "," );
        Assert.assertNotNull( received );
        Assert.assertEquals( 4, received.size() );
    }

}
