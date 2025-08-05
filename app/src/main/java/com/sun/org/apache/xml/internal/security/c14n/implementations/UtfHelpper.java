package com.sun.org.apache.xml.internal.security.c14n.implementations;

import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/UtfHelpper.class */
public final class UtfHelpper {
    private static final boolean OLD_UTF8 = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("com.sun.org.apache.xml.internal.security.c14n.oldUtf8"));
    })).booleanValue();

    private UtfHelpper() {
    }

    public static void writeByte(String str, OutputStream outputStream, Map<String, byte[]> map) throws IOException {
        byte[] stringInUtf8 = map.get(str);
        if (stringInUtf8 == null) {
            stringInUtf8 = getStringInUtf8(str);
            map.put(str, stringInUtf8);
        }
        outputStream.write(stringInUtf8);
    }

    public static void writeCodePointToUtf8(int i2, OutputStream outputStream) throws IOException {
        int i3;
        if (!Character.isValidCodePoint(i2) || ((i2 >= 55296 && i2 <= 56319) || (i2 >= 56320 && i2 <= 57343))) {
            outputStream.write(63);
            return;
        }
        if (OLD_UTF8 && i2 >= 65536) {
            outputStream.write(63);
            outputStream.write(63);
            return;
        }
        if (i2 < 128) {
            outputStream.write(i2);
            return;
        }
        if (i2 < 2048) {
            i3 = 1;
        } else if (i2 < 65536) {
            i3 = 2;
        } else if (i2 < 2097152) {
            i3 = 3;
        } else if (i2 < 67108864) {
            i3 = 4;
        } else if (i2 <= Integer.MAX_VALUE) {
            i3 = 5;
        } else {
            outputStream.write(63);
            return;
        }
        int i4 = 6 * i3;
        outputStream.write((byte) ((254 << (6 - i3)) | (i2 >>> i4)));
        for (int i5 = i3 - 1; i5 >= 0; i5--) {
            i4 -= 6;
            outputStream.write((byte) (128 | ((i2 >>> i4) & 63)));
        }
    }

    @Deprecated
    public static void writeCharToUtf8(char c2, OutputStream outputStream) throws IOException {
        int i2;
        char c3;
        if (c2 < 128) {
            outputStream.write(c2);
            return;
        }
        if ((c2 >= 55296 && c2 <= 56319) || (c2 >= 56320 && c2 <= 57343)) {
            outputStream.write(63);
            return;
        }
        if (c2 > 2047) {
            char c4 = (char) (c2 >>> '\f');
            int i3 = 224;
            if (c4 > 0) {
                i3 = 224 | (c4 & 15);
            }
            outputStream.write(i3);
            i2 = 128;
            c3 = '?';
        } else {
            i2 = 192;
            c3 = 31;
        }
        char c5 = (char) (c2 >>> 6);
        if (c5 > 0) {
            i2 |= c5 & c3;
        }
        outputStream.write(i2);
        outputStream.write(128 | (c2 & 63));
    }

    public static void writeStringToUtf8(String str, OutputStream outputStream) throws IOException {
        int i2;
        int length = str.length();
        int iCharCount = 0;
        while (iCharCount < length) {
            int iCodePointAt = str.codePointAt(iCharCount);
            iCharCount += Character.charCount(iCodePointAt);
            if (!Character.isValidCodePoint(iCodePointAt) || ((iCodePointAt >= 55296 && iCodePointAt <= 56319) || (iCodePointAt >= 56320 && iCodePointAt <= 57343))) {
                outputStream.write(63);
            } else if (OLD_UTF8 && iCodePointAt >= 65536) {
                outputStream.write(63);
                outputStream.write(63);
            } else if (iCodePointAt < 128) {
                outputStream.write(iCodePointAt);
            } else {
                if (iCodePointAt < 2048) {
                    i2 = 1;
                } else if (iCodePointAt < 65536) {
                    i2 = 2;
                } else if (iCodePointAt < 2097152) {
                    i2 = 3;
                } else if (iCodePointAt < 67108864) {
                    i2 = 4;
                } else if (iCodePointAt <= Integer.MAX_VALUE) {
                    i2 = 5;
                } else {
                    outputStream.write(63);
                }
                int i3 = 6 * i2;
                outputStream.write((byte) ((254 << (6 - i2)) | (iCodePointAt >>> i3)));
                for (int i4 = i2 - 1; i4 >= 0; i4--) {
                    i3 -= 6;
                    outputStream.write((byte) (128 | ((iCodePointAt >>> i3) & 63)));
                }
            }
        }
    }

    public static byte[] getStringInUtf8(String str) {
        int i2;
        int length = str.length();
        boolean z2 = false;
        byte[] bArr = new byte[length];
        int iCharCount = 0;
        int i3 = 0;
        while (iCharCount < length) {
            int iCodePointAt = str.codePointAt(iCharCount);
            iCharCount += Character.charCount(iCodePointAt);
            if (!Character.isValidCodePoint(iCodePointAt) || ((iCodePointAt >= 55296 && iCodePointAt <= 56319) || (iCodePointAt >= 56320 && iCodePointAt <= 57343))) {
                int i4 = i3;
                i3++;
                bArr[i4] = 63;
            } else if (OLD_UTF8 && iCodePointAt >= 65536) {
                int i5 = i3;
                int i6 = i3 + 1;
                bArr[i5] = 63;
                i3 = i6 + 1;
                bArr[i6] = 63;
            } else if (iCodePointAt < 128) {
                int i7 = i3;
                i3++;
                bArr[i7] = (byte) iCodePointAt;
            } else {
                if (!z2) {
                    byte[] bArr2 = new byte[6 * length];
                    System.arraycopy(bArr, 0, bArr2, 0, i3);
                    bArr = bArr2;
                    z2 = true;
                }
                if (iCodePointAt < 2048) {
                    i2 = 1;
                } else if (iCodePointAt < 65536) {
                    i2 = 2;
                } else if (iCodePointAt < 2097152) {
                    i2 = 3;
                } else if (iCodePointAt < 67108864) {
                    i2 = 4;
                } else if (iCodePointAt <= Integer.MAX_VALUE) {
                    i2 = 5;
                } else {
                    int i8 = i3;
                    i3++;
                    bArr[i8] = 63;
                }
                int i9 = 6 * i2;
                int i10 = i3;
                i3++;
                bArr[i10] = (byte) ((254 << (6 - i2)) | (iCodePointAt >>> i9));
                for (int i11 = i2 - 1; i11 >= 0; i11--) {
                    i9 -= 6;
                    int i12 = i3;
                    i3++;
                    bArr[i12] = (byte) (128 | ((iCodePointAt >>> i9) & 63));
                }
            }
        }
        if (z2) {
            byte[] bArr3 = new byte[i3];
            System.arraycopy(bArr, 0, bArr3, 0, i3);
            bArr = bArr3;
        }
        return bArr;
    }
}
