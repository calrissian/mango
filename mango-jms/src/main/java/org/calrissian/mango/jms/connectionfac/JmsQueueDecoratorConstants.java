package org.calrissian.mango.jms.connectionfac;

import javax.jms.*;

/**
 * Class JmsTopicDecoratorConstants
 * Date: Dec 1, 2011
 * Time: 8:36:37 AM
 */
public class JmsQueueDecoratorConstants {
    public static final String JMS_TOPIC_PROP_STR = "selectTopic";
    public static final String JMS_REPLYTO_PROP_STR = "replyTo";

    public static void preSendMessage(Message msg, Queue queue, Destination destination) throws JMSException {
        if (destination != null) {
            String selectTopic = null;
            if(destination instanceof SelectorDestination) {
                selectTopic = ((SelectorDestination) destination).getDestination();
            } else {
                selectTopic = getDestination(destination);
            }
            msg.setStringProperty(JMS_TOPIC_PROP_STR, selectTopic);
        }
        Destination destReplyTo = msg.getJMSReplyTo();
        //Because Tibco does not let non Tibco types in the JmsReplyTo, we have to fake it
        if (destReplyTo != null && destReplyTo instanceof SelectorDestination) {
            msg.setJMSReplyTo(queue);
            msg.setStringProperty(JmsQueueDecoratorConstants.JMS_REPLYTO_PROP_STR, ((SelectorDestination) destReplyTo).getDestination());
        }
    }

    public static void postReceiveMessage(Message message) throws JMSException {
        String selectTopic = message.getStringProperty(JMS_TOPIC_PROP_STR);
        if(selectTopic != null) {
            //set the destination
            message.setJMSDestination(new SelectorDestination(selectTopic));
        }
        String replyTo = message.getStringProperty(JmsTopicDecoratorConstants.JMS_REPLYTO_PROP_STR);
        if (replyTo != null) {
            message.setJMSReplyTo(new SelectorDestination(replyTo));
        }
    }

    public static String generateSelector(String sd, String selector) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(").append(JMS_TOPIC_PROP_STR).append("='").append(sd).append("')");
        if (selector != null) {
            buffer.append(" AND ").append("(").append(selector).append(")");
        }
        return buffer.toString();
    }

    public static SelectorDestination decorateDestination(Destination destination) throws JMSException {
        if (destination == null)
            return null;

        if (destination instanceof SelectorDestination)
            return (SelectorDestination) destination;

        return new SelectorDestination(getDestination(destination));
    }

    public static String getDestination(Destination destination) throws JMSException {
        return (destination instanceof Topic) ? (((Topic) destination)).getTopicName() : (((Queue) destination)).getQueueName();
    }
}
