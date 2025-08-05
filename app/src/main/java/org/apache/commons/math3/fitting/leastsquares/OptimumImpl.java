package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/OptimumImpl.class */
class OptimumImpl implements LeastSquaresOptimizer.Optimum {
    private final LeastSquaresProblem.Evaluation value;
    private final int evaluations;
    private final int iterations;

    OptimumImpl(LeastSquaresProblem.Evaluation value, int evaluations, int iterations) {
        this.value = value;
        this.evaluations = evaluations;
        this.iterations = iterations;
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum
    public int getEvaluations() {
        return this.evaluations;
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum
    public int getIterations() {
        return this.iterations;
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealMatrix getCovariances(double threshold) {
        return this.value.getCovariances(threshold);
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealVector getSigma(double covarianceSingularityThreshold) {
        return this.value.getSigma(covarianceSingularityThreshold);
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public double getRMS() {
        return this.value.getRMS();
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealMatrix getJacobian() {
        return this.value.getJacobian();
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public double getCost() {
        return this.value.getCost();
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealVector getResiduals() {
        return this.value.getResiduals();
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealVector getPoint() {
        return this.value.getPoint();
    }
}
