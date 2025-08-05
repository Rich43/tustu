package org.apache.commons.math3.fraction;

import java.io.Serializable;
import java.math.BigInteger;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/Fraction.class */
public class Fraction extends Number implements FieldElement<Fraction>, Comparable<Fraction>, Serializable {
    public static final Fraction TWO = new Fraction(2, 1);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
    public static final Fraction ONE_FIFTH = new Fraction(1, 5);
    public static final Fraction ONE_HALF = new Fraction(1, 2);
    public static final Fraction ONE_QUARTER = new Fraction(1, 4);
    public static final Fraction ONE_THIRD = new Fraction(1, 3);
    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
    public static final Fraction TWO_THIRDS = new Fraction(2, 3);
    public static final Fraction MINUS_ONE = new Fraction(-1, 1);
    private static final long serialVersionUID = 3698073679419233275L;
    private static final double DEFAULT_EPSILON = 1.0E-5d;
    private final int denominator;
    private final int numerator;

    public Fraction(double value) throws FractionConversionException {
        this(value, DEFAULT_EPSILON, 100);
    }

    public Fraction(double value, double epsilon, int maxIterations) throws FractionConversionException {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    public Fraction(double value, int maxDenominator) throws FractionConversionException {
        this(value, 0.0d, maxDenominator, 100);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x010e  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0119  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private Fraction(double r10, double r12, int r14, int r15) throws org.apache.commons.math3.fraction.FractionConversionException {
        /*
            Method dump skipped, instructions count: 322
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.fraction.Fraction.<init>(double, double, int, int):void");
    }

    public Fraction(int num) {
        this(num, 1);
    }

    public Fraction(int num, int den) throws MathArithmeticException {
        if (den == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, Integer.valueOf(num), Integer.valueOf(den));
        }
        if (den < 0) {
            if (num == Integer.MIN_VALUE || den == Integer.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, Integer.valueOf(num), Integer.valueOf(den));
            }
            num = -num;
            den = -den;
        }
        int d2 = ArithmeticUtils.gcd(num, den);
        if (d2 > 1) {
            num /= d2;
            den /= d2;
        }
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.numerator = num;
        this.denominator = den;
    }

    public Fraction abs() {
        Fraction ret;
        if (this.numerator >= 0) {
            ret = this;
        } else {
            ret = negate();
        }
        return ret;
    }

    @Override // java.lang.Comparable
    public int compareTo(Fraction object) {
        long nOd = this.numerator * object.denominator;
        long dOn = this.denominator * object.numerator;
        if (nOd < dOn) {
            return -1;
        }
        return nOd > dOn ? 1 : 0;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.numerator / this.denominator;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Fraction) {
            Fraction rhs = (Fraction) other;
            return this.numerator == rhs.numerator && this.denominator == rhs.denominator;
        }
        return false;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) doubleValue();
    }

    public int getDenominator() {
        return this.denominator;
    }

    public int getNumerator() {
        return this.numerator;
    }

    public int hashCode() {
        return (37 * (629 + this.numerator)) + this.denominator;
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) doubleValue();
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) doubleValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public Fraction negate() {
        if (this.numerator == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, Integer.valueOf(this.numerator), Integer.valueOf(this.denominator));
        }
        return new Fraction(-this.numerator, this.denominator);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public Fraction reciprocal() {
        return new Fraction(this.denominator, this.numerator);
    }

    @Override // org.apache.commons.math3.FieldElement
    public Fraction add(Fraction fraction) {
        return addSub(fraction, true);
    }

    public Fraction add(int i2) {
        return new Fraction(this.numerator + (i2 * this.denominator), this.denominator);
    }

    @Override // org.apache.commons.math3.FieldElement
    public Fraction subtract(Fraction fraction) {
        return addSub(fraction, false);
    }

    public Fraction subtract(int i2) {
        return new Fraction(this.numerator - (i2 * this.denominator), this.denominator);
    }

    private Fraction addSub(Fraction fraction, boolean isAdd) throws MathArithmeticException {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        }
        if (this.numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this;
        }
        int d1 = ArithmeticUtils.gcd(this.denominator, fraction.denominator);
        if (d1 == 1) {
            int uvp = ArithmeticUtils.mulAndCheck(this.numerator, fraction.denominator);
            int upv = ArithmeticUtils.mulAndCheck(fraction.numerator, this.denominator);
            return new Fraction(isAdd ? ArithmeticUtils.addAndCheck(uvp, upv) : ArithmeticUtils.subAndCheck(uvp, upv), ArithmeticUtils.mulAndCheck(this.denominator, fraction.denominator));
        }
        BigInteger uvp2 = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf(fraction.denominator / d1));
        BigInteger upv2 = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf(this.denominator / d1));
        BigInteger t2 = isAdd ? uvp2.add(upv2) : uvp2.subtract(upv2);
        int tmodd1 = t2.mod(BigInteger.valueOf(d1)).intValue();
        int d2 = tmodd1 == 0 ? d1 : ArithmeticUtils.gcd(tmodd1, d1);
        BigInteger w2 = t2.divide(BigInteger.valueOf(d2));
        if (w2.bitLength() > 31) {
            throw new MathArithmeticException(LocalizedFormats.NUMERATOR_OVERFLOW_AFTER_MULTIPLY, w2);
        }
        return new Fraction(w2.intValue(), ArithmeticUtils.mulAndCheck(this.denominator / d1, fraction.denominator / d2));
    }

    @Override // org.apache.commons.math3.FieldElement
    public Fraction multiply(Fraction fraction) throws MathArithmeticException {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        }
        if (this.numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        }
        int d1 = ArithmeticUtils.gcd(this.numerator, fraction.denominator);
        int d2 = ArithmeticUtils.gcd(fraction.numerator, this.denominator);
        return getReducedFraction(ArithmeticUtils.mulAndCheck(this.numerator / d1, fraction.numerator / d2), ArithmeticUtils.mulAndCheck(this.denominator / d2, fraction.denominator / d1));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public Fraction multiply(int i2) {
        return multiply(new Fraction(i2));
    }

    @Override // org.apache.commons.math3.FieldElement
    public Fraction divide(Fraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        }
        if (fraction.numerator == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY, Integer.valueOf(fraction.numerator), Integer.valueOf(fraction.denominator));
        }
        return multiply(fraction.reciprocal());
    }

    public Fraction divide(int i2) {
        return divide(new Fraction(i2));
    }

    public double percentageValue() {
        return 100.0d * doubleValue();
    }

    public static Fraction getReducedFraction(int numerator, int denominator) throws MathArithmeticException {
        if (denominator == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, Integer.valueOf(numerator), Integer.valueOf(denominator));
        }
        if (numerator == 0) {
            return ZERO;
        }
        if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
            numerator /= 2;
            denominator /= 2;
        }
        if (denominator < 0) {
            if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, Integer.valueOf(numerator), Integer.valueOf(denominator));
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        int gcd = ArithmeticUtils.gcd(numerator, denominator);
        return new Fraction(numerator / gcd, denominator / gcd);
    }

    public String toString() {
        String str;
        if (this.denominator == 1) {
            str = Integer.toString(this.numerator);
        } else if (this.numerator == 0) {
            str = "0";
        } else {
            str = this.numerator + " / " + this.denominator;
        }
        return str;
    }

    @Override // org.apache.commons.math3.FieldElement
    /* renamed from: getField */
    public Field<Fraction> getField2() {
        return FractionField.getInstance();
    }
}
