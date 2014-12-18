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
 * Entity class for the oxwall DB table ow_base_avatar
 *
 * @version 1.0 (29.11.2014)
 * @author  Heri
 */
@Entity
@Table(name="ow_base_avatar")
public class OxAvatar
{
    /* Reverse engineered from a real oxwall DB (Nena1):
    *
       ow_base_avatar
           INSERT INTO `ow_base_avatar`(`id`, `userId`, `hash`)
               field 'hash'
                   e.g. "1396979170" (all have 10 digits)
               used for filename in ow_userfiles/plugins/base/avatars:
                   - avatar_<userId>_<hash>.jpg (1)
                   - avatar_big_<userId>_<hash>.jpg (2)
                   - avatar_original_<userId>_<hash>.jpg (3)
               where:
                   (1) 90x90 pixels, 96 dpi, 24 pixelBits
                   (2) 190x190 pixels, 96 dpi, 24 pixelBits
                   (3) any size (original uploaded)
               Linux-Access-Rights: -rw-r--r--
     */

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private Long hash;

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
     * @return hash
     */
    public Long getHash()
    {
        return hash;
    }

    /**
     * @param  aHash
     *         hash to set
     */
    public void setHash( Long aHash )
    {
        hash = aHash;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    id     : " ).append( id );
        sb.append( "\n    userId : " ).append( userId );
        sb.append( "\n    hash   : " ).append( hash );
        return sb.toString();
    }

}
