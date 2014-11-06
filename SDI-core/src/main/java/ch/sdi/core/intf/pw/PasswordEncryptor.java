/**
 * Copyright (c) 2014 Nena1.ch. All rights reserved.
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package ch.sdi.core.intf.pw;

import java.security.NoSuchAlgorithmException;

/**
 * Password encryptor
 *
 * @version 1.0 (01.11.2014)
 * @author Heri
 */
public interface PasswordEncryptor
{

    /**
     * Encrypts (or hashes) the given password
     * <p>
     * 
     * @param aPassword
     * @return the encrypted password
     * @throws NoSuchAlgorithmException 
     */
    public String encrypt( String aPassword ) throws NoSuchAlgorithmException;
}
