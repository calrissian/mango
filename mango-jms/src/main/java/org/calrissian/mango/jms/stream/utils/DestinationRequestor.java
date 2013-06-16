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
