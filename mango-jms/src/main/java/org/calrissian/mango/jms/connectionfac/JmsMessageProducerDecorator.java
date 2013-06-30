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

import org.calrissian.mango.jms.connectionfac.decorator.MessageProducerDecorator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import static org.calrissian.mango.jms.connectionfac.JmsDecoratorUtils.preSendMessage;

/**
 * Class JmsMessageProducerDecorator
 * Date: Nov 27, 2011
 * Time: 5:25:02 PM
 */
public class JmsMessageProducerDecorator extends MessageProducerDecorator {

    private final Destination destination;
    private final SelectorDestination selectorDestination;

    public JmsMessageProducerDecorator(MessageProducer messageProducer, Destination destination, SelectorDestination selectorDestination) {
        super(messageProducer);
        this.destination = destination;
        this.selectorDestination = selectorDestination;
    }

    public void send(Message message) throws JMSException {
        preSendMessage(message, destination, selectorDestination);
        super.send(message);
    }

    public void send(Message message, int i, int i1, long l) throws JMSException {
        preSendMessage(message, destination, selectorDestination);
        super.send(message, i, i1, l);
    }

    public void send(Destination destination, Message message) throws JMSException {
        if (destination == null)
            throw new IllegalArgumentException("Destination cannot be null");
        preSendMessage(message, this.destination, destination);
        super.send(message);
    }

    public void send(Destination destination, Message message, int i, int i1, long l) throws JMSException {
        if (destination == null)
            throw new IllegalArgumentException("Destination cannot be null");
        preSendMessage(message, this.destination, destination);
        super.send(message, i, i1, l);
    }
}

