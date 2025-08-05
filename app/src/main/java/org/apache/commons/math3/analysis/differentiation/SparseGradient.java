package org.apache.commons.math3.analysis.differentiation;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/SparseGradient.class */
public class SparseGradient implements RealFieldElement<SparseGradient>, Serializable {
    private static final long serialVersionUID = 20131025;
    private double value;
    private final Map<Integer, Double> derivatives = new HashMap();

    private SparseGradient(double value, Map<Integer, Double> derivatives) {
        this.value = value;
        if (derivatives != null) {
            this.derivatives.putAll(derivatives);
        }
    }

    private SparseGradient(double value, double scale, Map<Integer, Double> derivatives) {
        this.value = value;
        if (derivatives != null) {
            for (Map.Entry<Integer, Double> entry : derivatives.entrySet()) {
                this.derivatives.put(entry.getKey(), Double.valueOf(scale * entry.getValue().doubleValue()));
            }
        }
    }

    public static SparseGradient createConstant(double value) {
        return new SparseGradient(value, Collections.emptyMap());
    }

    public static SparseGradient createVariable(int idx, double value) {
        return new SparseGradient(value, Collections.singletonMap(Integer.valueOf(idx), Double.valueOf(1.0d)));
    }

    public int numVars() {
        return this.derivatives.size();
    }

    public double getDerivative(int index) {
        Double out = this.derivatives.get(Integer.valueOf(index));
        if (out == null) {
            return 0.0d;
        }
        return out.doubleValue();
    }

    public double getValue() {
        return this.value;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public double getReal() {
        return this.value;
    }

    @Override // org.apache.commons.math3.FieldElement
    public SparseGradient add(SparseGradient a2) {
        SparseGradient out = new SparseGradient(this.value + a2.value, this.derivatives);
        for (Map.Entry<Integer, Double> entry : a2.derivatives.entrySet()) {
            int id = entry.getKey().intValue();
            Double old = out.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                out.derivatives.put(Integer.valueOf(id), entry.getValue());
            } else {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() + entry.getValue().doubleValue()));
            }
        }
        return out;
    }

    public void addInPlace(SparseGradient a2) {
        this.value += a2.value;
        for (Map.Entry<Integer, Double> entry : a2.derivatives.entrySet()) {
            int id = entry.getKey().intValue();
            Double old = this.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                this.derivatives.put(Integer.valueOf(id), entry.getValue());
            } else {
                this.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() + entry.getValue().doubleValue()));
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient add(double c2) {
        SparseGradient out = new SparseGradient(this.value + c2, this.derivatives);
        return out;
    }

    @Override // org.apache.commons.math3.FieldElement
    public SparseGradient subtract(SparseGradient a2) {
        SparseGradient out = new SparseGradient(this.value - a2.value, this.derivatives);
        for (Map.Entry<Integer, Double> entry : a2.derivatives.entrySet()) {
            int id = entry.getKey().intValue();
            Double old = out.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(-entry.getValue().doubleValue()));
            } else {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() - entry.getValue().doubleValue()));
            }
        }
        return out;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient subtract(double c2) {
        return new SparseGradient(this.value - c2, this.derivatives);
    }

    @Override // org.apache.commons.math3.FieldElement
    public SparseGradient multiply(SparseGradient a2) {
        SparseGradient out = new SparseGradient(this.value * a2.value, Collections.emptyMap());
        for (Map.Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            out.derivatives.put(entry.getKey(), Double.valueOf(a2.value * entry.getValue().doubleValue()));
        }
        for (Map.Entry<Integer, Double> entry2 : a2.derivatives.entrySet()) {
            int id = entry2.getKey().intValue();
            Double old = out.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(this.value * entry2.getValue().doubleValue()));
            } else {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() + (this.value * entry2.getValue().doubleValue())));
            }
        }
        return out;
    }

    public void multiplyInPlace(SparseGradient a2) {
        for (Map.Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            this.derivatives.put(entry.getKey(), Double.valueOf(a2.value * entry.getValue().doubleValue()));
        }
        for (Map.Entry<Integer, Double> entry2 : a2.derivatives.entrySet()) {
            int id = entry2.getKey().intValue();
            Double old = this.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                this.derivatives.put(Integer.valueOf(id), Double.valueOf(this.value * entry2.getValue().doubleValue()));
            } else {
                this.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() + (this.value * entry2.getValue().doubleValue())));
            }
        }
        this.value *= a2.value;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient multiply(double c2) {
        return new SparseGradient(this.value * c2, c2, this.derivatives);
    }

    @Override // org.apache.commons.math3.FieldElement
    public SparseGradient multiply(int n2) {
        return new SparseGradient(this.value * n2, n2, this.derivatives);
    }

    @Override // org.apache.commons.math3.FieldElement
    public SparseGradient divide(SparseGradient a2) {
        SparseGradient out = new SparseGradient(this.value / a2.value, Collections.emptyMap());
        for (Map.Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            out.derivatives.put(entry.getKey(), Double.valueOf(entry.getValue().doubleValue() / a2.value));
        }
        for (Map.Entry<Integer, Double> entry2 : a2.derivatives.entrySet()) {
            int id = entry2.getKey().intValue();
            Double old = out.derivatives.get(Integer.valueOf(id));
            if (old == null) {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(((-out.value) / a2.value) * entry2.getValue().doubleValue()));
            } else {
                out.derivatives.put(Integer.valueOf(id), Double.valueOf(old.doubleValue() - ((out.value / a2.value) * entry2.getValue().doubleValue())));
            }
        }
        return out;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient divide(double c2) {
        return new SparseGradient(this.value / c2, 1.0d / c2, this.derivatives);
    }

    @Override // org.apache.commons.math3.FieldElement
    public SparseGradient negate() {
        return new SparseGradient(-this.value, -1.0d, this.derivatives);
    }

    @Override // org.apache.commons.math3.FieldElement
    /* renamed from: getField */
    public Field<SparseGradient> getField2() {
        return new Field<SparseGradient>() { // from class: org.apache.commons.math3.analysis.differentiation.SparseGradient.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.apache.commons.math3.Field
            public SparseGradient getZero() {
                return SparseGradient.createConstant(0.0d);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.apache.commons.math3.Field
            public SparseGradient getOne() {
                return SparseGradient.createConstant(1.0d);
            }

            @Override // org.apache.commons.math3.Field
            public Class<? extends FieldElement<SparseGradient>> getRuntimeClass() {
                return SparseGradient.class;
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient remainder(double a2) {
        return new SparseGradient(FastMath.IEEEremainder(this.value, a2), this.derivatives);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient remainder(SparseGradient a2) {
        double rem = FastMath.IEEEremainder(this.value, a2.value);
        double k2 = FastMath.rint((this.value - rem) / a2.value);
        return subtract(a2.multiply(k2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient abs() {
        if (Double.doubleToLongBits(this.value) < 0) {
            return negate();
        }
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient ceil() {
        return createConstant(FastMath.ceil(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient floor() {
        return createConstant(FastMath.floor(this.value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient rint() {
        return createConstant(FastMath.rint(this.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public long round() {
        return FastMath.round(this.value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient signum() {
        return createConstant(FastMath.signum(this.value));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient copySign(SparseGradient sign) {
        long m2 = Double.doubleToLongBits(this.value);
        long s2 = Double.doubleToLongBits(sign.value);
        if ((m2 >= 0 && s2 >= 0) || (m2 < 0 && s2 < 0)) {
            return this;
        }
        return negate();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient copySign(double sign) {
        long m2 = Double.doubleToLongBits(this.value);
        long s2 = Double.doubleToLongBits(sign);
        if ((m2 >= 0 && s2 >= 0) || (m2 < 0 && s2 < 0)) {
            return this;
        }
        return negate();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient scalb(int n2) {
        SparseGradient out = new SparseGradient(FastMath.scalb(this.value, n2), Collections.emptyMap());
        for (Map.Entry<Integer, Double> entry : this.derivatives.entrySet()) {
            out.derivatives.put(entry.getKey(), Double.valueOf(FastMath.scalb(entry.getValue().doubleValue(), n2)));
        }
        return out;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient hypot(SparseGradient y2) {
        if (Double.isInfinite(this.value) || Double.isInfinite(y2.value)) {
            return createConstant(Double.POSITIVE_INFINITY);
        }
        if (Double.isNaN(this.value) || Double.isNaN(y2.value)) {
            return createConstant(Double.NaN);
        }
        int expX = FastMath.getExponent(this.value);
        int expY = FastMath.getExponent(y2.value);
        if (expX > expY + 27) {
            return abs();
        }
        if (expY > expX + 27) {
            return y2.abs();
        }
        int middleExp = (expX + expY) / 2;
        SparseGradient scaledX = scalb(-middleExp);
        SparseGradient scaledY = y2.scalb(-middleExp);
        SparseGradient scaledH = scaledX.multiply(scaledX).add(scaledY.multiply(scaledY)).sqrt();
        return scaledH.scalb(middleExp);
    }

    public static SparseGradient hypot(SparseGradient x2, SparseGradient y2) {
        return x2.hypot(y2);
    }

    @Override // org.apache.commons.math3.RealFieldElement, org.apache.commons.math3.FieldElement
    public SparseGradient reciprocal() {
        return new SparseGradient(1.0d / this.value, (-1.0d) / (this.value * this.value), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient sqrt() {
        double sqrt = FastMath.sqrt(this.value);
        return new SparseGradient(sqrt, 0.5d / sqrt, this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient cbrt() {
        double cbrt = FastMath.cbrt(this.value);
        return new SparseGradient(cbrt, 1.0d / ((3.0d * cbrt) * cbrt), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient rootN(int n2) {
        if (n2 == 2) {
            return sqrt();
        }
        if (n2 == 3) {
            return cbrt();
        }
        double root = FastMath.pow(this.value, 1.0d / n2);
        return new SparseGradient(root, 1.0d / (n2 * FastMath.pow(root, n2 - 1)), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient pow(double p2) {
        return new SparseGradient(FastMath.pow(this.value, p2), p2 * FastMath.pow(this.value, p2 - 1.0d), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient pow(int n2) {
        if (n2 == 0) {
            return getField2().getOne();
        }
        double valueNm1 = FastMath.pow(this.value, n2 - 1);
        return new SparseGradient(this.value * valueNm1, n2 * valueNm1, this.derivatives);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient pow(SparseGradient e2) {
        return log().multiply(e2).exp();
    }

    public static SparseGradient pow(double a2, SparseGradient x2) {
        if (a2 != 0.0d) {
            double ax2 = FastMath.pow(a2, x2.value);
            return new SparseGradient(ax2, ax2 * FastMath.log(a2), x2.derivatives);
        }
        if (x2.value == 0.0d) {
            return x2.compose(1.0d, Double.NEGATIVE_INFINITY);
        }
        if (x2.value < 0.0d) {
            return x2.compose(Double.NaN, Double.NaN);
        }
        return x2.getField2().getZero();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient exp() {
        double e2 = FastMath.exp(this.value);
        return new SparseGradient(e2, e2, this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient expm1() {
        return new SparseGradient(FastMath.expm1(this.value), FastMath.exp(this.value), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient log() {
        return new SparseGradient(FastMath.log(this.value), 1.0d / this.value, this.derivatives);
    }

    public SparseGradient log10() {
        return new SparseGradient(FastMath.log10(this.value), 1.0d / (FastMath.log(10.0d) * this.value), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient log1p() {
        return new SparseGradient(FastMath.log1p(this.value), 1.0d / (1.0d + this.value), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient cos() {
        return new SparseGradient(FastMath.cos(this.value), -FastMath.sin(this.value), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient sin() {
        return new SparseGradient(FastMath.sin(this.value), FastMath.cos(this.value), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient tan() {
        double t2 = FastMath.tan(this.value);
        return new SparseGradient(t2, 1.0d + (t2 * t2), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient acos() {
        return new SparseGradient(FastMath.acos(this.value), (-1.0d) / FastMath.sqrt(1.0d - (this.value * this.value)), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient asin() {
        return new SparseGradient(FastMath.asin(this.value), 1.0d / FastMath.sqrt(1.0d - (this.value * this.value)), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient atan() {
        return new SparseGradient(FastMath.atan(this.value), 1.0d / (1.0d + (this.value * this.value)), this.derivatives);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient atan2(SparseGradient x2) {
        SparseGradient a2;
        SparseGradient r2 = multiply(this).add(x2.multiply(x2)).sqrt();
        if (x2.value >= 0.0d) {
            a2 = divide(r2.add(x2)).atan().multiply(2);
        } else {
            SparseGradient tmp = divide(r2.subtract(x2)).atan().multiply(-2);
            a2 = tmp.add(tmp.value <= 0.0d ? -3.141592653589793d : 3.141592653589793d);
        }
        a2.value = FastMath.atan2(this.value, x2.value);
        return a2;
    }

    public static SparseGradient atan2(SparseGradient y2, SparseGradient x2) {
        return y2.atan2(x2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient cosh() {
        return new SparseGradient(FastMath.cosh(this.value), FastMath.sinh(this.value), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient sinh() {
        return new SparseGradient(FastMath.sinh(this.value), FastMath.cosh(this.value), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient tanh() {
        double t2 = FastMath.tanh(this.value);
        return new SparseGradient(t2, 1.0d - (t2 * t2), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient acosh() {
        return new SparseGradient(FastMath.acosh(this.value), 1.0d / FastMath.sqrt((this.value * this.value) - 1.0d), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient asinh() {
        return new SparseGradient(FastMath.asinh(this.value), 1.0d / FastMath.sqrt((this.value * this.value) + 1.0d), this.derivatives);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient atanh() {
        return new SparseGradient(FastMath.atanh(this.value), 1.0d / (1.0d - (this.value * this.value)), this.derivatives);
    }

    public SparseGradient toDegrees() {
        return new SparseGradient(FastMath.toDegrees(this.value), FastMath.toDegrees(1.0d), this.derivatives);
    }

    public SparseGradient toRadians() {
        return new SparseGradient(FastMath.toRadians(this.value), FastMath.toRadians(1.0d), this.derivatives);
    }

    public double taylor(double... delta) {
        double y2 = this.value;
        for (int i2 = 0; i2 < delta.length; i2++) {
            y2 += delta[i2] * getDerivative(i2);
        }
        return y2;
    }

    public SparseGradient compose(double f0, double f1) {
        return new SparseGradient(f0, f1, this.derivatives);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient linearCombination(SparseGradient[] a2, SparseGradient[] b2) throws DimensionMismatchException {
        SparseGradient out = a2[0].getField2().getZero();
        for (int i2 = 0; i2 < a2.length; i2++) {
            out = out.add(a2[i2].multiply(b2[i2]));
        }
        double[] aDouble = new double[a2.length];
        for (int i3 = 0; i3 < a2.length; i3++) {
            aDouble[i3] = a2[i3].getValue();
        }
        double[] bDouble = new double[b2.length];
        for (int i4 = 0; i4 < b2.length; i4++) {
            bDouble[i4] = b2[i4].getValue();
        }
        out.value = MathArrays.linearCombination(aDouble, bDouble);
        return out;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient linearCombination(double[] a2, SparseGradient[] b2) throws DimensionMismatchException {
        SparseGradient out = b2[0].getField2().getZero();
        for (int i2 = 0; i2 < a2.length; i2++) {
            out = out.add(b2[i2].multiply(a2[i2]));
        }
        double[] bDouble = new double[b2.length];
        for (int i3 = 0; i3 < b2.length; i3++) {
            bDouble[i3] = b2[i3].getValue();
        }
        out.value = MathArrays.linearCombination(a2, bDouble);
        return out;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient linearCombination(SparseGradient a1, SparseGradient b1, SparseGradient a2, SparseGradient b2) {
        SparseGradient out = a1.multiply(b1).add(a2.multiply(b2));
        out.value = MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value);
        return out;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient linearCombination(double a1, SparseGradient b1, double a2, SparseGradient b2) {
        SparseGradient out = b1.multiply(a1).add(b2.multiply(a2));
        out.value = MathArrays.linearCombination(a1, b1.value, a2, b2.value);
        return out;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient linearCombination(SparseGradient a1, SparseGradient b1, SparseGradient a2, SparseGradient b2, SparseGradient a3, SparseGradient b3) {
        SparseGradient out = a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3));
        out.value = MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value, a3.value, b3.value);
        return out;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient linearCombination(double a1, SparseGradient b1, double a2, SparseGradient b2, double a3, SparseGradient b3) {
        SparseGradient out = b1.multiply(a1).add(b2.multiply(a2)).add(b3.multiply(a3));
        out.value = MathArrays.linearCombination(a1, b1.value, a2, b2.value, a3, b3.value);
        return out;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient linearCombination(SparseGradient a1, SparseGradient b1, SparseGradient a2, SparseGradient b2, SparseGradient a3, SparseGradient b3, SparseGradient a4, SparseGradient b4) {
        SparseGradient out = a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3)).add(a4.multiply(b4));
        out.value = MathArrays.linearCombination(a1.value, b1.value, a2.value, b2.value, a3.value, b3.value, a4.value, b4.value);
        return out;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public SparseGradient linearCombination(double a1, SparseGradient b1, double a2, SparseGradient b2, double a3, SparseGradient b3, double a4, SparseGradient b4) {
        SparseGradient out = b1.multiply(a1).add(b2.multiply(a2)).add(b3.multiply(a3)).add(b4.multiply(a4));
        out.value = MathArrays.linearCombination(a1, b1.value, a2, b2.value, a3, b3.value, a4, b4.value);
        return out;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof SparseGradient) {
            SparseGradient rhs = (SparseGradient) other;
            if (!Precision.equals(this.value, rhs.value, 1) || this.derivatives.size() != rhs.derivatives.size()) {
                return false;
            }
            for (Map.Entry<Integer, Double> entry : this.derivatives.entrySet()) {
                if (!rhs.derivatives.containsKey(entry.getKey()) || !Precision.equals(entry.getValue().doubleValue(), rhs.derivatives.get(entry.getKey()).doubleValue(), 1)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 743 + (809 * MathUtils.hash(this.value)) + (167 * this.derivatives.hashCode());
    }
}
