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
 * Entity class for the oxwall DB table ow_groups_group_user
 *
 * @version 1.0 (29.11.2014)
 * @author  Heri
 */
@Entity
@Table(name="ow_groups_group_user")
public class OxUserGroupMembership
{
    /*
     * Reverse engineered from a real oxwall DB (Nena1):
     *
      Group membership:
      ----------------
          ow_groups_group
              INSERT INTO `ow_groups_group`(`id`, `title`, `description`, `imageHash`, `timeStamp`, `userId`, `privacy`, `whoCanView`, `whoCanInvite`)
                  id: used in m:n relationship in

          ow_groups_group_user
              m:n relationship between user and group
              INSERT INTO `ow_groups_group_user`(`id`, `groupId`, `userId`, `timeStamp`, `privacy`)
                  userId: new userId
                  groupId: the group the new user belongs to
                  timeStamp: join date (unix timestamp (seconds since 1970) )
                  privacy (varchar(100)):  ??? in my installation all is 'everybody'
                          I cannot see any corresponding functionality in GUI. There you just can join or leave a group, but not configure any privacy.

     */

    @Id
    @GeneratedValue
    private Long id;

    private Long groupId;
    private Long userId;
    private Long timeStamp;
    private String privacy;

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
     * @return groupId
     */
    public Long getGroupId()
    {
        return groupId;
    }

    /**
     * @param  aGroupId
     *         groupId to set
     */
    public void setGroupId( Long aGroupId )
    {
        groupId = aGroupId;
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
     * @return timeStamp
     */
    public Long getTimeStamp()
    {
        return timeStamp;
    }

    /**
     * @param  aTimeStamp
     *         timeStamp to set
     */
    public void setTimeStamp( Long aTimeStamp )
    {
        timeStamp = aTimeStamp;
    }

    /**
     * @return privacy
     */
    public String getPrivacy()
    {
        return privacy;
    }

    /**
     * @param  aPrivacy
     *         privacy to set
     */
    public void setPrivacy( String aPrivacy )
    {
        privacy = aPrivacy;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    id        : " ).append( id );
        sb.append( "\n    groupId   : " ).append( groupId );
        sb.append( "\n    userId    : " ).append( userId );
        sb.append( "\n    timeStamp : " ).append( timeStamp );
        sb.append( "\n    privacy   : " ).append( privacy );
        return sb.toString();
    }

}
