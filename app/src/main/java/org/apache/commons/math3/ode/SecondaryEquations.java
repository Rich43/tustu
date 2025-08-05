package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/SecondaryEquations.class */
public interface SecondaryEquations {
    int getDimension();

    void computeDerivatives(double d2, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4) throws MaxCountExceededException, DimensionMismatchException;
}
