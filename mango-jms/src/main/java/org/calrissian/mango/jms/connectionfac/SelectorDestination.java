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
