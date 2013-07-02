package org.calrissian.mango.jms.connectionfac.decorator;


import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class MessageListenerDecorator implements MessageListener {

    private final MessageListener messageListener;

    protected MessageListenerDecorator(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void onMessage(Message message) {
        messageListener.onMessage(message);
    }

    public MessageListener getInternal() {
        return messageListener;
    }
}
