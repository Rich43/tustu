package org.apache.commons.math3.stat.descriptive;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/AbstractUnivariateStatistic.class */
public abstract class AbstractUnivariateStatistic implements UnivariateStatistic {
    private double[] storedData;

    @Override // org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public abstract double evaluate(double[] dArr, int i2, int i3) throws MathIllegalArgumentException;

    @Override // org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public abstract UnivariateStatistic copy();

    public void setData(double[] values) {
        this.storedData = values == null ? null : (double[]) values.clone();
    }

    public double[] getData() {
        if (this.storedData == null) {
            return null;
        }
        return (double[]) this.storedData.clone();
    }

    protected double[] getDataRef() {
        return this.storedData;
    }

    public void setData(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (values == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        if (begin < 0) {
            throw new NotPositiveException(LocalizedFormats.START_POSITION, Integer.valueOf(begin));
        }
        if (length < 0) {
            throw new NotPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(length));
        }
        if (begin + length > values.length) {
            throw new NumberIsTooLargeException(LocalizedFormats.SUBARRAY_ENDS_AFTER_ARRAY_END, Integer.valueOf(begin + length), Integer.valueOf(values.length), true);
        }
        this.storedData = new double[length];
        System.arraycopy(values, begin, this.storedData, 0, length);
    }

    public double evaluate() throws MathIllegalArgumentException {
        return evaluate(this.storedData);
    }

    @Override // org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.util.MathArrays.Function
    public double evaluate(double[] values) throws MathIllegalArgumentException {
        test(values, 0, 0);
        return evaluate(values, 0, values.length);
    }

    protected boolean test(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return MathArrays.verifyValues(values, begin, length, false);
    }

    protected boolean test(double[] values, int begin, int length, boolean allowEmpty) throws MathIllegalArgumentException {
        return MathArrays.verifyValues(values, begin, length, allowEmpty);
    }

    protected boolean test(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        return MathArrays.verifyValues(values, weights, begin, length, false);
    }

    protected boolean test(double[] values, double[] weights, int begin, int length, boolean allowEmpty) throws MathIllegalArgumentException {
        return MathArrays.verifyValues(values, weights, begin, length, allowEmpty);
    }
}
