package mango.uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

//TODO: Implement KRYO for stream compression
public interface UriResolver<T> {

    String getServiceName();

    /**
     * Null should be returned if uri couldn't be resolved or item referenced doesn't exist
     * @param uri
     * @param auths
     * @return
     */
    T resolveUri(URI uri, String[] auths);

    T fromStream(InputStream is) throws IOException;

    InputStream toStream(T obj) throws IOException;
}
