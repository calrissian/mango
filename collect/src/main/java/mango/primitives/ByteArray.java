package com.texeltek.mango.primitives;

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

        if (!Arrays.equals(byteArray, byteArray1.byteArray)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return byteArray != null ? Arrays.hashCode(byteArray) : 0;
    }
}
