package org.calrissian.mango.jms.stream.utils;

import org.calrissian.mango.jms.stream.AbstractJmsFileTransferSupport;
import org.calrissian.mango.jms.stream.JmsFileTransferException;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * As messages come in they are queued
 * Class MessageQueueListener
 * Date: Dec 10, 2011
 * Time: 2:09:54 PM
 */
public class MessageQueueListener implements MessageListener {

    private BlockingQueue<Message> queueMessages = new LinkedBlockingQueue<Message>();
    private SimpleMessageListenerContainer messageListenerContainer;
    private String destination;
    private AbstractJmsFileTransferSupport support;

    public MessageQueueListener(AbstractJmsFileTransferSupport support, String destination) {
        this.support = support;
        this.destination = destination;

        //set up listener
        messageListenerContainer = new SimpleMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(support.getJmsTemplate().getConnectionFactory());
        messageListenerContainer.setDestinationName(destination);
        messageListenerContainer.setMessageListener(this);
        messageListenerContainer.start();
    }

    public MessageQueueListener(AbstractJmsFileTransferSupport support, Destination destination) {
        this.support = support;
//        this.destination = destination;

        //set up listener
        messageListenerContainer = new SimpleMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(support.getJmsTemplate().getConnectionFactory());
        messageListenerContainer.setDestination(destination);
        messageListenerContainer.setMessageListener(this);
        messageListenerContainer.start();
    }

    public void close() {
        messageListenerContainer.stop();
    }

    @Override
    public void onMessage(Message message) {
        try {
            queueMessages.put(message);
        } catch (InterruptedException e) {

        }
    }

    public Message getMessageInQueue() throws JmsFileTransferException {
        //poll the queue
        try {
            Object o = queueMessages.poll(support.getJmsTemplate().getReceiveTimeout(), TimeUnit.MILLISECONDS);
            if (o == null)
                throw new JmsFileTransferException("Timeout reached in waiting for message");
            return (Message) o;
        } catch (InterruptedException e) {

        }
        return null;
    }
}
