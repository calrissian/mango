package mango.jms.connectionfac;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * Class JmsMessageConsumerTopicDecorator
 * Date: Nov 30, 2011
 * Time: 5:05:42 PM
 */
public class JmsMessageConsumerTopicDecorator implements MessageConsumer {

    private MessageConsumer consumer;

    public JmsMessageConsumerTopicDecorator(MessageConsumer consumer) {
        this.consumer = consumer;
    }

    public MessageConsumer getConsumer() {
        return consumer;
    }

    @Override
    public String getMessageSelector() throws JMSException {
        return consumer.getMessageSelector();
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return ((JmsMessageListenerTopicDecorator) consumer.getMessageListener()).getMessageListener();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        consumer.setMessageListener(new JmsMessageListenerTopicDecorator(messageListener));
    }

    @Override
    public Message receive() throws JMSException {
        Message msg = consumer.receive();
        if (msg != null) {
            JmsTopicDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public Message receive(long l) throws JMSException {
        Message msg = consumer.receive(l);
        if (msg != null) {
            JmsTopicDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public Message receiveNoWait() throws JMSException {
        Message msg = consumer.receiveNoWait();
        if(msg != null) {
            JmsTopicDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public void close() throws JMSException {
        consumer.close();
    }
}
