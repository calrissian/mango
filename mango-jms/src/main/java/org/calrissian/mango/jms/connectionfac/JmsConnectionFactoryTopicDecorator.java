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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * Class JmsConnectionFactoryTopicDecorator
 * Date: Nov 27, 2011
 * Time: 4:38:17 PM
 */
public class JmsConnectionFactoryTopicDecorator implements ConnectionFactory {

    private ConnectionFactory connectionFactory;

    private String baseTopic;

    @Override
    public Connection createConnection() throws JMSException {
        JmsConnectionTopicDecorator connectionTopicDecorator = new JmsConnectionTopicDecorator(connectionFactory.createConnection(), baseTopic);
        return connectionTopicDecorator;
    }

    @Override
    public Connection createConnection(String userName, String password) throws JMSException {
        JmsConnectionTopicDecorator connectionTopicDecorator = new JmsConnectionTopicDecorator(connectionFactory.createConnection(userName, password), baseTopic);
        return connectionTopicDecorator;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String getBaseTopic() {
        return baseTopic;
    }

    public void setBaseTopic(String baseTopic) {
        this.baseTopic = baseTopic;
    }
}
