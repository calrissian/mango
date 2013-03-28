package mango.uri.common.resolver;


import mango.uri.common.UriResolver;

import java.io.*;

public abstract class BasicObjectUriResolver<T> implements UriResolver<T> {


    @Override
    public T fromStream(InputStream is) throws IOException {

        ObjectInputStream ois = new ObjectInputStream(is);

        try {
            return (T)ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    @Override
    public InputStream toStream(T obj) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(obj);
        oos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        return bais;
    }
}
