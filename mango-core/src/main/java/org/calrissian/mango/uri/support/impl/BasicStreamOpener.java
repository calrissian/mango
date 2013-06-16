package org.calrissian.mango.uri.support.impl;

import org.calrissian.mango.uri.support.UriStreamOpener;

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
