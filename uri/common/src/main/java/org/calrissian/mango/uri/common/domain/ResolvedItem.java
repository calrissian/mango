package org.calrissian.mango.uri.common.domain;


import com.google.common.net.MediaType;

import java.io.InputStream;
import java.util.Map;

public class ResolvedItem {

    MediaType contentType;
    Map<String,String> additionalHeaders;

    InputStream object;

    public ResolvedItem(MediaType contentType, Map<String, String> additionalHeaders, InputStream object) {
        this.contentType = contentType;
        this.additionalHeaders = additionalHeaders;
        this.object = object;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders;
    }

    public InputStream getObject() {
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResolvedItem)) return false;

        ResolvedItem that = (ResolvedItem) o;

        if (additionalHeaders != null ? !additionalHeaders.equals(that.additionalHeaders) : that.additionalHeaders != null)
            return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
        if (object != null ? !object.equals(that.object) : that.object != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + (additionalHeaders != null ? additionalHeaders.hashCode() : 0);
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResolvedItem{" +
                "contentType=" + contentType +
                ", additionalHeaders=" + additionalHeaders +
                ", object=" + object +
                '}';
    }
}
