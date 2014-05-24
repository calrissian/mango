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


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public abstract class MessageConsumerDecorator implements MessageConsumer {

    private final MessageConsumer messageConsumer;

    public MessageConsumerDecorator(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public String getMessageSelector() throws JMSException {
        return messageConsumer.getMessageSelector();
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return messageConsumer.getMessageListener();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        messageConsumer.setMessageListener(messageListener);
    }

    @Override
    public Message receive() throws JMSException {
        return messageConsumer.receive();
    }

    @Override
    public Message receive(long l) throws JMSException {
        return messageConsumer.receive(l);
    }

    @Override
    public Message receiveNoWait() throws JMSException {
        return messageConsumer.receiveNoWait();
    }

    @Override
    public void close() throws JMSException {
        messageConsumer.close();
    }

    public MessageConsumer getInternal() {
        return messageConsumer;
    }
}
