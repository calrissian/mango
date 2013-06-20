/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
