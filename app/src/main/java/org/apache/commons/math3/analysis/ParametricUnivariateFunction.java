package org.apache.commons.math3.analysis;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/ParametricUnivariateFunction.class */
public interface ParametricUnivariateFunction {
    double value(double d2, double... dArr);

    double[] gradient(double d2, double... dArr);
}
