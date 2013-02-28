package mango.jms.stream.utils;

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
