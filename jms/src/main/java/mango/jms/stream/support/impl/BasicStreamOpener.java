package mango.jms.stream.support.impl;

import mango.jms.stream.support.UriStreamOpener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;

public class BasicStreamOpener implements UriStreamOpener {
    @Override
    public InputStream openStream(URI uri) throws IOException {

        final URLConnection connection = uri.toURL().openConnection();
        return connection.getInputStream();
    }
}
