package org.apache.commons.math3.stat.descriptive;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/UnivariateStatistic.class */
public interface UnivariateStatistic extends MathArrays.Function {
    @Override // org.apache.commons.math3.util.MathArrays.Function
    double evaluate(double[] dArr) throws MathIllegalArgumentException;

    @Override // org.apache.commons.math3.util.MathArrays.Function
    double evaluate(double[] dArr, int i2, int i3) throws MathIllegalArgumentException;

    UnivariateStatistic copy();
}
