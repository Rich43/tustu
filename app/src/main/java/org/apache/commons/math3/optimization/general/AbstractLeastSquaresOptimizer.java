package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableVectorFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.Target;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateVectorOptimizer;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/general/AbstractLeastSquaresOptimizer.class */
public abstract class AbstractLeastSquaresOptimizer extends BaseAbstractMultivariateVectorOptimizer<DifferentiableMultivariateVectorFunction> implements DifferentiableMultivariateVectorOptimizer {

    @Deprecated
    private static final double DEFAULT_SINGULARITY_THRESHOLD = 1.0E-14d;

    @Deprecated
    protected double[][] weightedResidualJacobian;

    @Deprecated
    protected int cols;

    @Deprecated
    protected int rows;

    @Deprecated
    protected double[] point;

    @Deprecated
    protected double[] objective;

    @Deprecated
    protected double[] weightedResiduals;

    @Deprecated
    protected double cost;
    private MultivariateDifferentiableVectorFunction jF;
    private int jacobianEvaluations;
    private RealMatrix weightMatrixSqrt;

    @Deprecated
    protected AbstractLeastSquaresOptimizer() {
    }

    protected AbstractLeastSquaresOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        super(checker);
    }

    public int getJacobianEvaluations() {
        return this.jacobianEvaluations;
    }

    @Deprecated
    protected void updateJacobian() throws MathIllegalArgumentException {
        RealMatrix weightedJacobian = computeWeightedJacobian(this.point);
        this.weightedResidualJacobian = weightedJacobian.scalarMultiply(-1.0d).getData();
    }

    protected RealMatrix computeWeightedJacobian(double[] params) throws MathIllegalArgumentException {
        this.jacobianEvaluations++;
        DerivativeStructure[] dsPoint = new DerivativeStructure[params.length];
        int nC = params.length;
        for (int i2 = 0; i2 < nC; i2++) {
            dsPoint[i2] = new DerivativeStructure(nC, 1, i2, params[i2]);
        }
        DerivativeStructure[] dsValue = this.jF.value(dsPoint);
        int nR = getTarget().length;
        if (dsValue.length != nR) {
            throw new DimensionMismatchException(dsValue.length, nR);
        }
        double[][] jacobianData = new double[nR][nC];
        for (int i3 = 0; i3 < nR; i3++) {
            int[] orders = new int[nC];
            for (int j2 = 0; j2 < nC; j2++) {
                orders[j2] = 1;
                jacobianData[i3][j2] = dsValue[i3].getPartialDerivative(orders);
                orders[j2] = 0;
            }
        }
        return this.weightMatrixSqrt.multiply(MatrixUtils.createRealMatrix(jacobianData));
    }

    @Deprecated
    protected void updateResidualsAndCost() {
        this.objective = computeObjectiveValue(this.point);
        double[] res = computeResiduals(this.objective);
        this.cost = computeCost(res);
        ArrayRealVector residuals = new ArrayRealVector(res);
        this.weightedResiduals = this.weightMatrixSqrt.operate(residuals).toArray();
    }

    protected double computeCost(double[] residuals) {
        ArrayRealVector r2 = new ArrayRealVector(residuals);
        return FastMath.sqrt(r2.dotProduct(getWeight().operate(r2)));
    }

    public double getRMS() {
        return FastMath.sqrt(getChiSquare() / this.rows);
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

    @Deprecated
    public double[][] getCovariances() {
        return getCovariances(DEFAULT_SINGULARITY_THRESHOLD);
    }

    @Deprecated
    public double[][] getCovariances(double threshold) {
        return computeCovariances(this.point, threshold);
    }

    public double[][] computeCovariances(double[] params, double threshold) throws MathIllegalArgumentException {
        RealMatrix j2 = computeWeightedJacobian(params);
        RealMatrix jTj = j2.transpose().multiply(j2);
        DecompositionSolver solver = new QRDecomposition(jTj, threshold).getSolver();
        return solver.getInverse().getData();
    }

    @Deprecated
    public double[] guessParametersErrors() throws MathIllegalArgumentException {
        if (this.rows <= this.cols) {
            throw new NumberIsTooSmallException(LocalizedFormats.NO_DEGREES_OF_FREEDOM, Integer.valueOf(this.rows), Integer.valueOf(this.cols), false);
        }
        double[] errors = new double[this.cols];
        double c2 = FastMath.sqrt(getChiSquare() / (this.rows - this.cols));
        double[][] covar = computeCovariances(this.point, DEFAULT_SINGULARITY_THRESHOLD);
        for (int i2 = 0; i2 < errors.length; i2++) {
            errors[i2] = FastMath.sqrt(covar[i2][i2]) * c2;
        }
        return errors;
    }

    public double[] computeSigma(double[] params, double covarianceSingularityThreshold) throws MathIllegalArgumentException {
        int nC = params.length;
        double[] sig = new double[nC];
        double[][] cov = computeCovariances(params, covarianceSingularityThreshold);
        for (int i2 = 0; i2 < nC; i2++) {
            sig[i2] = FastMath.sqrt(cov[i2][i2]);
        }
        return sig;
    }

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateVectorOptimizer, org.apache.commons.math3.optimization.BaseMultivariateVectorOptimizer
    @Deprecated
    public PointVectorValuePair optimize(int maxEval, DifferentiableMultivariateVectorFunction f2, double[] target, double[] weights, double[] startPoint) {
        return optimizeInternal(maxEval, FunctionUtils.toMultivariateDifferentiableVectorFunction(f2), new Target(target), new Weight(weights), new InitialGuess(startPoint));
    }

    @Deprecated
    public PointVectorValuePair optimize(int maxEval, MultivariateDifferentiableVectorFunction f2, double[] target, double[] weights, double[] startPoint) {
        return optimizeInternal(maxEval, f2, new Target(target), new Weight(weights), new InitialGuess(startPoint));
    }

    @Deprecated
    protected PointVectorValuePair optimizeInternal(int maxEval, MultivariateDifferentiableVectorFunction f2, OptimizationData... optData) {
        return super.optimizeInternal(maxEval, (int) FunctionUtils.toDifferentiableMultivariateVectorFunction(f2), optData);
    }

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateVectorOptimizer
    protected void setUp() {
        super.setUp();
        this.jacobianEvaluations = 0;
        this.weightMatrixSqrt = squareRoot(getWeight());
        this.jF = FunctionUtils.toMultivariateDifferentiableVectorFunction(getObjectiveFunction());
        this.point = getStartPoint();
        this.rows = getTarget().length;
        this.cols = this.point.length;
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
