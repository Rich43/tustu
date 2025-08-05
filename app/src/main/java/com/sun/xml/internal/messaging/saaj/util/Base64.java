package com.sun.xml.internal.messaging.saaj.util;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/Base64.class */
public final class Base64 {
    private static final int BASELENGTH = 255;
    private static final int LOOKUPLENGTH = 63;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int SIXBIT = 6;
    private static final int FOURBYTE = 4;
    private static final byte PAD = 61;
    private static byte[] base64Alphabet = new byte[255];
    private static byte[] lookUpBase64Alphabet = new byte[63];
    static final int[] base64;

    static {
        for (int i2 = 0; i2 < 255; i2++) {
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
            lookUpBase64Alphabet[i6] = (byte) (65 + i6);
        }
        int i7 = 26;
        int j2 = 0;
        while (i7 <= 51) {
            lookUpBase64Alphabet[i7] = (byte) (97 + j2);
            i7++;
            j2++;
        }
        int i8 = 52;
        int j3 = 0;
        while (i8 <= 61) {
            lookUpBase64Alphabet[i8] = (byte) (48 + j3);
            i8++;
            j3++;
        }
        base64 = new int[]{64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 62, 64, 64, 64, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 64, 64, 64, 64, 64, 64, 64, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 64, 64, 64, 64, 64, 64, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64};
    }

    static boolean isBase64(byte octect) {
        return octect == 61 || base64Alphabet[octect] != -1;
    }

    static boolean isArrayByteBase64(byte[] arrayOctect) {
        int length = arrayOctect.length;
        if (length == 0) {
            return false;
        }
        for (byte b2 : arrayOctect) {
            if (!isBase64(b2)) {
                return false;
            }
        }
        return true;
    }

    public static byte[] encode(byte[] binaryData) {
        byte[] encodedData;
        int lengthDataBits = binaryData.length * 8;
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        if (fewerThan24bits != 0) {
            encodedData = new byte[(numberTriplets + 1) * 4];
        } else {
            encodedData = new byte[numberTriplets * 4];
        }
        int i2 = 0;
        while (i2 < numberTriplets) {
            int dataIndex = i2 * 3;
            byte b1 = binaryData[dataIndex];
            byte b2 = binaryData[dataIndex + 1];
            byte b3 = binaryData[dataIndex + 2];
            byte l2 = (byte) (b2 & 15);
            byte k2 = (byte) (b1 & 3);
            int encodedIndex = i2 * 4;
            encodedData[encodedIndex] = lookUpBase64Alphabet[b1 >> 2];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[(b2 >> 4) | (k2 << 4)];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[(l2 << 2) | (b3 >> 6)];
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 63];
            i2++;
        }
        int dataIndex2 = i2 * 3;
        int encodedIndex2 = i2 * 4;
        if (fewerThan24bits == 8) {
            byte b12 = binaryData[dataIndex2];
            byte k3 = (byte) (b12 & 3);
            encodedData[encodedIndex2] = lookUpBase64Alphabet[b12 >> 2];
            encodedData[encodedIndex2 + 1] = lookUpBase64Alphabet[k3 << 4];
            encodedData[encodedIndex2 + 2] = 61;
            encodedData[encodedIndex2 + 3] = 61;
        } else if (fewerThan24bits == 16) {
            byte b13 = binaryData[dataIndex2];
            byte b22 = binaryData[dataIndex2 + 1];
            byte l3 = (byte) (b22 & 15);
            byte k4 = (byte) (b13 & 3);
            encodedData[encodedIndex2] = lookUpBase64Alphabet[b13 >> 2];
            encodedData[encodedIndex2 + 1] = lookUpBase64Alphabet[(b22 >> 4) | (k4 << 4)];
            encodedData[encodedIndex2 + 2] = lookUpBase64Alphabet[l3 << 2];
            encodedData[encodedIndex2 + 3] = 61;
        }
        return encodedData;
    }

    public byte[] decode(byte[] base64Data) {
        int numberQuadruple = base64Data.length / 4;
        int encodedIndex = 0;
        byte[] decodedData = new byte[(numberQuadruple * 3) + 1];
        for (int i2 = 0; i2 < numberQuadruple; i2++) {
            int dataIndex = i2 * 4;
            byte marker0 = base64Data[dataIndex + 2];
            byte marker1 = base64Data[dataIndex + 3];
            byte b1 = base64Alphabet[base64Data[dataIndex]];
            byte b2 = base64Alphabet[base64Data[dataIndex + 1]];
            if (marker0 != 61 && marker1 != 61) {
                byte b3 = base64Alphabet[marker0];
                byte b4 = base64Alphabet[marker1];
                decodedData[encodedIndex] = (byte) ((b1 << 2) | (b2 >> 4));
                decodedData[encodedIndex + 1] = (byte) (((b2 & 15) << 4) | ((b3 >> 2) & 15));
                decodedData[encodedIndex + 2] = (byte) ((b3 << 6) | b4);
            } else if (marker0 == 61) {
                decodedData[encodedIndex] = (byte) ((b1 << 2) | (b2 >> 4));
                decodedData[encodedIndex + 1] = (byte) ((b2 & 15) << 4);
                decodedData[encodedIndex + 2] = 0;
            } else if (marker1 == 61) {
                byte b32 = base64Alphabet[marker0];
                decodedData[encodedIndex] = (byte) ((b1 << 2) | (b2 >> 4));
                decodedData[encodedIndex + 1] = (byte) (((b2 & 15) << 4) | ((b32 >> 2) & 15));
                decodedData[encodedIndex + 2] = (byte) (b32 << 6);
            }
            encodedIndex += 3;
        }
        return decodedData;
    }

    public static String base64Decode(String orig) {
        char[] chars = orig.toCharArray();
        StringBuffer sb = new StringBuffer();
        int shift = 0;
        int acc = 0;
        for (int i2 = 0; i2 < chars.length; i2++) {
            int v2 = base64[chars[i2] & 255];
            if (v2 >= 64) {
                if (chars[i2] != '=') {
                    System.out.println("Wrong char in base64: " + chars[i2]);
                }
            } else {
                acc = (acc << 6) | v2;
                shift += 6;
                if (shift >= 8) {
                    shift -= 8;
                    sb.append((char) ((acc >> shift) & 255));
                }
            }
        }
        return sb.toString();
    }
}
