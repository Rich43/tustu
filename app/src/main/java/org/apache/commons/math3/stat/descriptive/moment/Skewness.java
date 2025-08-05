package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/moment/Skewness.class */
public class Skewness extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = 7101857578996691352L;
    protected ThirdMoment moment;
    protected boolean incMoment;

    public Skewness() {
        this.moment = null;
        this.incMoment = true;
        this.moment = new ThirdMoment();
    }

    public Skewness(ThirdMoment m3) {
        this.moment = null;
        this.incMoment = false;
        this.moment = m3;
    }

    public Skewness(Skewness original) throws NullArgumentException {
        this.moment = null;
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
        if (this.moment.f13099n < 3) {
            return Double.NaN;
        }
        double variance = this.moment.m2 / (this.moment.f13099n - 1);
        if (variance < 1.0E-19d) {
            return 0.0d;
        }
        double n0 = this.moment.getN();
        return (n0 * this.moment.m3) / ((((n0 - 1.0d) * (n0 - 2.0d)) * FastMath.sqrt(variance)) * variance);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.moment.getN();
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        double skew = Double.NaN;
        if (test(values, begin, length) && length > 2) {
            Mean mean = new Mean();
            double m2 = mean.evaluate(values, begin, length);
            double accum = 0.0d;
            double accum2 = 0.0d;
            for (int i2 = begin; i2 < begin + length; i2++) {
                double d2 = values[i2] - m2;
                accum += d2 * d2;
                accum2 += d2;
            }
            double variance = (accum - ((accum2 * accum2) / length)) / (length - 1);
            double accum3 = 0.0d;
            for (int i3 = begin; i3 < begin + length; i3++) {
                double d3 = values[i3] - m2;
                accum3 += d3 * d3 * d3;
            }
            double accum32 = accum3 / (variance * FastMath.sqrt(variance));
            double n0 = length;
            skew = (n0 / ((n0 - 1.0d) * (n0 - 2.0d))) * accum32;
        }
        return skew;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public Skewness copy() throws NullArgumentException {
        Skewness result = new Skewness();
        copy(this, result);
        return result;
    }

    public static void copy(Skewness source, Skewness dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.moment = new ThirdMoment(source.moment.copy());
        dest.incMoment = source.incMoment;
    }
}
