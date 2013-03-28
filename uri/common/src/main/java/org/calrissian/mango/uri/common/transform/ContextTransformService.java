package org.calrissian.mango.uri.common.transform;

import com.google.common.net.MediaType;
import org.calrissian.mango.uri.common.domain.ResolvedItem;
import org.calrissian.mango.uri.common.exception.ContextTransformException;
import org.calrissian.mango.uri.common.transform.interceptor.ContextTransformInterceptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContextTransformService {

    Map<String, ContextTransformer> contextTransformMap = new HashMap<String,ContextTransformer>();
    Map<Class, ContextTransformInterceptor> transformInterceptorMap = new HashMap<Class,ContextTransformInterceptor>();

    public ContextTransformService(Collection<ContextTransformer> contextTransforms,
                                   Collection<ContextTransformInterceptor> contextTransformInterceptors) {

        if(contextTransforms != null) {
            for(ContextTransformer transform : contextTransforms) {
                contextTransformMap.put(transform.getContextName(), transform);
            }
        }

        if(contextTransformInterceptors != null) {
            for(ContextTransformInterceptor interceptor : contextTransformInterceptors) {
                transformInterceptorMap.put(interceptor.intercepts(), interceptor);
            }
        }

        System.out.println(contextTransforms);
        System.out.println(contextTransformInterceptors);
    }

    public ResolvedItem transform(String contextName, Object obj) throws ContextTransformException {

        ContextTransformInterceptor interceptor = transformInterceptorMap.get(obj.getClass());

        if(interceptor != null) {

            return interceptor.transform(obj);
        }

        else {

            ContextTransformer transform = contextTransformMap.get(contextName);
            return transform.transform(obj);
        }
    }

    public MediaType getMediaType(String contextName, Object obj) throws ContextTransformException {

        ContextTransformInterceptor interceptor = transformInterceptorMap.get(obj.getClass());

        if(interceptor != null) {

            return interceptor.getMediaType(obj);
        }

        else {

            ContextTransformer transform = contextTransformMap.get(contextName);
            return transform.getMediaType(obj);
        }

    }
}
