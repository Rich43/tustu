package org.apache.commons.math3.stat.descriptive;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/StorelessUnivariateStatistic.class */
public interface StorelessUnivariateStatistic extends UnivariateStatistic {
    void increment(double d2);

    void incrementAll(double[] dArr) throws MathIllegalArgumentException;

    void incrementAll(double[] dArr, int i2, int i3) throws MathIllegalArgumentException;

    double getResult();

    long getN();

    void clear();

    StorelessUnivariateStatistic copy();
}
