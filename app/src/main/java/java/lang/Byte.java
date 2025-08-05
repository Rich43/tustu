package java.lang;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;

/* loaded from: rt.jar:java/lang/Byte.class */
public final class Byte extends Number implements Comparable<Byte> {
    public static final byte MIN_VALUE = Byte.MIN_VALUE;
    public static final byte MAX_VALUE = Byte.MAX_VALUE;
    public static final Class<Byte> TYPE = Class.getPrimitiveClass(SchemaSymbols.ATTVAL_BYTE);
    private final byte value;
    public static final int SIZE = 8;
    public static final int BYTES = 1;
    private static final long serialVersionUID = -7183698231559129828L;

    public static String toString(byte b2) {
        return Integer.toString(b2, 10);
    }

    /* loaded from: rt.jar:java/lang/Byte$ByteCache.class */
    private static class ByteCache {
        static final Byte[] cache = new Byte[256];

        private ByteCache() {
        }

        static {
            for (int i2 = 0; i2 < cache.length; i2++) {
                cache[i2] = new Byte((byte) (i2 - 128));
            }
        }
    }

    public static Byte valueOf(byte b2) {
        return ByteCache.cache[b2 + 128];
    }

    public static byte parseByte(String str, int i2) throws NumberFormatException {
        int i3 = Integer.parseInt(str, i2);
        if (i3 < -128 || i3 > 127) {
            throw new NumberFormatException("Value out of range. Value:\"" + str + "\" Radix:" + i2);
        }
        return (byte) i3;
    }

    public static byte parseByte(String str) throws NumberFormatException {
        return parseByte(str, 10);
    }

    public static Byte valueOf(String str, int i2) throws NumberFormatException {
        return valueOf(parseByte(str, i2));
    }

    public static Byte valueOf(String str) throws NumberFormatException {
        return valueOf(str, 10);
    }

    public static Byte decode(String str) throws NumberFormatException {
        int iIntValue = Integer.decode(str).intValue();
        if (iIntValue < -128 || iIntValue > 127) {
            throw new NumberFormatException("Value " + iIntValue + " out of range from input " + str);
        }
        return valueOf((byte) iIntValue);
    }

    public Byte(byte b2) {
        this.value = b2;
    }

    public Byte(String str) throws NumberFormatException {
        this.value = parseByte(str, 10);
    }

    @Override // java.lang.Number
    public byte byteValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public short shortValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public String toString() {
        return Integer.toString(this.value);
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public static int hashCode(byte b2) {
        return b2;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Byte) && this.value == ((Byte) obj).byteValue();
    }

    @Override // java.lang.Comparable
    public int compareTo(Byte b2) {
        return compare(this.value, b2.value);
    }

    public static int compare(byte b2, byte b3) {
        return b2 - b3;
    }

    public static int toUnsignedInt(byte b2) {
        return b2 & 255;
    }

    public static long toUnsignedLong(byte b2) {
        return b2 & 255;
    }
}
