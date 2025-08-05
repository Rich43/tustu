package org.apache.commons.math3.optim.nonlinear.scalar.gradient;

import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GradientMultivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.LineSearch;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/gradient/NonLinearConjugateGradientOptimizer.class */
public class NonLinearConjugateGradientOptimizer extends GradientMultivariateOptimizer {
    private final Formula updateFormula;
    private final Preconditioner preconditioner;
    private final LineSearch line;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/gradient/NonLinearConjugateGradientOptimizer$Formula.class */
    public enum Formula {
        FLETCHER_REEVES,
        POLAK_RIBIERE
    }

    @Deprecated
    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/gradient/NonLinearConjugateGradientOptimizer$BracketingStep.class */
    public static class BracketingStep implements OptimizationData {
        private final double initialStep;

        public BracketingStep(double step) {
            this.initialStep = step;
        }

        public double getBracketingStep() {
            return this.initialStep;
        }
    }

    public NonLinearConjugateGradientOptimizer(Formula updateFormula, ConvergenceChecker<PointValuePair> checker) {
        this(updateFormula, checker, 1.0E-8d, 1.0E-8d, 1.0E-8d, new IdentityPreconditioner());
    }

    @Deprecated
    public NonLinearConjugateGradientOptimizer(Formula updateFormula, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver) {
        this(updateFormula, checker, lineSearchSolver, new IdentityPreconditioner());
    }

    public NonLinearConjugateGradientOptimizer(Formula updateFormula, ConvergenceChecker<PointValuePair> checker, double relativeTolerance, double absoluteTolerance, double initialBracketingRange) {
        this(updateFormula, checker, relativeTolerance, absoluteTolerance, initialBracketingRange, new IdentityPreconditioner());
    }

    @Deprecated
    public NonLinearConjugateGradientOptimizer(Formula updateFormula, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver, Preconditioner preconditioner) {
        this(updateFormula, checker, lineSearchSolver.getRelativeAccuracy(), lineSearchSolver.getAbsoluteAccuracy(), lineSearchSolver.getAbsoluteAccuracy(), preconditioner);
    }

    public NonLinearConjugateGradientOptimizer(Formula updateFormula, ConvergenceChecker<PointValuePair> checker, double relativeTolerance, double absoluteTolerance, double initialBracketingRange, Preconditioner preconditioner) {
        super(checker);
        this.updateFormula = updateFormula;
        this.preconditioner = preconditioner;
        this.line = new LineSearch(this, relativeTolerance, absoluteTolerance, initialBracketingRange);
    }

    @Override // org.apache.commons.math3.optim.nonlinear.scalar.GradientMultivariateOptimizer, org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    public PointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException {
        return super.optimize(optData);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.optim.BaseOptimizer
    public PointValuePair doOptimize() {
        double beta;
        ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        double[] point = getStartPoint();
        GoalType goal = getGoalType();
        int n2 = point.length;
        double[] r2 = computeObjectiveGradient(point);
        if (goal == GoalType.MINIMIZE) {
            for (int i2 = 0; i2 < n2; i2++) {
                r2[i2] = -r2[i2];
            }
        }
        double[] steepestDescent = this.preconditioner.precondition(point, r2);
        double[] searchDirection = (double[]) steepestDescent.clone();
        double delta = 0.0d;
        for (int i3 = 0; i3 < n2; i3++) {
            delta += r2[i3] * searchDirection[i3];
        }
        PointValuePair current = null;
        while (true) {
            incrementIterationCount();
            double objective = computeObjectiveValue(point);
            PointValuePair previous = current;
            current = new PointValuePair(point, objective);
            if (previous != null && checker.converged(getIterations(), previous, current)) {
                return current;
            }
            double step = this.line.search(point, searchDirection).getPoint();
            for (int i4 = 0; i4 < point.length; i4++) {
                int i5 = i4;
                point[i5] = point[i5] + (step * searchDirection[i4]);
            }
            double[] r3 = computeObjectiveGradient(point);
            if (goal == GoalType.MINIMIZE) {
                for (int i6 = 0; i6 < n2; i6++) {
                    r3[i6] = -r3[i6];
                }
            }
            double deltaOld = delta;
            double[] newSteepestDescent = this.preconditioner.precondition(point, r3);
            delta = 0.0d;
            for (int i7 = 0; i7 < n2; i7++) {
                delta += r3[i7] * newSteepestDescent[i7];
            }
            switch (this.updateFormula) {
                case FLETCHER_REEVES:
                    beta = delta / deltaOld;
                    break;
                case POLAK_RIBIERE:
                    double deltaMid = 0.0d;
                    for (int i8 = 0; i8 < r3.length; i8++) {
                        deltaMid += r3[i8] * steepestDescent[i8];
                    }
                    beta = (delta - deltaMid) / deltaOld;
                    break;
                default:
                    throw new MathInternalError();
            }
            steepestDescent = newSteepestDescent;
            if (getIterations() % n2 == 0 || beta < 0.0d) {
                searchDirection = (double[]) steepestDescent.clone();
            } else {
                for (int i9 = 0; i9 < n2; i9++) {
                    searchDirection[i9] = steepestDescent[i9] + (beta * searchDirection[i9]);
                }
            }
        }
    }

    @Override // org.apache.commons.math3.optim.nonlinear.scalar.GradientMultivariateOptimizer, org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    protected void parseOptimizationData(OptimizationData... optData) {
        super.parseOptimizationData(optData);
        checkParameters();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/gradient/NonLinearConjugateGradientOptimizer$IdentityPreconditioner.class */
    public static class IdentityPreconditioner implements Preconditioner {
        @Override // org.apache.commons.math3.optim.nonlinear.scalar.gradient.Preconditioner
        public double[] precondition(double[] variables, double[] r2) {
            return (double[]) r2.clone();
        }
    }

    private void checkParameters() {
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new MathUnsupportedOperationException(LocalizedFormats.CONSTRAINT, new Object[0]);
        }
    }
}
