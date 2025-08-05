package net.lingala.zip4j.crypto.PBKDF2;

import org.icepdf.core.util.PdfOps;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/PBKDF2/BinTools.class */
class BinTools {
    public static final String hex = "0123456789ABCDEF";

    BinTools() {
    }

    public static String bin2hex(byte[] b2) {
        if (b2 == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(2 * b2.length);
        for (byte b3 : b2) {
            int v2 = (256 + b3) % 256;
            sb.append(hex.charAt((v2 / 16) & 15));
            sb.append(hex.charAt((v2 % 16) & 15));
        }
        return sb.toString();
    }

    public static byte[] hex2bin(String s2) {
        String m2 = s2;
        if (s2 == null) {
            m2 = "";
        } else if (s2.length() % 2 != 0) {
            m2 = new StringBuffer("0").append(s2).toString();
        }
        byte[] r2 = new byte[m2.length() / 2];
        int i2 = 0;
        int n2 = 0;
        while (i2 < m2.length()) {
            int i3 = i2;
            int i4 = i2 + 1;
            char h2 = m2.charAt(i3);
            i2 = i4 + 1;
            char l2 = m2.charAt(i4);
            r2[n2] = (byte) ((hex2bin(h2) * 16) + hex2bin(l2));
            n2++;
        }
        return r2;
    }

    public static int hex2bin(char c2) {
        if (c2 >= '0' && c2 <= '9') {
            return c2 - '0';
        }
        if (c2 >= 'A' && c2 <= 'F') {
            return (c2 - 'A') + 10;
        }
        if (c2 >= 'a' && c2 <= 'f') {
            return (c2 - 'a') + 10;
        }
        throw new IllegalArgumentException(new StringBuffer("Input string may only contain hex digits, but found '").append(c2).append(PdfOps.SINGLE_QUOTE_TOKEN).toString());
    }
}
