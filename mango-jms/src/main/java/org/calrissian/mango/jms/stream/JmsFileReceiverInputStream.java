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

import org.calrissian.mango.io.AbstractBufferedInputStream;
import org.calrissian.mango.jms.stream.domain.Piece;
import org.calrissian.mango.jms.stream.domain.Response;
import org.calrissian.mango.jms.stream.domain.ResponseStatusEnum;
import org.calrissian.mango.jms.stream.utils.MessageQueueListener;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.security.MessageDigest;

import static org.calrissian.mango.jms.stream.utils.DomainMessageUtils.fromMessage;
import static org.calrissian.mango.jms.stream.utils.DomainMessageUtils.toResponseMessage;

public class JmsFileReceiverInputStream extends AbstractBufferedInputStream {

    private boolean done = false;
    private boolean started = false;

    private AbstractJmsFileTransferSupport support;
    private String sendDataDestination;
    private Destination receiveAckDestination;

    private MessageQueueListener messageQueueListener;

    protected JmsFileReceiverInputStream(AbstractJmsFileTransferSupport support,
                                         String sendDataDestination, Destination receiveAckDestination) {
        this.support = support;
        this.sendDataDestination = sendDataDestination;
        this.receiveAckDestination = receiveAckDestination;

        //set up listener
        messageQueueListener = new MessageQueueListener(support, sendDataDestination);
    }

    @Override
    public int read() throws IOException {
        if (!started)
            start();
        return super.read();
    }

    @Override
    protected boolean isEOF() {
        return done;
    }

    @Override
    protected byte[] getNextBuffer() throws IOException {
        try {
            byte[] data = null;
            Message message = messageQueueListener.getMessageInQueue();

            Object object = fromMessage(message);
            ResponseStatusEnum responseStatus = ResponseStatusEnum.ACCEPT;
            if (object instanceof Piece) {
                Piece piece = (Piece) object;

                String sentHash = piece.getHash();
                if (!new String(MessageDigest.getInstance(support.getHashAlgorithm()).digest(piece.getData())).equals(sentHash))
                    responseStatus = ResponseStatusEnum.RESEND;
                else
                    data = piece.getData();

            } else if (object instanceof Response) {
                Response transferResp = (Response) object;
                if (transferResp.getStatus() == ResponseStatusEnum.STOPSEND)
                    done = true;
                else
                    throw new IOException("Transfer aborted with status[" + transferResp.getStatus() + "] from server");
            } else {
                throw new IOException("Unexpected message received: " + message);
            }

            // ACK
            final ResponseStatusEnum toSendStatus = responseStatus;
            support.getJmsTemplate().send(receiveAckDestination,
                    new MessageCreator() {

                        @Override
                        public Message createMessage(Session session)
                                throws JMSException {
                            return toResponseMessage(
                                    session, new Response(toSendStatus));
                        }

                    });

            return data;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }

    }

    @Override
    public void close() throws IOException {
        messageQueueListener.close();
        if (!done) {
            done = true;
            final ResponseStatusEnum responseStatus = ResponseStatusEnum.DENY;
            support.getJmsTemplate().send(receiveAckDestination,
                    new MessageCreator() {

                        @Override
                        public Message createMessage(Session session)
                                throws JMSException {
                            return toResponseMessage(
                                    session, new Response(responseStatus));
                        }

                    });
        }
        super.close();
    }

    protected void start() {
        support.getJmsTemplate().send(receiveAckDestination,
                new MessageCreator() {

                    @Override
                    public Message createMessage(Session session)
                            throws JMSException {

                        final Message responseMessage =
                                toResponseMessage(session, new Response(
                                        ResponseStatusEnum.STARTSEND));
                        Destination factoryQueue = support.factoryQueue(
                                session, sendDataDestination);
                        responseMessage.setJMSReplyTo(factoryQueue);
                        return responseMessage;
                    }

                });
        started = true;
    }
}
