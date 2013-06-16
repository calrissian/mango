package org.calrissian.mango.types.exception;


public class TypeNormalizationException extends Exception {

    public TypeNormalizationException() {
    }

    public TypeNormalizationException(String s) {
        super(s);
    }

    public TypeNormalizationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TypeNormalizationException(Throwable throwable) {
        super(throwable);
    }
}
