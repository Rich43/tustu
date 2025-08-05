package java.io;

/* loaded from: rt.jar:java/io/Bits.class */
class Bits {
    Bits() {
    }

    static boolean getBoolean(byte[] bArr, int i2) {
        return bArr[i2] != 0;
    }

    static char getChar(byte[] bArr, int i2) {
        return (char) ((bArr[i2 + 1] & 255) + (bArr[i2] << 8));
    }

    static short getShort(byte[] bArr, int i2) {
        return (short) ((bArr[i2 + 1] & 255) + (bArr[i2] << 8));
    }

    static int getInt(byte[] bArr, int i2) {
        return (bArr[i2 + 3] & 255) + ((bArr[i2 + 2] & 255) << 8) + ((bArr[i2 + 1] & 255) << 16) + (bArr[i2] << 24);
    }

    static float getFloat(byte[] bArr, int i2) {
        return Float.intBitsToFloat(getInt(bArr, i2));
    }

    static long getLong(byte[] bArr, int i2) {
        return (bArr[i2 + 7] & 255) + ((bArr[i2 + 6] & 255) << 8) + ((bArr[i2 + 5] & 255) << 16) + ((bArr[i2 + 4] & 255) << 24) + ((bArr[i2 + 3] & 255) << 32) + ((bArr[i2 + 2] & 255) << 40) + ((bArr[i2 + 1] & 255) << 48) + (bArr[i2] << 56);
    }

    static double getDouble(byte[] bArr, int i2) {
        return Double.longBitsToDouble(getLong(bArr, i2));
    }

    static void putBoolean(byte[] bArr, int i2, boolean z2) {
        bArr[i2] = (byte) (z2 ? 1 : 0);
    }

    static void putChar(byte[] bArr, int i2, char c2) {
        bArr[i2 + 1] = (byte) c2;
        bArr[i2] = (byte) (c2 >>> '\b');
    }

    static void putShort(byte[] bArr, int i2, short s2) {
        bArr[i2 + 1] = (byte) s2;
        bArr[i2] = (byte) (s2 >>> 8);
    }

    static void putInt(byte[] bArr, int i2, int i3) {
        bArr[i2 + 3] = (byte) i3;
        bArr[i2 + 2] = (byte) (i3 >>> 8);
        bArr[i2 + 1] = (byte) (i3 >>> 16);
        bArr[i2] = (byte) (i3 >>> 24);
    }

    static void putFloat(byte[] bArr, int i2, float f2) {
        putInt(bArr, i2, Float.floatToIntBits(f2));
    }

    static void putLong(byte[] bArr, int i2, long j2) {
        bArr[i2 + 7] = (byte) j2;
        bArr[i2 + 6] = (byte) (j2 >>> 8);
        bArr[i2 + 5] = (byte) (j2 >>> 16);
        bArr[i2 + 4] = (byte) (j2 >>> 24);
        bArr[i2 + 3] = (byte) (j2 >>> 32);
        bArr[i2 + 2] = (byte) (j2 >>> 40);
        bArr[i2 + 1] = (byte) (j2 >>> 48);
        bArr[i2] = (byte) (j2 >>> 56);
    }

    static void putDouble(byte[] bArr, int i2, double d2) {
        putLong(bArr, i2, Double.doubleToLongBits(d2));
    }
}
