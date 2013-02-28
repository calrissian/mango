package mango.jms.stream;

import junit.framework.TestCase;
import mango.jms.decorator.JmsConnectionFactoryTopicDecorator;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class JmsFileTransferSupportTest extends TestCase {

    private static final String TEST_STR = "this is my looong story of something that I can't really explain. So I'm stopping here.";

    public void testFullCycle() throws Exception {
        try {
            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {

                @Override
                public URLStreamHandler createURLStreamHandler(String protocol) {
                    if ("testprot".equals(protocol))
                        return new URLStreamHandler() {

                            @Override
                            protected URLConnection openConnection(URL u)
                                    throws IOException {
                                return new URLConnection(u) {

                                    @Override
                                    public void connect() throws IOException {

                                    }

                                    @Override
                                    public InputStream getInputStream()
                                            throws IOException {
                                        return new ByteArrayInputStream(TEST_STR
                                                .getBytes());
                                    }

                                };
                            }
                        };
                    return null;
                }
            });
        } catch (Error e) {
        }

        final ActiveMQTopic ft = new ActiveMQTopic("testFileTransfer");

        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.initialize();

        JmsFileSenderListener listener = new JmsFileSenderListener();
        final String hashAlgorithm = "MD5";
        listener.setHashAlgorithm(hashAlgorithm);
        listener.setStreamRequestDestination(ft);
        listener.setPieceSize(9);
        listener.setTaskExecutor(te);

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(cf);
        jmsTemplate.setReceiveTimeout(60000);
        listener.setJmsTemplate(jmsTemplate);

        Connection conn = cf.createConnection();
        conn.start();
        Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = sess.createConsumer(ft);
        consumer.setMessageListener(listener);

        JmsFileReceiver receiver = new JmsFileReceiver();
        receiver.setHashAlgorithm(hashAlgorithm);
        receiver.setStreamRequestDestination(ft);
        receiver.setJmsTemplate(jmsTemplate);
        receiver.setPieceSize(9);

        InputStream stream = receiver.receiveStream("testprot:test");
        StringBuffer buffer = new StringBuffer();
        int read = 0;
        while ((read = stream.read()) >= 0) {
            buffer.append((char) read);
        }
        stream.close();

        assertEquals(TEST_STR, buffer.toString());

        conn.stop();
    }

    public void testFullCycleWithDecoratedFactory() throws Exception {
        try {
            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {

                @Override
                public URLStreamHandler createURLStreamHandler(String protocol) {
                    if ("testprot".equals(protocol))
                        return new URLStreamHandler() {

                            @Override
                            protected URLConnection openConnection(URL u)
                                    throws IOException {
                                return new URLConnection(u) {

                                    @Override
                                    public void connect() throws IOException {

                                    }

                                    @Override
                                    public InputStream getInputStream()
                                            throws IOException {
                                        return new ByteArrayInputStream(TEST_STR
                                                .getBytes());
                                    }

                                };
                            }
                        };
                    return null;
                }
            });
        } catch (Error e) {
        }

        final ActiveMQTopic ft = new ActiveMQTopic("testFileTransfer");

        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.initialize();

        JmsFileSenderListener listener = new JmsFileSenderListener();
        final String hashAlgorithm = "MD5";
        listener.setHashAlgorithm(hashAlgorithm);

        listener.setStreamRequestDestination(ft);
        listener.setPieceSize(9);
        listener.setTaskExecutor(te);

        ConnectionFactory cf = new ActiveMQConnectionFactory(
                "vm://localhost?broker.persistent=false");
        JmsConnectionFactoryTopicDecorator decFac = new JmsConnectionFactoryTopicDecorator();
        decFac.setConnectionFactory(cf);
        decFac.setBaseTopic("baseTopic");

        cf = decFac;

        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(cf);
        jmsTemplate.setReceiveTimeout(5000);
        listener.setJmsTemplate(jmsTemplate);

        Connection conn = cf.createConnection();
        conn.start();
        Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = sess.createConsumer(ft);
        consumer.setMessageListener(listener);

        JmsFileReceiver receiver = new JmsFileReceiver();
        receiver.setHashAlgorithm(hashAlgorithm);
        receiver.setStreamRequestDestination(ft);
        receiver.setJmsTemplate(jmsTemplate);
        receiver.setPieceSize(9);

        InputStream stream = receiver.receiveStream("testprot:test");
        StringBuffer buffer = new StringBuffer();
        int read = 0;
        while ((read = stream.read()) >= 0) {
            buffer.append((char) read);
        }
        stream.close();

        assertEquals(TEST_STR, buffer.toString());

        conn.stop();
    }
}
