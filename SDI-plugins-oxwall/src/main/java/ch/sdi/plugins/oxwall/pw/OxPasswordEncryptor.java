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

package ch.sdi.plugins.oxwall.pw;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import ch.sdi.plugins.oxwall.TargetConfiguration;


/**
 * TODO
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
@Component
public class OxPasswordEncryptor implements ch.sdi.core.intf.PasswordEncryptor
{

    private Logger myLog = LogManager.getLogger( OxPasswordEncryptor.class );
    @Autowired
    private ConfigurableEnvironment  myEnv;

    /**
     * @throws NoSuchAlgorithmException
     * @see ch.sdi.core.intf.PasswordEncryptor#encrypt(java.lang.String)
     */
    @Override
    public String encrypt( String aPassword )
    {
        String salt = myEnv.getProperty( TargetConfiguration.KEY_PW_SALT );
        String hash = DigestUtils.sha256Hex( salt + aPassword );
        myLog.debug( "hashed password: " + hash );
        return new String( hash );
    }

}
