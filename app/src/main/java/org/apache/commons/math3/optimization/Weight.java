package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/Weight.class */
public class Weight implements OptimizationData {
    private final RealMatrix weightMatrix;

    public Weight(double[] weight) {
        this.weightMatrix = new DiagonalMatrix(weight);
    }

    public Weight(RealMatrix weight) {
        if (weight.getColumnDimension() != weight.getRowDimension()) {
            throw new NonSquareMatrixException(weight.getColumnDimension(), weight.getRowDimension());
        }
        this.weightMatrix = weight.copy();
    }

    public RealMatrix getWeight() {
        return this.weightMatrix.copy();
    }
}
