package mango.jms.connectionfac;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * Class JmsConnectionFactoryTopicDecorator
 * Date: Nov 27, 2011
 * Time: 4:38:17 PM
 */
public class JmsConnectionFactoryQueueDecorator implements ConnectionFactory {

    private ConnectionFactory connectionFactory;

    private String baseQueue;

    @Override
    public Connection createConnection() throws JMSException {
        JmsConnectionQueueDecorator connectionTopicDecorator = new JmsConnectionQueueDecorator(connectionFactory.createConnection(), baseQueue);
        return connectionTopicDecorator;
    }

    @Override
    public Connection createConnection(String userName, String password) throws JMSException {
        JmsConnectionTopicDecorator connectionTopicDecorator = new JmsConnectionTopicDecorator(connectionFactory.createConnection(userName, password), baseQueue);
        return connectionTopicDecorator;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String getBaseQueue() {
        return baseQueue;
    }

    public void setBaseQueue(String baseQueue) {
        this.baseQueue = baseQueue;
    }
}
