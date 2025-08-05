package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/moment/Kurtosis.class */
public class Kurtosis extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = 2784465764798260919L;
    protected FourthMoment moment;
    protected boolean incMoment;

    public Kurtosis() {
        this.incMoment = true;
        this.moment = new FourthMoment();
    }

    public Kurtosis(FourthMoment m4) {
        this.incMoment = false;
        this.moment = m4;
    }

    public Kurtosis(Kurtosis original) throws NullArgumentException {
        copy(original, this);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d2) {
        if (this.incMoment) {
            this.moment.increment(d2);
        }
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        double kurtosis = Double.NaN;
        if (this.moment.getN() > 3) {
            double variance = this.moment.m2 / (this.moment.f13099n - 1);
            if (this.moment.f13099n <= 3 || variance < 1.0E-19d) {
                kurtosis = 0.0d;
            } else {
                double n2 = this.moment.f13099n;
                kurtosis = (((n2 * (n2 + 1.0d)) * this.moment.getResult()) - (((3.0d * this.moment.m2) * this.moment.m2) * (n2 - 1.0d))) / (((((n2 - 1.0d) * (n2 - 2.0d)) * (n2 - 3.0d)) * variance) * variance);
            }
        }
        return kurtosis;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
    }

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.moment.getN();
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        double kurt = Double.NaN;
        if (test(values, begin, length) && length > 3) {
            Variance variance = new Variance();
            variance.incrementAll(values, begin, length);
            double mean = variance.moment.m1;
            double stdDev = FastMath.sqrt(variance.getResult());
            double accum3 = 0.0d;
            for (int i2 = begin; i2 < begin + length; i2++) {
                accum3 += FastMath.pow(values[i2] - mean, 4.0d);
            }
            double accum32 = accum3 / FastMath.pow(stdDev, 4.0d);
            double n0 = length;
            double coefficientOne = (n0 * (n0 + 1.0d)) / (((n0 - 1.0d) * (n0 - 2.0d)) * (n0 - 3.0d));
            double termTwo = (3.0d * FastMath.pow(n0 - 1.0d, 2.0d)) / ((n0 - 2.0d) * (n0 - 3.0d));
            kurt = (coefficientOne * accum32) - termTwo;
        }
        return kurt;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public Kurtosis copy() throws NullArgumentException {
        Kurtosis result = new Kurtosis();
        copy(this, result);
        return result;
    }

    public static void copy(Kurtosis source, Kurtosis dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.moment = source.moment.copy();
        dest.incMoment = source.incMoment;
    }
}
