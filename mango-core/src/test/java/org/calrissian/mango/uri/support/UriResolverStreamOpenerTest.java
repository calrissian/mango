package org.calrissian.mango.uri.support;


import org.calrissian.mango.uri.UriResolver;
import org.calrissian.mango.uri.UriResolverContext;
import org.calrissian.mango.uri.resolver.BasicObjectUriResolver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class UriResolverStreamOpenerTest {


    UriStreamOpener streamOpener;
    UriResolver resolver;

    @Before
    public void setUp() {

        streamOpener = new UriResolverSteamOpener();

        resolver = new BasicObjectUriResolver<String>() {
            @Override
            public String getServiceName() {
                return "service";
            }

            @Override
            public String resolveUri(URI uri, String[] auths) {
                return "test";
            }
        };

        UriResolverContext.getInstance().addResolver(resolver);

    }

    @Test
    public void testOpenStream() throws IOException, URISyntaxException {

        URI uri = new URI("service://blah#A,B,C");

        InputStream is = streamOpener.openStream(uri);

        assertEquals("test", resolver.fromStream(is));
    }
}
