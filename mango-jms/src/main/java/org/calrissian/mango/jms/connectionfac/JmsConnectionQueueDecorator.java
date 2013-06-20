package org.calrissian.mango.jms.connectionfac;

import javax.jms.*;

/**
 * Class JmsConnectionTopicDecorator
 * Date: Nov 27, 2011
 * Time: 4:43:19 PM
 */
public class JmsConnectionQueueDecorator implements QueueConnection{

    private QueueConnection connection;
    private String baseQueue;

    public JmsConnectionQueueDecorator(Connection connection, String baseQueue) {
        this.connection = (QueueConnection)connection;
        this.baseQueue = baseQueue;
    }

    @Override
    public Session createSession(boolean transacted, int ackMode) throws JMSException {
        return new JmsSessionQueueDecorator(connection.createQueueSession(transacted, ackMode), baseQueue);
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
        return connection.getMetaData();
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
    public ConnectionConsumer createConnectionConsumer(Destination destination, String s, ServerSessionPool serverSessionPool, int i) throws JMSException {
        throw new UnsupportedOperationException();
        //return connection.createConnectionConsumer(destination, s, serverSessionPool, i);
    }

    @Override
    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String s, String s1, ServerSessionPool serverSessionPool, int i) throws JMSException {
        throw new UnsupportedOperationException();
        //return connection.createDurableConnectionConsumer(queue, s, s1, serverSessionPool, i);
    }

    public Connection getInnerConnection() {
        return connection;
    }

    public String getBaseQueue() {
        return baseQueue;
    }

    @Override
    public QueueSession createQueueSession(boolean b, int i) throws JMSException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Queue queue, String s, ServerSessionPool serverSessionPool, int i) throws JMSException {

        throw new UnsupportedOperationException();
    }
}
