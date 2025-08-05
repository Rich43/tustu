package org.apache.commons.math3.fraction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import sun.misc.DoubleConsts;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/BigFraction.class */
public class BigFraction extends Number implements FieldElement<BigFraction>, Comparable<BigFraction>, Serializable {
    private static final long serialVersionUID = -5630213147331578515L;
    private final BigInteger numerator;
    private final BigInteger denominator;
    public static final BigFraction TWO = new BigFraction(2);
    public static final BigFraction ONE = new BigFraction(1);
    public static final BigFraction ZERO = new BigFraction(0);
    public static final BigFraction MINUS_ONE = new BigFraction(-1);
    public static final BigFraction FOUR_FIFTHS = new BigFraction(4, 5);
    public static final BigFraction ONE_FIFTH = new BigFraction(1, 5);
    public static final BigFraction ONE_HALF = new BigFraction(1, 2);
    public static final BigFraction ONE_QUARTER = new BigFraction(1, 4);
    public static final BigFraction ONE_THIRD = new BigFraction(1, 3);
    public static final BigFraction THREE_FIFTHS = new BigFraction(3, 5);
    public static final BigFraction THREE_QUARTERS = new BigFraction(3, 4);
    public static final BigFraction TWO_FIFTHS = new BigFraction(2, 5);
    public static final BigFraction TWO_QUARTERS = new BigFraction(2, 4);
    public static final BigFraction TWO_THIRDS = new BigFraction(2, 3);
    private static final BigInteger ONE_HUNDRED = BigInteger.valueOf(100);

    public BigFraction(BigInteger num) {
        this(num, BigInteger.ONE);
    }

    public BigFraction(BigInteger num, BigInteger den) throws NullArgumentException {
        MathUtils.checkNotNull(num, LocalizedFormats.NUMERATOR, new Object[0]);
        MathUtils.checkNotNull(den, LocalizedFormats.DENOMINATOR, new Object[0]);
        if (den.signum() == 0) {
            throw new ZeroException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        if (num.signum() == 0) {
            this.numerator = BigInteger.ZERO;
            this.denominator = BigInteger.ONE;
            return;
        }
        BigInteger gcd = num.gcd(den);
        if (BigInteger.ONE.compareTo(gcd) < 0) {
            num = num.divide(gcd);
            den = den.divide(gcd);
        }
        if (den.signum() == -1) {
            num = num.negate();
            den = den.negate();
        }
        this.numerator = num;
        this.denominator = den;
    }

    public BigFraction(double value) throws MathIllegalArgumentException {
        if (Double.isNaN(value)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NAN_VALUE_CONVERSION, new Object[0]);
        }
        if (Double.isInfinite(value)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_VALUE_CONVERSION, new Object[0]);
        }
        long bits = Double.doubleToLongBits(value);
        long sign = bits & Long.MIN_VALUE;
        long exponent = bits & DoubleConsts.EXP_BIT_MASK;
        long m2 = bits & DoubleConsts.SIGNIF_BIT_MASK;
        m2 = exponent != 0 ? m2 | 4503599627370496L : m2;
        m2 = sign != 0 ? -m2 : m2;
        int k2 = ((int) (exponent >> 52)) - 1075;
        while ((m2 & 9007199254740990L) != 0 && (m2 & 1) == 0) {
            m2 >>= 1;
            k2++;
        }
        if (k2 < 0) {
            this.numerator = BigInteger.valueOf(m2);
            this.denominator = BigInteger.ZERO.flipBit(-k2);
        } else {
            this.numerator = BigInteger.valueOf(m2).multiply(BigInteger.ZERO.flipBit(k2));
            this.denominator = BigInteger.ONE;
        }
    }

    public BigFraction(double value, double epsilon, int maxIterations) throws FractionConversionException {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0117  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private BigFraction(double r10, double r12, int r14, int r15) throws org.apache.commons.math3.fraction.FractionConversionException {
        /*
            Method dump skipped, instructions count: 328
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.fraction.BigFraction.<init>(double, double, int, int):void");
    }

    public BigFraction(double value, int maxDenominator) throws FractionConversionException {
        this(value, 0.0d, maxDenominator, 100);
    }

    public BigFraction(int num) {
        this(BigInteger.valueOf(num), BigInteger.ONE);
    }

    public BigFraction(int num, int den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    public BigFraction(long num) {
        this(BigInteger.valueOf(num), BigInteger.ONE);
    }

    public BigFraction(long num, long den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    public static BigFraction getReducedFraction(int numerator, int denominator) {
        if (numerator == 0) {
            return ZERO;
        }
        return new BigFraction(numerator, denominator);
    }

    public BigFraction abs() {
        return this.numerator.signum() == 1 ? this : negate();
    }

    public BigFraction add(BigInteger bg2) throws NullArgumentException {
        MathUtils.checkNotNull(bg2);
        if (this.numerator.signum() == 0) {
            return new BigFraction(bg2);
        }
        if (bg2.signum() == 0) {
            return this;
        }
        return new BigFraction(this.numerator.add(this.denominator.multiply(bg2)), this.denominator);
    }

    public BigFraction add(int i2) {
        return add(BigInteger.valueOf(i2));
    }

    public BigFraction add(long l2) {
        return add(BigInteger.valueOf(l2));
    }

    @Override // org.apache.commons.math3.FieldElement
    public BigFraction add(BigFraction fraction) {
        BigInteger num;
        BigInteger den;
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        }
        if (fraction.numerator.signum() == 0) {
            return this;
        }
        if (this.numerator.signum() == 0) {
            return fraction;
        }
        if (this.denominator.equals(fraction.denominator)) {
            num = this.numerator.add(fraction.numerator);
            den = this.denominator;
        } else {
            num = this.numerator.multiply(fraction.denominator).add(fraction.numerator.multiply(this.denominator));
            den = this.denominator.multiply(fraction.denominator);
        }
        if (num.signum() == 0) {
            return ZERO;
        }
        return new BigFraction(num, den);
    }

    public BigDecimal bigDecimalValue() {
        return new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator));
    }

    public BigDecimal bigDecimalValue(int roundingMode) {
        return new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator), roundingMode);
    }

    public BigDecimal bigDecimalValue(int scale, int roundingMode) {
        return new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator), scale, roundingMode);
    }

    @Override // java.lang.Comparable
    public int compareTo(BigFraction object) {
        int lhsSigNum = this.numerator.signum();
        int rhsSigNum = object.numerator.signum();
        if (lhsSigNum != rhsSigNum) {
            return lhsSigNum > rhsSigNum ? 1 : -1;
        }
        if (lhsSigNum == 0) {
            return 0;
        }
        BigInteger nOd = this.numerator.multiply(object.denominator);
        BigInteger dOn = this.denominator.multiply(object.numerator);
        return nOd.compareTo(dOn);
    }

    public BigFraction divide(BigInteger bg2) {
        if (bg2 == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        }
        if (bg2.signum() == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        if (this.numerator.signum() == 0) {
            return ZERO;
        }
        return new BigFraction(this.numerator, this.denominator.multiply(bg2));
    }

    public BigFraction divide(int i2) {
        return divide(BigInteger.valueOf(i2));
    }

    public BigFraction divide(long l2) {
        return divide(BigInteger.valueOf(l2));
    }

    @Override // org.apache.commons.math3.FieldElement
    public BigFraction divide(BigFraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        }
        if (fraction.numerator.signum() == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        if (this.numerator.signum() == 0) {
            return ZERO;
        }
        return multiply(fraction.reciprocal());
    }

    @Override // java.lang.Number
    public double doubleValue() {
        double result = this.numerator.doubleValue() / this.denominator.doubleValue();
        if (Double.isNaN(result)) {
            int shift = FastMath.max(this.numerator.bitLength(), this.denominator.bitLength()) - FastMath.getExponent(Double.MAX_VALUE);
            result = this.numerator.shiftRight(shift).doubleValue() / this.denominator.shiftRight(shift).doubleValue();
        }
        return result;
    }

    public boolean equals(Object other) {
        boolean ret = false;
        if (this == other) {
            ret = true;
        } else if (other instanceof BigFraction) {
            BigFraction rhs = ((BigFraction) other).reduce();
            BigFraction thisOne = reduce();
            ret = thisOne.numerator.equals(rhs.numerator) && thisOne.denominator.equals(rhs.denominator);
        }
        return ret;
    }

    @Override // java.lang.Number
    public float floatValue() {
        float result = this.numerator.floatValue() / this.denominator.floatValue();
        if (Double.isNaN(result)) {
            int shift = FastMath.max(this.numerator.bitLength(), this.denominator.bitLength()) - FastMath.getExponent(Float.MAX_VALUE);
            result = this.numerator.shiftRight(shift).floatValue() / this.denominator.shiftRight(shift).floatValue();
        }
        return result;
    }

    public BigInteger getDenominator() {
        return this.denominator;
    }

    public int getDenominatorAsInt() {
        return this.denominator.intValue();
    }

    public long getDenominatorAsLong() {
        return this.denominator.longValue();
    }

    public BigInteger getNumerator() {
        return this.numerator;
    }

    public int getNumeratorAsInt() {
        return this.numerator.intValue();
    }

    public long getNumeratorAsLong() {
        return this.numerator.longValue();
    }

    public int hashCode() {
        return (37 * (629 + this.numerator.hashCode())) + this.denominator.hashCode();
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.numerator.divide(this.denominator).intValue();
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.numerator.divide(this.denominator).longValue();
    }

    public BigFraction multiply(BigInteger bg2) {
        if (bg2 == null) {
            throw new NullArgumentException();
        }
        if (this.numerator.signum() == 0 || bg2.signum() == 0) {
            return ZERO;
        }
        return new BigFraction(bg2.multiply(this.numerator), this.denominator);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public BigFraction multiply(int i2) {
        if (i2 == 0 || this.numerator.signum() == 0) {
            return ZERO;
        }
        return multiply(BigInteger.valueOf(i2));
    }

    public BigFraction multiply(long l2) {
        if (l2 == 0 || this.numerator.signum() == 0) {
            return ZERO;
        }
        return multiply(BigInteger.valueOf(l2));
    }

    @Override // org.apache.commons.math3.FieldElement
    public BigFraction multiply(BigFraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        }
        if (this.numerator.signum() == 0 || fraction.numerator.signum() == 0) {
            return ZERO;
        }
        return new BigFraction(this.numerator.multiply(fraction.numerator), this.denominator.multiply(fraction.denominator));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public BigFraction negate() {
        return new BigFraction(this.numerator.negate(), this.denominator);
    }

    public double percentageValue() {
        return multiply(ONE_HUNDRED).doubleValue();
    }

    public BigFraction pow(int exponent) {
        if (exponent == 0) {
            return ONE;
        }
        if (this.numerator.signum() == 0) {
            return this;
        }
        if (exponent < 0) {
            return new BigFraction(this.denominator.pow(-exponent), this.numerator.pow(-exponent));
        }
        return new BigFraction(this.numerator.pow(exponent), this.denominator.pow(exponent));
    }

    public BigFraction pow(long exponent) {
        if (exponent == 0) {
            return ONE;
        }
        if (this.numerator.signum() == 0) {
            return this;
        }
        if (exponent < 0) {
            return new BigFraction(ArithmeticUtils.pow(this.denominator, -exponent), ArithmeticUtils.pow(this.numerator, -exponent));
        }
        return new BigFraction(ArithmeticUtils.pow(this.numerator, exponent), ArithmeticUtils.pow(this.denominator, exponent));
    }

    public BigFraction pow(BigInteger exponent) {
        if (exponent.signum() == 0) {
            return ONE;
        }
        if (this.numerator.signum() == 0) {
            return this;
        }
        if (exponent.signum() == -1) {
            BigInteger eNeg = exponent.negate();
            return new BigFraction(ArithmeticUtils.pow(this.denominator, eNeg), ArithmeticUtils.pow(this.numerator, eNeg));
        }
        return new BigFraction(ArithmeticUtils.pow(this.numerator, exponent), ArithmeticUtils.pow(this.denominator, exponent));
    }

    public double pow(double exponent) {
        return FastMath.pow(this.numerator.doubleValue(), exponent) / FastMath.pow(this.denominator.doubleValue(), exponent);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public BigFraction reciprocal() {
        return new BigFraction(this.denominator, this.numerator);
    }

    public BigFraction reduce() {
        BigInteger gcd = this.numerator.gcd(this.denominator);
        if (BigInteger.ONE.compareTo(gcd) < 0) {
            return new BigFraction(this.numerator.divide(gcd), this.denominator.divide(gcd));
        }
        return this;
    }

    public BigFraction subtract(BigInteger bg2) {
        if (bg2 == null) {
            throw new NullArgumentException();
        }
        if (bg2.signum() == 0) {
            return this;
        }
        if (this.numerator.signum() == 0) {
            return new BigFraction(bg2.negate());
        }
        return new BigFraction(this.numerator.subtract(this.denominator.multiply(bg2)), this.denominator);
    }

    public BigFraction subtract(int i2) {
        return subtract(BigInteger.valueOf(i2));
    }

    public BigFraction subtract(long l2) {
        return subtract(BigInteger.valueOf(l2));
    }

    @Override // org.apache.commons.math3.FieldElement
    public BigFraction subtract(BigFraction fraction) {
        BigInteger num;
        BigInteger den;
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        }
        if (fraction.numerator.signum() == 0) {
            return this;
        }
        if (this.numerator.signum() == 0) {
            return fraction.negate();
        }
        if (this.denominator.equals(fraction.denominator)) {
            num = this.numerator.subtract(fraction.numerator);
            den = this.denominator;
        } else {
            num = this.numerator.multiply(fraction.denominator).subtract(fraction.numerator.multiply(this.denominator));
            den = this.denominator.multiply(fraction.denominator);
        }
        return new BigFraction(num, den);
    }

    public String toString() {
        String str;
        if (BigInteger.ONE.equals(this.denominator)) {
            str = this.numerator.toString();
        } else if (BigInteger.ZERO.equals(this.numerator)) {
            str = "0";
        } else {
            str = ((Object) this.numerator) + " / " + ((Object) this.denominator);
        }
        return str;
    }

    @Override // org.apache.commons.math3.FieldElement
    /* renamed from: getField */
    public Field<BigFraction> getField2() {
        return BigFractionField.getInstance();
    }
}
