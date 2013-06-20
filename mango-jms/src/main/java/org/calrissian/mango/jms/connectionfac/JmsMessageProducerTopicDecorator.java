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
package org.calrissian.mango.jms.connectionfac;

import javax.jms.*;

/**
 * Class JmsMessageProducerTopicDecorator
 * Date: Nov 27, 2011
 * Time: 5:25:02 PM
 */
public class JmsMessageProducerTopicDecorator implements MessageProducer {
    private MessageProducer producer;
    private SelectorDestination selectorDestination;
    private Topic topic;

    public JmsMessageProducerTopicDecorator(MessageProducer producer, Topic topic, SelectorDestination selectorDestination) {
        this.producer = producer;
        this.topic = topic;
        this.selectorDestination = selectorDestination;
    }

    public void setDisableMessageID(boolean b) throws JMSException {
        producer.setDisableMessageID(b);
    }

    public boolean getDisableMessageID() throws JMSException {
        return producer.getDisableMessageID();
    }

    public void setDisableMessageTimestamp(boolean b) throws JMSException {
        producer.setDisableMessageTimestamp(b);
    }

    public boolean getDisableMessageTimestamp() throws JMSException {
        return producer.getDisableMessageTimestamp();
    }

    public void setDeliveryMode(int i) throws JMSException {
        producer.setDeliveryMode(i);
    }

    public int getDeliveryMode() throws JMSException {
        return producer.getDeliveryMode();
    }

    public void setPriority(int i) throws JMSException {
        producer.setPriority(i);
    }

    public int getPriority() throws JMSException {
        return producer.getPriority();
    }

    public void setTimeToLive(long l) throws JMSException {
        producer.setTimeToLive(l);
    }

    public long getTimeToLive() throws JMSException {
        return producer.getTimeToLive();
    }

    public Destination getDestination() throws JMSException {
        return producer.getDestination();
    }

    public void close() throws JMSException {
        producer.close();
    }

    public void send(Message message) throws JMSException {
        JmsTopicDecoratorConstants.preSendMessage(message, topic, selectorDestination);
        producer.send(message);
    }

    public void send(Message message, int i, int i1, long l) throws JMSException {
        JmsTopicDecoratorConstants.preSendMessage(message, topic, selectorDestination);
        producer.send(message, i, i1, l);
    }

    public void send(Destination destination, Message message) throws JMSException {
        if (destination == null)
            throw new IllegalArgumentException("Destination cannot be null");
        JmsTopicDecoratorConstants.preSendMessage(message, topic, destination);
        producer.send(message);
    }

    public void send(Destination destination, Message message, int i, int i1, long l) throws JMSException {
        if (destination == null)
            throw new IllegalArgumentException("Destination cannot be null");
        JmsTopicDecoratorConstants.preSendMessage(message, topic, destination);
        producer.send(message, i, i1, l);
    }

    public MessageProducer getProducer() {
        return producer;
    }

    public SelectorDestination getSelectorDestination() {
        return selectorDestination;
    }
}

