package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/AbstractEvaluation.class */
public abstract class AbstractEvaluation implements LeastSquaresProblem.Evaluation {
    private final int observationSize;

    AbstractEvaluation(int observationSize) {
        this.observationSize = observationSize;
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealMatrix getCovariances(double threshold) throws DimensionMismatchException {
        RealMatrix j2 = getJacobian();
        RealMatrix jTj = j2.transpose().multiply(j2);
        DecompositionSolver solver = new QRDecomposition(jTj, threshold).getSolver();
        return solver.getInverse();
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public RealVector getSigma(double covarianceSingularityThreshold) throws OutOfRangeException, DimensionMismatchException {
        RealMatrix cov = getCovariances(covarianceSingularityThreshold);
        int nC = cov.getColumnDimension();
        RealVector sig = new ArrayRealVector(nC);
        for (int i2 = 0; i2 < nC; i2++) {
            sig.setEntry(i2, FastMath.sqrt(cov.getEntry(i2, i2)));
        }
        return sig;
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public double getRMS() {
        double cost = getCost();
        return FastMath.sqrt((cost * cost) / this.observationSize);
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation
    public double getCost() {
        ArrayRealVector r2 = new ArrayRealVector(getResiduals());
        return FastMath.sqrt(r2.dotProduct(r2));
    }
}
