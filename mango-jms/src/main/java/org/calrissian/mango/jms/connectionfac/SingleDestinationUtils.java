/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.jms.connectionfac;

import javax.jms.*;

/**
 * Class SingleDestinationUtils
 * Date: Dec 1, 2011
 * Time: 8:36:37 AM
 */
@Deprecated
class SingleDestinationUtils {

    private static final String JMS_TOPIC_PROP_STR = "selectTopic";
    private static final String JMS_REPLYTO_PROP_STR = "replyTo";

    private SingleDestinationUtils() {/* private constructor */}

    public static void preSendMessage(Message msg, Destination topic, Destination destination) throws JMSException {
        if (destination != null) {
            String selectTopic = getDestination(destination);
            msg.setStringProperty(JMS_TOPIC_PROP_STR, selectTopic);
        }
        Destination destReplyTo = msg.getJMSReplyTo();
        //Because Tibco does not let non Tibco types in the JmsReplyTo, we have to fake it
        if (destReplyTo != null) {
            msg.setJMSReplyTo(topic);
            msg.setStringProperty(JMS_REPLYTO_PROP_STR, getDestination(destReplyTo));
        }
    }

    public static void postReceiveMessage(Message message) throws JMSException {
        String selectTopic = message.getStringProperty(JMS_TOPIC_PROP_STR);
        if (selectTopic != null) {
            //set the destination
            message.setJMSDestination(new SelectorDestination(selectTopic));
        }
        String replyTo = message.getStringProperty(JMS_REPLYTO_PROP_STR);
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
