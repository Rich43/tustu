package org.apache.commons.math3.ode;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FieldSecondaryEquations.class */
public interface FieldSecondaryEquations<T extends RealFieldElement<T>> {
    int getDimension();

    void init(T t2, T[] tArr, T[] tArr2, T t3);

    T[] computeDerivatives(T t2, T[] tArr, T[] tArr2, T[] tArr3) throws MaxCountExceededException, DimensionMismatchException;
}
