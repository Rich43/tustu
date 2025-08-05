package com.sun.org.apache.xerces.internal.impl.dv.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/util/Base64.class */
public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int SIXBIT = 6;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final boolean fDebug = false;
    private static final byte[] base64Alphabet = new byte[128];
    private static final char[] lookUpBase64Alphabet = new char[64];

    static {
        for (int i2 = 0; i2 < 128; i2++) {
            base64Alphabet[i2] = -1;
        }
        for (int i3 = 90; i3 >= 65; i3--) {
            base64Alphabet[i3] = (byte) (i3 - 65);
        }
        for (int i4 = 122; i4 >= 97; i4--) {
            base64Alphabet[i4] = (byte) ((i4 - 97) + 26);
        }
        for (int i5 = 57; i5 >= 48; i5--) {
            base64Alphabet[i5] = (byte) ((i5 - 48) + 52);
        }
        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;
        for (int i6 = 0; i6 <= 25; i6++) {
            lookUpBase64Alphabet[i6] = (char) (65 + i6);
        }
        int i7 = 26;
        int j2 = 0;
        while (i7 <= 51) {
            lookUpBase64Alphabet[i7] = (char) (97 + j2);
            i7++;
            j2++;
        }
        int i8 = 52;
        int j3 = 0;
        while (i8 <= 61) {
            lookUpBase64Alphabet[i8] = (char) (48 + j3);
            i8++;
            j3++;
        }
        lookUpBase64Alphabet[62] = '+';
        lookUpBase64Alphabet[63] = '/';
    }

    protected static boolean isWhiteSpace(char octect) {
        return octect == ' ' || octect == '\r' || octect == '\n' || octect == '\t';
    }

    protected static boolean isPad(char octect) {
        return octect == '=';
    }

    protected static boolean isData(char octect) {
        return octect < 128 && base64Alphabet[octect] != -1;
    }

    protected static boolean isBase64(char octect) {
        return isWhiteSpace(octect) || isPad(octect) || isData(octect);
    }

    public static String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }
        int lengthDataBits = binaryData.length * 8;
        if (lengthDataBits == 0) {
            return "";
        }
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char[] encodedData = new char[numberQuartet * 4];
        int encodedIndex = 0;
        int dataIndex = 0;
        for (int i2 = 0; i2 < numberTriplets; i2++) {
            int i3 = dataIndex;
            int dataIndex2 = dataIndex + 1;
            byte b1 = binaryData[i3];
            int dataIndex3 = dataIndex2 + 1;
            byte b2 = binaryData[dataIndex2];
            dataIndex = dataIndex3 + 1;
            byte b3 = binaryData[dataIndex3];
            byte l2 = (byte) (b2 & 15);
            byte k2 = (byte) (b1 & 3);
            byte val1 = (b1 & Byte.MIN_VALUE) == 0 ? (byte) (b1 >> 2) : (byte) ((b1 >> 2) ^ 192);
            byte val2 = (b2 & Byte.MIN_VALUE) == 0 ? (byte) (b2 >> 4) : (byte) ((b2 >> 4) ^ 240);
            byte val3 = (byte) ((b3 & Byte.MIN_VALUE) == 0 ? b3 >> 6 : (b3 >> 6) ^ 252);
            int i4 = encodedIndex;
            int encodedIndex2 = encodedIndex + 1;
            encodedData[i4] = lookUpBase64Alphabet[val1];
            int encodedIndex3 = encodedIndex2 + 1;
            encodedData[encodedIndex2] = lookUpBase64Alphabet[val2 | (k2 << 4)];
            int encodedIndex4 = encodedIndex3 + 1;
            encodedData[encodedIndex3] = lookUpBase64Alphabet[(l2 << 2) | val3];
            encodedIndex = encodedIndex4 + 1;
            encodedData[encodedIndex4] = lookUpBase64Alphabet[b3 & 63];
        }
        if (fewerThan24bits == 8) {
            byte b12 = binaryData[dataIndex];
            byte k3 = (byte) (b12 & 3);
            byte val12 = (b12 & Byte.MIN_VALUE) == 0 ? (byte) (b12 >> 2) : (byte) ((b12 >> 2) ^ 192);
            int i5 = encodedIndex;
            int encodedIndex5 = encodedIndex + 1;
            encodedData[i5] = lookUpBase64Alphabet[val12];
            int encodedIndex6 = encodedIndex5 + 1;
            encodedData[encodedIndex5] = lookUpBase64Alphabet[k3 << 4];
            int encodedIndex7 = encodedIndex6 + 1;
            encodedData[encodedIndex6] = '=';
            int i6 = encodedIndex7 + 1;
            encodedData[encodedIndex7] = '=';
        } else if (fewerThan24bits == 16) {
            byte b13 = binaryData[dataIndex];
            byte b22 = binaryData[dataIndex + 1];
            byte l3 = (byte) (b22 & 15);
            byte k4 = (byte) (b13 & 3);
            byte val13 = (b13 & Byte.MIN_VALUE) == 0 ? (byte) (b13 >> 2) : (byte) ((b13 >> 2) ^ 192);
            byte val22 = (b22 & Byte.MIN_VALUE) == 0 ? (byte) (b22 >> 4) : (byte) ((b22 >> 4) ^ 240);
            int i7 = encodedIndex;
            int encodedIndex8 = encodedIndex + 1;
            encodedData[i7] = lookUpBase64Alphabet[val13];
            int encodedIndex9 = encodedIndex8 + 1;
            encodedData[encodedIndex8] = lookUpBase64Alphabet[val22 | (k4 << 4)];
            int encodedIndex10 = encodedIndex9 + 1;
            encodedData[encodedIndex9] = lookUpBase64Alphabet[l3 << 2];
            int i8 = encodedIndex10 + 1;
            encodedData[encodedIndex10] = '=';
        }
        return new String(encodedData);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        char[] base64Data = encoded.toCharArray();
        int len = removeWhiteSpace(base64Data);
        if (len % 4 != 0) {
            return null;
        }
        int numberQuadruple = len / 4;
        if (numberQuadruple == 0) {
            return new byte[0];
        }
        int i2 = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        byte[] decodedData = new byte[numberQuadruple * 3];
        while (i2 < numberQuadruple - 1) {
            int i3 = dataIndex;
            int dataIndex2 = dataIndex + 1;
            char d1 = base64Data[i3];
            if (isData(d1)) {
                int dataIndex3 = dataIndex2 + 1;
                char d2 = base64Data[dataIndex2];
                if (isData(d2)) {
                    int dataIndex4 = dataIndex3 + 1;
                    char d3 = base64Data[dataIndex3];
                    if (isData(d3)) {
                        dataIndex = dataIndex4 + 1;
                        char d4 = base64Data[dataIndex4];
                        if (!isData(d4)) {
                            return null;
                        }
                        byte b1 = base64Alphabet[d1];
                        byte b2 = base64Alphabet[d2];
                        byte b3 = base64Alphabet[d3];
                        byte b4 = base64Alphabet[d4];
                        int i4 = encodedIndex;
                        int encodedIndex2 = encodedIndex + 1;
                        decodedData[i4] = (byte) ((b1 << 2) | (b2 >> 4));
                        int encodedIndex3 = encodedIndex2 + 1;
                        decodedData[encodedIndex2] = (byte) (((b2 & 15) << 4) | ((b3 >> 2) & 15));
                        encodedIndex = encodedIndex3 + 1;
                        decodedData[encodedIndex3] = (byte) ((b3 << 6) | b4);
                        i2++;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        int i5 = dataIndex;
        int dataIndex5 = dataIndex + 1;
        char d12 = base64Data[i5];
        if (isData(d12)) {
            int dataIndex6 = dataIndex5 + 1;
            char d22 = base64Data[dataIndex5];
            if (!isData(d22)) {
                return null;
            }
            byte b12 = base64Alphabet[d12];
            byte b22 = base64Alphabet[d22];
            int dataIndex7 = dataIndex6 + 1;
            char d32 = base64Data[dataIndex6];
            int i6 = dataIndex7 + 1;
            char d42 = base64Data[dataIndex7];
            if (!isData(d32) || !isData(d42)) {
                if (isPad(d32) && isPad(d42)) {
                    if ((b22 & 15) != 0) {
                        return null;
                    }
                    byte[] tmp = new byte[(i2 * 3) + 1];
                    System.arraycopy(decodedData, 0, tmp, 0, i2 * 3);
                    tmp[encodedIndex] = (byte) ((b12 << 2) | (b22 >> 4));
                    return tmp;
                }
                if (!isPad(d32) && isPad(d42)) {
                    byte b32 = base64Alphabet[d32];
                    if ((b32 & 3) != 0) {
                        return null;
                    }
                    byte[] tmp2 = new byte[(i2 * 3) + 2];
                    System.arraycopy(decodedData, 0, tmp2, 0, i2 * 3);
                    tmp2[encodedIndex] = (byte) ((b12 << 2) | (b22 >> 4));
                    tmp2[encodedIndex + 1] = (byte) (((b22 & 15) << 4) | ((b32 >> 2) & 15));
                    return tmp2;
                }
                return null;
            }
            byte b33 = base64Alphabet[d32];
            byte b42 = base64Alphabet[d42];
            int i7 = encodedIndex;
            int encodedIndex4 = encodedIndex + 1;
            decodedData[i7] = (byte) ((b12 << 2) | (b22 >> 4));
            int encodedIndex5 = encodedIndex4 + 1;
            decodedData[encodedIndex4] = (byte) (((b22 & 15) << 4) | ((b33 >> 2) & 15));
            int i8 = encodedIndex5 + 1;
            decodedData[encodedIndex5] = (byte) ((b33 << 6) | b42);
            return decodedData;
        }
        return null;
    }

    protected static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }
        int newSize = 0;
        int len = data.length;
        for (int i2 = 0; i2 < len; i2++) {
            if (!isWhiteSpace(data[i2])) {
                int i3 = newSize;
                newSize++;
                data[i3] = data[i2];
            }
        }
        return newSize;
    }
}
