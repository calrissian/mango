package mango.jms.decorator;


import javax.jms.*;
import java.io.Serializable;
import java.util.UUID;

import static mango.jms.decorator.JmsTopicDecoratorConstants.decorateDestination;
import static mango.jms.decorator.JmsTopicDecoratorConstants.generateSelector;
import static mango.jms.decorator.JmsTopicDecoratorConstants.getDestination;

/**
 * Class JmsSessionTopicDecorator
 * Date: Nov 27, 2011
 * Time: 4:44:38 PM
 */
public class JmsSessionTopicDecorator implements Session {
    private Session session;
    private String baseTopic;
    protected Topic topic;

    public JmsSessionTopicDecorator(Session session, String baseTopic) throws JMSException {
        this.session = session;
        this.baseTopic = baseTopic;
        topic = session.createTopic(baseTopic);
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
    public ObjectMessage createObjectMessage(Serializable serializable) throws JMSException {
        return session.createObjectMessage(serializable);
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
    public TextMessage createTextMessage(String s) throws JMSException {
        return session.createTextMessage(s);
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
        //return session.getMessageListener();
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        //session.setMessageListener(messageListener);
        throw new UnsupportedOperationException();
    }

    @Override
    public void run() {
        session.run();
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        return new JmsMessageProducerTopicDecorator(session.createProducer(topic), topic, decorateDestination(destination));
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return new JmsMessageConsumerTopicDecorator(session.createConsumer(topic, generateSelector(
                getDestination(destination), null)));
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String selector) throws JMSException {
        return new JmsMessageConsumerTopicDecorator(session.createConsumer(topic, generateSelector(
                getDestination(destination), selector)));
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String selector, boolean noLocal) throws JMSException {
        return new JmsMessageConsumerTopicDecorator(session.createConsumer(topic, generateSelector(getDestination(destination), selector), noLocal));
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic destination, String name) throws JMSException {
        //TODO
        return session.createDurableSubscriber(topic, name,
                generateSelector(getDestination(destination), null), false);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic destination, String name, String selector, boolean noLocal) throws JMSException {
        //TODO
        return session.createDurableSubscriber(topic, name,
                generateSelector(getDestination(destination), selector), noLocal);
    }

    @Override
    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        throw new UnsupportedOperationException("Queue Browser not supported as of yet");
        //return session.createBrowser(queue);
    }

    @Override
    public QueueBrowser createBrowser(Queue queue, String selector) throws JMSException {
        throw new UnsupportedOperationException("Queue Browser not supported as of yet");
        //return session.createBrowser(queue, selector);
    }

//    @Override
//    public Topic createTopic(String s) throws JMSException {
//        return session.createTopic(s);
//    }
//
//    @Override
//    public Queue createQueue(String s) throws JMSException {
//        return session.createQueue(s);
//    }
//
//    @Override
//    public TemporaryQueue createTemporaryQueue() throws JMSException {
//        return session.createTemporaryQueue();
//    }
//
//    @Override
//    public TemporaryTopic createTemporaryTopic() throws JMSException {
//        return session.createTemporaryTopic();
//    }

    //    @Override

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return new SelectorDestination(UUID.randomUUID().toString());
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return new SelectorDestination(UUID.randomUUID().toString());
    }

    @Override
    public Queue createQueue(String name) throws JMSException {
        return new SelectorDestination(name);
    }

    @Override
    public Topic createTopic(String name) throws JMSException {
        return new SelectorDestination(name);
    }

    @Override
    public void unsubscribe(String name) throws JMSException {
        session.unsubscribe(name);
    }

    public Session getInnerSession() {
        return session;
    }

    public String getBaseTopic() {
        return baseTopic;
    }
}
