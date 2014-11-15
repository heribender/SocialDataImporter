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

package ch.sdi.core.intf;

import ch.sdi.core.annotations.SdiProps;


/**
 * TODO
 *
 * @version 1.0 (04.11.2014)
 * @author  Heri
 */
@SdiProps( "sdimain" )
public interface SdiMainProperties extends SdiProperties
{
    public static final String USER_OVERRIDE_PREFIX = "user.";
    public static final String SDI_PROPERTIES_RESOURCE_NAME = "Sdi.properties";

    public static final String KEY_SDI_DRYRUN = "sdi.dry-run";
    public static final String KEY_SDI_PROPERTIESOVERRIDE_INCLUDEROOT =
            "sdi.propertiesoverride.includeroot";

    public static final String KEY_COLLECT_TYPE = "sdi.collect.type";

    public static final String KEY_COLLECT_CSV_FILENAME = "sdi.collect.csv.filename";
    public static final String KEY_COLLECT_CSV_DELIMITER = "sdi.collect.csv.delimiter";
    public static final String KEY_COLLECT_CSV_HEADER_ROW = "sdi.collect.csv.headerrow";
    public static final String KEY_COLLECT_CSV_SKIP_AFTER_HEADER = "sdi.collect.csv.skipafterheader";
    public static final String KEY_COLLECT_CSV_FIELD_NAMES = "sdi.collect.csv.fieldnames";

    public static final String KEY_PREFIX_CONVERTER = "sdi.converter.";

}
