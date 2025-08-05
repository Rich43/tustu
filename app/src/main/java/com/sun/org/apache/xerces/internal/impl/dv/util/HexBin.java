package com.sun.org.apache.xerces.internal.impl.dv.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/util/HexBin.class */
public final class HexBin {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 16;
    private static final byte[] hexNumberTable = new byte[128];
    private static final char[] lookUpHexAlphabet = new char[16];

    static {
        for (int i2 = 0; i2 < 128; i2++) {
            hexNumberTable[i2] = -1;
        }
        for (int i3 = 57; i3 >= 48; i3--) {
            hexNumberTable[i3] = (byte) (i3 - 48);
        }
        for (int i4 = 70; i4 >= 65; i4--) {
            hexNumberTable[i4] = (byte) ((i4 - 65) + 10);
        }
        for (int i5 = 102; i5 >= 97; i5--) {
            hexNumberTable[i5] = (byte) ((i5 - 97) + 10);
        }
        for (int i6 = 0; i6 < 10; i6++) {
            lookUpHexAlphabet[i6] = (char) (48 + i6);
        }
        for (int i7 = 10; i7 <= 15; i7++) {
            lookUpHexAlphabet[i7] = (char) ((65 + i7) - 10);
        }
    }

    public static String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }
        int lengthData = binaryData.length;
        int lengthEncode = lengthData * 2;
        char[] encodedData = new char[lengthEncode];
        for (int i2 = 0; i2 < lengthData; i2++) {
            int temp = binaryData[i2];
            if (temp < 0) {
                temp += 256;
            }
            encodedData[i2 * 2] = lookUpHexAlphabet[temp >> 4];
            encodedData[(i2 * 2) + 1] = lookUpHexAlphabet[temp & 15];
        }
        return new String(encodedData);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        int lengthData = encoded.length();
        if (lengthData % 2 != 0) {
            return null;
        }
        char[] binaryData = encoded.toCharArray();
        int lengthDecode = lengthData / 2;
        byte[] decodedData = new byte[lengthDecode];
        for (int i2 = 0; i2 < lengthDecode; i2++) {
            char tempChar = binaryData[i2 * 2];
            byte temp1 = tempChar < 128 ? hexNumberTable[tempChar] : (byte) -1;
            if (temp1 == -1) {
                return null;
            }
            char tempChar2 = binaryData[(i2 * 2) + 1];
            byte temp2 = tempChar2 < 128 ? hexNumberTable[tempChar2] : (byte) -1;
            if (temp2 == -1) {
                return null;
            }
            decodedData[i2] = (byte) ((temp1 << 4) | temp2);
        }
        return decodedData;
    }
}
