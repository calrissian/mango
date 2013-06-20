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
package org.calrissian.mango.uri;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * A singleton context by which to register a number of URI Resolvers that can be recalled given a URI.
 * By convention, the scheme of the URI determines the service upon which honors the request.
 */
public class UriResolverContext {

    protected static UriResolverContext resolverService;

    public static synchronized UriResolverContext getInstance() {
        if(resolverService == null) {
            resolverService = new UriResolverContext();
        }

        return resolverService;
    }

    protected Map<String, UriResolver> resolverMap = new HashMap<String, UriResolver>();

    public void addResolver(UriResolver resolver) {

        resolverMap.put(resolver.getServiceName(), resolver);
    }

    public UriResolver getResolver(URI uri) {

        String scheme = uri.getScheme();

        System.out.println(uri.getScheme());

        UriResolver resolver = resolverMap.get(scheme);

        return resolver;
    }
}
