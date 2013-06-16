package org.calrissian.mango.accumulo.exception;

/**
 */
public class AlreadyClosedException extends RuntimeException{

    public AlreadyClosedException() {
    }

    public AlreadyClosedException(String s) {
        super(s);
    }

    public AlreadyClosedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AlreadyClosedException(Throwable throwable) {
        super(throwable);
    }
}
