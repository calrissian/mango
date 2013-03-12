package mango.types.normalizers;

import mango.types.TypeNormalizer;
import mango.types.exception.TypeNormalizationException;

import java.net.URI;
import java.net.URISyntaxException;


public class URINormalizer implements TypeNormalizer<URI> {

    @Override
    public String normalize(URI obj) throws TypeNormalizationException {
        return obj.toString();
    }

    @Override
    public URI denormalize(String str) throws TypeNormalizationException {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String getAlias() {
        return "uri";
    }

    @Override
    public Class resolves() {
        return URI.class;
    }

    @Override
    public URI fromString(String str) throws TypeNormalizationException {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String asString(URI obj) throws TypeNormalizationException {
        return obj.toString();
    }
}
