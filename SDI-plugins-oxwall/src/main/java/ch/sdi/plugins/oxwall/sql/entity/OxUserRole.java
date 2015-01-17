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


package ch.sdi.plugins.oxwall.sql.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Entity class for the oxwall DB table ow_base_authorization_user_role
 *
 * @version 1.0 (08.12.2014)
 * @author  Heri
 */
@Entity
@Table(name="ow_base_authorization_user_role")
public class OxUserRole
{

    /*
     * Reverse engineered from a real oxwall DB (Nena1):
     *
        ow_base_authorization_role
        ----------------------------
            INSERT INTO `ow_base_authorization_role`(`id`, `name`, `sortOrder`, `displayLabel`, `custom`) VALUES ([value-1],[value-2],[value-3],[value-4],[value-5])
                12   wqewq
                31   nena1_mitglieder

        ow_base_authorization_user_role
        --------------------------------
            INSERT INTO `ow_base_authorization_user_role`(`id`, `userId`, `roleId`) VALUES ([value-1],[value-2],[value-3])
                default role: 12
                special role: 31
            -> all existing members have default role

     */

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private Long roleId;

    /**
     * Constructor
     *
     */
    public OxUserRole()
    {
        super();
    }


    /**
     * @return id
     */
    public Long getId()
    {
        return id;
    }


    /**
     * @param  aId
     *         id to set
     */
    public void setId( Long aId )
    {
        id = aId;
    }


    /**
     * @return userId
     */
    public Long getUserId()
    {
        return userId;
    }


    /**
     * @param  aUserId
     *         userId to set
     */
    public void setUserId( Long aUserId )
    {
        userId = aUserId;
    }


    /**
     * @return roleId
     */
    public Long getRoleId()
    {
        return roleId;
    }


    /**
     * @param  aRoleId
     *         roleId to set
     */
    public void setRoleId( Long aRoleId )
    {
        roleId = aRoleId;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    id         : " ).append( id );
        sb.append( "\n    userId     : " ).append( userId );
        sb.append( "\n    roleId     : " ).append( roleId );
        return sb.toString();
    }


}
