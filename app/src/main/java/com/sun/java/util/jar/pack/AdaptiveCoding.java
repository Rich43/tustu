package com.sun.java.util.jar.pack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/AdaptiveCoding.class */
class AdaptiveCoding implements CodingMethod {
    CodingMethod headCoding;
    int headLength;
    CodingMethod tailCoding;
    public static final int KX_MIN = 0;
    public static final int KX_MAX = 3;
    public static final int KX_LG2BASE = 4;
    public static final int KX_BASE = 16;
    public static final int KB_MIN = 0;
    public static final int KB_MAX = 255;
    public static final int KB_OFFSET = 1;
    public static final int KB_DEFAULT = 3;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AdaptiveCoding.class.desiredAssertionStatus();
    }

    public AdaptiveCoding(int i2, CodingMethod codingMethod, CodingMethod codingMethod2) {
        if (!$assertionsDisabled && !isCodableLength(i2)) {
            throw new AssertionError();
        }
        this.headLength = i2;
        this.headCoding = codingMethod;
        this.tailCoding = codingMethod2;
    }

    public void setHeadCoding(CodingMethod codingMethod) {
        this.headCoding = codingMethod;
    }

    public void setHeadLength(int i2) {
        if (!$assertionsDisabled && !isCodableLength(i2)) {
            throw new AssertionError();
        }
        this.headLength = i2;
    }

    public void setTailCoding(CodingMethod codingMethod) {
        this.tailCoding = codingMethod;
    }

    public boolean isTrivial() {
        return this.headCoding == this.tailCoding;
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public void writeArrayTo(OutputStream outputStream, int[] iArr, int i2, int i3) throws IOException {
        writeArray(this, outputStream, iArr, i2, i3);
    }

    private static void writeArray(AdaptiveCoding adaptiveCoding, OutputStream outputStream, int[] iArr, int i2, int i3) throws IOException {
        while (true) {
            int i4 = i2 + adaptiveCoding.headLength;
            if (!$assertionsDisabled && i4 > i3) {
                throw new AssertionError();
            }
            adaptiveCoding.headCoding.writeArrayTo(outputStream, iArr, i2, i4);
            i2 = i4;
            if (adaptiveCoding.tailCoding instanceof AdaptiveCoding) {
                adaptiveCoding = (AdaptiveCoding) adaptiveCoding.tailCoding;
            } else {
                adaptiveCoding.tailCoding.writeArrayTo(outputStream, iArr, i2, i3);
                return;
            }
        }
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public void readArrayFrom(InputStream inputStream, int[] iArr, int i2, int i3) throws IOException {
        readArray(this, inputStream, iArr, i2, i3);
    }

    private static void readArray(AdaptiveCoding adaptiveCoding, InputStream inputStream, int[] iArr, int i2, int i3) throws IOException {
        while (true) {
            int i4 = i2 + adaptiveCoding.headLength;
            if (!$assertionsDisabled && i4 > i3) {
                throw new AssertionError();
            }
            adaptiveCoding.headCoding.readArrayFrom(inputStream, iArr, i2, i4);
            i2 = i4;
            if (adaptiveCoding.tailCoding instanceof AdaptiveCoding) {
                adaptiveCoding = (AdaptiveCoding) adaptiveCoding.tailCoding;
            } else {
                adaptiveCoding.tailCoding.readArrayFrom(inputStream, iArr, i2, i3);
                return;
            }
        }
    }

    static int getKXOf(int i2) {
        for (int i3 = 0; i3 <= 3; i3++) {
            if (((i2 - 1) & (-256)) == 0) {
                return i3;
            }
            i2 >>>= 4;
        }
        return -1;
    }

    static int getKBOf(int i2) {
        int kXOf = getKXOf(i2);
        if (kXOf < 0) {
            return -1;
        }
        return (i2 >>> (kXOf * 4)) - 1;
    }

    static int decodeK(int i2, int i3) {
        if (!$assertionsDisabled && (0 > i2 || i2 > 3)) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || (0 <= i3 && i3 <= 255)) {
            return (i3 + 1) << (i2 * 4);
        }
        throw new AssertionError();
    }

    static int getNextK(int i2) {
        if (i2 <= 0) {
            return 1;
        }
        int kXOf = getKXOf(i2);
        if (kXOf < 0) {
            return Integer.MAX_VALUE;
        }
        int i3 = 1 << (kXOf * 4);
        int i4 = 255 << (kXOf * 4);
        int i5 = (i2 + i3) & ((i3 - 1) ^ (-1));
        if (((i5 - i3) & (i4 ^ (-1))) == 0) {
            if ($assertionsDisabled || getKXOf(i5) == kXOf) {
                return i5;
            }
            throw new AssertionError();
        }
        if (kXOf == 3) {
            return Integer.MAX_VALUE;
        }
        int i6 = kXOf + 1;
        int i7 = (i5 | (i4 & ((255 << (i6 * 4)) ^ (-1)))) + i3;
        if ($assertionsDisabled || getKXOf(i7) == i6) {
            return i7;
        }
        throw new AssertionError();
    }

    public static boolean isCodableLength(int i2) {
        int kXOf = getKXOf(i2);
        if (kXOf < 0) {
            return false;
        }
        return ((i2 - (1 << (kXOf * 4))) & ((255 << (kXOf * 4)) ^ (-1))) == 0;
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public byte[] getMetaCoding(Coding coding) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10);
        try {
            makeMetaCoding(this, coding, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private static void makeMetaCoding(AdaptiveCoding adaptiveCoding, Coding coding, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        while (true) {
            CodingMethod codingMethod = adaptiveCoding.headCoding;
            int i2 = adaptiveCoding.headLength;
            CodingMethod codingMethod2 = adaptiveCoding.tailCoding;
            if (!$assertionsDisabled && !isCodableLength(i2)) {
                throw new AssertionError();
            }
            int i3 = codingMethod == coding ? 1 : 0;
            int i4 = codingMethod2 == coding ? 1 : 0;
            if (i3 + i4 > 1) {
                i4 = 0;
            }
            int i5 = (1 * i3) + (2 * i4);
            if (!$assertionsDisabled && i5 >= 3) {
                throw new AssertionError();
            }
            int kXOf = getKXOf(i2);
            int kBOf = getKBOf(i2);
            if (!$assertionsDisabled && decodeK(kXOf, kBOf) != i2) {
                throw new AssertionError();
            }
            int i6 = kBOf != 3 ? 1 : 0;
            byteArrayOutputStream.write(117 + kXOf + (4 * i6) + (8 * i5));
            if (i6 != 0) {
                byteArrayOutputStream.write(kBOf);
            }
            if (i3 == 0) {
                byteArrayOutputStream.write(codingMethod.getMetaCoding(coding));
            }
            if (codingMethod2 instanceof AdaptiveCoding) {
                adaptiveCoding = (AdaptiveCoding) codingMethod2;
            } else {
                if (i4 == 0) {
                    byteArrayOutputStream.write(codingMethod2.getMetaCoding(coding));
                    return;
                }
                return;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00d5 A[PHI: r11
  0x00d5: PHI (r11v3 int) = (r11v2 int), (r11v5 int), (r11v5 int) binds: [B:30:0x00b2, B:32:0x00c1, B:34:0x00c9] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int parseMetaCoding(byte[] r7, int r8, com.sun.java.util.jar.pack.Coding r9, com.sun.java.util.jar.pack.CodingMethod[] r10) {
        /*
            Method dump skipped, instructions count: 280
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.AdaptiveCoding.parseMetaCoding(byte[], int, com.sun.java.util.jar.pack.Coding, com.sun.java.util.jar.pack.CodingMethod[]):int");
    }

    private String keyString(CodingMethod codingMethod) {
        if (codingMethod instanceof Coding) {
            return ((Coding) codingMethod).keyString();
        }
        return codingMethod.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        AdaptiveCoding adaptiveCoding = this;
        sb.append("run(");
        while (true) {
            sb.append(adaptiveCoding.headLength).append("*");
            sb.append(keyString(adaptiveCoding.headCoding));
            if (adaptiveCoding.tailCoding instanceof AdaptiveCoding) {
                adaptiveCoding = (AdaptiveCoding) adaptiveCoding.tailCoding;
                sb.append(" ");
            } else {
                sb.append(" **").append(keyString(adaptiveCoding.tailCoding));
                sb.append(")");
                return sb.toString();
            }
        }
    }
}
