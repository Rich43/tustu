package sun.security.provider;

import java.nio.ByteOrder;
import java.security.AccessController;
import sun.misc.Unsafe;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/security/provider/ByteArrayAccess.class */
final class ByteArrayAccess {
    private static final boolean littleEndianUnaligned;
    private static final boolean bigEndian;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final int byteArrayOfs = unsafe.arrayBaseOffset(byte[].class);

    private ByteArrayAccess() {
    }

    static {
        boolean z2 = unsafe.arrayIndexScale(byte[].class) == 1 && unsafe.arrayIndexScale(int[].class) == 4 && unsafe.arrayIndexScale(long[].class) == 8 && (byteArrayOfs & 3) == 0;
        ByteOrder byteOrderNativeOrder = ByteOrder.nativeOrder();
        littleEndianUnaligned = z2 && unaligned() && byteOrderNativeOrder == ByteOrder.LITTLE_ENDIAN;
        bigEndian = z2 && byteOrderNativeOrder == ByteOrder.BIG_ENDIAN;
    }

    private static boolean unaligned() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("os.arch", ""));
        return str.equals("i386") || str.equals("x86") || str.equals("amd64") || str.equals("x86_64") || str.equals("ppc64") || str.equals("ppc64le") || str.equals("aarch64");
    }

    static void b2iLittle(byte[] bArr, int i2, int[] iArr, int i3, int i4) {
        if (i2 < 0 || bArr.length - i2 < i4 || i3 < 0 || iArr.length - i3 < i4 / 4) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            int i5 = i2 + byteArrayOfs;
            int i6 = i4 + i5;
            while (i5 < i6) {
                int i7 = i3;
                i3++;
                iArr[i7] = unsafe.getInt(bArr, i5);
                i5 += 4;
            }
            return;
        }
        if (bigEndian && (i2 & 3) == 0) {
            int i8 = i2 + byteArrayOfs;
            int i9 = i4 + i8;
            while (i8 < i9) {
                int i10 = i3;
                i3++;
                iArr[i10] = Integer.reverseBytes(unsafe.getInt(bArr, i8));
                i8 += 4;
            }
            return;
        }
        int i11 = i4 + i2;
        while (i2 < i11) {
            int i12 = i3;
            i3++;
            iArr[i12] = (bArr[i2] & 255) | ((bArr[i2 + 1] & 255) << 8) | ((bArr[i2 + 2] & 255) << 16) | (bArr[i2 + 3] << 24);
            i2 += 4;
        }
    }

    static void b2iLittle64(byte[] bArr, int i2, int[] iArr) {
        if (i2 < 0 || bArr.length - i2 < 64 || iArr.length < 16) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            iArr[0] = unsafe.getInt(bArr, i2 + byteArrayOfs);
            iArr[1] = unsafe.getInt(bArr, r0 + 4);
            iArr[2] = unsafe.getInt(bArr, r0 + 8);
            iArr[3] = unsafe.getInt(bArr, r0 + 12);
            iArr[4] = unsafe.getInt(bArr, r0 + 16);
            iArr[5] = unsafe.getInt(bArr, r0 + 20);
            iArr[6] = unsafe.getInt(bArr, r0 + 24);
            iArr[7] = unsafe.getInt(bArr, r0 + 28);
            iArr[8] = unsafe.getInt(bArr, r0 + 32);
            iArr[9] = unsafe.getInt(bArr, r0 + 36);
            iArr[10] = unsafe.getInt(bArr, r0 + 40);
            iArr[11] = unsafe.getInt(bArr, r0 + 44);
            iArr[12] = unsafe.getInt(bArr, r0 + 48);
            iArr[13] = unsafe.getInt(bArr, r0 + 52);
            iArr[14] = unsafe.getInt(bArr, r0 + 56);
            iArr[15] = unsafe.getInt(bArr, r0 + 60);
            return;
        }
        if (bigEndian && (i2 & 3) == 0) {
            iArr[0] = Integer.reverseBytes(unsafe.getInt(bArr, i2 + byteArrayOfs));
            iArr[1] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 4));
            iArr[2] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 8));
            iArr[3] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 12));
            iArr[4] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 16));
            iArr[5] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 20));
            iArr[6] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 24));
            iArr[7] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 28));
            iArr[8] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 32));
            iArr[9] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 36));
            iArr[10] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 40));
            iArr[11] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 44));
            iArr[12] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 48));
            iArr[13] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 52));
            iArr[14] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 56));
            iArr[15] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 60));
            return;
        }
        b2iLittle(bArr, i2, iArr, 0, 64);
    }

    static void i2bLittle(int[] iArr, int i2, byte[] bArr, int i3, int i4) {
        if (i2 < 0 || iArr.length - i2 < i4 / 4 || i3 < 0 || bArr.length - i3 < i4) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            int i5 = i3 + byteArrayOfs;
            int i6 = i4 + i5;
            while (i5 < i6) {
                int i7 = i2;
                i2++;
                unsafe.putInt(bArr, i5, iArr[i7]);
                i5 += 4;
            }
            return;
        }
        if (bigEndian && (i3 & 3) == 0) {
            int i8 = i3 + byteArrayOfs;
            int i9 = i4 + i8;
            while (i8 < i9) {
                int i10 = i2;
                i2++;
                unsafe.putInt(bArr, i8, Integer.reverseBytes(iArr[i10]));
                i8 += 4;
            }
            return;
        }
        int i11 = i4 + i3;
        while (i3 < i11) {
            int i12 = i2;
            i2++;
            int i13 = iArr[i12];
            int i14 = i3;
            int i15 = i3 + 1;
            bArr[i14] = (byte) i13;
            int i16 = i15 + 1;
            bArr[i15] = (byte) (i13 >> 8);
            int i17 = i16 + 1;
            bArr[i16] = (byte) (i13 >> 16);
            i3 = i17 + 1;
            bArr[i17] = (byte) (i13 >> 24);
        }
    }

    static void i2bLittle4(int i2, byte[] bArr, int i3) {
        if (i3 < 0 || bArr.length - i3 < 4) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            unsafe.putInt(bArr, byteArrayOfs + i3, i2);
            return;
        }
        if (bigEndian && (i3 & 3) == 0) {
            unsafe.putInt(bArr, byteArrayOfs + i3, Integer.reverseBytes(i2));
            return;
        }
        bArr[i3] = (byte) i2;
        bArr[i3 + 1] = (byte) (i2 >> 8);
        bArr[i3 + 2] = (byte) (i2 >> 16);
        bArr[i3 + 3] = (byte) (i2 >> 24);
    }

    static void b2iBig(byte[] bArr, int i2, int[] iArr, int i3, int i4) {
        if (i2 < 0 || bArr.length - i2 < i4 || i3 < 0 || iArr.length - i3 < i4 / 4) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            int i5 = i2 + byteArrayOfs;
            int i6 = i4 + i5;
            while (i5 < i6) {
                int i7 = i3;
                i3++;
                iArr[i7] = Integer.reverseBytes(unsafe.getInt(bArr, i5));
                i5 += 4;
            }
            return;
        }
        if (bigEndian && (i2 & 3) == 0) {
            int i8 = i2 + byteArrayOfs;
            int i9 = i4 + i8;
            while (i8 < i9) {
                int i10 = i3;
                i3++;
                iArr[i10] = unsafe.getInt(bArr, i8);
                i8 += 4;
            }
            return;
        }
        int i11 = i4 + i2;
        while (i2 < i11) {
            int i12 = i3;
            i3++;
            iArr[i12] = (bArr[i2 + 3] & 255) | ((bArr[i2 + 2] & 255) << 8) | ((bArr[i2 + 1] & 255) << 16) | (bArr[i2] << 24);
            i2 += 4;
        }
    }

    static void b2iBig64(byte[] bArr, int i2, int[] iArr) {
        if (i2 < 0 || bArr.length - i2 < 64 || iArr.length < 16) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            iArr[0] = Integer.reverseBytes(unsafe.getInt(bArr, i2 + byteArrayOfs));
            iArr[1] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 4));
            iArr[2] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 8));
            iArr[3] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 12));
            iArr[4] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 16));
            iArr[5] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 20));
            iArr[6] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 24));
            iArr[7] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 28));
            iArr[8] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 32));
            iArr[9] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 36));
            iArr[10] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 40));
            iArr[11] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 44));
            iArr[12] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 48));
            iArr[13] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 52));
            iArr[14] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 56));
            iArr[15] = Integer.reverseBytes(unsafe.getInt(bArr, r0 + 60));
            return;
        }
        if (bigEndian && (i2 & 3) == 0) {
            iArr[0] = unsafe.getInt(bArr, i2 + byteArrayOfs);
            iArr[1] = unsafe.getInt(bArr, r0 + 4);
            iArr[2] = unsafe.getInt(bArr, r0 + 8);
            iArr[3] = unsafe.getInt(bArr, r0 + 12);
            iArr[4] = unsafe.getInt(bArr, r0 + 16);
            iArr[5] = unsafe.getInt(bArr, r0 + 20);
            iArr[6] = unsafe.getInt(bArr, r0 + 24);
            iArr[7] = unsafe.getInt(bArr, r0 + 28);
            iArr[8] = unsafe.getInt(bArr, r0 + 32);
            iArr[9] = unsafe.getInt(bArr, r0 + 36);
            iArr[10] = unsafe.getInt(bArr, r0 + 40);
            iArr[11] = unsafe.getInt(bArr, r0 + 44);
            iArr[12] = unsafe.getInt(bArr, r0 + 48);
            iArr[13] = unsafe.getInt(bArr, r0 + 52);
            iArr[14] = unsafe.getInt(bArr, r0 + 56);
            iArr[15] = unsafe.getInt(bArr, r0 + 60);
            return;
        }
        b2iBig(bArr, i2, iArr, 0, 64);
    }

    static void i2bBig(int[] iArr, int i2, byte[] bArr, int i3, int i4) {
        if (i2 < 0 || iArr.length - i2 < i4 / 4 || i3 < 0 || bArr.length - i3 < i4) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            int i5 = i3 + byteArrayOfs;
            int i6 = i4 + i5;
            while (i5 < i6) {
                int i7 = i2;
                i2++;
                unsafe.putInt(bArr, i5, Integer.reverseBytes(iArr[i7]));
                i5 += 4;
            }
            return;
        }
        if (bigEndian && (i3 & 3) == 0) {
            int i8 = i3 + byteArrayOfs;
            int i9 = i4 + i8;
            while (i8 < i9) {
                int i10 = i2;
                i2++;
                unsafe.putInt(bArr, i8, iArr[i10]);
                i8 += 4;
            }
            return;
        }
        int i11 = i4 + i3;
        while (i3 < i11) {
            int i12 = i2;
            i2++;
            int i13 = iArr[i12];
            int i14 = i3;
            int i15 = i3 + 1;
            bArr[i14] = (byte) (i13 >> 24);
            int i16 = i15 + 1;
            bArr[i15] = (byte) (i13 >> 16);
            int i17 = i16 + 1;
            bArr[i16] = (byte) (i13 >> 8);
            i3 = i17 + 1;
            bArr[i17] = (byte) i13;
        }
    }

    static void i2bBig4(int i2, byte[] bArr, int i3) {
        if (i3 < 0 || bArr.length - i3 < 4) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            unsafe.putInt(bArr, byteArrayOfs + i3, Integer.reverseBytes(i2));
            return;
        }
        if (bigEndian && (i3 & 3) == 0) {
            unsafe.putInt(bArr, byteArrayOfs + i3, i2);
            return;
        }
        bArr[i3] = (byte) (i2 >> 24);
        bArr[i3 + 1] = (byte) (i2 >> 16);
        bArr[i3 + 2] = (byte) (i2 >> 8);
        bArr[i3 + 3] = (byte) i2;
    }

    static void b2lBig(byte[] bArr, int i2, long[] jArr, int i3, int i4) {
        if (i2 < 0 || bArr.length - i2 < i4 || i3 < 0 || jArr.length - i3 < i4 / 8) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            int i5 = i2 + byteArrayOfs;
            int i6 = i4 + i5;
            while (i5 < i6) {
                int i7 = i3;
                i3++;
                jArr[i7] = Long.reverseBytes(unsafe.getLong(bArr, i5));
                i5 += 8;
            }
            return;
        }
        if (bigEndian && (i2 & 3) == 0) {
            int i8 = i2 + byteArrayOfs;
            int i9 = i4 + i8;
            while (i8 < i9) {
                int i10 = i3;
                i3++;
                jArr[i10] = (unsafe.getInt(bArr, i8) << 32) | (unsafe.getInt(bArr, i8 + 4) & 4294967295L);
                i8 += 8;
            }
            return;
        }
        int i11 = i4 + i2;
        while (i2 < i11) {
            int i12 = (bArr[i2 + 3] & 255) | ((bArr[i2 + 2] & 255) << 8) | ((bArr[i2 + 1] & 255) << 16) | (bArr[i2] << 24);
            int i13 = i3;
            i3++;
            jArr[i13] = (i12 << 32) | (((bArr[r10 + 3] & 255) | ((bArr[r10 + 2] & 255) << 8) | ((bArr[r10 + 1] & 255) << 16) | (bArr[r10] << 24)) & 4294967295L);
            i2 = i2 + 4 + 4;
        }
    }

    static void b2lBig128(byte[] bArr, int i2, long[] jArr) {
        if (i2 < 0 || bArr.length - i2 < 128 || jArr.length < 16) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (littleEndianUnaligned) {
            jArr[0] = Long.reverseBytes(unsafe.getLong(bArr, i2 + byteArrayOfs));
            jArr[1] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 8));
            jArr[2] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 16));
            jArr[3] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 24));
            jArr[4] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 32));
            jArr[5] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 40));
            jArr[6] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 48));
            jArr[7] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 56));
            jArr[8] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 64));
            jArr[9] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 72));
            jArr[10] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 80));
            jArr[11] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 88));
            jArr[12] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 96));
            jArr[13] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 104));
            jArr[14] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 112));
            jArr[15] = Long.reverseBytes(unsafe.getLong(bArr, r0 + 120));
            return;
        }
        b2lBig(bArr, i2, jArr, 0, 128);
    }

    static void l2bBig(long[] jArr, int i2, byte[] bArr, int i3, int i4) {
        if (i2 < 0 || jArr.length - i2 < i4 / 8 || i3 < 0 || bArr.length - i3 < i4) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i5 = i4 + i3;
        while (i3 < i5) {
            int i6 = i2;
            i2++;
            long j2 = jArr[i6];
            int i7 = i3;
            int i8 = i3 + 1;
            bArr[i7] = (byte) (j2 >> 56);
            int i9 = i8 + 1;
            bArr[i8] = (byte) (j2 >> 48);
            int i10 = i9 + 1;
            bArr[i9] = (byte) (j2 >> 40);
            int i11 = i10 + 1;
            bArr[i10] = (byte) (j2 >> 32);
            int i12 = i11 + 1;
            bArr[i11] = (byte) (j2 >> 24);
            int i13 = i12 + 1;
            bArr[i12] = (byte) (j2 >> 16);
            int i14 = i13 + 1;
            bArr[i13] = (byte) (j2 >> 8);
            i3 = i14 + 1;
            bArr[i14] = (byte) j2;
        }
    }
}
