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
public class PasswordEncryptor implements ch.sdi.core.intf.pw.PasswordEncryptor
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
     * @see ch.sdi.core.intf.pw.PasswordEncryptor#encrypt(java.lang.String)
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
