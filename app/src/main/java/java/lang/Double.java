package java.lang;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import sun.misc.DoubleConsts;
import sun.misc.FloatingDecimal;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/lang/Double.class */
public final class Double extends Number implements Comparable<Double> {
    public static final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    public static final double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    public static final double NaN = Double.NaN;
    public static final double MAX_VALUE = Double.MAX_VALUE;
    public static final double MIN_NORMAL = Double.MIN_NORMAL;
    public static final double MIN_VALUE = Double.MIN_VALUE;
    public static final int MAX_EXPONENT = 1023;
    public static final int MIN_EXPONENT = -1022;
    public static final int SIZE = 64;
    public static final int BYTES = 8;
    public static final Class<Double> TYPE = Class.getPrimitiveClass(SchemaSymbols.ATTVAL_DOUBLE);
    private final double value;
    private static final long serialVersionUID = -9172774392245257468L;

    public static native long doubleToRawLongBits(double d2);

    public static native double longBitsToDouble(long j2);

    public static String toString(double d2) {
        return FloatingDecimal.toJavaFormatString(d2);
    }

    public static String toHexString(double d2) {
        if (!isFinite(d2)) {
            return toString(d2);
        }
        StringBuilder sb = new StringBuilder(24);
        if (Math.copySign(1.0d, d2) == -1.0d) {
            sb.append(LanguageTag.SEP);
        }
        sb.append("0x");
        double dAbs = Math.abs(d2);
        if (dAbs == 0.0d) {
            sb.append("0.0p0");
        } else {
            boolean z2 = dAbs < Double.MIN_NORMAL;
            long jDoubleToLongBits = (doubleToLongBits(dAbs) & DoubleConsts.SIGNIF_BIT_MASK) | 1152921504606846976L;
            sb.append(z2 ? "0." : "1.");
            String strSubstring = Long.toHexString(jDoubleToLongBits).substring(3, 16);
            sb.append(strSubstring.equals("0000000000000") ? "0" : strSubstring.replaceFirst("0{1,12}$", ""));
            sb.append('p');
            sb.append(z2 ? -1022 : Math.getExponent(dAbs));
        }
        return sb.toString();
    }

    public static Double valueOf(String str) throws NumberFormatException {
        return new Double(parseDouble(str));
    }

    public static Double valueOf(double d2) {
        return new Double(d2);
    }

    public static double parseDouble(String str) throws NumberFormatException {
        return FloatingDecimal.parseDouble(str);
    }

    public static boolean isNaN(double d2) {
        return d2 != d2;
    }

    public static boolean isInfinite(double d2) {
        return d2 == Double.POSITIVE_INFINITY || d2 == Double.NEGATIVE_INFINITY;
    }

    public static boolean isFinite(double d2) {
        return Math.abs(d2) <= Double.MAX_VALUE;
    }

    public Double(double d2) {
        this.value = d2;
    }

    public Double(String str) throws NumberFormatException {
        this.value = parseDouble(str);
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
        return (float) this.value;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public static int hashCode(double d2) {
        long jDoubleToLongBits = doubleToLongBits(d2);
        return (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
    }

    public boolean equals(Object obj) {
        return (obj instanceof Double) && doubleToLongBits(((Double) obj).value) == doubleToLongBits(this.value);
    }

    public static long doubleToLongBits(double d2) {
        long jDoubleToRawLongBits = doubleToRawLongBits(d2);
        if ((jDoubleToRawLongBits & DoubleConsts.EXP_BIT_MASK) == DoubleConsts.EXP_BIT_MASK && (jDoubleToRawLongBits & DoubleConsts.SIGNIF_BIT_MASK) != 0) {
            jDoubleToRawLongBits = 9221120237041090560L;
        }
        return jDoubleToRawLongBits;
    }

    @Override // java.lang.Comparable
    public int compareTo(Double d2) {
        return compare(this.value, d2.value);
    }

    public static int compare(double d2, double d3) {
        if (d2 < d3) {
            return -1;
        }
        if (d2 > d3) {
            return 1;
        }
        long jDoubleToLongBits = doubleToLongBits(d2);
        long jDoubleToLongBits2 = doubleToLongBits(d3);
        if (jDoubleToLongBits == jDoubleToLongBits2) {
            return 0;
        }
        return jDoubleToLongBits < jDoubleToLongBits2 ? -1 : 1;
    }

    public static double sum(double d2, double d3) {
        return d2 + d3;
    }

    public static double max(double d2, double d3) {
        return Math.max(d2, d3);
    }

    public static double min(double d2, double d3) {
        return Math.min(d2, d3);
    }
}
