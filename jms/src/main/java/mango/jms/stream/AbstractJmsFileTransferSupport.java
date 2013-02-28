package mango.jms.stream;

import mango.jms.stream.domain.Piece;
import mango.jms.stream.domain.Request;
import mango.jms.stream.domain.Response;
import mango.jms.stream.domain.ResponseStatusEnum;
import mango.jms.stream.utils.DestinationRequestor;
import mango.jms.stream.utils.DomainMessageUtils;
import mango.jms.stream.utils.MessageDigestUtils;
import mango.jms.stream.utils.MessageQueueListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.SessionCallback;
import org.springframework.util.Assert;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public abstract class AbstractJmsFileTransferSupport {

    Logger logger = LoggerFactory.getLogger(getClass());

    protected JmsTemplate jmsTemplate;
    private int pieceSize = 10240;
    private Destination streamRequestDestination;
    private String hashAlgorithm;

    private Logger logService;

    protected String generateId() {
        return UUID.randomUUID().toString();
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public int getPieceSize() {
        return pieceSize;
    }

    public Destination getStreamRequestDestination() {
        return streamRequestDestination;
    }

    public InputStream receiveStream(final Request req)
            throws JmsFileTransferException {
        try {
            logger.info(
                    "Broadcasting request for [" + req.getDownloadUrl()
                            + "] and id[" + req.getRequestId() + "]");
            Message returnMessage = sendWithResponse(new MessageCreator() {

                @Override
                public Message createMessage(Session session)
                        throws JMSException {
                    return DomainMessageUtils.toRequestMessage(session, req);
                }

            }, getStreamRequestDestination());

            // No one could fulfill the request
            if (returnMessage == null) {
                logger.info(
                        "No one can fullfil this request ["
                                + req.getDownloadUrl() + "]");
                return null;
            }

            String contentType = DomainMessageUtils.extractContentTypeFromMessage(returnMessage);

            // start the stream transfer at this destination
            final Destination receiveAckDestination = returnMessage
                    .getJMSReplyTo();
            final String sendDataDestination = UUID.randomUUID().toString();

            logger.info(
                    "Receiver[" + req.getRequestId()
                            + "]: File Transfer starting");

            System.out.println("CONTENTTYPE: " + contentType);
            return new JmsFileReceiverInputStream(contentType, this, sendDataDestination,
                    receiveAckDestination);
        } catch (Exception e) {
            throw new JmsFileTransferException(e);
        }
    }


    public void sendStream(Request req, final Destination replyTo)
            throws IOException, JmsFileTransferException {

        DigestInputStream is = null;
        Assert.notNull(req, "Request cannot be null");
        final URL downloadUrl = new URL(req.getDownloadUrl());
        final URLConnection connection = downloadUrl.openConnection();
        try {

            is = new DigestInputStream(new BufferedInputStream(connection.getInputStream()),
                    MessageDigest.getInstance(getHashAlgorithm()));



        } catch (NoSuchAlgorithmException e) {
            throw new JmsFileTransferException(e);
        } catch (Throwable e) {
            return;
        }

        MessageQueueListener queueListener = null;
        try {

            Message returnMessage = (Message) jmsTemplate.execute(
                    new SessionCallback() {

                        @Override
                        public Object doInJms(Session session)
                                throws JMSException {
                            DestinationRequestor requestor = null;
                            try {
                                Message responseMessage = DomainMessageUtils
                                        .toResponseMessage(session, new Response(
                                                ResponseStatusEnum.ACCEPT));

                                DomainMessageUtils
                                        .setContentTypeOnMessage(responseMessage, connection.getContentType());

                                // Actual file transfer should be done on a queue.
                                // Topics will not work
                                Destination streamTransferDestination = factoryQueue(
                                        session, UUID.randomUUID().toString());
                                requestor = new DestinationRequestor(
                                        session, replyTo,
                                        streamTransferDestination, jmsTemplate
                                        .getReceiveTimeout());
                                Message returnMessage = requestor
                                        .request(responseMessage);
                                requestor.close();
                                return returnMessage;
                            } finally {
                                if (requestor != null)
                                    requestor.close();
                            }
                        }

                    }, true);

            // timeout
            if (returnMessage == null)
                return;
            Response response = DomainMessageUtils
                    .fromResponseMessage(returnMessage);

            // cancel transfer
            if (!ResponseStatusEnum.STARTSEND.equals(response.getStatus()))
                return;

            final Destination receiveAckDestination = returnMessage
                    .getJMSDestination();
            final Destination sendDataDestination = returnMessage
                    .getJMSReplyTo();

            queueListener = new MessageQueueListener(this, receiveAckDestination);

            logger.info(
                    "Sender[" + req.getRequestId() + "]: Starting send to: "
                            + sendDataDestination);

            byte[] buffer = new byte[getPieceSize()];
            int read = is.read(buffer);
            long placeInFile = 0;
            while (read >= 0) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                stream.write(buffer, 0, read);
                stream.close();
                final byte[] pieceData = stream.toByteArray();
                final Piece piece = new Piece(placeInFile, pieceData,
                        getHashAlgorithm());
                logger.info(
                        "Sender[" + req.getRequestId()
                                + "]: Sending piece with position: "
                                + piece.getPosition() + " Size of piece: "
                                + pieceData.length);
                jmsTemplate.send(sendDataDestination, new MessageCreator() {

                    @Override
                    public Message createMessage(Session session)
                            throws JMSException {
                        return DomainMessageUtils
                                .toPieceMessage(session, piece);
                    }

                });
//                Message ret = jmsTemplate.receive(receiveAckDestination);
                Message ret = queueListener.getMessageInQueue();
                logger.info(
                        "Sender[" + req.getRequestId()
                                + "]: Sent piece and got ack");

                // no one on the other end any longer, timeout
                if (ret == null)
                    return;

                Response res = DomainMessageUtils.fromResponseMessage(ret);
                // stop transfer
                if (ResponseStatusEnum.RESEND.equals(res.getStatus())) {
                    // resend piece
                    logger.info(
                            "Sender[" + req.getRequestId()
                                    + "]: Resending piece");
                } else if (ResponseStatusEnum.DENY.equals(res.getStatus())) {
                    return;
                } else {
                    buffer = new byte[getPieceSize()];
                    placeInFile += read;
                    read = is.read(buffer);
                }
            }

            logger.info(
                    "Sender[" + req.getRequestId() + "]: Sending stop send");

            final DigestInputStream fiIs = is;

            jmsTemplate.send(sendDataDestination, new MessageCreator() {

                @Override
                public Message createMessage(Session session)
                        throws JMSException {
                    Response stopSendResponse = new Response(
                            ResponseStatusEnum.STOPSEND);
                    stopSendResponse.setHash(MessageDigestUtils
                            .getHashString(fiIs.getMessageDigest().digest()));
                    return DomainMessageUtils.toResponseMessage(session,
                            stopSendResponse);
                }

            });

//            Message ackMessage = jmsTemplate.receive(receiveAckDestination);
            Message ackMessage = queueListener.getMessageInQueue();

            Object fromMessage = DomainMessageUtils.fromMessage(ackMessage);
            if (fromMessage instanceof Response) {
                Response ackResponse = (Response) fromMessage;
                if (ResponseStatusEnum.RESEND.equals(ackResponse.getStatus())) {
                    // TODO: resend the whole file
                }
            }

        } catch (Exception e) {
            throw new JmsFileTransferException(e);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                }
            if (queueListener != null)
                queueListener.close();
        }
    }

    protected Message sendWithResponse(final MessageCreator mc,
                                       final Destination replyTo) {
        return (Message) jmsTemplate.execute(new SessionCallback() {

            @Override
            public Object doInJms(Session session) throws JMSException {
                DestinationRequestor requestor = null;
                try {
                    Message sendMessage = mc.createMessage(session);

                    requestor = new DestinationRequestor(
                            session, replyTo, jmsTemplate.getReceiveTimeout());

                    Message returnMessage = requestor.request(sendMessage);

                    requestor.close();
                    return returnMessage;
                } finally {
                    if (requestor != null)
                        requestor.close();
                }
            }

        }, true);
    }

    protected Destination factoryQueue(Session session, String queueName)
            throws JMSException {
        return jmsTemplate.getDestinationResolver().resolveDestinationName(
                session, queueName, false);
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setPieceSize(int pieceSize) {
        this.pieceSize = pieceSize;
    }

    public void setStreamRequestDestination(Destination streamRequestDestination) {
        this.streamRequestDestination = streamRequestDestination;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }
}
