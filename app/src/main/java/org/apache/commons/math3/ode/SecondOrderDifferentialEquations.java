package org.apache.commons.math3.ode;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/SecondOrderDifferentialEquations.class */
public interface SecondOrderDifferentialEquations {
    int getDimension();

    void computeSecondDerivatives(double d2, double[] dArr, double[] dArr2, double[] dArr3);
}
