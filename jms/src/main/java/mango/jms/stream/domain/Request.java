package mango.jms.stream.domain;

import java.io.Serializable;
import java.util.UUID;

public class Request implements Serializable{

    private static final long serialVersionUID = 3835642167224689315L;

    private String downloadUri;

    private String requestId;

    public Request(String downloadUri) {
        this(downloadUri, UUID.randomUUID().toString());
    }

    public Request(String downloadUrl, String requestId) {
        this.downloadUri = downloadUrl;
        this.requestId = requestId;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public String getRequestId() {
        return requestId;
    }
}
