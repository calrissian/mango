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

import javax.jms.*;

public class DestinationRequestor {

    private long timeout = 0;

    protected Session session; // The queue session the queue belongs to.
    protected Destination topic; // The queue to perform the request/reply on.
    protected Destination tempDestination;
    protected MessageProducer publisher;
    protected MessageConsumer subscriber;

    public DestinationRequestor(Session session, Destination destination)
            throws JMSException {
        this(session, destination, 0l);
    }

    public DestinationRequestor(Session session, Destination destination,
                                long timeout) throws JMSException {
        this(session, destination, (session instanceof TopicSession) ? (session
                .createTemporaryTopic()) : (session.createTemporaryQueue()),
                timeout);
    }

    public DestinationRequestor(Session session, Destination destination,
                                Destination replyTo, long timeout) throws JMSException {
        this.session = session;
        this.topic = destination;

        tempDestination = replyTo;
        publisher = session.createProducer(destination);
        subscriber = session.createConsumer(tempDestination);
        this.timeout = timeout;
    }

    public Message request(Message message) throws JMSException {
        message.setJMSReplyTo(tempDestination);
        publisher.send(message);
        if (timeout < 0l)
            return subscriber.receiveNoWait();
        else if (timeout == 0)
            return subscriber.receive();
        else
            return subscriber.receive(timeout);
    }

    public void close() throws JMSException {
        close(false);
    }

    public void close(boolean closeSession) throws JMSException {
        publisher.close();
        subscriber.close();
        if (closeSession)
            session.close();
        if (tempDestination instanceof TemporaryQueue)
            ((TemporaryQueue) tempDestination).delete();
        else if (tempDestination instanceof TemporaryTopic)
            ((TemporaryTopic) tempDestination).delete();
        // Method method =
        // ReflectionUtils.findMethod(tempDestination.getClass(),
        // "delete");
        // if (method != null)
        // ReflectionUtils.invokeMethod(method, tempDestination);
    }

}
