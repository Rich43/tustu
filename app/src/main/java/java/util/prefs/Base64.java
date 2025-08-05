package java.util.prefs;

import java.util.Arrays;
import java.util.Random;

/* loaded from: rt.jar:java/util/prefs/Base64.class */
class Base64 {
    private static final char[] intToBase64 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final char[] intToAltBase64 = {'!', '\"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?'};
    private static final byte[] base64ToInt = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    private static final byte[] altBase64ToInt = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25};

    Base64() {
    }

    static String byteArrayToBase64(byte[] bArr) {
        return byteArrayToBase64(bArr, false);
    }

    static String byteArrayToAltBase64(byte[] bArr) {
        return byteArrayToBase64(bArr, true);
    }

    private static String byteArrayToBase64(byte[] bArr, boolean z2) {
        int length = bArr.length;
        int i2 = length / 3;
        int i3 = length - (3 * i2);
        StringBuffer stringBuffer = new StringBuffer(4 * ((length + 2) / 3));
        char[] cArr = z2 ? intToAltBase64 : intToBase64;
        int i4 = 0;
        for (int i5 = 0; i5 < i2; i5++) {
            int i6 = i4;
            int i7 = i4 + 1;
            int i8 = bArr[i6] & 255;
            int i9 = i7 + 1;
            int i10 = bArr[i7] & 255;
            i4 = i9 + 1;
            int i11 = bArr[i9] & 255;
            stringBuffer.append(cArr[i8 >> 2]);
            stringBuffer.append(cArr[((i8 << 4) & 63) | (i10 >> 4)]);
            stringBuffer.append(cArr[((i10 << 2) & 63) | (i11 >> 6)]);
            stringBuffer.append(cArr[i11 & 63]);
        }
        if (i3 != 0) {
            int i12 = i4;
            int i13 = i4 + 1;
            int i14 = bArr[i12] & 255;
            stringBuffer.append(cArr[i14 >> 2]);
            if (i3 == 1) {
                stringBuffer.append(cArr[(i14 << 4) & 63]);
                stringBuffer.append("==");
            } else {
                int i15 = i13 + 1;
                int i16 = bArr[i13] & 255;
                stringBuffer.append(cArr[((i14 << 4) & 63) | (i16 >> 4)]);
                stringBuffer.append(cArr[(i16 << 2) & 63]);
                stringBuffer.append('=');
            }
        }
        return stringBuffer.toString();
    }

    static byte[] base64ToByteArray(String str) {
        return base64ToByteArray(str, false);
    }

    static byte[] altBase64ToByteArray(String str) {
        return base64ToByteArray(str, true);
    }

    private static byte[] base64ToByteArray(String str, boolean z2) {
        byte[] bArr = z2 ? altBase64ToInt : base64ToInt;
        int length = str.length();
        int i2 = length / 4;
        if (4 * i2 != length) {
            throw new IllegalArgumentException("String length must be a multiple of four.");
        }
        int i3 = 0;
        int i4 = i2;
        if (length != 0) {
            if (str.charAt(length - 1) == '=') {
                i3 = 0 + 1;
                i4--;
            }
            if (str.charAt(length - 2) == '=') {
                i3++;
            }
        }
        byte[] bArr2 = new byte[(3 * i2) - i3];
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < i4; i7++) {
            int i8 = i5;
            int i9 = i5 + 1;
            int iBase64toInt = base64toInt(str.charAt(i8), bArr);
            int i10 = i9 + 1;
            int iBase64toInt2 = base64toInt(str.charAt(i9), bArr);
            int i11 = i10 + 1;
            int iBase64toInt3 = base64toInt(str.charAt(i10), bArr);
            i5 = i11 + 1;
            int iBase64toInt4 = base64toInt(str.charAt(i11), bArr);
            int i12 = i6;
            int i13 = i6 + 1;
            bArr2[i12] = (byte) ((iBase64toInt << 2) | (iBase64toInt2 >> 4));
            int i14 = i13 + 1;
            bArr2[i13] = (byte) ((iBase64toInt2 << 4) | (iBase64toInt3 >> 2));
            i6 = i14 + 1;
            bArr2[i14] = (byte) ((iBase64toInt3 << 6) | iBase64toInt4);
        }
        if (i3 != 0) {
            int i15 = i5;
            int i16 = i5 + 1;
            int iBase64toInt5 = base64toInt(str.charAt(i15), bArr);
            int i17 = i16 + 1;
            int iBase64toInt6 = base64toInt(str.charAt(i16), bArr);
            int i18 = i6;
            int i19 = i6 + 1;
            bArr2[i18] = (byte) ((iBase64toInt5 << 2) | (iBase64toInt6 >> 4));
            if (i3 == 1) {
                int i20 = i17 + 1;
                int i21 = i19 + 1;
                bArr2[i19] = (byte) ((iBase64toInt6 << 4) | (base64toInt(str.charAt(i17), bArr) >> 2));
            }
        }
        return bArr2;
    }

    private static int base64toInt(char c2, byte[] bArr) {
        byte b2 = bArr[c2];
        if (b2 < 0) {
            throw new IllegalArgumentException("Illegal character " + c2);
        }
        return b2;
    }

    public static void main(String[] strArr) throws NumberFormatException {
        int i2 = Integer.parseInt(strArr[0]);
        int i3 = Integer.parseInt(strArr[1]);
        Random random = new Random();
        for (int i4 = 0; i4 < i2; i4++) {
            for (int i5 = 0; i5 < i3; i5++) {
                byte[] bArr = new byte[i5];
                for (int i6 = 0; i6 < i5; i6++) {
                    bArr[i6] = (byte) random.nextInt();
                }
                if (!Arrays.equals(bArr, base64ToByteArray(byteArrayToBase64(bArr)))) {
                    System.out.println("Dismal failure!");
                }
                if (!Arrays.equals(bArr, altBase64ToByteArray(byteArrayToAltBase64(bArr)))) {
                    System.out.println("Alternate dismal failure!");
                }
            }
        }
    }
}
