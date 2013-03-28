package mango.uri.transform;

import com.google.common.net.MediaType;
import mango.uri.domain.ResolvedItem;
import mango.uri.exception.ContextTransformException;

public interface Transformable<T> {

    ResolvedItem transform(T obj) throws ContextTransformException;

    MediaType getMediaType(T obj) throws ContextTransformException;
}
