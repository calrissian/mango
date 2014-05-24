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
package org.calrissian.mango.jms.stream.utils;

import org.calrissian.mango.jms.stream.domain.Piece;
import org.calrissian.mango.jms.stream.domain.Request;
import org.calrissian.mango.jms.stream.domain.Response;
import org.calrissian.mango.jms.stream.domain.ResponseStatusEnum;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


public class DomainMessageUtils {

    public static final String REQUEST_ID = "filetransfer.request.id";
    public static final String REQUEST_DOWNLOADURL = "filetransfer.request.downloadurl";
    public static final String MESSAGETYPE = "filetransfer.message.type";
    public static final String MESSAGETYPE_REQUEST = "filetransfer.message.type.request";
    public static final String MESSAGETYPE_RESPONSE = "filetransfer.message.type.response";
    public static final String MESSAGETYPE_PIECE = "filetransfer.message.type.piece";
    private static final String PIECE_POSITION = "filetransfer.piece.position";
    private static final String PIECE_HASH = "filetransfer.piece.hash";
    private static final String RESPONSE_STATUS = "filetransfer.response.status";
    private static final String RESPONSE_HASH = "filetransfer.response.hash";
    private static final String CONTENT_TYPE = "filestransfer.message.contentType";

    private DomainMessageUtils() {/* private constructor */}

    public static Message toRequestMessage(Session session, Request request)
            throws JMSException {
        Message message = session.createMessage();
        message.setStringProperty(MESSAGETYPE, MESSAGETYPE_REQUEST);
        message
                .setStringProperty(REQUEST_DOWNLOADURL, request
                        .getDownloadUri());
        message.setStringProperty(REQUEST_ID, request.getRequestId());

        return message;
    }

    public static Request fromRequestMessage(Message message)
            throws JMSException {
        String id = message.getStringProperty(REQUEST_ID);
        String downloadUrl = message.getStringProperty(REQUEST_DOWNLOADURL);
        return new Request(downloadUrl, id);
    }

    public static Message toResponseMessage(Session session, Response response)
            throws JMSException {
        Message message = session.createMessage();
        message.setStringProperty(MESSAGETYPE, MESSAGETYPE_RESPONSE);
        message.setStringProperty(RESPONSE_STATUS, response.getStatus()
                .toString());
        message.setStringProperty(RESPONSE_HASH, response.getHash());

        return message;
    }

    public static String extractContentTypeFromMessage(Message message) throws JMSException {

        return message.getStringProperty(CONTENT_TYPE);
    }

    public static void setContentTypeOnMessage(Message message, String contentType) throws JMSException {
        message.setStringProperty(CONTENT_TYPE, contentType);
    }

    public static Response fromResponseMessage(Message message)
            throws JMSException {
        String status = message.getStringProperty(RESPONSE_STATUS);
        String hash = message.getStringProperty(RESPONSE_HASH);

        final Response response = new Response(ResponseStatusEnum
                .valueOf(status));
        response.setHash(hash);
        return response;
    }

    public static Message toPieceMessage(Session session, Piece piece)
            throws JMSException {
        BytesMessage message = session.createBytesMessage();
        message.setStringProperty(MESSAGETYPE, MESSAGETYPE_PIECE);
        message.setLongProperty(PIECE_POSITION, piece.getPosition());
        message.setStringProperty(PIECE_HASH, piece.getHash());
        message.writeBytes(piece.getData());

        return message;
    }

    public static Piece fromPieceMessage(Message message) throws JMSException {
        if (message instanceof BytesMessage) {
            BytesMessage bm = (BytesMessage) message;
            byte[] data = new byte[(int) bm.getBodyLength()];
            bm.readBytes(data);
            long position = bm.getLongProperty(PIECE_POSITION);
            String hash = bm.getStringProperty(PIECE_HASH);
            final Piece piece = new Piece(position, data);
            piece.setHash(hash);
            return piece;
        }

        return null;
    }

    public static Object fromMessage(Message message) throws JMSException {
        String type = message.getStringProperty(MESSAGETYPE);
        if (MESSAGETYPE_PIECE.equals(type))
            return fromPieceMessage(message);
        if (MESSAGETYPE_RESPONSE.equals(type))
            return fromResponseMessage(message);
        if (MESSAGETYPE_REQUEST.equals(type))
            return fromRequestMessage(message);
        return null;
    }

}
