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

package ch.sdi.core.intf.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;



/**
 * TODO
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
public interface InputParser
{

    /**
     * Parses the given input stream.
     * <p>
     * If the member Charset is set, it will be used. Otherwise the system default character set is used.
     * <p>
     *
     * @param aInputStream
     *        must not be null
     * @param aDelimiter
     *        must not be null
     * @return a list which contains a list for each found line. The inner list contains the found values
     *         in each line.
     * @throws IOException
     */
    public abstract List<List<String>> parse( InputStream aInputStream, String aDelimiter ) throws IOException;

}
