package org.calrissian.mango.jms.connectionfac;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * Class JmsMessageConsumerTopicDecorator
 * Date: Nov 30, 2011
 * Time: 5:05:42 PM
 */
public class JmsMessageConsumerQueueDecorator implements MessageConsumer {

    private MessageConsumer consumer;

    public JmsMessageConsumerQueueDecorator(MessageConsumer consumer) {
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
        return ((JmsMessageListenerQueueDecorator) consumer.getMessageListener()).getMessageListener();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        consumer.setMessageListener(new JmsMessageListenerQueueDecorator(messageListener));
    }

    @Override
    public Message receive() throws JMSException {
        Message msg = consumer.receive();
        if (msg != null) {
            JmsQueueDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public Message receive(long l) throws JMSException {
        Message msg = consumer.receive(l);
        if (msg != null) {
            JmsQueueDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public Message receiveNoWait() throws JMSException {
        Message msg = consumer.receiveNoWait();
        if(msg != null) {
            JmsQueueDecoratorConstants.postReceiveMessage(msg);
        }
        return msg;
    }

    @Override
    public void close() throws JMSException {
        consumer.close();
    }
}
