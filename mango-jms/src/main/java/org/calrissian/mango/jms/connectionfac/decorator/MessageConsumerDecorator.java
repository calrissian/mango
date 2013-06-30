package org.calrissian.mango.jms.connectionfac.decorator;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public abstract class MessageConsumerDecorator implements MessageConsumer{

    private final MessageConsumer messageConsumer;

    public MessageConsumerDecorator(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public String getMessageSelector() throws JMSException {
        return messageConsumer.getMessageSelector();
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return messageConsumer.getMessageListener();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        messageConsumer.setMessageListener(messageListener);
    }

    @Override
    public Message receive() throws JMSException {
        return messageConsumer.receive();
    }

    @Override
    public Message receive(long l) throws JMSException {
        return messageConsumer.receive(l);
    }

    @Override
    public Message receiveNoWait() throws JMSException {
        return messageConsumer.receiveNoWait();
    }

    @Override
    public void close() throws JMSException {
        messageConsumer.close();
    }

    public MessageConsumer getInternal() {
        return messageConsumer;
    }
}
