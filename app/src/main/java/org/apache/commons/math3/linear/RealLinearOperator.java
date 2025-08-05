package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealLinearOperator.class */
public abstract class RealLinearOperator {
    public abstract int getRowDimension();

    public abstract int getColumnDimension();

    public abstract RealVector operate(RealVector realVector) throws DimensionMismatchException;

    public RealVector operateTranspose(RealVector x2) throws UnsupportedOperationException, DimensionMismatchException {
        throw new UnsupportedOperationException();
    }

    public boolean isTransposable() {
        return false;
    }
}
