package org.apache.commons.math3.stat.descriptive.summary;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/summary/Sum.class */
public class Sum extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = -8231831954703408316L;

    /* renamed from: n, reason: collision with root package name */
    private long f13105n;
    private double value;

    public Sum() {
        this.f13105n = 0L;
        this.value = 0.0d;
    }

    public Sum(Sum original) throws NullArgumentException {
        copy(original, this);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d2) {
        this.value += d2;
        this.f13105n++;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.value;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.f13105n;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.value = 0.0d;
        this.f13105n = 0L;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        double sum = Double.NaN;
        if (test(values, begin, length, true)) {
            sum = 0.0d;
            for (int i2 = begin; i2 < begin + length; i2++) {
                sum += values[i2];
            }
        }
        return sum;
    }

    public double evaluate(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        double sum = Double.NaN;
        if (test(values, weights, begin, length, true)) {
            sum = 0.0d;
            for (int i2 = begin; i2 < begin + length; i2++) {
                sum += values[i2] * weights[i2];
            }
        }
        return sum;
    }

    public double evaluate(double[] values, double[] weights) throws MathIllegalArgumentException {
        return evaluate(values, weights, 0, values.length);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public Sum copy() throws NullArgumentException {
        Sum result = new Sum();
        copy(this, result);
        return result;
    }

    public static void copy(Sum source, Sum dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.f13105n = source.f13105n;
        dest.value = source.value;
    }
}
