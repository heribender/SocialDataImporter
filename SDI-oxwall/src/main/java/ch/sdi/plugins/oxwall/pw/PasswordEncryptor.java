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


/**
 * TODO
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
public class PasswordEncryptor implements ch.sdi.core.intf.PasswordEncryptor
{

    private Logger myLog = LogManager.getLogger( PasswordEncryptor.class );

    private String mySalt;


    /**
     * Constructor
     *
     * @param aSalt
     */
    public PasswordEncryptor( String aSalt )
    {
        super();
        mySalt = aSalt;
    }

    /**
     * @throws NoSuchAlgorithmException
     * @see ch.sdi.core.intf.PasswordEncryptor#encrypt(java.lang.String)
     */
    @Override
    public String encrypt( String aPassword ) throws NoSuchAlgorithmException
    {
        String hash = DigestUtils.sha256Hex( mySalt + aPassword );
        myLog.debug( "hashed password: " + hash );
        return new String( hash );
    }

    /**
     * @return salt
     */
    public String getSalt()
    {
        return mySalt;
    }

    /**
     * @param  aSalt
     *         salt to set
     */
    public void setSalt( String aSalt )
    {
        mySalt = aSalt;
    }
}
