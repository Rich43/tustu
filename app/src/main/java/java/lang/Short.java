package java.lang;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;

/* loaded from: rt.jar:java/lang/Short.class */
public final class Short extends Number implements Comparable<Short> {
    public static final short MIN_VALUE = Short.MIN_VALUE;
    public static final short MAX_VALUE = Short.MAX_VALUE;
    public static final Class<Short> TYPE = Class.getPrimitiveClass(SchemaSymbols.ATTVAL_SHORT);
    private final short value;
    public static final int SIZE = 16;
    public static final int BYTES = 2;
    private static final long serialVersionUID = 7515723908773894738L;

    public static String toString(short s2) {
        return Integer.toString(s2, 10);
    }

    public static short parseShort(String str, int i2) throws NumberFormatException {
        int i3 = Integer.parseInt(str, i2);
        if (i3 < -32768 || i3 > 32767) {
            throw new NumberFormatException("Value out of range. Value:\"" + str + "\" Radix:" + i2);
        }
        return (short) i3;
    }

    public static short parseShort(String str) throws NumberFormatException {
        return parseShort(str, 10);
    }

    public static Short valueOf(String str, int i2) throws NumberFormatException {
        return valueOf(parseShort(str, i2));
    }

    public static Short valueOf(String str) throws NumberFormatException {
        return valueOf(str, 10);
    }

    /* loaded from: rt.jar:java/lang/Short$ShortCache.class */
    private static class ShortCache {
        static final Short[] cache = new Short[256];

        private ShortCache() {
        }

        static {
            for (int i2 = 0; i2 < cache.length; i2++) {
                cache[i2] = new Short((short) (i2 - 128));
            }
        }
    }

    public static Short valueOf(short s2) {
        if (s2 >= -128 && s2 <= 127) {
            return ShortCache.cache[s2 + 128];
        }
        return new Short(s2);
    }

    public static Short decode(String str) throws NumberFormatException {
        int iIntValue = Integer.decode(str).intValue();
        if (iIntValue < -32768 || iIntValue > 32767) {
            throw new NumberFormatException("Value " + iIntValue + " out of range from input " + str);
        }
        return valueOf((short) iIntValue);
    }

    public Short(short s2) {
        this.value = s2;
    }

    public Short(String str) throws NumberFormatException {
        this.value = parseShort(str, 10);
    }

    @Override // java.lang.Number
    public byte byteValue() {
        return (byte) this.value;
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

    public static int hashCode(short s2) {
        return s2;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Short) && this.value == ((Short) obj).shortValue();
    }

    @Override // java.lang.Comparable
    public int compareTo(Short sh) {
        return compare(this.value, sh.value);
    }

    public static int compare(short s2, short s3) {
        return s2 - s3;
    }

    public static short reverseBytes(short s2) {
        return (short) (((s2 & 65280) >> 8) | (s2 << 8));
    }

    public static int toUnsignedInt(short s2) {
        return s2 & 65535;
    }

    public static long toUnsignedLong(short s2) {
        return s2 & 65535;
    }
}
