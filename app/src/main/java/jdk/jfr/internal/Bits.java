package jdk.jfr.internal;

import java.nio.ByteOrder;
import sun.misc.Unsafe;

/* loaded from: jfr.jar:jdk/jfr/internal/Bits.class */
final class Bits {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final boolean unalignedAccess = false;
    private static final boolean bigEndian;

    static {
        bigEndian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    }

    private Bits() {
    }

    private static short swap(short s2) {
        return Short.reverseBytes(s2);
    }

    private static char swap(char c2) {
        return Character.reverseBytes(c2);
    }

    private static int swap(int i2) {
        return Integer.reverseBytes(i2);
    }

    private static long swap(long j2) {
        return Long.reverseBytes(j2);
    }

    private static float swap(float f2) {
        return Float.intBitsToFloat(swap(Float.floatToIntBits(f2)));
    }

    private static double swap(double d2) {
        return Double.longBitsToDouble(swap(Double.doubleToLongBits(d2)));
    }

    private static boolean isAddressAligned(long j2, int i2) {
        return (j2 & ((long) (i2 - 1))) == 0;
    }

    private static byte char1(char c2) {
        return (byte) (c2 >> '\b');
    }

    private static byte char0(char c2) {
        return (byte) c2;
    }

    private static byte short1(short s2) {
        return (byte) (s2 >> 8);
    }

    private static byte short0(short s2) {
        return (byte) s2;
    }

    private static byte int3(int i2) {
        return (byte) (i2 >> 24);
    }

    private static byte int2(int i2) {
        return (byte) (i2 >> 16);
    }

    private static byte int1(int i2) {
        return (byte) (i2 >> 8);
    }

    private static byte int0(int i2) {
        return (byte) i2;
    }

    private static byte long7(long j2) {
        return (byte) (j2 >> 56);
    }

    private static byte long6(long j2) {
        return (byte) (j2 >> 48);
    }

    private static byte long5(long j2) {
        return (byte) (j2 >> 40);
    }

    private static byte long4(long j2) {
        return (byte) (j2 >> 32);
    }

    private static byte long3(long j2) {
        return (byte) (j2 >> 24);
    }

    private static byte long2(long j2) {
        return (byte) (j2 >> 16);
    }

    private static byte long1(long j2) {
        return (byte) (j2 >> 8);
    }

    private static byte long0(long j2) {
        return (byte) j2;
    }

    private static void putCharBigEndianUnaligned(long j2, char c2) {
        putByte_(j2, char1(c2));
        putByte_(j2 + 1, char0(c2));
    }

    private static void putShortBigEndianUnaligned(long j2, short s2) {
        putByte_(j2, short1(s2));
        putByte_(j2 + 1, short0(s2));
    }

    private static void putIntBigEndianUnaligned(long j2, int i2) {
        putByte_(j2, int3(i2));
        putByte_(j2 + 1, int2(i2));
        putByte_(j2 + 2, int1(i2));
        putByte_(j2 + 3, int0(i2));
    }

    private static void putLongBigEndianUnaligned(long j2, long j3) {
        putByte_(j2, long7(j3));
        putByte_(j2 + 1, long6(j3));
        putByte_(j2 + 2, long5(j3));
        putByte_(j2 + 3, long4(j3));
        putByte_(j2 + 4, long3(j3));
        putByte_(j2 + 5, long2(j3));
        putByte_(j2 + 6, long1(j3));
        putByte_(j2 + 7, long0(j3));
    }

    private static void putFloatBigEndianUnaligned(long j2, float f2) {
        putIntBigEndianUnaligned(j2, Float.floatToRawIntBits(f2));
    }

    private static void putDoubleBigEndianUnaligned(long j2, double d2) {
        putLongBigEndianUnaligned(j2, Double.doubleToRawLongBits(d2));
    }

    private static void putByte_(long j2, byte b2) {
        unsafe.putByte(j2, b2);
    }

    private static void putBoolean_(long j2, boolean z2) {
        unsafe.putBoolean((Object) null, j2, z2);
    }

    private static void putChar_(long j2, char c2) {
        unsafe.putChar(j2, bigEndian ? c2 : swap(c2));
    }

    private static void putShort_(long j2, short s2) {
        unsafe.putShort(j2, bigEndian ? s2 : swap(s2));
    }

    private static void putInt_(long j2, int i2) {
        unsafe.putInt(j2, bigEndian ? i2 : swap(i2));
    }

    private static void putLong_(long j2, long j3) {
        unsafe.putLong(j2, bigEndian ? j3 : swap(j3));
    }

    private static void putFloat_(long j2, float f2) {
        unsafe.putFloat(j2, bigEndian ? f2 : swap(f2));
    }

    private static void putDouble_(long j2, double d2) {
        unsafe.putDouble(j2, bigEndian ? d2 : swap(d2));
    }

    static int putByte(long j2, byte b2) {
        putByte_(j2, b2);
        return 1;
    }

    static int putBoolean(long j2, boolean z2) {
        putBoolean_(j2, z2);
        return 1;
    }

    static int putChar(long j2, char c2) {
        if (isAddressAligned(j2, 2)) {
            putChar_(j2, c2);
            return 2;
        }
        putCharBigEndianUnaligned(j2, c2);
        return 2;
    }

    static int putShort(long j2, short s2) {
        if (isAddressAligned(j2, 2)) {
            putShort_(j2, s2);
            return 2;
        }
        putShortBigEndianUnaligned(j2, s2);
        return 2;
    }

    static int putInt(long j2, int i2) {
        if (isAddressAligned(j2, 4)) {
            putInt_(j2, i2);
            return 4;
        }
        putIntBigEndianUnaligned(j2, i2);
        return 4;
    }

    static int putLong(long j2, long j3) {
        if (isAddressAligned(j2, 8)) {
            putLong_(j2, j3);
            return 8;
        }
        putLongBigEndianUnaligned(j2, j3);
        return 8;
    }

    static int putFloat(long j2, float f2) {
        if (isAddressAligned(j2, 4)) {
            putFloat_(j2, f2);
            return 4;
        }
        putFloatBigEndianUnaligned(j2, f2);
        return 4;
    }

    static int putDouble(long j2, double d2) {
        if (isAddressAligned(j2, 8)) {
            putDouble_(j2, d2);
            return 8;
        }
        putDoubleBigEndianUnaligned(j2, d2);
        return 8;
    }
}
