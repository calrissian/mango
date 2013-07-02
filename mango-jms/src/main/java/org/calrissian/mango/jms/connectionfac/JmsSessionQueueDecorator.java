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


import org.calrissian.mango.jms.connectionfac.decorator.SessionDecorator;

import javax.jms.*;

import static java.util.UUID.randomUUID;
import static org.calrissian.mango.jms.connectionfac.JmsDecoratorUtils.*;

/**
 * Class JmsSessionTopicDecorator
 * Date: Nov 27, 2011
 * Time: 4:44:38 PM
 */
public class JmsSessionQueueDecorator extends SessionDecorator {

    private final Queue queue;

    public JmsSessionQueueDecorator(QueueSession session, String baseQueue) throws JMSException {
        super(session);
        this.queue = session.createQueue(baseQueue);
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        return new JmsMessageProducerDecorator(super.createProducer(queue), queue, decorateDestination(destination));
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return new JmsMessageConsumerDecorator(super.createConsumer(queue, generateSelector(getDestination(destination), null)));
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String selector) throws JMSException {
        return new JmsMessageConsumerDecorator(super.createConsumer(queue, generateSelector(getDestination(destination), selector)));
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String selector, boolean noLocal) throws JMSException {
        return new JmsMessageConsumerDecorator(super.createConsumer(queue, generateSelector(getDestination(destination), selector), noLocal));
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic destination, String name) throws JMSException {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic destination, String name, String selector, boolean noLocal) throws JMSException {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        throw new UnsupportedOperationException("Queue Browser not supported as of yet");
    }

    @Override
    public QueueBrowser createBrowser(Queue queue, String selector) throws JMSException {
        throw new UnsupportedOperationException("Queue Browser not supported as of yet");
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return new SelectorDestination(randomUUID().toString());
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return new SelectorDestination(randomUUID().toString());
    }

    @Override
    public Queue createQueue(String name) throws JMSException {
        return new SelectorDestination(name);
    }

    @Override
    public Topic createTopic(String name) throws JMSException {
        return new SelectorDestination(name);
    }

}
