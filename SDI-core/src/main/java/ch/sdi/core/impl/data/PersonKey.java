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


package ch.sdi.core.impl.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;


/**
 * Enumeration of the internally used normalized field names of a person associated with the string
 * used in key/value pairs.
 * <p>
 * The property names are according to http://schema.org/Person. See also comments in
 * template person.properties
 *
 *
 * @version 1.0 (13.11.2014)
 * @author  Heri
 */
public enum PersonKey
{
    /**
     * Description: An additional name for a Person, can be used for a middle name.<p>
     * Type       : Text<p>
     */
    PERSON_ADDITIONALNAME ( "person.additionalName" ),

    /**
     * Description: Physical address of the item.<p>
     * Type       : PostalAddress<p>
     */
    PERSON_ADDRESS ( "person.address" ),

    /**
     * Description: An organization that this person is affiliated with. For example, a school/university,
     *              a club, or a team.<p>
     * Type       : Organization<p>
     */
    PERSON_AFFILIATION ( "person.affiliation" ),

    /**
     * Description: An educational organizations that the person is an alumni of.
     *              Inverse property: alumni.<p>
     * Type       : EducationalOrganization<p>
     */
    PERSON_ALUMNIOF ( "person.alumniOf" ),

    /**
     * Description: An award won by this person or for this creative work. Supersedes awards.<p>
     * Type       : Text<p>
     */
    PERSON_AWARD ( "person.award" ),

    /**
     * Description: Date of birth.<p>
     * Type       : Date<p>
     */
    PERSON_BIRTHDATE ( "person.birthDate" ),

    /**
     * Description: The brand(s) associated with a product or service, or the brand(s) maintained by an
     *              organization or business person.<p>
     * Type       : Organization,Brand<p>
     */
    PERSON_BRAND ( "person.brand" ),

    /**
     * Description: A child of the person.<p>
     * Type       : Person<p>
     */
    PERSON_CHILDREN ( "person.children" ),

    /**
     * Description: A colleague of the person. Supersedes colleagues.<p>
     * Type       : Person<p>
     */
    PERSON_COLLEAGUE ( "person.colleague" ),

    /**
     * Description: A contact point for a person or organization. Supersedes contactPoints.<p>
     * Type       : ContactPoint<p>
     */
    PERSON_CONTACTPOINT ( "person.contactPoint" ),

    /**
     * Description: Date of death.<p>
     * Type       : Date<p>
     */
    PERSON_DEATHDATE ( "person.deathDate" ),

    /**
     * Description: The Dun & Bradstreet DUNS number for identifying an organization or business person.<p>
     * Type       : Text<p>
     */
    PERSON_DUNS ( "person.duns" ),

    /**
     * Description: Email address.<p>
     * Type       : Text<p>
     */
    PERSON_EMAIL ( "person.email" ),

    /**
     * Description: Family name. In the U.S., the last name of an Person. This can be used along with
     *              givenName instead of the Name property.<p>
     * Type       : Text<p>
     */
    PERSON_FAMILYNAME ( "person.familyName" ),

    /**
     * Description: The fax number.<p>
     * Type       : Text<p>
     */
    PERSON_FAXNUMBER ( "person.faxNumber" ),

    /**
     * Description: The most generic uni-directional social relation.<p>
     * Type       : Person<p>
     */
    PERSON_FOLLOWS ( "person.follows" ),

    /**
     * Description: Gender of the person.<p>
     * Type       : Text<p>
     */
    PERSON_GENDER ( "person.gender" ),

    /**
     * Description: Given name. In the U.S., the first name of a Person. This can be used along with
     *              familyName instead of the Name property.<p>
     * Type       : Text<p>
     */
    PERSON_GIVENNAME ( "person.givenName" ),

    /**
     * Description: The Global Location Number (GLN, sometimes also referred to as International Location
     *              Number or ILN) of the respective organization, person, or place. The GLN is a 13-digit
     *              number used to identify parties and physical locations.<p>
     * Type       : Text<p>
     */
    PERSON_GLOBALLOCATIONNUMBER ( "person.globalLocationNumber" ),

    /**
     * Description: Points-of-Sales operated by the organization or person.<p>
     * Type       : Place<p>
     */
    PERSON_HASPOS ( "person.hasPOS" ),

    /**
     * Description: A contact location for a person's residence.<p>
     * Type       : ContactPoint,Place<p>
     */
    PERSON_HOMELOCATION ( "person.homeLocation" ),

    /**
     * Description: An honorific prefix preceding a Person's name such as Dr/Mrs/Mr.<p>
     * Type       : Text<p>
     */
    PERSON_HONORIFICPREFIX ( "person.honorificPrefix" ),

    /**
     * Description: An honorific suffix preceding a Person's name such as M.D. /PhD/MSCSW.<p>
     * Type       : Text<p>
     */
    PERSON_HONORIFICSUFFIX ( "person.honorificSuffix" ),

    /**
     * Description: A count of a specific user interactions with this item, for example, 20 UserLikes,
     *              5 UserComments, or 300 UserDownloads. The user interaction type should be one of the
     *              sub types of UserInteraction.<p>
     * Type       : Text<p>
     */
    PERSON_INTERACTIONCOUNT ( "person.interactionCount" ),

    /**
     * Description: The International Standard of Industrial Classification of All Economic Activities
     *              (ISIC), Revision 4 code for a particular organization, business person, or place.<p>
     * Type       : Text<p>
     */
    PERSON_ISICV4 ( "person.isicV4" ),

    /**
     * Description: The job title of the person (for example, Financial Manager).<p>
     * Type       : Text<p>
     */
    PERSON_JOBTITLE ( "person.jobTitle" ),

    /**
     * Description: The most generic bi-directional social/work relation.<p>
     * Type       : Person<p>
     */
    PERSON_KNOWS ( "person.knows" ),

    /**
     * Description: A pointer to products or services offered by the organization or person.<p>
     * Type       : Offer<p>
     */
    PERSON_MAKESOFFER ( "person.makesOffer" ),

    /**
     * Description: An Organization (or ProgramMembership) to which this Person or Organization belongs.
     *              Inverse property: member.<p>
     * Type       : ProgramMembership,Organization<p>
     */
    PERSON_MEMBEROF ( "person.memberOf" ),

    /**
     * Description: The North American Industry Classification System (NAICS) code for a particular
     *              organization or business person.<p>
     * Type       : Text<p>
     */
    PERSON_NAICS ( "person.naics" ),

    /**
     * Description: Nationality of the person.<p>
     * Type       : Country<p>
     */
    PERSON_NATIONALITY ( "person.nationality" ),

    /**
     * Description: Products owned by the organization or person.<p>
     * Type       : OwnershipInfo,Product<p>
     */
    PERSON_OWNS ( "person.owns" ),

    /**
     * Description: A parent of this person. Supersedes parents.<p>
     * Type       : Person<p>
     */
    PERSON_PARENT ( "person.parent" ),

    /**
     * Description: Event that this person is a performer or participant in.<p>
     * Type       : Event<p>
     */
    PERSON_PERFORMERIN ( "person.performerIn" ),

    /**
     * Description: The most generic familial relation.<p>
     * Type       : Person<p>
     */
    PERSON_RELATEDTO ( "person.relatedTo" ),

    /**
     * Description: A pointer to products or services sought by the organization or person (demand).<p>
     * Type       : Demand<p>
     */
    PERSON_SEEKS ( "person.seeks" ),

    /**
     * Description: A sibling of the person. Supersedes siblings.<p>
     * Type       : Person<p>
     */
    PERSON_SIBLING ( "person.sibling" ),

    /**
     * Description: The person's spouse.<p>
     * Type       : Person<p>
     */
    PERSON_SPOUSE ( "person.spouse" ),

    /**
     * Description: The Tax / Fiscal ID of the organization or person, e.g. the TIN in the US or the
     *              CIF/NIF in Spain.<p>
     * Type       : Text<p>
     */
    PERSON_TAXID ( "person.taxID" ),

    /**
     * Description: The telephone number.<p>
     * Type       : Text<p>
     */
    PERSON_TELEPHONE ( "person.telephone" ),

    /**
     * Description: The Value-added Tax ID of the organization or person.<p>
     * Type       : Text<p>
     */
    PERSON_VATID ( "person.vatID" ),

    /**
     * Description: A contact location for a person's place of work.<p>
     * Type       : ContactPoint,Place<p>
     */
    PERSON_WORKLOCATION ( "person.workLocation" ),

    /**
     * Description: Organizations that the person works for.<p>
     * Type       : Organization<p>
     */
    PERSON_WORKSFOR ( "person.worksFor" ),

    //////////////////////////////////////////////////////////////////
    // inherited Properties from Thing
    //////////////////////////////////////////////////////////////////

    /**
     * Description: An additional type for the item, typically used for adding more specific types from
     *              external vocabularies in microdata syntax. This is a relationship between something
     *              and a class that the thing is in. In RDFa syntax, it is better to use the native RDFa
     *              syntax - the 'typeof' attribute - for multiple types. Schema.org tools may have only
     *              weaker understanding of extra types, in particular those defined externally.<p>
     * Type       : URL<p>
     */
    THING_ADDITIONALTYPE ( "thing.additionalType" ),

    /**
     * Description: An alias for the item. Here used for username or loginname<p>
     * Type       : Text<p>
     */
    THING_ALTERNATENAME ( "thing.alternateName" ),

    /**
     * Description: A short description of the item.<p>
     * Type       : Text<p>
     */
    THING_DESCRIPTION ( "thing.description" ),

    /**
     * Description: An image of the item. This can be a URL or a fully described ImageObject.<p>
     * Type       : URL,ImageObject<p>
     */
    THING_IMAGE ( "thing.image" ),

    /**
     * Description: The name of the item.<p>
     * Type       : Text<p>
     */
    THING_NAME ( "thing.name" ),

    /**
     * Description: Indicates a potential Action, which describes an idealized action in which this thing
     *              would play an 'object' role.<p>
     * Type       : Action<p>
     */
    THING_POTENTIALACTION ( "thing.potentialAction" ),

    /**
     * Description: URL of a reference Web page that unambiguously indicates the item's identity. E.g.
     *              the URL of the item's Wikipedia page, Freebase page, or official website.<p>
     * Type       : URL<p>
     */
    THING_SAMEAS ( "thing.sameAs" ),

    /**
     * Description: URL of the item.<p>
     * Type       : URL<p>
     */
    THING_URL ( "thing.url" )
    ;

    private String myKeyName;

    /**
     * Constructor
     *
     * @param aKeyName
     */
    private PersonKey( String aKeyName )
    {
        myKeyName = aKeyName;
    }

    /**
     * @return keyName
     */
    public String getKeyName()
    {
        return myKeyName;
    }

    public static final String KEY_CUSTOM_KEYS = "sdi.person.customkeys";
    private static List<String> myCustomKeys = new ArrayList<String>();

    /**
     * @param aEnv
     */
    public static void initCustomKeys( ConfigurableEnvironment aEnv )
    {
        myCustomKeys = new ArrayList<String>();

        String configured = aEnv.getProperty( KEY_CUSTOM_KEYS );
        if ( !StringUtils.hasText( configured ) )
        {
            return;
        } // if !StringUtils.hasText( configured )

        String[] keys = configured.trim().split( "," );

        for ( String key : keys )
        {
            myCustomKeys.add( key );
        }
    }

    public static List<String> getKeyNames()
    {
        List<String> result = new ArrayList<String>();

        for ( PersonKey key : PersonKey.values() )
        {
            result.add( key.getKeyName() );
        }

        result.addAll( myCustomKeys );

        return result;
    }




}
