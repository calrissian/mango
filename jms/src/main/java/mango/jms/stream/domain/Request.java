package mango.jms.stream.domain;

import java.io.Serializable;
import java.util.UUID;

public class Request implements Serializable{


    private static final long serialVersionUID = 3835642167224689315L;

    private String downloadUrl;

    private String requestId;

    public Request(String downloadUrl) {
        this(downloadUrl, UUID.randomUUID().toString());
    }

    public Request(String downloadUrl, String requestId) {
        this.downloadUrl = downloadUrl;
        this.requestId = requestId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getRequestId() {
        return requestId;
    }
}
