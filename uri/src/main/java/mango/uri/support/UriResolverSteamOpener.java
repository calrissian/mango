package mango.uri.support;


import mango.jms.stream.support.UriStreamOpener;
import mango.uri.UriResolver;
import mango.uri.UriResolverContext;
import mango.uri.exception.BadUriException;
import mango.uri.exception.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class UriResolverSteamOpener implements UriStreamOpener {

    @Override
    public InputStream openStream(URI uri) throws IOException {

        try {
            String[] auths = DataResolverFormatUtils.extractAuthsFromUri(uri);
            URI requestURI = DataResolverFormatUtils.extractURIFromRequestURI(uri);

            UriResolver resolver = UriResolverContext.getInstance().getResolver(requestURI);

            if(resolver == null) {
                throw new BadUriException();
            }

            Object obj = resolver.resolveUri(requestURI, auths);

            if(obj != null) {
                return resolver.toStream(obj);
            }

            else {
                throw new ResourceNotFoundException();
            }
        }

        catch(Exception e){
            throw new IOException(e);
        }
    }
}
