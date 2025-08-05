package sun.reflect;

/* loaded from: rt.jar:sun/reflect/UTF8.class */
class UTF8 {
    UTF8() {
    }

    static byte[] encode(String str) {
        int length = str.length();
        byte[] bArr = new byte[utf8Length(str)];
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            try {
                int iCharAt = str.charAt(i3) & 65535;
                if (iCharAt >= 1 && iCharAt <= 127) {
                    int i4 = i2;
                    i2++;
                    bArr[i4] = (byte) iCharAt;
                } else if (iCharAt == 0 || (iCharAt >= 128 && iCharAt <= 2047)) {
                    int i5 = i2;
                    int i6 = i2 + 1;
                    bArr[i5] = (byte) (192 + (iCharAt >> 6));
                    i2 = i6 + 1;
                    bArr[i6] = (byte) (128 + (iCharAt & 63));
                } else {
                    int i7 = i2;
                    int i8 = i2 + 1;
                    bArr[i7] = (byte) (224 + (iCharAt >> 12));
                    int i9 = i8 + 1;
                    bArr[i8] = (byte) (128 + ((iCharAt >> 6) & 63));
                    i2 = i9 + 1;
                    bArr[i9] = (byte) (128 + (iCharAt & 63));
                }
            } catch (ArrayIndexOutOfBoundsException e2) {
                throw new InternalError("Bug in sun.reflect bootstrap UTF-8 encoder", e2);
            }
        }
        return bArr;
    }

    private static int utf8Length(String str) {
        int length = str.length();
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int iCharAt = str.charAt(i3) & 65535;
            if (iCharAt >= 1 && iCharAt <= 127) {
                i2++;
            } else if (iCharAt == 0 || (iCharAt >= 128 && iCharAt <= 2047)) {
                i2 += 2;
            } else {
                i2 += 3;
            }
        }
        return i2;
    }
}
