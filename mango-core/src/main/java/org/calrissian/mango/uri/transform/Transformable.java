package org.calrissian.mango.uri.transform;

import com.google.common.net.MediaType;
import org.calrissian.mango.uri.domain.ResolvedItem;
import org.calrissian.mango.uri.exception.ContextTransformException;

public interface Transformable<T> {

    ResolvedItem transform(T obj) throws ContextTransformException;

    MediaType getMediaType(T obj) throws ContextTransformException;
}
