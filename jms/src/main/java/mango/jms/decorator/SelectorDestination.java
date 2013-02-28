package mango.jms.decorator;

import javax.jms.*;
import java.io.Serializable;

/**
 * Class SelectorQueue
 * Date: Nov 27, 2011
 * Time: 5:01:13 PM
 */
public class SelectorDestination implements Queue, Topic, TemporaryQueue, TemporaryTopic, Serializable {

    private String destination;

    public SelectorDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String getQueueName() throws JMSException {
        return getDestination();
    }

    @Override
    public String getTopicName() throws JMSException {
        return getDestination();
    }

    @Override
    public void delete() throws JMSException {

    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "SelectorDestination{" +
                "destination='" + destination + '\'' +
                '}';
    }
}
