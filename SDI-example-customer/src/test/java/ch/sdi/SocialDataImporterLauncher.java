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

package ch.sdi;


/**
 * This launcher just starts the main application.<p>
 *
 * You can use this launcher within eclipse in order to run the application with the project's class files
 * and resources on the classpath. It is in test branch in order to pick up the test log4j2 configuration
 * of in sdi-core.
 * <p>
 *
 * <b>Note for running within eclipse:</b> The execution directory is not the same as if the app would be
 * started in the bin subfolder, but the default configurations are provided for running in standalone
 * mode.<p>
 *
 * Therefore you have to provide some command line arguments for running in eclipse. Go to
 * RunConfigurations/&lt;yourLaunchConfiguration>/Arguments/ and enter following parameters:
 *
 * <pre>
 *     --sdi.collect.csv.filename=./input/testImport_4_all_fields.csv
 *     --sdi.dry-run=true
 *     --sdi.mail.body.template=./input/mailbody.txt
 * </pre>
 *
 * The <code>sdi.dry-run=true</code> property setting has the effect that no attempts are done for
 * connecting to external systems (DB, FTP target, SMTP server).
 * <p>
 *
 * @version 1.0 (07.11.2014)
 * @author  Heri
 */
public class SocialDataImporterLauncher
{

    /**
     * @param args
     */
    public static void main( String[] args )
    {
        SocialDataImporter.main( args );
    }

}
