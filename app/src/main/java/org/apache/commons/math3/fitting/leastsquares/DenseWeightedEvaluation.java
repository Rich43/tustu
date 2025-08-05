package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/DenseWeightedEvaluation.class */
class DenseWeightedEvaluation extends AbstractEvaluation {
    private final LeastSquaresProblem.Evaluation unweighted;
    private final RealMatrix weightSqrt;

    DenseWeightedEvaluation(LeastSquaresProblem.Evaluation unweighted, RealMatrix weightSqrt) {
        super(weightSqrt.getColumnDimension());
        this.unweighted = unweighted;
        this.weightSqrt = weightSqrt;
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealMatrix getJacobian() {
        return this.weightSqrt.multiply(this.unweighted.getJacobian());
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealVector getResiduals() {
        return this.weightSqrt.operate(this.unweighted.getResiduals());
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealVector getPoint() {
        return this.unweighted.getPoint();
    }
}
