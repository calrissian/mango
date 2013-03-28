package mango.uri.common.transform.interceptor;

import mango.uri.common.transform.Transformable;

public interface ContextTransformInterceptor<T> extends Transformable<T> {

    Class intercepts();
}
