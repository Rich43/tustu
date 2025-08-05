package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.IterationManager;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/PreconditionedIterativeLinearSolver.class */
public abstract class PreconditionedIterativeLinearSolver extends IterativeLinearSolver {
    public abstract RealVector solveInPlace(RealLinearOperator realLinearOperator, RealLinearOperator realLinearOperator2, RealVector realVector, RealVector realVector2) throws NullArgumentException, DimensionMismatchException, MaxCountExceededException;

    public PreconditionedIterativeLinearSolver(int maxIterations) {
        super(maxIterations);
    }

    public PreconditionedIterativeLinearSolver(IterationManager manager) throws NullArgumentException {
        super(manager);
    }

    public RealVector solve(RealLinearOperator a2, RealLinearOperator m2, RealVector b2, RealVector x0) throws NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(x0);
        return solveInPlace(a2, m2, b2, x0.copy());
    }

    @Override // org.apache.commons.math3.linear.IterativeLinearSolver
    public RealVector solve(RealLinearOperator a2, RealVector b2) throws OutOfRangeException, NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(a2);
        RealVector x2 = new ArrayRealVector(a2.getColumnDimension());
        x2.set(0.0d);
        return solveInPlace(a2, null, b2, x2);
    }

    @Override // org.apache.commons.math3.linear.IterativeLinearSolver
    public RealVector solve(RealLinearOperator a2, RealVector b2, RealVector x0) throws NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(x0);
        return solveInPlace(a2, null, b2, x0.copy());
    }

    protected static void checkParameters(RealLinearOperator a2, RealLinearOperator m2, RealVector b2, RealVector x0) throws NullArgumentException, DimensionMismatchException {
        checkParameters(a2, b2, x0);
        if (m2 != null) {
            if (m2.getColumnDimension() != m2.getRowDimension()) {
                throw new NonSquareOperatorException(m2.getColumnDimension(), m2.getRowDimension());
            }
            if (m2.getRowDimension() != a2.getRowDimension()) {
                throw new DimensionMismatchException(m2.getRowDimension(), a2.getRowDimension());
            }
        }
    }

    public RealVector solve(RealLinearOperator a2, RealLinearOperator m2, RealVector b2) throws NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(a2);
        RealVector x2 = new ArrayRealVector(a2.getColumnDimension());
        return solveInPlace(a2, m2, b2, x2);
    }

    @Override // org.apache.commons.math3.linear.IterativeLinearSolver
    public RealVector solveInPlace(RealLinearOperator a2, RealVector b2, RealVector x0) throws NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        return solveInPlace(a2, null, b2, x0);
    }
}
