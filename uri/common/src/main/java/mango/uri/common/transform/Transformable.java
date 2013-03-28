package mango.uri.common.transform;

import com.google.common.net.MediaType;
import mango.uri.common.domain.ResolvedItem;
import mango.uri.common.exception.ContextTransformException;

public interface Transformable<T> {

    ResolvedItem transform(T obj) throws ContextTransformException;

    MediaType getMediaType(T obj) throws ContextTransformException;
}
