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
public class OxUser
{

    /*
     * Reverse engineered from a real oxwall DB (Nena1):
     *
        ow_base_user
        ------------
            INSERT INTO `ow_base_user`(`id`, `email`, `username`, `password`, `joinStamp`, `activityStamp`, `accountType`, `emailVerify`, `joinIp`)
            username: SDI-name: thing.alternateName
            password: SHA-256(salt+password)
            joinStamp: join date (unix timestamp (seconds since 1970) ) default '0'
            activityStamp: (unix timestamp (seconds since 1970) ) default '0'
            accountType: the desired account
                         Nena1: all have 290365aadde35a97f11207ca7e4279cc
                         see below ow_base_question_account_type
            emailVerify: 0 if user has not yet verified email, 1 if it is verified ( TODO: (if 0) additional steps have to be done like providing a dataset in ow_base_email_verify and send the hash to the new user)
            joinIp: int(11) Nena1 example: 1535710340 (all positive)
                    PHP: ip2long(OW::getRequest()->getRemoteAddress())
                        example: (see http://php.net/manual/en/function.ip2long.php)
                            $ip   = gethostbyname('www.example.com');
                            $long = ip2long($ip);
                            if ($long == -1 || $long === FALSE) {
                                echo 'Invalid IP, please try again';
                            } else {
                                echo $ip   . "\n";           // 192.0.34.166
                                echo $long . "\n";           // -1073732954
                                printf("%u\n", ip2long($ip)); // 3221234342
                            }

                            example above reverse-engineered:
                            192.0.34.166 -> 0xC0 0x00 0x22 0xA6 -> 0xC00022A6 -> 3221234342 (unsinged) -> -1073732954 (signed)

                            real example 1535710340 (Nena1 and Pi):
                            0x5B891484 -> 0x5B 0x89 0x14 0x84 -> 91.137.20.132 (whois resolves to "Thueringer Netkom GmbH")
                            real example 3559071562 (heri):
                            0x1536B654 -> 0x15 0x36 0xB6 0x54 -> 21.54.182.84 (whois resolves to DNIC, Columbus, OH)
                                -> both resolutions do not make any sense to me!
                            real example from local network (192.168.99.110): 1234
                            0x4D2 -> 0x00 0x00 0x04 0xD2 -> 0.0.4.210
                                -> neither this makes any sense!
                -> Source V1.7.1: the joinIp is only inserted but never read. So I think it does not really matter what to insert here.

     */

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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    id           : " ).append( id );
        sb.append( "\n    email        : " ).append( email );
        sb.append( "\n    username     : " ).append( username );
        sb.append( "\n    password     : " ).append( password );
        sb.append( "\n    joinStamp    : " ).append( joinStamp );
        sb.append( "\n    activityStamp: " ).append( activityStamp );
        sb.append( "\n    accountType  : " ).append( accountType );
        sb.append( "\n    emailVerify  : " ).append( emailVerify );
        sb.append( "\n    joinIp       : " ).append( joinIp );
        return sb.toString();
    }


    /**
     * @param  aId
     *         id to set
     */
    public void setId( Long aId )
    {
        id = aId;
    }



}
