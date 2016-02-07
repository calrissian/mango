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
package org.calrissian.mango.io;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class Serializables {

    private Serializables() {}

    public static byte[] serialize(Serializable serializable) throws IOException {
        return serialize(serializable, false);
    }

    public static byte [] serialize(Serializable serializable, boolean compress) throws IOException {
        checkNotNull(serializable);

        try (ByteArrayOutputStream o = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(compress ? new GZIPOutputStream(o) : o)) {
            oos.writeObject(serializable);
            oos.flush();
            oos.close();  //must close before getting bytes because GZip streams require it.
            return o.toByteArray();
        }
    }

    public static <T extends Serializable> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        return deserialize(bytes, false);
    }

    public static <T extends Serializable> T deserialize(byte[] bytes, boolean compressed) throws IOException, ClassNotFoundException {
        checkNotNull(bytes);

        try (ByteArrayInputStream i = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(compressed ? new GZIPInputStream(i) : i)) {
            return (T) ois.readObject();
        }
    }

    public static byte[] toBase64(Serializable serializable) throws IOException {
        return encodeBase64(serialize(serializable));
    }

    public static <T extends Serializable>T fromBase64(byte[] bytes) throws IOException, ClassNotFoundException {
        return deserialize(decodeBase64(bytes));
    }
}
