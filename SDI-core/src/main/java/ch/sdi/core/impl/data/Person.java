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



package ch.sdi.core.impl.data;


/**
 * TODO
 *
 * @version 1.0 (01.11.2014)
 * @author  Heri
 */
public class Person
{
    private String myUsername;
    private String myPrename;
    private String myMiddlename;
    private String myName;
    private String myEMail;

    /**
     * @return username
     */
    public String getUsername()
    {
        return myUsername;
    }

    /**
     * @param  aUsername
     *         username to set
     */
    public void setUsername( String aUsername )
    {
        myUsername = aUsername;
    }

    /**
     * @return prename
     */
    public String getPrename()
    {
        return myPrename;
    }

    /**
     * @param  aPrename
     *         prename to set
     */
    public void setPrename( String aPrename )
    {
        myPrename = aPrename;
    }

    /**
     * @return middlename
     */
    public String getMiddlename()
    {
        return myMiddlename;
    }

    /**
     * @param  aMiddlename
     *         middlename to set
     */
    public void setMiddlename( String aMiddlename )
    {
        myMiddlename = aMiddlename;
    }

    /**
     * @return name
     */
    public String getName()
    {
        return myName;
    }

    /**
     * @param  aName
     *         name to set
     */
    public void setName( String aName )
    {
        myName = aName;
    }

    /**
     * @return eMail
     */
    public String getEMail()
    {
        return myEMail;
    }

    /**
     * @param  aEMail
     *         eMail to set
     */
    public void setEMail( String aEMail )
    {
        myEMail = aEMail;
    }


}
