package org.calrissian.mango.jms.stream.domain;

import org.calrissian.mango.jms.stream.utils.MessageDigestUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Piece implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4560858818551813851L;

    private long position;

    private byte[] data;

    private String hash;

    public Piece(long position, byte[] data) {
        super();
        this.position = position;
        this.data = data;
    }

    public Piece(long position, byte[] data, String hashAlgorithm)
            throws NoSuchAlgorithmException {
        super();
        this.position = position;
        this.data = data;
        this.hash = MessageDigestUtils.getHashString(MessageDigest.getInstance(
                hashAlgorithm).digest(data));
    }

    public long getPosition() {
        return position;
    }

    public byte[] getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
