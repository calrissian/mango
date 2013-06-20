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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * Class JmsMessageConsumerTopicDecorator
 * Date: Nov 30, 2011
 * Time: 5:05:42 PM
 */
public class JmsMessageConsumerQueueDecorator implements MessageConsumer {

    private MessageConsumer consumer;

    public JmsMessageConsumerQueueDecorator(MessageConsumer consumer) {
        this.consumer = consumer;
    }

    public MessageConsumer getConsumer() {
        return consumer;
    }

    @Override
    public String getMessageSelector() throws JMSException {
        return consumer.getMessageSelector();
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return ((JmsMessageListenerQueueDecorator) consumer.getMessageListener()).getMessageListener();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        consumer.setMessageListener(new JmsMessageListenerQueueDecorator(messageListener));
    }

    @Override
    public Message receive() throws JMSException {
        Message msg = consumer.receive();
        if (msg != null) {
            JmsQueueDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public Message receive(long l) throws JMSException {
        Message msg = consumer.receive(l);
        if (msg != null) {
            JmsQueueDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public Message receiveNoWait() throws JMSException {
        Message msg = consumer.receiveNoWait();
        if(msg != null) {
            JmsQueueDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public void close() throws JMSException {
        consumer.close();
    }
}
