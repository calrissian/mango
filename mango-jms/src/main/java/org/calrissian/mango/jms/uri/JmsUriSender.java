/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.jms.uri;

import org.calrissian.mango.jms.stream.JmsFileSenderListener;
import org.calrissian.mango.jms.stream.domain.Request;
import org.calrissian.mango.jms.stream.utils.DomainMessageUtils;
import org.calrissian.mango.uri.support.DataResolverConstants;
import org.calrissian.mango.uri.support.DataResolverFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.net.URI;


public class JmsUriSender extends JmsFileSenderListener
        implements MessageListener {

    public static Logger logger = LoggerFactory.getLogger(JmsUriSender.class);

    protected String systemName;

    public JmsUriSender(String systemName) {

        this.systemName = systemName;

    }

    @Override
    public void onMessage(Message request) {

        Request req = null;
        try {
            req = DomainMessageUtils.fromRequestMessage(request);
            URI uri = new URI(req.getDownloadUri());

            // filter based on system
            String systemNamePart = DataResolverFormatUtils.extractTargetSystemFromUri(uri);

            if(systemNamePart.equals(systemName) || systemNamePart.equals(DataResolverConstants.SYSTEM_ALL)) {

                String requestUriPart = uri.toString().replaceFirst(uri.getScheme() + ":", "");

                logger.info("Honoring request with uri: " + requestUriPart);

                req = new Request(requestUriPart, req.getRequestId());

                getTaskExecutor().execute(new JmsFileSenderRunnable(req, request
                        .getJMSReplyTo()));
            }

            else {

                logger.info("Ignoring uri download request: " + uri);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private class JmsFileSenderRunnable implements Runnable {

        private Request req;
        private Destination jmsReplyTo;

        JmsFileSenderRunnable(Request req, Destination jmsReplyTo) {
            this.req = req;
            this.jmsReplyTo = jmsReplyTo;
        }

        @Override
        public void run() {
            try {
                sendStream(req, jmsReplyTo);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

    }
}
