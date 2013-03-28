package org.calrissian.mango.uri.jms;

import org.calrissian.mango.jms.stream.JmsFileReceiver;
import org.calrissian.mango.jms.stream.JmsFileTransferException;
import org.calrissian.mango.uri.common.UriResolver;
import org.calrissian.mango.uri.common.UriResolverContext;
import org.calrissian.mango.uri.common.exception.BadUriException;
import org.calrissian.mango.uri.common.exception.ResourceNotFoundException;
import org.calrissian.mango.uri.common.support.DataResolverFormatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class JmsUriReceiver extends JmsFileReceiver {

    public Object resolveUri(String uri, String[] auths) throws JmsFileTransferException, IOException {

        try {

            URI theUri = new URI(uri);
            URI requestURI = new URI(theUri.toString().replaceFirst(theUri.getScheme() + ":", ""));

            UriResolver resolver = UriResolverContext.getInstance().getResolver(requestURI);

            if(resolver == null) {
                throw new BadUriException();
            }

            URI uriWithAuths = DataResolverFormatUtils.buildRequestURI(theUri, auths);
            InputStream is = receiveStream(uriWithAuths.toString());

            if(is == null) {
                throw new ResourceNotFoundException();
            }

            return resolver.fromStream(is);

        } catch (URISyntaxException e) {

            throw new BadUriException(e);
        }
    }

}