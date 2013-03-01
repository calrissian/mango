package mango.jms.stream.support;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface UrlStreamOpener {

    InputStream openStream(URL url) throws IOException;
}
