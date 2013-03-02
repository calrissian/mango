package mango.jms.connectionfac;

import javax.jms.*;

/**
 * Class JmsMessageProducerTopicDecorator
 * Date: Nov 27, 2011
 * Time: 5:25:02 PM
 */
public class JmsMessageProducerTopicDecorator implements MessageProducer {
    private MessageProducer producer;
    private SelectorDestination selectorDestination;
    private Topic topic;

    public JmsMessageProducerTopicDecorator(MessageProducer producer, Topic topic, SelectorDestination selectorDestination) {
        this.producer = producer;
        this.topic = topic;
        this.selectorDestination = selectorDestination;
    }

    public void setDisableMessageID(boolean b) throws JMSException {
        producer.setDisableMessageID(b);
    }

    public boolean getDisableMessageID() throws JMSException {
        return producer.getDisableMessageID();
    }

    public void setDisableMessageTimestamp(boolean b) throws JMSException {
        producer.setDisableMessageTimestamp(b);
    }

    public boolean getDisableMessageTimestamp() throws JMSException {
        return producer.getDisableMessageTimestamp();
    }

    public void setDeliveryMode(int i) throws JMSException {
        producer.setDeliveryMode(i);
    }

    public int getDeliveryMode() throws JMSException {
        return producer.getDeliveryMode();
    }

    public void setPriority(int i) throws JMSException {
        producer.setPriority(i);
    }

    public int getPriority() throws JMSException {
        return producer.getPriority();
    }

    public void setTimeToLive(long l) throws JMSException {
        producer.setTimeToLive(l);
    }

    public long getTimeToLive() throws JMSException {
        return producer.getTimeToLive();
    }

    public Destination getDestination() throws JMSException {
        return producer.getDestination();
    }

    public void close() throws JMSException {
        producer.close();
    }

    public void send(Message message) throws JMSException {
        JmsTopicDecoratorConstants.preSendMessage(message, topic, selectorDestination);
        producer.send(message);
    }

    public void send(Message message, int i, int i1, long l) throws JMSException {
        JmsTopicDecoratorConstants.preSendMessage(message, topic, selectorDestination);
        producer.send(message, i, i1, l);
    }

    public void send(Destination destination, Message message) throws JMSException {
        if (destination == null)
            throw new IllegalArgumentException("Destination cannot be null");
        JmsTopicDecoratorConstants.preSendMessage(message, topic, destination);
        producer.send(message);
    }

    public void send(Destination destination, Message message, int i, int i1, long l) throws JMSException {
        if (destination == null)
            throw new IllegalArgumentException("Destination cannot be null");
        JmsTopicDecoratorConstants.preSendMessage(message, topic, destination);
        producer.send(message, i, i1, l);
    }

    public MessageProducer getProducer() {
        return producer;
    }

    public SelectorDestination getSelectorDestination() {
        return selectorDestination;
    }
}

