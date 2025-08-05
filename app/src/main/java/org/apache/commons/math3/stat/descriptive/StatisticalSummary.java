package org.apache.commons.math3.stat.descriptive;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/StatisticalSummary.class */
public interface StatisticalSummary {
    double getMean();

    double getVariance();

    double getStandardDeviation();

    double getMax();

    double getMin();

    long getN();

    double getSum();
}
