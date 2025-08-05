package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.WeightedEvaluation;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/moment/Variance.class */
public class Variance extends AbstractStorelessUnivariateStatistic implements Serializable, WeightedEvaluation {
    private static final long serialVersionUID = -9111962718267217978L;
    protected SecondMoment moment;
    protected boolean incMoment;
    private boolean isBiasCorrected;

    public Variance() {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
        this.moment = new SecondMoment();
    }

    public Variance(SecondMoment m2) {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
        this.incMoment = false;
        this.moment = m2;
    }

    public Variance(boolean isBiasCorrected) {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
        this.moment = new SecondMoment();
        this.isBiasCorrected = isBiasCorrected;
    }

    public Variance(boolean isBiasCorrected, SecondMoment m2) {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
        this.incMoment = false;
        this.moment = m2;
        this.isBiasCorrected = isBiasCorrected;
    }

    public Variance(Variance original) throws NullArgumentException {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
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
        if (this.moment.f13099n == 0) {
            return Double.NaN;
        }
        if (this.moment.f13099n == 1) {
            return 0.0d;
        }
        if (this.isBiasCorrected) {
            return this.moment.m2 / (this.moment.f13099n - 1.0d);
        }
        return this.moment.m2 / this.moment.f13099n;
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
    public double evaluate(double[] values) throws MathIllegalArgumentException {
        if (values == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        return evaluate(values, 0, values.length);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        double var = Double.NaN;
        if (test(values, begin, length)) {
            clear();
            if (length == 1) {
                var = 0.0d;
            } else if (length > 1) {
                Mean mean = new Mean();
                double m2 = mean.evaluate(values, begin, length);
                var = evaluate(values, m2, begin, length);
            }
        }
        return var;
    }

    @Override // org.apache.commons.math3.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        double var = Double.NaN;
        if (test(values, weights, begin, length)) {
            clear();
            if (length == 1) {
                var = 0.0d;
            } else if (length > 1) {
                Mean mean = new Mean();
                double m2 = mean.evaluate(values, weights, begin, length);
                var = evaluate(values, weights, m2, begin, length);
            }
        }
        return var;
    }

    @Override // org.apache.commons.math3.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights) throws MathIllegalArgumentException {
        return evaluate(values, weights, 0, values.length);
    }

    public double evaluate(double[] values, double mean, int begin, int length) throws MathIllegalArgumentException {
        double var = Double.NaN;
        if (test(values, begin, length)) {
            if (length == 1) {
                var = 0.0d;
            } else if (length > 1) {
                double accum = 0.0d;
                double accum2 = 0.0d;
                for (int i2 = begin; i2 < begin + length; i2++) {
                    double dev = values[i2] - mean;
                    accum += dev * dev;
                    accum2 += dev;
                }
                double len = length;
                var = this.isBiasCorrected ? (accum - ((accum2 * accum2) / len)) / (len - 1.0d) : (accum - ((accum2 * accum2) / len)) / len;
            }
        }
        return var;
    }

    public double evaluate(double[] values, double mean) throws MathIllegalArgumentException {
        return evaluate(values, mean, 0, values.length);
    }

    public double evaluate(double[] values, double[] weights, double mean, int begin, int length) throws MathIllegalArgumentException {
        double var = Double.NaN;
        if (test(values, weights, begin, length)) {
            if (length == 1) {
                var = 0.0d;
            } else if (length > 1) {
                double accum = 0.0d;
                double accum2 = 0.0d;
                for (int i2 = begin; i2 < begin + length; i2++) {
                    double dev = values[i2] - mean;
                    accum += weights[i2] * dev * dev;
                    accum2 += weights[i2] * dev;
                }
                double sumWts = 0.0d;
                for (int i3 = begin; i3 < begin + length; i3++) {
                    sumWts += weights[i3];
                }
                var = this.isBiasCorrected ? (accum - ((accum2 * accum2) / sumWts)) / (sumWts - 1.0d) : (accum - ((accum2 * accum2) / sumWts)) / sumWts;
            }
        }
        return var;
    }

    public double evaluate(double[] values, double[] weights, double mean) throws MathIllegalArgumentException {
        return evaluate(values, weights, mean, 0, values.length);
    }

    public boolean isBiasCorrected() {
        return this.isBiasCorrected;
    }

    public void setBiasCorrected(boolean biasCorrected) {
        this.isBiasCorrected = biasCorrected;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public Variance copy() throws NullArgumentException {
        Variance result = new Variance();
        copy(this, result);
        return result;
    }

    public static void copy(Variance source, Variance dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.moment = source.moment.copy();
        dest.isBiasCorrected = source.isBiasCorrected;
        dest.incMoment = source.incMoment;
    }
}
