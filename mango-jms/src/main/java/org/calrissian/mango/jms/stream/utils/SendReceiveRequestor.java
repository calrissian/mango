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
package org.calrissian.mango.jms.stream.utils;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Deprecated
public class SendReceiveRequestor implements MessagePostProcessor {

    public static final String RECV_ID = "JMS_SYNC_RECVID";
    public static final String REPLYTO_ID = "JMS_SYNC_REPLYTOID";

    protected Destination topic; // The topic to perform the request/reply on.
    protected JmsTemplate jmsTemplate;

    protected String recvId;
    protected String replyToId;
    /**
     * If true, then replyTo is the recvId
     */
    protected boolean send = false;

    public SendReceiveRequestor(JmsTemplate jmsTemplate, Destination destination,
                                String recvId, String replyToId, boolean send) throws JMSException {
        this.jmsTemplate = jmsTemplate;
        this.topic = destination;

        this.replyToId = replyToId;
        this.recvId = recvId;
        this.send = send;
    }

    public static String getReplyToId(Message msg) throws JMSException {
        return msg.getStringProperty(REPLYTO_ID);
    }

    protected String createReceiveSelector() {
        String s = (send) ? replyToId : recvId;
        return new StringBuilder().append(RECV_ID).append(" = '").append(s).append("'").toString();
    }

    protected Message populateProperties(Message msg) throws JMSException {
        String recvS = (send) ? recvId : replyToId;
        String replyToS = (send) ? replyToId : recvId;
        msg.setStringProperty(RECV_ID, recvS);
        msg.setStringProperty(REPLYTO_ID, replyToS);
        return msg;
    }

    public Message receive() throws JMSException {
        return jmsTemplate.receiveSelected(topic, createReceiveSelector());
    }

    public void send(Message message) throws JMSException {
        message.setJMSReplyTo(topic);
        final Message msg = populateProperties(message);
        jmsTemplate.send(topic, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                return msg;
            }
        });
    }

    public void convertAndSend(Object obj) throws JMSException {
        jmsTemplate.convertAndSend(topic, obj, this);
    }

    public void close() throws JMSException {
    }

    public String getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(String replyToId) {
        this.replyToId = replyToId;
    }

    @Override
    public Message postProcessMessage(Message message) throws JMSException {
        Message msg = populateProperties(message);
        msg.setJMSReplyTo(topic);
        return msg;
    }
}
