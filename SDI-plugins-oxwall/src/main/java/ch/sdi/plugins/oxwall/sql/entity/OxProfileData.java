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
          where one of the xxxValue fields is filled, depending on the type of the value

       some question names are hardcoded:
       - sex
       - realname
       - birthdate
       the others are hashes:
        the others are hashes (!) and concern the platform instance specific questions/data
                    e.g. 0b0e44da932442a185649d851e108d71           -> phone number
                        is referenced in ow_base_question   (properties of the question itself like editable etc.)
                            INSERT INTO `ow_base_question` (`id`, `name`, `sectionName`, `accountTypeName`, `type`, `presentation`, `required`, `onJoin`, `onEdit`, `onSearch`, `onView`, `base`, `removable`, `columnCount`, `sortOrder`, `custom`, `parent
                            (128, '0b0e44da932442a185649d851e108d71', 'f90cde5913235d172603cc4e7b9726e3', NULL, 'text', 'text', 0, 1, 1, 0, 1, 0, 1, 1, 6, '[]', NULL);
                        type: enum field: (text, select, datetime, boolean, multiselect) default text
                        sectionName: the section (Tab in GUI) where the profile field appears
                        acctountTypeName: (all NULL in Nena1. TODO: Maybe if there were more than one account type there would be an entry)
                   entered values of each user is in ow_base_question_data (foreign key userId):
                           INSERT INTO `ow_base_question_data` (`id`, `questionName`, `userId`, `textValue`, `intValue`, `dateValue`) VALUES
                           (61, '0b0e44da932442a185649d851e108d71', 2, '+41 79 823 54 32', 0, NULL),

            INSERT INTO `ow_base_language_value` (`id`, `languageId`, `keyId`, `value`) VALUES
            (64715, 28, 18020, 'Natelnummer'),
                28 -> deutsch   (1 is english)
                18020 -> key (id) in ow_base_language_key:
                     INSERT INTO `ow_base_language_key` (`id`, `prefixId`, `key`) VALUES
                    (18020, 7, 'questions_question_0b0e44da932442a185649d851e108d71_label'),
                        7 -> ow_base_language_prefix.id=7, .prefix=base, .label=BASE
                        questions_question_0b0e44da932442a185649d851e108d71_label -> ???
            (63173, 28, 2010, 'Drei Gründe, warum ich bei NeNa1 mitmache:'),


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
