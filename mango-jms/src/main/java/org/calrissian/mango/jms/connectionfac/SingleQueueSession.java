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
import static org.calrissian.mango.jms.connectionfac.SingleDestinationUtils.*;

/**
 * Class SingleTopicSession
 * Date: Nov 27, 2011
 * Time: 4:44:38 PM
 */
class SingleQueueSession extends SessionDecorator {

    private final Queue queue;

    public SingleQueueSession(Session session, String baseQueue) throws JMSException {
        super(session);
        this.queue = session.createQueue(baseQueue);
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        if (destination instanceof Queue)
            return new SingleDestinationMessageProducer(super.createProducer(queue), queue, decorateDestination(destination));

        return super.createProducer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        if (destination instanceof Queue)
            return new SingleDestinationMessageConsumer(super.createConsumer(queue, generateSelector(getDestination(destination), null)));

        return super.createConsumer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String selector) throws JMSException {
        if (destination instanceof Queue)
            return new SingleDestinationMessageConsumer(super.createConsumer(queue, generateSelector(getDestination(destination), selector)));

        return super.createConsumer(destination, selector);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String selector, boolean noLocal) throws JMSException {
        if (destination instanceof Queue)
            return new SingleDestinationMessageConsumer(super.createConsumer(queue, generateSelector(getDestination(destination), selector), noLocal));

        return super.createConsumer(destination, selector, noLocal);
    }

    @Override
    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        return super.createBrowser(queue, generateSelector(this.queue.getQueueName(), null));
    }

    @Override
    public QueueBrowser createBrowser(Queue queue, String selector) throws JMSException {
        return super.createBrowser(queue, generateSelector(this.queue.getQueueName(), selector));
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return new SelectorDestination(randomUUID().toString());
    }

    @Override
    public Queue createQueue(String name) throws JMSException {
        return new SelectorDestination(name);
    }
}
