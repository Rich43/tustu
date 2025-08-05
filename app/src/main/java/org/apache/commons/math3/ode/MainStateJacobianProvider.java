package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/MainStateJacobianProvider.class */
public interface MainStateJacobianProvider extends FirstOrderDifferentialEquations {
    void computeMainStateJacobian(double d2, double[] dArr, double[] dArr2, double[][] dArr3) throws MaxCountExceededException, DimensionMismatchException;
}
