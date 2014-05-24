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

import org.calrissian.mango.jms.connectionfac.decorator.MessageConsumerDecorator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import static org.calrissian.mango.jms.connectionfac.SingleDestinationUtils.postReceiveMessage;

/**
 * Class SingleDestinationMessageConsumer
 * Date: Nov 30, 2011
 * Time: 5:05:42 PM
 */
class SingleDestinationMessageConsumer extends MessageConsumerDecorator {

    public SingleDestinationMessageConsumer(MessageConsumer messageConsumer) {
        super(messageConsumer);
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return ((SingleDestinationMessageListener) super.getMessageListener()).getInternal();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        super.setMessageListener(new SingleDestinationMessageListener(messageListener));
    }

    @Override
    public Message receive() throws JMSException {
        Message msg = super.receive();
        if (msg != null) {
            postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public Message receive(long l) throws JMSException {
        Message msg = super.receive(l);
        if (msg != null) {
            postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public Message receiveNoWait() throws JMSException {
        Message msg = super.receiveNoWait();
        if (msg != null) {
            postReceiveMessage(msg);
        }
        return msg;
    }
}
