/*
 * Copyright (C) 2013 The Calrissian Authors
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
package org.calrissian.mango.jms.stream.domain;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Piece implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4560858818551813851L;

    private long position;

    private byte[] data;

    private String hash;

    public Piece(long position, byte[] data) {
        super();
        this.position = position;
        this.data = data;
    }

    public Piece(long position, byte[] data, String hashAlgorithm)
            throws NoSuchAlgorithmException {
        super();
        this.position = position;
        this.data = data;
        this.hash = new String(MessageDigest.getInstance(
                hashAlgorithm).digest(data));
    }

    public long getPosition() {
        return position;
    }

    public byte[] getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
