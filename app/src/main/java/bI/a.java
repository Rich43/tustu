package bI;

import bH.C;
import bH.C1008p;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: TunerStudioMS.jar:bI/a.class */
public class a {

    /* renamed from: b, reason: collision with root package name */
    private static final byte[] f7070b;

    /* renamed from: c, reason: collision with root package name */
    private static final byte[] f7071c;

    /* renamed from: d, reason: collision with root package name */
    private static final byte[] f7072d;

    /* renamed from: e, reason: collision with root package name */
    private static final byte[] f7073e;

    /* renamed from: f, reason: collision with root package name */
    private static final byte[] f7074f;

    /* renamed from: g, reason: collision with root package name */
    private static final byte[] f7075g;

    /* renamed from: a, reason: collision with root package name */
    static final /* synthetic */ boolean f7076a;

    private static final byte[] b(int i2) {
        return (i2 & 16) == 16 ? f7072d : (i2 & 32) == 32 ? f7074f : f7070b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final byte[] c(int i2) {
        return (i2 & 16) == 16 ? f7073e : (i2 & 32) == 32 ? f7075g : f7071c;
    }

    private a() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] b(byte[] bArr, byte[] bArr2, int i2, int i3) {
        b(bArr2, 0, i2, bArr, 0, i3);
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] b(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) {
        byte[] bArrB = b(i5);
        int i6 = (i3 > 0 ? (bArr[i2] << 24) >>> 8 : 0) | (i3 > 1 ? (bArr[i2 + 1] << 24) >>> 16 : 0) | (i3 > 2 ? (bArr[i2 + 2] << 24) >>> 24 : 0);
        switch (i3) {
            case 1:
                bArr2[i4] = bArrB[i6 >>> 18];
                bArr2[i4 + 1] = bArrB[(i6 >>> 12) & 63];
                bArr2[i4 + 2] = 61;
                bArr2[i4 + 3] = 61;
                break;
            case 2:
                bArr2[i4] = bArrB[i6 >>> 18];
                bArr2[i4 + 1] = bArrB[(i6 >>> 12) & 63];
                bArr2[i4 + 2] = bArrB[(i6 >>> 6) & 63];
                bArr2[i4 + 3] = 61;
                break;
            case 3:
                bArr2[i4] = bArrB[i6 >>> 18];
                bArr2[i4 + 1] = bArrB[(i6 >>> 12) & 63];
                bArr2[i4 + 2] = bArrB[(i6 >>> 6) & 63];
                bArr2[i4 + 3] = bArrB[i6 & 63];
                break;
        }
        return bArr2;
    }

    public static String a(byte[] bArr) {
        String strA = null;
        try {
            strA = a(bArr, 0, bArr.length, 0);
        } catch (IOException e2) {
            if (!f7076a) {
                throw new AssertionError((Object) e2.getMessage());
            }
        }
        if (f7076a || strA != null) {
            return strA;
        }
        throw new AssertionError();
    }

    public static String a(byte[] bArr, int i2) {
        return a(bArr, 0, bArr.length, i2);
    }

    public static String a(byte[] bArr, int i2, int i3, int i4) {
        byte[] bArrB = b(bArr, i2, i3, i4);
        try {
            return new String(bArrB, "US-ASCII");
        } catch (UnsupportedEncodingException e2) {
            return new String(bArrB);
        }
    }

    public static byte[] b(byte[] bArr, int i2, int i3, int i4) {
        if (bArr == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("Cannot have negative offset: " + i2);
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + i3);
        }
        if (i2 + i3 > bArr.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(bArr.length)));
        }
        if ((i4 & 2) != 0) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            GZIPOutputStream gZIPOutputStream = null;
            c cVar = null;
            try {
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    cVar = new c(byteArrayOutputStream, 1 | i4);
                    gZIPOutputStream = new GZIPOutputStream(cVar);
                    gZIPOutputStream.write(bArr, i2, i3);
                    gZIPOutputStream.close();
                    try {
                        gZIPOutputStream.close();
                    } catch (Exception e2) {
                    }
                    try {
                        cVar.close();
                    } catch (Exception e3) {
                    }
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e4) {
                    }
                    return byteArrayOutputStream.toByteArray();
                } catch (Throwable th) {
                    try {
                        gZIPOutputStream.close();
                    } catch (Exception e5) {
                    }
                    try {
                        cVar.close();
                    } catch (Exception e6) {
                    }
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e7) {
                    }
                    throw th;
                }
            } catch (IOException e8) {
                throw e8;
            }
        }
        boolean z2 = (i4 & 8) != 0;
        int i5 = ((i3 / 3) * 4) + (i3 % 3 > 0 ? 4 : 0);
        if (z2) {
            i5 += i5 / 76;
        }
        byte[] bArr2 = new byte[i5];
        int i6 = 0;
        int i7 = 0;
        int i8 = i3 - 2;
        int i9 = 0;
        while (i6 < i8) {
            b(bArr, i6 + i2, 3, bArr2, i7, i4);
            i9 += 4;
            if (z2 && i9 >= 76) {
                bArr2[i7 + 4] = 10;
                i7++;
                i9 = 0;
            }
            i6 += 3;
            i7 += 4;
        }
        if (i6 < i3) {
            b(bArr, i6 + i2, i3 - i6, bArr2, i7, i4);
            i7 += 4;
        }
        if (i7 > bArr2.length - 1) {
            return bArr2;
        }
        byte[] bArr3 = new byte[i7];
        System.arraycopy(bArr2, 0, bArr3, 0, i7);
        return bArr3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int b(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) {
        if (bArr == null) {
            throw new NullPointerException("Source array was null.");
        }
        if (bArr2 == null) {
            throw new NullPointerException("Destination array was null.");
        }
        if (i2 < 0 || i2 + 3 >= bArr.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", Integer.valueOf(bArr.length), Integer.valueOf(i2)));
        }
        if (i3 < 0 || i3 + 2 >= bArr2.length) {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", Integer.valueOf(bArr2.length), Integer.valueOf(i3)));
        }
        byte[] bArrC = c(i4);
        if (bArr[i2 + 2] == 61) {
            bArr2[i3] = (byte) ((((bArrC[bArr[i2]] & 255) << 18) | ((bArrC[bArr[i2 + 1]] & 255) << 12)) >>> 16);
            return 1;
        }
        if (bArr[i2 + 3] == 61) {
            int i5 = ((bArrC[bArr[i2]] & 255) << 18) | ((bArrC[bArr[i2 + 1]] & 255) << 12) | ((bArrC[bArr[i2 + 2]] & 255) << 6);
            bArr2[i3] = (byte) (i5 >>> 16);
            bArr2[i3 + 1] = (byte) (i5 >>> 8);
            return 2;
        }
        int i6 = ((bArrC[bArr[i2]] & 255) << 18) | ((bArrC[bArr[i2 + 1]] & 255) << 12) | ((bArrC[bArr[i2 + 2]] & 255) << 6) | (bArrC[bArr[i2 + 3]] & 255);
        bArr2[i3] = (byte) (i6 >> 16);
        bArr2[i3 + 1] = (byte) (i6 >> 8);
        bArr2[i3 + 2] = (byte) i6;
        return 3;
    }

    public static byte[] c(byte[] bArr, int i2, int i3, int i4) throws IOException {
        if (bArr == null) {
            throw new NullPointerException("Cannot decode null source array.");
        }
        if (i2 < 0 || i2 + i3 > bArr.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", Integer.valueOf(bArr.length), Integer.valueOf(i2), Integer.valueOf(i3)));
        }
        if (i3 == 0) {
            return new byte[0];
        }
        if (i3 < 4) {
            throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + i3);
        }
        byte[] bArrC = c(i4);
        byte[] bArr2 = new byte[(i3 * 3) / 4];
        int iB = 0;
        byte[] bArr3 = new byte[4];
        int i5 = 0;
        for (int i6 = i2; i6 < i2 + i3; i6++) {
            byte b2 = bArrC[bArr[i6] & 255];
            if (b2 < -5) {
                throw new IOException(String.format("Bad Base64 input character decimal %d in array position %d", Integer.valueOf(bArr[i6] & 255), Integer.valueOf(i6)));
            }
            if (b2 >= -1) {
                int i7 = i5;
                i5++;
                bArr3[i7] = bArr[i6];
                if (i5 > 3) {
                    iB += b(bArr3, 0, bArr2, iB, i4);
                    i5 = 0;
                    if (bArr[i6] == 61) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        byte[] bArr4 = new byte[iB];
        System.arraycopy(bArr2, 0, bArr4, 0, iB);
        return bArr4;
    }

    public static byte[] a(String str) {
        return a(str, 0);
    }

    public static byte[] a(String str, int i2) throws IOException {
        byte[] bytes;
        if (str == null) {
            throw new NullPointerException("Input string was null.");
        }
        float fC = C1008p.c();
        if (!Float.isNaN(fC) && fC >= 1.8f) {
            try {
                Class<?> cls = Class.forName("java.util.Base64");
                return (byte[]) Class.forName("java.util.Base64$Decoder").getMethod("decode", String.class).invoke(cls.getMethod("getDecoder", new Class[0]).invoke(cls, new Object[0]), str);
            } catch (Exception e2) {
                C.b("Decode using JRE Base64 failed, using legacy Base64 decoder");
            }
        }
        try {
            bytes = str.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e3) {
            bytes = str.getBytes();
        }
        byte[] bArrC = c(bytes, 0, bytes.length, i2);
        boolean z2 = (i2 & 4) != 0;
        if (bArrC != null && bArrC.length >= 4 && !z2 && 35615 == ((bArrC[0] & 255) | ((bArrC[1] << 8) & NormalizerImpl.CC_MASK))) {
            ByteArrayInputStream byteArrayInputStream = null;
            GZIPInputStream gZIPInputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            byte[] bArr = new byte[2048];
            try {
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayInputStream = new ByteArrayInputStream(bArrC);
                    gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
                    while (true) {
                        int i3 = gZIPInputStream.read(bArr);
                        if (i3 < 0) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, i3);
                    }
                    bArrC = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e4) {
                    }
                    try {
                        gZIPInputStream.close();
                    } catch (Exception e5) {
                    }
                    try {
                        byteArrayInputStream.close();
                    } catch (Exception e6) {
                    }
                } catch (Throwable th) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e7) {
                    }
                    try {
                        gZIPInputStream.close();
                    } catch (Exception e8) {
                    }
                    try {
                        byteArrayInputStream.close();
                    } catch (Exception e9) {
                    }
                    throw th;
                }
            } catch (IOException e10) {
                e10.printStackTrace();
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e11) {
                }
                try {
                    gZIPInputStream.close();
                } catch (Exception e12) {
                }
                try {
                    byteArrayInputStream.close();
                } catch (Exception e13) {
                }
            }
        }
        return bArrC;
    }

    public static void a(String str, String str2) {
        c cVar = null;
        try {
            try {
                cVar = new c(new FileOutputStream(str2), 0);
                cVar.write(str.getBytes("US-ASCII"));
                try {
                    cVar.close();
                } catch (Exception e2) {
                }
            } catch (IOException e3) {
                throw e3;
            }
        } catch (Throwable th) {
            try {
                cVar.close();
            } catch (Exception e4) {
            }
            throw th;
        }
    }

    public static String b(String str) {
        b bVar = null;
        try {
            try {
                File file = new File(str);
                byte[] bArr = new byte[Math.max((int) ((file.length() * 1.4d) + 1.0d), 40)];
                int i2 = 0;
                bVar = new b(new BufferedInputStream(new FileInputStream(file)), 1);
                while (true) {
                    int i3 = bVar.read(bArr, i2, 4096);
                    if (i3 < 0) {
                        break;
                    }
                    i2 += i3;
                }
                String str2 = new String(bArr, 0, i2, "US-ASCII");
                try {
                    bVar.close();
                } catch (Exception e2) {
                }
                return str2;
            } catch (IOException e3) {
                throw e3;
            }
        } catch (Throwable th) {
            try {
                bVar.close();
            } catch (Exception e4) {
            }
            throw th;
        }
    }

    static {
        f7076a = !a.class.desiredAssertionStatus();
        f7070b = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        f7071c = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
        f7072d = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        f7073e = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
        f7074f = new byte[]{45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
        f7075g = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
    }
}
