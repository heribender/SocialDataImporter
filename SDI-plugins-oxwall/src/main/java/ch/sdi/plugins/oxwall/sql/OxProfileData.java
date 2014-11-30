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



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * TODO
 *
 * @version 1.0 (29.11.2014)
 * @author  Heri
 */
@Entity
@Table(name="ow_base_question_data")
public class OxProfileData
{
    /*
     * Reverse engineered from a real oxwall DB (Nena1):
     *
     * Profile data:
     * ------------
     * Values are inserted into ow_base_question_data:
          INSERT INTO `ow_base_question_data`(`id`, `questionName`, `userId`, `textValue`, `intValue`, `dateValue`)
              questionName: one of the field ow_base_question_data.questionName
              userId:       the new userId
              value:        depends on the type of the field (text, int, date)
     */

    @Id
    @GeneratedValue
    private Long id;
    private String questionName;
    private Long userId;
    private String textValue = "";
    private Long intValue = 0L;
    private Date dateValue;

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
     * @return questionName
     */
    public String getQuestionName()
    {
        return questionName;
    }

    /**
     * @param  aQuestionName
     *         questionName to set
     */
    public void setQuestionName( String aQuestionName )
    {
        questionName = aQuestionName;
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
     * @return textValue
     */
    public String getTextValue()
    {
        return textValue;
    }

    /**
     * @param  aTextValue
     *         textValue to set
     */
    public void setTextValue( String aTextValue )
    {
        // column is defined to be NotNull
        if ( aTextValue == null )
        {
            textValue = "";
        }
        else
        {
            textValue = aTextValue;
        } // if..else aIntValue == null
    }

    /**
     * @return intValue
     */
    public Long getIntValue()
    {
        return intValue;
    }

    /**
     * @param  aIntValue
     *         intValue to set
     */
    public void setIntValue( Long aIntValue )
    {
        // column is defined to be NotNull
        if ( aIntValue == null )
        {
            intValue = 0L;
        }
        else
        {
            intValue = aIntValue;
        } // if..else aIntValue == null
    }

    /**
     * @return dateValue
     */
    public Date getDateValue()
    {
        return dateValue;
    }

    /**
     * @param  aDateValue
     *         dateValue to set
     */
    public void setDateValue( Date aDateValue )
    {
        dateValue = aDateValue;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( super.toString() );

        sb.append( "\n    id           : " ).append( id );
        sb.append( "\n    questionName : " ).append( questionName );
        sb.append( "\n    userId       : " ).append( userId );
        sb.append( "\n    textValue    : " ).append( textValue );
        sb.append( "\n    intValue     : " ).append( intValue );
        sb.append( "\n    dateValue    : " ).append( dateValue );

        return sb.toString();
    }

}
