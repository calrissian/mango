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
package org.calrissian.mango.jms.stream.utils;

import org.springframework.beans.factory.FactoryBean;

import javax.jms.*;

public class TopicFactoryBean implements FactoryBean {

    private ConnectionFactory connectionFactory;

    private String name;

    @Override
    public Object getObject() throws Exception {
        Connection c = connectionFactory.createConnection();
        Session s = c.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = s.createTopic(name);
        s.close();
        c.close();
        return topic;
    }

    @Override
    public Class getObjectType() {
        return Destination.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
