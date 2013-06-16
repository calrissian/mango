package org.calrissian.mango.jms.connectionfac;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Class JmsMessageListenerTopicDecorator
 * Date: Nov 30, 2011
 * Time: 5:07:28 PM
 */
public class JmsMessageListenerTopicDecorator implements MessageListener {

    private MessageListener messageListener;

    public JmsMessageListenerTopicDecorator(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void onMessage(Message message) {
        try {
            JmsTopicDecoratorConstants.postReceiveMessage(message);
        } catch (JMSException e) {
            //not sure what to do here
        }
        messageListener.onMessage(message);
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }
}
