package org.calrissian.mango.hash.support;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    /**
     * Turns a string into an MD5 hash
     * @param itemToHash
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String hashString(String itemToHash) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        return new String(hashBytes(itemToHash.getBytes("UTF-8")));
    }

    /**
     * Turns a byte array into an MD5 hash
     * @param bytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static byte[] hashBytes(byte[] bytes) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytes);
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i < thedigest.length; i++) {
            String hex=Integer.toHexString(0xff & thedigest[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString().getBytes();
    }
}
