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
package org.calrissian.mango.jms.uri;

import org.calrissian.mango.jms.stream.JmsFileReceiver;
import org.calrissian.mango.uri.UriResolver;
import org.calrissian.mango.uri.UriResolverRegistry;
import org.calrissian.mango.uri.exception.BadUriException;
import org.calrissian.mango.uri.exception.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.uri.support.DataResolverFormatUtils.buildRequestURI;

public class JmsUriReceiver extends JmsFileReceiver {

    private final UriResolverRegistry resolverRegistry;

    public JmsUriReceiver(UriResolverRegistry resolverRegistry) {
        checkNotNull(resolverRegistry);
        this.resolverRegistry = resolverRegistry;
    }

    public Object resolveUri(String uri, String[] auths) throws IOException {

        try {

            URI theUri = new URI(uri);
            URI requestURI = new URI(theUri.toString().replaceFirst(theUri.getScheme() + ":", ""));

            UriResolver resolver = resolverRegistry.getResolver(requestURI);

            if(resolver == null)
                throw new BadUriException();

            URI uriWithAuths = buildRequestURI(theUri, auths);
            InputStream is = receiveStream(uriWithAuths.toString());

            if(is == null)
                throw new ResourceNotFoundException();


            return resolver.fromStream(is);

        } catch (URISyntaxException e) {
            throw new BadUriException(e);
        }
    }

}