package mango.jms.stream.support.impl;

import mango.jms.stream.support.UrlStreamOpener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class BasicStreamOpener implements UrlStreamOpener {
    @Override
    public InputStream openStream(URL url) throws IOException {

        final URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }
}
