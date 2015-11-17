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
 * Entity class for the oxwall DB table ow_base_user_disapprove
 *
 * @version 1.0 (29.11.2014)
 * @author  Heri
 */
@Entity
@Table(name="ow_base_user_disapprove")
public class OxUserUnapproved
{
    /* Reverse engineered from a real oxwall DB (Nena1):
    *
       ow_base_user_disapprove
       -----------------------
           INSERT INTO `ow_base_user_disapprove`(`id`, `userId`)
               used for
                   - an unapproved user has an entry in this table
     */

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;

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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    id     : " ).append( id );
        sb.append( "\n    userId : " ).append( userId );
        return sb.toString();
    }

}
