/**
 * Copyright (c) 2014 Nena1.ch. All rights reserved.
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */



package ch.sdi.core.intf.mail;

import org.apache.commons.mail.EmailException;


/**
 * Generic base for the mail sender to be used.
 *
 * @version 1.0 (07.11.2014)
 * @author  Heri
 */
public interface MailSender<T>
{

    /**
     * Enriches the given mail instance with the needed parameters for delivering it to an SMTP host and
     * sends it.
     * <p>
     *
     * @param aMail
     *        the mail to be sent. All content which is not transport specific (receipient(s), subject,
     *        body, etc.) are expected to be already set correctyl.
     * @throws EmailException
     *         on any problem. The original exception is embedded.
     */
    public void sendMail( T aMail ) throws EmailException;

}
