package org.calrissian.mango.jms.connectionfac.decorator;

import javax.jms.*;

public abstract class ConnectionDecorator implements Connection, TopicConnection, QueueConnection {

    private final Connection connection;

    public ConnectionDecorator(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Session createSession(boolean transacted, int ackMode) throws JMSException {
        return connection.createSession(transacted, ackMode);
    }

    @Override
    public String getClientID() throws JMSException {
        return connection.getClientID();
    }

    @Override
    public void setClientID(String s) throws JMSException {
        connection.setClientID(s);
    }

    @Override
    public ConnectionMetaData getMetaData() throws JMSException {
        return connection.getMetaData() ;
    }

    @Override
    public ExceptionListener getExceptionListener() throws JMSException {
        return connection.getExceptionListener();
    }

    @Override
    public void setExceptionListener(ExceptionListener exceptionListener) throws JMSException {
        connection.setExceptionListener(exceptionListener);
    }

    @Override
    public void start() throws JMSException {
        connection.start();
    }

    @Override
    public void stop() throws JMSException {
        connection.stop();
    }

    @Override
    public void close() throws JMSException {
        connection.close();
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool serverSessionPool, int maxMessages) throws JMSException {
        return connection.createConnectionConsumer(destination, messageSelector, serverSessionPool, maxMessages);
    }

    @Override
    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool serverSessionPool, int maxMessages) throws JMSException {
        return connection.createDurableConnectionConsumer(topic, subscriptionName, messageSelector, serverSessionPool, maxMessages);
    }

    @Override
    public QueueSession createQueueSession(boolean b, int i) throws JMSException {
        return ((QueueConnection)connection).createQueueSession(b, i);
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Queue queue, String s, ServerSessionPool serverSessionPool, int i) throws JMSException {
        return ((QueueConnection)connection).createConnectionConsumer(queue, s, serverSessionPool, i);
    }

    @Override
    public TopicSession createTopicSession(boolean b, int i) throws JMSException {
        return ((TopicConnection)connection).createTopicSession(b, i);
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Topic topic, String s, ServerSessionPool serverSessionPool, int i) throws JMSException {
        return ((TopicConnection)connection).createConnectionConsumer(topic, s, serverSessionPool, i);
    }

    public Connection getInternal() {
        return connection;
    }
}
