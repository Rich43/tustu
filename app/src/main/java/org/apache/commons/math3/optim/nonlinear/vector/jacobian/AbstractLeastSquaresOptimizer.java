package org.apache.commons.math3.optim.nonlinear.vector.jacobian;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.optim.nonlinear.vector.JacobianMultivariateVectorOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.Weight;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/vector/jacobian/AbstractLeastSquaresOptimizer.class */
public abstract class AbstractLeastSquaresOptimizer extends JacobianMultivariateVectorOptimizer {
    private RealMatrix weightMatrixSqrt;
    private double cost;

    protected AbstractLeastSquaresOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        super(checker);
    }

    protected RealMatrix computeWeightedJacobian(double[] params) {
        return this.weightMatrixSqrt.multiply(MatrixUtils.createRealMatrix(computeJacobian(params)));
    }

    protected double computeCost(double[] residuals) {
        ArrayRealVector r2 = new ArrayRealVector(residuals);
        return FastMath.sqrt(r2.dotProduct(getWeight().operate(r2)));
    }

    public double getRMS() {
        return FastMath.sqrt(getChiSquare() / getTargetSize());
    }

    public double getChiSquare() {
        return this.cost * this.cost;
    }

    public RealMatrix getWeightSquareRoot() {
        return this.weightMatrixSqrt.copy();
    }

    protected void setCost(double cost) {
        this.cost = cost;
    }

    public double[][] computeCovariances(double[] params, double threshold) throws DimensionMismatchException {
        RealMatrix j2 = computeWeightedJacobian(params);
        RealMatrix jTj = j2.transpose().multiply(j2);
        DecompositionSolver solver = new QRDecomposition(jTj, threshold).getSolver();
        return solver.getInverse().getData();
    }

    public double[] computeSigma(double[] params, double covarianceSingularityThreshold) throws DimensionMismatchException {
        int nC = params.length;
        double[] sig = new double[nC];
        double[][] cov = computeCovariances(params, covarianceSingularityThreshold);
        for (int i2 = 0; i2 < nC; i2++) {
            sig[i2] = FastMath.sqrt(cov[i2][i2]);
        }
        return sig;
    }

    @Override // org.apache.commons.math3.optim.nonlinear.vector.JacobianMultivariateVectorOptimizer, org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    public PointVectorValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException {
        return super.optimize(optData);
    }

    protected double[] computeResiduals(double[] objectiveValue) {
        double[] target = getTarget();
        if (objectiveValue.length != target.length) {
            throw new DimensionMismatchException(target.length, objectiveValue.length);
        }
        double[] residuals = new double[target.length];
        for (int i2 = 0; i2 < target.length; i2++) {
            residuals[i2] = target[i2] - objectiveValue[i2];
        }
        return residuals;
    }

    @Override // org.apache.commons.math3.optim.nonlinear.vector.JacobianMultivariateVectorOptimizer, org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    protected void parseOptimizationData(OptimizationData... optData) {
        super.parseOptimizationData(optData);
        for (OptimizationData data : optData) {
            if (data instanceof Weight) {
                this.weightMatrixSqrt = squareRoot(((Weight) data).getWeight());
                return;
            }
        }
    }

    private RealMatrix squareRoot(RealMatrix m2) throws OutOfRangeException {
        if (m2 instanceof DiagonalMatrix) {
            int dim = m2.getRowDimension();
            RealMatrix sqrtM = new DiagonalMatrix(dim);
            for (int i2 = 0; i2 < dim; i2++) {
                sqrtM.setEntry(i2, i2, FastMath.sqrt(m2.getEntry(i2, i2)));
            }
            return sqrtM;
        }
        EigenDecomposition dec = new EigenDecomposition(m2);
        return dec.getSquareRoot();
    }
}
