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

package ch.sdi.core.intf;

import ch.sdi.core.annotations.SdiProps;
import ch.sdi.core.impl.cfg.ConfigUtils;


/**
 * Configuration interface for the mail.properties.<p>
 *
 * The expected property names are defined her.<p>
 *
 * Because this interface is annotated with @SdiProps it is subject to the automatic mechanisme for
 * overloading default properties with user defined properties (see {@link ch.sdi.core.annotations.SdiProps}).
 *
 * @version 1.0 (02.11.2014)
 * @author  Heri
 */
@SdiProps()
public interface MailProperties extends SdiProperties
{

    public static final String RESOURCE_NAME =
            ConfigUtils.makePropertyResourceName( MailProperties.class );


    public static final String KEY_SENDER_NAME = "mail.sender.name";
    public static final String KEY_SENDER_ADDRESS = "mail.sender.address";
    public static final String KEY_SUBJECT = "mail.subject";
    public static final String KEY_BODY = "mail.body";
    public static final String KEY_BODY_TEMPLATE = "mail.body.template";
    public static final String KEY_BODY_TEMPLATE_CHARSET = "mail.body.template.charset";
    public static final String KEY_CONTENT_TYPE = "mail.contenttype";
    public static final String KEY_HOST = "mail.smtp.host";
    public static final String KEY_PORT = "mail.smtp.port";
    public static final String KEY_PORT_SSL = "mail.smtp.port.ssl";
    public static final String KEY_SMTP_USER = "mail.smtp.user";
    public static final String KEY_SMTP_PASSWORD = "mail.smtp.password";
    public static final String KEY_SSL = "mail.ssl";
    public static final String KEY_SSL_ON_CONNECT = "mail.ssl.onconnect";
    public static final String KEY_START_TLS_REQUIRED = "mail.start.tls.required";
    public static final String KEY_START_TLS_ENABLED = "mail.start.tls.enabled";

}
