package mango.jms.stream.utils;

public class MessageDigestUtils {

    protected MessageDigestUtils() {

    }

    public static String getHashString(byte[] hash) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : hash) {
            buffer.append(b);
        }
        return buffer.toString();
    }
}
