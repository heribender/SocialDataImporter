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

    public static final String KEY_DRYRUN = "sdi.dry-run";
    public static final String KEY_PROPERTIESOVERRIDE_INCLUDEROOT =
            "sdi.propertiesoverride.includeroot";
    public static final String KEY_OUTPUT_DIR = "sdi.outputDir";

    public static final String KEY_COLLECT_TYPE = "sdi.collect.type";

    public static final String KEY_COLLECT_CSV_FILENAME = "sdi.collect.csv.filename";
    public static final String KEY_COLLECT_CSV_DELIMITER = "sdi.collect.csv.delimiter";
    public static final String KEY_COLLECT_CSV_HEADER_ROW = "sdi.collect.csv.headerrow";
    public static final String KEY_COLLECT_CSV_SKIP_AFTER_HEADER = "sdi.collect.csv.skipafterheader";
    public static final String KEY_COLLECT_CSV_FIELD_NAMES = "sdi.collect.csv.fieldnames";

    public static final String KEY_PREFIX_CONVERTER = "sdi.converter.";

    public static final String KEY_TARGET_TEMP_DIR = "sdi.target.tmpDir";
    public static final String KEY_TARGET_IGNORE_FAILED_PERSON = "sdi.target.ignoreFailedPerson";

    public static final String KEY_PREFIX_TARGET_JOB = "sdi.targetjob.";

    public static final String KEY_MAIL_JOB = KEY_PREFIX_TARGET_JOB + "mail";
    public static final String KEY_SQL_JOB = KEY_PREFIX_TARGET_JOB + "sql";
    public static final String KEY_FTP_JOB = KEY_PREFIX_TARGET_JOB + "ftp";

    public static final String KEY_SQL_CONNECT_STRING = KEY_SQL_JOB + ".connectString";
    public static final String KEY_SQL_USER = KEY_SQL_JOB + ".user";
    public static final String KEY_SQL_PASSWORD = KEY_SQL_JOB + ".password";

    public static final String KEY_TARGET_HOST = "sdi.target.host";

    public static final String KEY_FTP_DEST_DIR = KEY_FTP_JOB + ".destinationDir";
    public static final String KEY_FTP_CMD_LINE = KEY_FTP_JOB + ".cmdLine";
    public static final String KEY_FTP_USER = KEY_FTP_JOB + ".user";
    public static final String KEY_FTP_PASSWORD = KEY_FTP_JOB + ".password";

    public static final String KEY_SSH_USER = "sdi.ssh.user";
    public static final String KEY_SSH_PASSWORD = "sdi.ssh.password";
    public static final String KEY_SSH_PORT = "sdi.ssh.port";
    public static final String KEY_SSH_CHECK_CERTIFICATE = "sdi.ssh.checkcertificate";

    public static final String KEY_TARGET_NAME = "sdi.target.name";



}
