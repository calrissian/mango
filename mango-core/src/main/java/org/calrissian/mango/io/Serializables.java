package org.calrissian.mango.io;

import java.io.*;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class Serializables {

    private Serializables() {}

    public static byte[] serialize(Serializable serializable) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream  oos = new ObjectOutputStream( baos );
        oos.writeObject(serializable);
        oos.flush();
        oos.close();
        return baos.toByteArray();
    }

    public static <T extends Serializable> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        T retVal = (T) ois.readObject();
        ois.close();
        return retVal;
    }

    public static final byte[] toBase64(Serializable serializable) throws IOException {
        return encodeBase64(serialize(serializable));
    }

    public static final <T extends Serializable>T fromBase64(byte[] bytes) throws IOException, ClassNotFoundException {
        return deserialize(decodeBase64(bytes));
    }
}
