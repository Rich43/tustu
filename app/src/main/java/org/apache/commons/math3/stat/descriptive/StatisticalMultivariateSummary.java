package org.apache.commons.math3.stat.descriptive;

import org.apache.commons.math3.linear.RealMatrix;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/StatisticalMultivariateSummary.class */
public interface StatisticalMultivariateSummary {
    int getDimension();

    double[] getMean();

    RealMatrix getCovariance();

    double[] getStandardDeviation();

    double[] getMax();

    double[] getMin();

    long getN();

    double[] getGeometricMean();

    double[] getSum();

    double[] getSumSq();

    double[] getSumLog();
}
