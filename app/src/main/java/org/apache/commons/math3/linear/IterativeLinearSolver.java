package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.IterationManager;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/IterativeLinearSolver.class */
public abstract class IterativeLinearSolver {
    private final IterationManager manager;

    public abstract RealVector solveInPlace(RealLinearOperator realLinearOperator, RealVector realVector, RealVector realVector2) throws NullArgumentException, DimensionMismatchException, MaxCountExceededException;

    public IterativeLinearSolver(int maxIterations) {
        this.manager = new IterationManager(maxIterations);
    }

    public IterativeLinearSolver(IterationManager manager) throws NullArgumentException {
        MathUtils.checkNotNull(manager);
        this.manager = manager;
    }

    protected static void checkParameters(RealLinearOperator a2, RealVector b2, RealVector x0) throws NullArgumentException, DimensionMismatchException {
        MathUtils.checkNotNull(a2);
        MathUtils.checkNotNull(b2);
        MathUtils.checkNotNull(x0);
        if (a2.getRowDimension() != a2.getColumnDimension()) {
            throw new NonSquareOperatorException(a2.getRowDimension(), a2.getColumnDimension());
        }
        if (b2.getDimension() != a2.getRowDimension()) {
            throw new DimensionMismatchException(b2.getDimension(), a2.getRowDimension());
        }
        if (x0.getDimension() != a2.getColumnDimension()) {
            throw new DimensionMismatchException(x0.getDimension(), a2.getColumnDimension());
        }
    }

    public IterationManager getIterationManager() {
        return this.manager;
    }

    public RealVector solve(RealLinearOperator a2, RealVector b2) throws OutOfRangeException, NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(a2);
        RealVector x2 = new ArrayRealVector(a2.getColumnDimension());
        x2.set(0.0d);
        return solveInPlace(a2, b2, x2);
    }

    public RealVector solve(RealLinearOperator a2, RealVector b2, RealVector x0) throws NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(x0);
        return solveInPlace(a2, b2, x0.copy());
    }
}
