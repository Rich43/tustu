package sun.misc;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: rt.jar:sun/misc/IOUtils.class */
public class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int MAX_BUFFER_SIZE = 2147483639;

    public static byte[] readExactlyNBytes(InputStream inputStream, int i2) throws IOException {
        if (i2 < 0) {
            throw new IOException("length cannot be negative: " + i2);
        }
        byte[] nBytes = readNBytes(inputStream, i2);
        if (nBytes.length < i2) {
            throw new EOFException();
        }
        return nBytes;
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        return readNBytes(inputStream, Integer.MAX_VALUE);
    }

    public static byte[] readNBytes(InputStream inputStream, int i2) throws IOException {
        int i3;
        if (i2 < 0) {
            throw new IllegalArgumentException("len < 0");
        }
        ArrayList<byte[]> arrayList = null;
        byte[] bArr = null;
        int i4 = 0;
        int i5 = i2;
        do {
            byte[] bArr2 = new byte[Math.min(i5, 8192)];
            int i6 = 0;
            while (true) {
                i3 = inputStream.read(bArr2, i6, Math.min(bArr2.length - i6, i5));
                if (i3 <= 0) {
                    break;
                }
                i6 += i3;
                i5 -= i3;
            }
            if (i6 > 0) {
                if (MAX_BUFFER_SIZE - i4 < i6) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                i4 += i6;
                if (bArr == null) {
                    bArr = bArr2;
                } else {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        arrayList.add(bArr);
                    }
                    arrayList.add(bArr2);
                }
            }
            if (i3 < 0) {
                break;
            }
        } while (i5 > 0);
        if (arrayList == null) {
            if (bArr == null) {
                return new byte[0];
            }
            return bArr.length == i4 ? bArr : Arrays.copyOf(bArr, i4);
        }
        byte[] bArr3 = new byte[i4];
        int i7 = 0;
        int i8 = i4;
        for (byte[] bArr4 : arrayList) {
            int iMin = Math.min(bArr4.length, i8);
            System.arraycopy(bArr4, 0, bArr3, i7, iMin);
            i7 += iMin;
            i8 -= iMin;
        }
        return bArr3;
    }

    public static int readNBytes(InputStream inputStream, byte[] bArr, int i2, int i3) throws IOException {
        int i4;
        int i5;
        Objects.requireNonNull(bArr);
        if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        }
        int i6 = 0;
        while (true) {
            i4 = i6;
            if (i4 >= i3 || (i5 = inputStream.read(bArr, i2 + i4, i3 - i4)) < 0) {
                break;
            }
            i6 = i4 + i5;
        }
        return i4;
    }

    public static byte[] readFully(InputStream inputStream, int i2, boolean z2) throws IOException {
        if (i2 < 0) {
            throw new IOException("length cannot be negative: " + i2);
        }
        if (z2) {
            return readExactlyNBytes(inputStream, i2);
        }
        return readNBytes(inputStream, i2);
    }
}
