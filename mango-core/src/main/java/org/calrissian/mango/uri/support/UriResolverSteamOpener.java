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
import org.calrissian.mango.uri.UriResolverRegistry;
import org.calrissian.mango.uri.exception.BadUriException;
import org.calrissian.mango.uri.exception.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.uri.support.DataResolverFormatUtils.extractAuthsFromUri;
import static org.calrissian.mango.uri.support.DataResolverFormatUtils.extractURIFromRequestURI;

public class UriResolverSteamOpener implements UriStreamOpener {

    private final UriResolverRegistry resolverRegistry;

    public UriResolverSteamOpener(UriResolverRegistry resolverRegistry) {
        checkNotNull(resolverRegistry);
        this.resolverRegistry = resolverRegistry;
    }

    @Override
    public InputStream openStream(URI uri) throws IOException {

        try {
            String[] auths = extractAuthsFromUri(uri);
            URI requestURI = extractURIFromRequestURI(uri);

            UriResolver resolver = resolverRegistry.getResolver(requestURI);

            if(resolver == null)
                throw new BadUriException();

            Object obj = resolver.resolveUri(requestURI, auths);

            if(obj == null)
                throw new ResourceNotFoundException();

            return resolver.toStream(obj);
        }

        catch(Exception e){
            throw new IOException(e);
        }
    }
}
