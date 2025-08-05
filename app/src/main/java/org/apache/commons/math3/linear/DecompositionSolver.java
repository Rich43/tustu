package org.apache.commons.math3.linear;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/DecompositionSolver.class */
public interface DecompositionSolver {
    RealVector solve(RealVector realVector) throws SingularMatrixException;

    RealMatrix solve(RealMatrix realMatrix) throws SingularMatrixException;

    boolean isNonSingular();

    RealMatrix getInverse() throws SingularMatrixException;
}
