package org.calrissian.mango.jms.stream;

import org.calrissian.mango.jms.stream.domain.Piece;
import org.calrissian.mango.jms.stream.domain.Response;
import org.calrissian.mango.jms.stream.domain.ResponseStatusEnum;
import org.calrissian.mango.jms.stream.utils.DomainMessageUtils;
import org.calrissian.mango.jms.stream.utils.MessageDigestUtils;
import org.calrissian.mango.jms.stream.utils.MessageQueueListener;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JmsFileReceiverInputStream extends InputStream {

    private InputStream current;
    private int index = 0;
    private boolean closed = false;
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
        int read = -1;
        if (current != null)
            read = current.read();
        if (!closed && read >= 0) {
            return read;
        } else if (closed)
            return -1;
        else {
            try {
                readNext();
                return read();
            } catch (JmsFileTransferException e) {
                new IOException(e);
            }
        }

        return -1;
    }

    protected void readNext() throws JmsFileTransferException {
        try {
            Message message = getMessageInQueue();

            Object object = DomainMessageUtils.fromMessage(message);
            ResponseStatusEnum responseStatus = ResponseStatusEnum.ACCEPT;
            if (object instanceof Piece) {
                Piece piece = (Piece) object;

                String sentHash = piece.getHash();
                if (!MessageDigestUtils.getHashString(
                        MessageDigest.getInstance(support.getHashAlgorithm())
                                .digest(piece.getData())).equals(sentHash)) {
                    responseStatus = ResponseStatusEnum.RESEND;
                } else {
                    byte[] data = piece.getData();
                    current = new ByteArrayInputStream(data);
                }
            } else if (object instanceof Response) {
                Response transferResp = (Response) object;
                if (transferResp.getStatus() == ResponseStatusEnum.STOPSEND) {
                    closed = true;

                } else
                    throw new JmsFileTransferException(
                            "Transfer aborted with status["
                                    + transferResp.getStatus()
                                    + "] from server");
            } else
                throw new JmsFileTransferException(
                        "Unexpected message received: " + message);
            // ACK
            final ResponseStatusEnum toSendStatus = responseStatus;
            support.getJmsTemplate().send(receiveAckDestination,
                    new MessageCreator() {

                        @Override
                        public Message createMessage(Session session)
                                throws JMSException {
                            return DomainMessageUtils.toResponseMessage(
                                    session, new Response(toSendStatus));
                        }

                    });
        } catch (JMSException e) {
            throw new JmsFileTransferException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new JmsFileTransferException(e);
        }
    }

    protected Message getMessageInQueue() throws JmsFileTransferException {
        return messageQueueListener.getMessageInQueue();
    }

    @Override
    public void close() throws IOException {
        super.close();
        messageQueueListener.close();
        if (!closed) {
            closed = true;
            final ResponseStatusEnum responseStatus = ResponseStatusEnum.DENY;
            support.getJmsTemplate().send(receiveAckDestination,
                    new MessageCreator() {

                        @Override
                        public Message createMessage(Session session)
                                throws JMSException {
                            return DomainMessageUtils.toResponseMessage(
                                    session, new Response(responseStatus));
                        }

                    });
        }
    }

    protected void start() {
        support.getJmsTemplate().send(receiveAckDestination,
                new MessageCreator() {

                    @Override
                    public Message createMessage(Session session)
                            throws JMSException {

                        final Message responseMessage = DomainMessageUtils
                                .toResponseMessage(session, new Response(
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
