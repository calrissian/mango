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
class SingleTopicSession extends SessionDecorator {

    private final Topic topic;

    public SingleTopicSession(Session session, String baseTopic) throws JMSException {
        super(session);
        this.topic = session.createTopic(baseTopic);
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        if (destination instanceof Topic)
            return new SingleDestinationMessageProducer(super.createProducer(topic), topic, decorateDestination(destination));

        return super.createProducer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        if (destination instanceof Topic)
            return new SingleDestinationMessageConsumer(super.createConsumer(topic, generateSelector(getDestination(destination), null)));

        return super.createConsumer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String selector) throws JMSException {
        if (destination instanceof Topic)
            return new SingleDestinationMessageConsumer(super.createConsumer(topic, generateSelector(getDestination(destination), selector)));

        return super.createConsumer(destination, selector);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String selector, boolean noLocal) throws JMSException {
        if (destination instanceof Topic)
            return new SingleDestinationMessageConsumer(super.createConsumer(topic, generateSelector(getDestination(destination), selector), noLocal));

        return super.createConsumer(destination, selector, noLocal);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic destination, String name) throws JMSException {
        return super.createDurableSubscriber(topic, name, generateSelector(getDestination(destination), null), false);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic destination, String name, String selector, boolean noLocal) throws JMSException {
        return super.createDurableSubscriber(topic, name, generateSelector(getDestination(destination), selector), noLocal);
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return new SelectorDestination(randomUUID().toString());
    }

    @Override
    public Topic createTopic(String name) throws JMSException {
        return new SelectorDestination(name);
    }
}
