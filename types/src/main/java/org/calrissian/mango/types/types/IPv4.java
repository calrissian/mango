package org.calrissian.mango.types.types;


import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPv4 implements Serializable {

    final Long value;

    private static final String IP_ADDRESS = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

    public IPv4(String ip) {

        Pattern p = Pattern.compile(IP_ADDRESS);
        Matcher m = p.matcher(ip);

        if(!m.matches()) {
            throw new IllegalArgumentException("For input string: " + ip);
        }

        value = ipToLong(ip);
    }

    public IPv4(Long ip) {
        this.value = ip;
    }

    public Long getValue() {
        return value;
    }

    private long ipToLong(String addr) {
        String[] addrArray = addr.split("\\.");

        long num = 0;
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;

            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
        }

        return num;
    }

    public String toString() {

        return ((value >> 24) & 0xFF) + "." +
                ((value >> 16) & 0xFF) + "." +
                ((value >> 8) & 0xFF) + "." +
                (value & 0xFF);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPv4)) return false;

        IPv4 iPv4 = (IPv4) o;

        if (value != null ? !value.equals(iPv4.value) : iPv4.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
