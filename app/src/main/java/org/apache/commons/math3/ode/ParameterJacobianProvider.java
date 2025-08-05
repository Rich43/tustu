package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/ParameterJacobianProvider.class */
public interface ParameterJacobianProvider extends Parameterizable {
    void computeParameterJacobian(double d2, double[] dArr, double[] dArr2, String str, double[] dArr3) throws UnknownParameterException, DimensionMismatchException, MaxCountExceededException;
}
