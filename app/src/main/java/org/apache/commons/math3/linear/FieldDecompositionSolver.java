package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/FieldDecompositionSolver.class */
public interface FieldDecompositionSolver<T extends FieldElement<T>> {
    FieldVector<T> solve(FieldVector<T> fieldVector);

    FieldMatrix<T> solve(FieldMatrix<T> fieldMatrix);

    boolean isNonSingular();

    FieldMatrix<T> getInverse();
}
