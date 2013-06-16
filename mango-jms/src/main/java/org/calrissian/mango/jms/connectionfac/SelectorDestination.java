package org.calrissian.mango.jms.connectionfac;

import javax.jms.*;
import java.io.Serializable;


public class SelectorDestination implements Queue, Topic, TemporaryQueue, TemporaryTopic, Serializable {
    
    private String destination;

    public SelectorDestination(String destination) {
        this.destination = destination;
    }

    public boolean isQueue() {
        return false;
    }

    public boolean isTopic() {
        return true;
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
