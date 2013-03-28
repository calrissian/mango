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
