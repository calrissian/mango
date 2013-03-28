package mango.uri;

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
