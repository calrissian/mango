package org.calrissian.mango.uri.exception;

import java.io.IOException;


public class BadUriException extends IOException {
    public BadUriException() {
    }

    public BadUriException(String s) {
        super(s);
    }

    public BadUriException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BadUriException(Throwable throwable) {
        super(throwable);
    }
}
