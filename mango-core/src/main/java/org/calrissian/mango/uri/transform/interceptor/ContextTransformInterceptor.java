package org.calrissian.mango.uri.transform.interceptor;

import org.calrissian.mango.uri.transform.Transformable;

public interface ContextTransformInterceptor<T> extends Transformable<T> {

    Class intercepts();
}
