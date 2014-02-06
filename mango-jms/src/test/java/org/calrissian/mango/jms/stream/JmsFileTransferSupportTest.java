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
package org.calrissian.mango.jms.stream;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import junit.framework.TestCase;

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

                                    @Override
                                    public String getContentType() {
                                        return "content/notnull";
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

        JmsFileReceiverInputStream stream = (JmsFileReceiverInputStream) receiver.receiveStream("testprot:test");
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


