package org.calrissian.mango.uri.transform;

public interface ContextTransformer<T> extends Transformable<T> {

    String getContextName();
}
