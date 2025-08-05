package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/GaussNewtonOptimizer.class */
public class GaussNewtonOptimizer implements LeastSquaresOptimizer {
    private static final double SINGULARITY_THRESHOLD = 1.0E-11d;
    private final Decomposition decomposition;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/GaussNewtonOptimizer$Decomposition.class */
    public enum Decomposition {
        LU { // from class: org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer.Decomposition.1
            @Override // org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer.Decomposition
            protected RealVector solve(RealMatrix jacobian, RealVector residuals) throws OutOfRangeException {
                try {
                    Pair<RealMatrix, RealVector> normalEquation = GaussNewtonOptimizer.computeNormalMatrix(jacobian, residuals);
                    RealMatrix normal = normalEquation.getFirst();
                    RealVector jTr = normalEquation.getSecond();
                    return new LUDecomposition(normal, GaussNewtonOptimizer.SINGULARITY_THRESHOLD).getSolver().solve(jTr);
                } catch (SingularMatrixException e2) {
                    throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, e2);
                }
            }
        },
        QR { // from class: org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer.Decomposition.2
            @Override // org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer.Decomposition
            protected RealVector solve(RealMatrix jacobian, RealVector residuals) {
                try {
                    return new QRDecomposition(jacobian, GaussNewtonOptimizer.SINGULARITY_THRESHOLD).getSolver().solve(residuals);
                } catch (SingularMatrixException e2) {
                    throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, e2);
                }
            }
        },
        CHOLESKY { // from class: org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer.Decomposition.3
            @Override // org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer.Decomposition
            protected RealVector solve(RealMatrix jacobian, RealVector residuals) throws OutOfRangeException {
                try {
                    Pair<RealMatrix, RealVector> normalEquation = GaussNewtonOptimizer.computeNormalMatrix(jacobian, residuals);
                    RealMatrix normal = normalEquation.getFirst();
                    RealVector jTr = normalEquation.getSecond();
                    return new CholeskyDecomposition(normal, GaussNewtonOptimizer.SINGULARITY_THRESHOLD, GaussNewtonOptimizer.SINGULARITY_THRESHOLD).getSolver().solve(jTr);
                } catch (NonPositiveDefiniteMatrixException e2) {
                    throw new ConvergenceException(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, e2);
                }
            }
        },
        SVD { // from class: org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer.Decomposition.4
            @Override // org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer.Decomposition
            protected RealVector solve(RealMatrix jacobian, RealVector residuals) {
                return new SingularValueDecomposition(jacobian).getSolver().solve(residuals);
            }
        };

        protected abstract RealVector solve(RealMatrix realMatrix, RealVector realVector);
    }

    public GaussNewtonOptimizer() {
        this(Decomposition.QR);
    }

    public GaussNewtonOptimizer(Decomposition decomposition) {
        this.decomposition = decomposition;
    }

    public Decomposition getDecomposition() {
        return this.decomposition;
    }

    public GaussNewtonOptimizer withDecomposition(Decomposition newDecomposition) {
        return new GaussNewtonOptimizer(newDecomposition);
    }

    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer
    public LeastSquaresOptimizer.Optimum optimize(LeastSquaresProblem lsp) throws OutOfRangeException, MaxCountExceededException, DimensionMismatchException {
        Incrementor evaluationCounter = lsp.getEvaluationCounter();
        Incrementor iterationCounter = lsp.getIterationCounter();
        ConvergenceChecker<LeastSquaresProblem.Evaluation> checker = lsp.getConvergenceChecker();
        if (checker == null) {
            throw new NullArgumentException();
        }
        RealVector currentPoint = lsp.getStart();
        LeastSquaresProblem.Evaluation current = null;
        while (true) {
            iterationCounter.incrementCount();
            LeastSquaresProblem.Evaluation previous = current;
            evaluationCounter.incrementCount();
            current = lsp.evaluate(currentPoint);
            RealVector currentResiduals = current.getResiduals();
            RealMatrix weightedJacobian = current.getJacobian();
            RealVector currentPoint2 = current.getPoint();
            if (previous != null && checker.converged(iterationCounter.getCount(), previous, current)) {
                return new OptimumImpl(current, evaluationCounter.getCount(), iterationCounter.getCount());
            }
            RealVector dX = this.decomposition.solve(weightedJacobian, currentResiduals);
            currentPoint = currentPoint2.add(dX);
        }
    }

    public String toString() {
        return "GaussNewtonOptimizer{decomposition=" + ((Object) this.decomposition) + '}';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Pair<RealMatrix, RealVector> computeNormalMatrix(RealMatrix jacobian, RealVector residuals) throws OutOfRangeException {
        int nR = jacobian.getRowDimension();
        int nC = jacobian.getColumnDimension();
        RealMatrix normal = MatrixUtils.createRealMatrix(nC, nC);
        RealVector jTr = new ArrayRealVector(nC);
        for (int i2 = 0; i2 < nR; i2++) {
            for (int j2 = 0; j2 < nC; j2++) {
                jTr.setEntry(j2, jTr.getEntry(j2) + (residuals.getEntry(i2) * jacobian.getEntry(i2, j2)));
            }
            for (int k2 = 0; k2 < nC; k2++) {
                for (int l2 = k2; l2 < nC; l2++) {
                    normal.setEntry(k2, l2, normal.getEntry(k2, l2) + (jacobian.getEntry(i2, k2) * jacobian.getEntry(i2, l2)));
                }
            }
        }
        for (int i3 = 0; i3 < nC; i3++) {
            for (int j3 = 0; j3 < i3; j3++) {
                normal.setEntry(i3, j3, normal.getEntry(j3, i3));
            }
        }
        return new Pair<>(normal, jTr);
    }
}
