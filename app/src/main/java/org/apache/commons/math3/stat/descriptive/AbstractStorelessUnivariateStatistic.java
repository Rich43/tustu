package org.apache.commons.math3.stat.descriptive;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/AbstractStorelessUnivariateStatistic.class */
public abstract class AbstractStorelessUnivariateStatistic extends AbstractUnivariateStatistic implements StorelessUnivariateStatistic {
    @Override // org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public abstract StorelessUnivariateStatistic copy();

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public abstract void clear();

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public abstract double getResult();

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public abstract void increment(double d2);

    @Override // org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public double evaluate(double[] values) throws MathIllegalArgumentException {
        if (values == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        return evaluate(values, 0, values.length);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (test(values, begin, length)) {
            clear();
            incrementAll(values, begin, length);
        }
        return getResult();
    }

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void incrementAll(double[] values) throws MathIllegalArgumentException {
        if (values == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        incrementAll(values, 0, values.length);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void incrementAll(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (test(values, begin, length)) {
            int k2 = begin + length;
            for (int i2 = begin; i2 < k2; i2++) {
                increment(values[i2]);
            }
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof AbstractStorelessUnivariateStatistic)) {
            return false;
        }
        AbstractStorelessUnivariateStatistic stat = (AbstractStorelessUnivariateStatistic) object;
        return Precision.equalsIncludingNaN(stat.getResult(), getResult()) && Precision.equalsIncludingNaN((float) stat.getN(), (float) getN());
    }

    public int hashCode() {
        return (31 * (31 + MathUtils.hash(getResult()))) + MathUtils.hash(getN());
    }
}
