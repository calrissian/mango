package org.calrissian.mango.primitives;

import java.util.Arrays;

/**
 */
public class ByteArray {
    private byte[] byteArray;

    public ByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByteArray byteArray1 = (ByteArray) o;

        return Arrays.equals(byteArray, byteArray1.byteArray);

    }

    @Override
    public int hashCode() {
        return byteArray != null ? Arrays.hashCode(byteArray) : 0;
    }
}
