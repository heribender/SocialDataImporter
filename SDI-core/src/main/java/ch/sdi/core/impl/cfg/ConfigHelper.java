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

package ch.sdi.core.impl.cfg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.sdi.core.exc.SdiException;


/**
 * TODO
 *
 * @version 1.0 (04.11.2014)
 * @author Heri
 */
@Component
public class ConfigHelper
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConfigHelper.class );
    public static final String PROP_SOURCE_NAME_CMD_LINE = "CommandLineArguments";

    /**
     * Converts the given classname into a properties file name according following rules:
     *
     * <ul>
     *     <li> if class name ends with "Properties" this suffix will be truncated and replaced by
     *     ".properties"</li>
     *     <li> any other class name is used as is and suffixed with ".properties"</li>
     * </ul>
     *
     */
    public static String makePropertyResourceName( Class<?> aClass )
    {
        String classname = aClass.getSimpleName();

        if ( classname.endsWith( "Properties" ) || classname.endsWith( "properties" ) )
        {
            return classname.substring( 0, classname.length() - "Properties".length() ) + ".properties";
        } // if condition

        return classname + ".properties";

    }

    /**
     * Tries to read the value from the given environment and to convert it to an int.
     * <p>
     * If the converstion fails, an excption is thrown.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @param aDefault
     * @return
     * @throws SdiException
     */
    public static int getIntProperty( Environment aEnv, String aKey ) throws SdiException
    {
        try
        {
            //TODO: use Springs StringToNumberConverterFactory
            return Integer.valueOf( aEnv.getProperty( aKey ) ).intValue();
        }
        catch ( Exception t )
        {
            throw new SdiException( "No integer value found for property "+ aKey,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }
    }

    /**
     * Tries to read the value from the given environment and to convert it to an int.
     * <p>
     * If the converstion fails, the default value is returned.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @param aDefault
     * @return
     */
    public static int getIntProperty( Environment aEnv, String aKey, int aDefault )
    {
        try
        {
            return Integer.valueOf( aEnv.getProperty( aKey ) ).intValue();
        }
        catch ( Exception t )
        {
            return aDefault;
        }
    }

    /**
     * Tries to read the value from the given environment.
     * <p>
     * If the converstion fails, an excption is thrown.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @return
     * @throws SdiException
     */
    public static String getStringProperty( Environment aEnv,
                                            String aKey ) throws SdiException
    {
        try
        {
            return aEnv.getRequiredProperty( aKey );
        }
        catch ( Exception t )
        {
            throw new SdiException( "No value found for property "+ aKey,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }
    }

    /**
     * Tries to read the value from the given environment and to convert it to a boolean.
     * <p>
     * If the converstion fails, an excption is thrown.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @return
     * @throws SdiException
     */
    public static boolean getBooleanProperty( Environment aEnv,
                                              String aKey ) throws SdiException
    {
        try
        {
            //TODO: use Springs StringToBooleanConverter
            return Boolean.valueOf( aEnv.getProperty( aKey ) ).booleanValue();
        }
        catch ( Exception t )
        {
            throw new SdiException( "No boolean value found for property "+ aKey,
                                    SdiException.EXIT_CODE_CONFIG_ERROR );
        }
    }

    /**
     * Tries to read the value from the given environment and to convert it to a boolean.
     * <p>
     * If the converstion fails, the default value is returned.
     * <p>
     *
     * @param aEnv
     * @param aKey
     * @param aDefault
     * @return
     */
    public static boolean getBooleanProperty( Environment aEnv,
                                              String aKey,
                                              boolean aDefault )
    {
        try
        {
            return Boolean.valueOf( aEnv.getProperty( aKey ) ).booleanValue();
        }
        catch ( Exception t )
        {
            return aDefault;
        }
    }


}
