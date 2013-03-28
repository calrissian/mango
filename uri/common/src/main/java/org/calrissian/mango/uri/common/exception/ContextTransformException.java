package org.calrissian.mango.uri.common.exception;

public class ContextTransformException extends Exception {
    public ContextTransformException() {
    }

    public ContextTransformException(String s) {
        super(s);
    }

    public ContextTransformException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ContextTransformException(Throwable throwable) {
        super(throwable);
    }
}
