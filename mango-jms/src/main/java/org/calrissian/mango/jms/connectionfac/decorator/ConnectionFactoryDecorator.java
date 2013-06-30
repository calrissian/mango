package org.calrissian.mango.jms.connectionfac.decorator;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public abstract class ConnectionFactoryDecorator implements ConnectionFactory{

    private final ConnectionFactory connectionFactory;

    public ConnectionFactoryDecorator(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Connection createConnection() throws JMSException {
        return connectionFactory.createConnection();
    }

    @Override
    public Connection createConnection(String userName, String password) throws JMSException {
        return connectionFactory.createConnection(userName, password);
    }

    public ConnectionFactory getInternal() {
        return connectionFactory;
    }
}
