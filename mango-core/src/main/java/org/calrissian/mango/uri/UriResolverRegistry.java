/*
 * Copyright (C) 2014 The Calrissian Authors
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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A utility class by which to register a number of URI Resolvers that can be recalled given a URI.
 * By convention, the scheme of the URI determines the service upon which honors the request.
 */
public class UriResolverRegistry {

    @SuppressWarnings("rawtypes")
    private final Map<String, UriResolver> resolverMap = new LinkedHashMap<>();

    @SuppressWarnings("rawtypes")
    public UriResolverRegistry addResolver(UriResolver resolver) {
        resolverMap.put(resolver.getServiceName(), resolver);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public UriResolver getResolver(URI uri) {
        return resolverMap.get(uri.getScheme());
    }
}
