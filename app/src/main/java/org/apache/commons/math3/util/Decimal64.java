package org.apache.commons.math3.util;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Decimal64.class */
public class Decimal64 extends Number implements RealFieldElement<Decimal64>, Comparable<Decimal64> {
    public static final Decimal64 ZERO = new Decimal64(0.0d);
    public static final Decimal64 ONE = new Decimal64(1.0d);
    public static final Decimal64 NEGATIVE_INFINITY = new Decimal64(Double.NEGATIVE_INFINITY);
    public static final Decimal64 POSITIVE_INFINITY = new Decimal64(Double.POSITIVE_INFINITY);
    public static final Decimal64 NAN = new Decimal64(Double.NaN);
    private static final long serialVersionUID = 20120227;
    private final double value;

    public Decimal64(double x2) {
        this.value = x2;
    }

    @Override // org.apache.commons.math3.FieldElement
    /* renamed from: getField */
    public Field<Decimal64> getField2() {
        return Decimal64Field.getInstance();
    }

    @Override // org.apache.commons.math3.FieldElement
    public Decimal64 add(Decimal64 a2) {
        return new Decimal64(this.value + a2.value);
    }

    @Override // org.apache.commons.math3.FieldElement
    public Decimal64 subtract(Decimal64 a2) {
        return new Decimal64(this.value - a2.value);
    }

    @Override // org.apache.commons.math3.FieldElement
    public Decimal64 negate() {
        return new Decimal64(-this.value);
    }

    @Override // org.apache.commons.math3.FieldElement
    public Decimal64 multiply(Decimal64 a2) {
        return new Decimal64(this.value * a2.value);
    }

    @Override // org.apache.commons.math3.FieldElement
    public Decimal64 multiply(int n2) {
        return new Decimal64(n2 * this.value);
    }

    @Override // org.apache.commons.math3.FieldElement
    public Decimal64 divide(Decimal64 a2) {
        return new Decimal64(this.value / a2.value);
    }

    @Override // org.apache.commons.math3.RealFieldElement, org.apache.commons.math3.FieldElement
    public Decimal64 reciprocal() {
        return new Decimal64(1.0d / this.value);
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

    @Override // java.lang.Comparable
    public int compareTo(Decimal64 o2) {
        return Double.compare(this.value, o2.value);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Decimal64) {
            Decimal64 that = (Decimal64) obj;
            return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value);
        }
        return false;
    }

    public int hashCode() {
        long v2 = Double.doubleToLongBits(this.value);
        return (int) (v2 ^ (v2 >>> 32));
    }

    public String toString() {
        return Double.toString(this.value);
    }

    public boolean isInfinite() {
        return Double.isInfinite(this.value);
    }

    public boolean isNaN() {
        return Double.isNaN(this.value);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public double getReal() {
        return this.value;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 add(double a2) {
        return new Decimal64(this.value + a2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 subtract(double a2) {
        return new Decimal64(this.value - a2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 multiply(double a2) {
        return new Decimal64(this.value * a2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 divide(double a2) {
        return new Decimal64(this.value / a2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 remainder(double a2) {
        return new Decimal64(FastMath.IEEEremainder(this.value, a2));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 remainder(Decimal64 a2) {
        return new Decimal64(FastMath.IEEEremainder(this.value, a2.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 abs() {
        return new Decimal64(FastMath.abs(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 ceil() {
        return new Decimal64(FastMath.ceil(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 floor() {
        return new Decimal64(FastMath.floor(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 rint() {
        return new Decimal64(FastMath.rint(this.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public long round() {
        return FastMath.round(this.value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 signum() {
        return new Decimal64(FastMath.signum(this.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 copySign(Decimal64 sign) {
        return new Decimal64(FastMath.copySign(this.value, sign.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 copySign(double sign) {
        return new Decimal64(FastMath.copySign(this.value, sign));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 scalb(int n2) {
        return new Decimal64(FastMath.scalb(this.value, n2));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 hypot(Decimal64 y2) {
        return new Decimal64(FastMath.hypot(this.value, y2.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 sqrt() {
        return new Decimal64(FastMath.sqrt(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 cbrt() {
        return new Decimal64(FastMath.cbrt(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 rootN(int n2) {
        if (this.value < 0.0d) {
            return new Decimal64(-FastMath.pow(-this.value, 1.0d / n2));
        }
        return new Decimal64(FastMath.pow(this.value, 1.0d / n2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 pow(double p2) {
        return new Decimal64(FastMath.pow(this.value, p2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 pow(int n2) {
        return new Decimal64(FastMath.pow(this.value, n2));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 pow(Decimal64 e2) {
        return new Decimal64(FastMath.pow(this.value, e2.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 exp() {
        return new Decimal64(FastMath.exp(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 expm1() {
        return new Decimal64(FastMath.expm1(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 log() {
        return new Decimal64(FastMath.log(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 log1p() {
        return new Decimal64(FastMath.log1p(this.value));
    }

    public Decimal64 log10() {
        return new Decimal64(FastMath.log10(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 cos() {
        return new Decimal64(FastMath.cos(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 sin() {
        return new Decimal64(FastMath.sin(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 tan() {
        return new Decimal64(FastMath.tan(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 acos() {
        return new Decimal64(FastMath.acos(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 asin() {
        return new Decimal64(FastMath.asin(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 atan() {
        return new Decimal64(FastMath.atan(this.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 atan2(Decimal64 x2) {
        return new Decimal64(FastMath.atan2(this.value, x2.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 cosh() {
        return new Decimal64(FastMath.cosh(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 sinh() {
        return new Decimal64(FastMath.sinh(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 tanh() {
        return new Decimal64(FastMath.tanh(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 acosh() {
        return new Decimal64(FastMath.acosh(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 asinh() {
        return new Decimal64(FastMath.asinh(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 atanh() {
        return new Decimal64(FastMath.atanh(this.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 linearCombination(Decimal64[] a2, Decimal64[] b2) throws DimensionMismatchException {
        if (a2.length != b2.length) {
            throw new DimensionMismatchException(a2.length, b2.length);
        }
        double[] aDouble = new double[a2.length];
        double[] bDouble = new double[b2.length];
        for (int i2 = 0; i2 < a2.length; i2++) {
            aDouble[i2] = a2[i2].value;
            bDouble[i2] = b2[i2].value;
        }
        return new Decimal64(MathArrays.linearCombination(aDouble, bDouble));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 linearCombination(double[] a2, Decimal64[] b2) throws DimensionMismatchException {
        if (a2.length != b2.length) {
            throw new DimensionMismatchException(a2.length, b2.length);
        }
        double[] bDouble = new double[b2.length];
        for (int i2 = 0; i2 < a2.length; i2++) {
            bDouble[i2] = b2[i2].value;
        }
        return new Decimal64(MathArrays.linearCombination(a2, bDouble));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 linearCombination(Decimal64 a1, Decimal64 b1, Decimal64 a2, Decimal64 b2) {
        return new Decimal64(MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 linearCombination(double a1, Decimal64 b1, double a2, Decimal64 b2) {
        return new Decimal64(MathArrays.linearCombination(a1, b1.value, a2, b2.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 linearCombination(Decimal64 a1, Decimal64 b1, Decimal64 a2, Decimal64 b2, Decimal64 a3, Decimal64 b3) {
        return new Decimal64(MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value, a3.value, b3.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 linearCombination(double a1, Decimal64 b1, double a2, Decimal64 b2, double a3, Decimal64 b3) {
        return new Decimal64(MathArrays.linearCombination(a1, b1.value, a2, b2.value, a3, b3.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 linearCombination(Decimal64 a1, Decimal64 b1, Decimal64 a2, Decimal64 b2, Decimal64 a3, Decimal64 b3, Decimal64 a4, Decimal64 b4) {
        return new Decimal64(MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value, a3.value, b3.value, a4.value, b4.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Decimal64 linearCombination(double a1, Decimal64 b1, double a2, Decimal64 b2, double a3, Decimal64 b3, double a4, Decimal64 b4) {
        return new Decimal64(MathArrays.linearCombination(a1, b1.value, a2, b2.value, a3, b3.value, a4, b4.value));
    }
}
