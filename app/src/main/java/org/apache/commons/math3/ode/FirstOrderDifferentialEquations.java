package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FirstOrderDifferentialEquations.class */
public interface FirstOrderDifferentialEquations {
    int getDimension();

    void computeDerivatives(double d2, double[] dArr, double[] dArr2) throws MaxCountExceededException, DimensionMismatchException;
}
