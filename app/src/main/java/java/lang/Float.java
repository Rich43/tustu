package java.lang;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import sun.misc.FloatConsts;
import sun.misc.FloatingDecimal;

/* loaded from: rt.jar:java/lang/Float.class */
public final class Float extends Number implements Comparable<Float> {
    public static final float POSITIVE_INFINITY = Float.POSITIVE_INFINITY;
    public static final float NEGATIVE_INFINITY = Float.NEGATIVE_INFINITY;
    public static final float NaN = Float.NaN;
    public static final float MAX_VALUE = Float.MAX_VALUE;
    public static final float MIN_NORMAL = Float.MIN_NORMAL;
    public static final float MIN_VALUE = Float.MIN_VALUE;
    public static final int MAX_EXPONENT = 127;
    public static final int MIN_EXPONENT = -126;
    public static final int SIZE = 32;
    public static final int BYTES = 4;
    public static final Class<Float> TYPE = Class.getPrimitiveClass(SchemaSymbols.ATTVAL_FLOAT);
    private final float value;
    private static final long serialVersionUID = -2671257302660747028L;

    public static native int floatToRawIntBits(float f2);

    public static native float intBitsToFloat(int i2);

    public static String toString(float f2) {
        return FloatingDecimal.toJavaFormatString(f2);
    }

    public static String toHexString(float f2) {
        if (Math.abs(f2) < Float.MIN_NORMAL && f2 != 0.0f) {
            return Double.toHexString(Math.scalb(f2, -896)).replaceFirst("p-1022$", "p-126");
        }
        return Double.toHexString(f2);
    }

    public static Float valueOf(String str) throws NumberFormatException {
        return new Float(parseFloat(str));
    }

    public static Float valueOf(float f2) {
        return new Float(f2);
    }

    public static float parseFloat(String str) throws NumberFormatException {
        return FloatingDecimal.parseFloat(str);
    }

    public static boolean isNaN(float f2) {
        return f2 != f2;
    }

    public static boolean isInfinite(float f2) {
        return f2 == Float.POSITIVE_INFINITY || f2 == Float.NEGATIVE_INFINITY;
    }

    public static boolean isFinite(float f2) {
        return Math.abs(f2) <= Float.MAX_VALUE;
    }

    public Float(float f2) {
        this.value = f2;
    }

    public Float(double d2) {
        this.value = (float) d2;
    }

    public Float(String str) throws NumberFormatException {
        this.value = parseFloat(str);
    }

    public boolean isNaN() {
        return isNaN(this.value);
    }

    public boolean isInfinite() {
        return isInfinite(this.value);
    }

    public String toString() {
        return toString(this.value);
    }

    @Override // java.lang.Number
    public byte byteValue() {
        return (byte) this.value;
    }

    @Override // java.lang.Number
    public short shortValue() {
        return (short) this.value;
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) this.value;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public static int hashCode(float f2) {
        return floatToIntBits(f2);
    }

    public boolean equals(Object obj) {
        return (obj instanceof Float) && floatToIntBits(((Float) obj).value) == floatToIntBits(this.value);
    }

    public static int floatToIntBits(float f2) {
        int iFloatToRawIntBits = floatToRawIntBits(f2);
        if ((iFloatToRawIntBits & FloatConsts.EXP_BIT_MASK) == 2139095040 && (iFloatToRawIntBits & FloatConsts.SIGNIF_BIT_MASK) != 0) {
            iFloatToRawIntBits = 2143289344;
        }
        return iFloatToRawIntBits;
    }

    @Override // java.lang.Comparable
    public int compareTo(Float f2) {
        return compare(this.value, f2.value);
    }

    public static int compare(float f2, float f3) {
        if (f2 < f3) {
            return -1;
        }
        if (f2 > f3) {
            return 1;
        }
        int iFloatToIntBits = floatToIntBits(f2);
        int iFloatToIntBits2 = floatToIntBits(f3);
        if (iFloatToIntBits == iFloatToIntBits2) {
            return 0;
        }
        return iFloatToIntBits < iFloatToIntBits2 ? -1 : 1;
    }

    public static float sum(float f2, float f3) {
        return f2 + f3;
    }

    public static float max(float f2, float f3) {
        return Math.max(f2, f3);
    }

    public static float min(float f2, float f3) {
        return Math.min(f2, f3);
    }
}
