package org.calrissian.mango.jms.connectionfac.decorator;


import javax.jms.*;
import java.io.Serializable;

public abstract class SessionDecorator implements Session, TopicSession, QueueSession {

    private final Session session;

    public SessionDecorator(Session session) {
        this.session = session;
    }

    @Override
    public BytesMessage createBytesMessage() throws JMSException {
        return session.createBytesMessage();
    }

    @Override
    public MapMessage createMapMessage() throws JMSException {
        return session.createMapMessage();
    }

    @Override
    public Message createMessage() throws JMSException {
        return session.createMessage();
    }

    @Override
    public ObjectMessage createObjectMessage() throws JMSException {
        return session.createObjectMessage();
    }

    @Override
    public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
        return session.createObjectMessage(object);
    }

    @Override
    public StreamMessage createStreamMessage() throws JMSException {
        return session.createStreamMessage();
    }

    @Override
    public TextMessage createTextMessage() throws JMSException {
        return session.createTextMessage();
    }

    @Override
    public TextMessage createTextMessage(String text) throws JMSException {
        return session.createTextMessage(text);
    }

    @Override
    public boolean getTransacted() throws JMSException {
        return session.getTransacted();
    }

    @Override
    public int getAcknowledgeMode() throws JMSException {
        return session.getAcknowledgeMode();
    }

    @Override
    public void commit() throws JMSException {
        session.commit();
    }

    @Override
    public void rollback() throws JMSException {
        session.rollback();
    }

    @Override
    public void close() throws JMSException {
        session.close();
    }

    @Override
    public void recover() throws JMSException {
        session.recover();
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return session.getMessageListener();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        session.setMessageListener(messageListener);
    }

    @Override
    public void run() {
        session.run();
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        return session.createProducer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return session.createConsumer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
        return session.createConsumer(destination, messageSelector);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean nolocal) throws JMSException {
        return session.createConsumer(destination, messageSelector, nolocal);
    }

    @Override
    public Queue createQueue(String queueName) throws JMSException {
        return session.createQueue(queueName);
    }

    @Override
    public Topic createTopic(String topicName) throws JMSException {
        return session.createTopic(topicName);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
        return session.createDurableSubscriber(topic, name);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean nolocal) throws JMSException {
        return session.createDurableSubscriber(topic, name, messageSelector, nolocal);
    }

    @Override
    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        return session.createBrowser(queue);
    }

    @Override
    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
        return session.createBrowser(queue, messageSelector);
    }

    @Override
    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return session.createTemporaryQueue();
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return session.createTemporaryTopic();
    }

    @Override
    public void unsubscribe(String name) throws JMSException {
        session.unsubscribe(name);
    }

    @Override
    public QueueReceiver createReceiver(Queue queue) throws JMSException {
        return ((QueueSession)session).createReceiver(queue);
    }

    @Override
    public QueueReceiver createReceiver(Queue queue, String s) throws JMSException {
        return ((QueueSession)session).createReceiver(queue, s);
    }

    @Override
    public QueueSender createSender(Queue queue) throws JMSException {
        return ((QueueSession)session).createSender(queue);
    }

    @Override
    public TopicSubscriber createSubscriber(Topic topic) throws JMSException {
        return ((TopicSession)session).createSubscriber(topic);
    }

    @Override
    public TopicSubscriber createSubscriber(Topic topic, String s, boolean b) throws JMSException {
        return ((TopicSession)session).createSubscriber(topic, s, b);
    }

    @Override
    public TopicPublisher createPublisher(Topic topic) throws JMSException {
        return ((TopicSession)session).createPublisher(topic);
    }

    public Session getInternal() {
        return session;
    }
}
