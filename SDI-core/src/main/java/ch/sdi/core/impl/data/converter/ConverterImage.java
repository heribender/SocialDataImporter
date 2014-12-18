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

package ch.sdi.core.impl.data.converter;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.sdi.core.exc.SdiException;
import ch.sdi.core.intf.FieldConverter;


/**
 * Abstract base class for converters which can convert to a BufferedImage
 *
 * @version 1.0 (16.11.2014)
 * @author Heri
 */
public abstract class ConverterImage implements FieldConverter<BufferedImage>
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterImage.class );

    /**
     * Converts the content of the given input stream to a BufferedImage.
     * @param aInputStream
     * @return
     * @throws SdiException
     */
    public BufferedImage convert( InputStream aInputStream ) throws SdiException
    {
        try
        {
            return ImageIO.read( aInputStream );
        }
        catch ( IOException e )
        {
            throw new SdiException( "Problems reading image input stream", e, SdiException.EXIT_CODE_PARSE_ERROR );
        }
    }

    /**
     * Resizes the given image to the given width and height.
     *
     * @param originalImage
     * @param aWidth
     * @param aHeight
     * @return
     */
    public static BufferedImage resizeImage( BufferedImage originalImage,
                                             int aWidth,
                                             int aHeight )
    {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        BufferedImage resizedImage = new BufferedImage( aWidth, aHeight, type );
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite( AlphaComposite.Src );
        g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        g.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY );
        g.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g.drawImage( originalImage, 0, 0, aWidth, aHeight, null );
        g.dispose();

        return resizedImage;
    }
}
