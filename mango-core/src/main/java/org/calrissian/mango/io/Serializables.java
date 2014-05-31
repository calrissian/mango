package org.calrissian.mango.io;

import java.io.*;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class Serializables {

    private Serializables() {}

    public static final byte[] toBase64(Serializable serializable) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream  oos = new ObjectOutputStream( baos );
        oos.writeObject(serializable);
        return encodeBase64(baos.toByteArray());
    }

    public static final <T extends Serializable>T fromBase64(byte[] bytes) throws IOException, ClassNotFoundException {

        byte[] obj = decodeBase64(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(obj);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (T) ois.readObject();
    }
}
