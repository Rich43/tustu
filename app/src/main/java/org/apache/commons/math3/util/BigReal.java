package org.apache.commons.math3.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/BigReal.class */
public class BigReal implements FieldElement<BigReal>, Comparable<BigReal>, Serializable {
    public static final BigReal ZERO = new BigReal(BigDecimal.ZERO);
    public static final BigReal ONE = new BigReal(BigDecimal.ONE);
    private static final long serialVersionUID = 4984534880991310382L;

    /* renamed from: d, reason: collision with root package name */
    private final BigDecimal f13112d;
    private RoundingMode roundingMode = RoundingMode.HALF_UP;
    private int scale = 64;

    public BigReal(BigDecimal val) {
        this.f13112d = val;
    }

    public BigReal(BigInteger val) {
        this.f13112d = new BigDecimal(val);
    }

    public BigReal(BigInteger unscaledVal, int scale) {
        this.f13112d = new BigDecimal(unscaledVal, scale);
    }

    public BigReal(BigInteger unscaledVal, int scale, MathContext mc) {
        this.f13112d = new BigDecimal(unscaledVal, scale, mc);
    }

    public BigReal(BigInteger val, MathContext mc) {
        this.f13112d = new BigDecimal(val, mc);
    }

    public BigReal(char[] in) {
        this.f13112d = new BigDecimal(in);
    }

    public BigReal(char[] in, int offset, int len) {
        this.f13112d = new BigDecimal(in, offset, len);
    }

    public BigReal(char[] in, int offset, int len, MathContext mc) {
        this.f13112d = new BigDecimal(in, offset, len, mc);
    }

    public BigReal(char[] in, MathContext mc) {
        this.f13112d = new BigDecimal(in, mc);
    }

    public BigReal(double val) {
        this.f13112d = new BigDecimal(val);
    }

    public BigReal(double val, MathContext mc) {
        this.f13112d = new BigDecimal(val, mc);
    }

    public BigReal(int val) {
        this.f13112d = new BigDecimal(val);
    }

    public BigReal(int val, MathContext mc) {
        this.f13112d = new BigDecimal(val, mc);
    }

    public BigReal(long val) {
        this.f13112d = new BigDecimal(val);
    }

    public BigReal(long val, MathContext mc) {
        this.f13112d = new BigDecimal(val, mc);
    }

    public BigReal(String val) {
        this.f13112d = new BigDecimal(val);
    }

    public BigReal(String val, MathContext mc) {
        this.f13112d = new BigDecimal(val, mc);
    }

    public RoundingMode getRoundingMode() {
        return this.roundingMode;
    }

    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override // org.apache.commons.math3.FieldElement
    public BigReal add(BigReal a2) {
        return new BigReal(this.f13112d.add(a2.f13112d));
    }

    @Override // org.apache.commons.math3.FieldElement
    public BigReal subtract(BigReal a2) {
        return new BigReal(this.f13112d.subtract(a2.f13112d));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public BigReal negate() {
        return new BigReal(this.f13112d.negate());
    }

    @Override // org.apache.commons.math3.FieldElement
    public BigReal divide(BigReal a2) throws MathArithmeticException {
        try {
            return new BigReal(this.f13112d.divide(a2.f13112d, this.scale, this.roundingMode));
        } catch (ArithmeticException e2) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public BigReal reciprocal() throws MathArithmeticException {
        try {
            return new BigReal(BigDecimal.ONE.divide(this.f13112d, this.scale, this.roundingMode));
        } catch (ArithmeticException e2) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
        }
    }

    @Override // org.apache.commons.math3.FieldElement
    public BigReal multiply(BigReal a2) {
        return new BigReal(this.f13112d.multiply(a2.f13112d));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.FieldElement
    public BigReal multiply(int n2) {
        return new BigReal(this.f13112d.multiply(new BigDecimal(n2)));
    }

    @Override // java.lang.Comparable
    public int compareTo(BigReal a2) {
        return this.f13112d.compareTo(a2.f13112d);
    }

    public double doubleValue() {
        return this.f13112d.doubleValue();
    }

    public BigDecimal bigDecimalValue() {
        return this.f13112d;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof BigReal) {
            return this.f13112d.equals(((BigReal) other).f13112d);
        }
        return false;
    }

    public int hashCode() {
        return this.f13112d.hashCode();
    }

    @Override // org.apache.commons.math3.FieldElement
    /* renamed from: getField */
    public Field<BigReal> getField2() {
        return BigRealField.getInstance();
    }
}
