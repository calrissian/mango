package org.calrissian.mango.uri.support;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface UriStreamOpener {

    InputStream openStream(URI uri) throws IOException;
}
