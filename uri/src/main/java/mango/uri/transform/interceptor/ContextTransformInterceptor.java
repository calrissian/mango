package mango.uri.transform.interceptor;

import mango.uri.transform.Transformable;

public interface ContextTransformInterceptor<T> extends Transformable<T> {

    Class intercepts();
}
