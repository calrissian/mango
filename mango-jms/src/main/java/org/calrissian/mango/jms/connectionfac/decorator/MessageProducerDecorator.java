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
package org.calrissian.mango.jms.connectionfac.decorator;


import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

@Deprecated
public abstract class MessageProducerDecorator implements MessageProducer {

    private final MessageProducer messageProducer;

    public MessageProducerDecorator(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @Override
    public boolean getDisableMessageID() throws JMSException {
        return messageProducer.getDisableMessageID();
    }

    @Override
    public void setDisableMessageID(boolean b) throws JMSException {
        messageProducer.setDisableMessageID(b);
    }

    @Override
    public boolean getDisableMessageTimestamp() throws JMSException {
        return messageProducer.getDisableMessageTimestamp();
    }

    @Override
    public void setDisableMessageTimestamp(boolean b) throws JMSException {
        messageProducer.setDisableMessageTimestamp(b);
    }

    @Override
    public int getDeliveryMode() throws JMSException {
        return messageProducer.getDeliveryMode();
    }

    @Override
    public void setDeliveryMode(int i) throws JMSException {
        messageProducer.setDeliveryMode(i);
    }

    @Override
    public int getPriority() throws JMSException {
        return messageProducer.getPriority();
    }

    @Override
    public void setPriority(int i) throws JMSException {
        messageProducer.setPriority(i);
    }

    @Override
    public long getTimeToLive() throws JMSException {
        return messageProducer.getTimeToLive();
    }

    @Override
    public void setTimeToLive(long l) throws JMSException {
        messageProducer.setTimeToLive(l);
    }

    @Override
    public Destination getDestination() throws JMSException {
        return messageProducer.getDestination();
    }

    @Override
    public void close() throws JMSException {
        messageProducer.close();
    }

    @Override
    public void send(Message message) throws JMSException {
        messageProducer.send(message);
    }

    @Override
    public void send(Message message, int i, int i2, long l) throws JMSException {
        messageProducer.send(message, i, i2, l);
    }

    @Override
    public void send(Destination destination, Message message) throws JMSException {
        messageProducer.send(destination, message);
    }

    @Override
    public void send(Destination destination, Message message, int i, int i2, long l) throws JMSException {
        messageProducer.send(destination, message, i, i2, l);
    }

    public MessageProducer getInternal() {
        return messageProducer;
    }
}
