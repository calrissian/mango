package org.calrissian.mango.jms.stream.domain;

import java.io.Serializable;

public class Response implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7060544519722574339L;

    private ResponseStatusEnum status;

    private String hash;

    public Response(ResponseStatusEnum status) {
        super();
        this.status = status;
    }

    public ResponseStatusEnum getStatus() {
        return status;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
