package org.calrissian.mango.uri.common.transform.interceptor;

import org.calrissian.mango.uri.common.transform.Transformable;

public interface ContextTransformInterceptor<T> extends Transformable<T> {

    Class intercepts();
}
