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


package ch.sdi.plugins.oxwall.sql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * TODO
 *
 * @version 1.0 (16.11.2014)
 * @author  Heri
 */
@Entity
@Table(name="ow_base_user")
public class User
{
    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String username;
    private String password;
    private Long joinStamp;
    private Long activityStamp;
    private String accountType;
    private boolean emailVerify;
    private Long joinIp;

    /**
     * @return email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param  aEmail
     *         email to set
     */
    public void setEmail( String aEmail )
    {
        email = aEmail;
    }

    /**
     * @return username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param  aUsername
     *         username to set
     */
    public void setUsername( String aUsername )
    {
        username = aUsername;
    }

    /**
     * @return password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param  aPassword
     *         password to set
     */
    public void setPassword( String aPassword )
    {
        password = aPassword;
    }

    /**
     * @return joinStamp
     */
    public Long getJoinStamp()
    {
        return joinStamp;
    }

    /**
     * @param  aJoinStamp
     *         joinStamp to set
     */
    public void setJoinStamp( Long aJoinStamp )
    {
        joinStamp = aJoinStamp;
    }

    /**
     * @return activityStamp
     */
    public Long getActivityStamp()
    {
        return activityStamp;
    }

    /**
     * @param  aActivityStamp
     *         activityStamp to set
     */
    public void setActivityStamp( Long aActivityStamp )
    {
        activityStamp = aActivityStamp;
    }

    /**
     * @return emailVerify
     */
    public boolean isEmailVerify()
    {
        return emailVerify;
    }

    /**
     * @param  aEmailVerify
     *         emailVerify to set
     */
    public void setEmailVerify( boolean aEmailVerify )
    {
        emailVerify = aEmailVerify;
    }

    /**
     * @return joinIp
     */
    public Long getJoinIp()
    {
        return joinIp;
    }

    /**
     * @param  aJoinIp
     *         joinIp to set
     */
    public void setJoinIp( Long aJoinIp )
    {
        joinIp = aJoinIp;
    }

    /**
     * @return id
     */
    public Long getId()
    {
        return id;
    }


    /**
     * @return accountType
     */
    public String getAccountType()
    {
        return accountType;
    }


    /**
     * @param  aAccountType
     *         accountType to set
     */
    public void setAccountType( String aAccountType )
    {
        accountType = aAccountType;
    }




}
