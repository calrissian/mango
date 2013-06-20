package org.calrissian.mango.uri.support;

import org.calrissian.mango.uri.UriResolver;
import org.calrissian.mango.uri.UriResolverContext;
import org.calrissian.mango.uri.exception.BadUriException;
import org.calrissian.mango.uri.exception.ResourceNotFoundException;

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
