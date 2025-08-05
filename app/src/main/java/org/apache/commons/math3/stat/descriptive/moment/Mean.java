package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.WeightedEvaluation;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/moment/Mean.class */
public class Mean extends AbstractStorelessUnivariateStatistic implements Serializable, WeightedEvaluation {
    private static final long serialVersionUID = -1296043746617791564L;
    protected FirstMoment moment;
    protected boolean incMoment;

    public Mean() {
        this.incMoment = true;
        this.moment = new FirstMoment();
    }

    public Mean(FirstMoment m1) {
        this.moment = m1;
        this.incMoment = false;
    }

    public Mean(Mean original) throws NullArgumentException {
        copy(original, this);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d2) {
        if (this.incMoment) {
            this.moment.increment(d2);
        }
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.moment.m1;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.moment.getN();
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (test(values, begin, length)) {
            Sum sum = new Sum();
            double sampleSize = length;
            double xbar = sum.evaluate(values, begin, length) / sampleSize;
            double correction = 0.0d;
            for (int i2 = begin; i2 < begin + length; i2++) {
                correction += values[i2] - xbar;
            }
            return xbar + (correction / sampleSize);
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math3.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        if (test(values, weights, begin, length)) {
            Sum sum = new Sum();
            double sumw = sum.evaluate(weights, begin, length);
            double xbarw = sum.evaluate(values, weights, begin, length) / sumw;
            double correction = 0.0d;
            for (int i2 = begin; i2 < begin + length; i2++) {
                correction += weights[i2] * (values[i2] - xbarw);
            }
            return xbarw + (correction / sumw);
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math3.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights) throws MathIllegalArgumentException {
        return evaluate(values, weights, 0, values.length);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public Mean copy() throws NullArgumentException {
        Mean result = new Mean();
        copy(this, result);
        return result;
    }

    public static void copy(Mean source, Mean dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.incMoment = source.incMoment;
        dest.moment = source.moment.copy();
    }
}
