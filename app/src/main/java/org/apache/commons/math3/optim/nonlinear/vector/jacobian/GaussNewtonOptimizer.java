package org.apache.commons.math3.optim.nonlinear.vector.jacobian;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointVectorValuePair;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/vector/jacobian/GaussNewtonOptimizer.class */
public class GaussNewtonOptimizer extends AbstractLeastSquaresOptimizer {
    private final boolean useLU;

    public GaussNewtonOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        this(true, checker);
    }

    public GaussNewtonOptimizer(boolean useLU, ConvergenceChecker<PointVectorValuePair> checker) {
        super(checker);
        this.useLU = useLU;
    }

    @Override // org.apache.commons.math3.optim.BaseOptimizer
    public PointVectorValuePair doOptimize() throws OutOfRangeException {
        checkParameters();
        ConvergenceChecker<PointVectorValuePair> checker = getConvergenceChecker();
        if (checker == null) {
            throw new NullArgumentException();
        }
        double[] targetValues = getTarget();
        int nR = targetValues.length;
        RealMatrix weightMatrix = getWeight();
        double[] residualsWeights = new double[nR];
        for (int i2 = 0; i2 < nR; i2++) {
            residualsWeights[i2] = weightMatrix.getEntry(i2, i2);
        }
        double[] currentPoint = getStartPoint();
        int nC = currentPoint.length;
        PointVectorValuePair current = null;
        boolean converged = false;
        while (!converged) {
            incrementIterationCount();
            PointVectorValuePair previous = current;
            double[] currentObjective = computeObjectiveValue(currentPoint);
            double[] currentResiduals = computeResiduals(currentObjective);
            RealMatrix weightedJacobian = computeWeightedJacobian(currentPoint);
            current = new PointVectorValuePair(currentPoint, currentObjective);
            double[] b2 = new double[nC];
            double[][] a2 = new double[nC][nC];
            for (int i3 = 0; i3 < nR; i3++) {
                double[] grad = weightedJacobian.getRow(i3);
                double weight = residualsWeights[i3];
                double residual = currentResiduals[i3];
                double wr = weight * residual;
                for (int j2 = 0; j2 < nC; j2++) {
                    int i4 = j2;
                    b2[i4] = b2[i4] + (wr * grad[j2]);
                }
                for (int k2 = 0; k2 < nC; k2++) {
                    double[] ak2 = a2[k2];
                    double wgk = weight * grad[k2];
                    for (int l2 = 0; l2 < nC; l2++) {
                        int i5 = l2;
                        ak2[i5] = ak2[i5] + (wgk * grad[l2]);
                    }
                }
            }
            if (previous != null) {
                converged = checker.converged(getIterations(), previous, current);
                if (converged) {
                    setCost(computeCost(currentResiduals));
                    return current;
                }
            }
            try {
                RealMatrix mA = new BlockRealMatrix(a2);
                DecompositionSolver solver = this.useLU ? new LUDecomposition(mA).getSolver() : new QRDecomposition(mA).getSolver();
                double[] dX = solver.solve(new ArrayRealVector(b2, false)).toArray();
                for (int i6 = 0; i6 < nC; i6++) {
                    int i7 = i6;
                    currentPoint[i7] = currentPoint[i7] + dX[i6];
                }
            } catch (SingularMatrixException e2) {
                throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, new Object[0]);
            }
        }
        throw new MathInternalError();
    }

    private void checkParameters() {
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new MathUnsupportedOperationException(LocalizedFormats.CONSTRAINT, new Object[0]);
        }
    }
}
