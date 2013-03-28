package mango.uri.common.support;

import org.apache.commons.lang.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

public class DataResolverFormatUtils {

    public static URI buildRequestURI(URI uri, String[] auths) throws URISyntaxException {

        return new URI(uri.toString() + DataResolverConstants.DELIM + StringUtils.join(auths, ","));
    }

    public static String extractTargetSystemFromUri(URI uri)
            throws URISyntaxException {

        return uri.getScheme();
    }

    public static String[] extractAuthsFromUri(URI uri) {

        return uri.toString().split(DataResolverConstants.DELIM)[1].split(",");
    }

    public static URI extractURIFromRequestURI(URI uri) throws URISyntaxException {

        return new URI(uri.toString().split(DataResolverConstants.DELIM)[0]);
    }
}
