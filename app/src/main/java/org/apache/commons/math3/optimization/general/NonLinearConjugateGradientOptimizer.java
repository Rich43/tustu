package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/general/NonLinearConjugateGradientOptimizer.class */
public class NonLinearConjugateGradientOptimizer extends AbstractScalarDifferentiableOptimizer {
    private final ConjugateGradientFormula updateFormula;
    private final Preconditioner preconditioner;
    private final UnivariateSolver solver;
    private double initialStep;
    private double[] point;

    @Deprecated
    public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula) {
        this(updateFormula, new SimpleValueChecker());
    }

    public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula, ConvergenceChecker<PointValuePair> checker) {
        this(updateFormula, checker, new BrentSolver(), new IdentityPreconditioner());
    }

    public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver) {
        this(updateFormula, checker, lineSearchSolver, new IdentityPreconditioner());
    }

    public NonLinearConjugateGradientOptimizer(ConjugateGradientFormula updateFormula, ConvergenceChecker<PointValuePair> checker, UnivariateSolver lineSearchSolver, Preconditioner preconditioner) {
        super(checker);
        this.updateFormula = updateFormula;
        this.solver = lineSearchSolver;
        this.preconditioner = preconditioner;
        this.initialStep = 1.0d;
    }

    public void setInitialStep(double initialStep) {
        if (initialStep <= 0.0d) {
            this.initialStep = 1.0d;
        } else {
            this.initialStep = initialStep;
        }
    }

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    protected PointValuePair doOptimize() {
        double beta;
        ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        this.point = getStartPoint();
        GoalType goal = getGoalType();
        int n2 = this.point.length;
        double[] r2 = computeObjectiveGradient(this.point);
        if (goal == GoalType.MINIMIZE) {
            for (int i2 = 0; i2 < n2; i2++) {
                r2[i2] = -r2[i2];
            }
        }
        double[] steepestDescent = this.preconditioner.precondition(this.point, r2);
        double[] searchDirection = (double[]) steepestDescent.clone();
        double delta = 0.0d;
        for (int i3 = 0; i3 < n2; i3++) {
            delta += r2[i3] * searchDirection[i3];
        }
        PointValuePair current = null;
        int iter = 0;
        int maxEval = getMaxEvaluations();
        while (true) {
            iter++;
            double objective = computeObjectiveValue(this.point);
            PointValuePair previous = current;
            current = new PointValuePair(this.point, objective);
            if (previous != null && checker.converged(iter, previous, current)) {
                return current;
            }
            LineSearchFunction lineSearchFunction = new LineSearchFunction(searchDirection);
            double uB = findUpperBound(lineSearchFunction, 0.0d, this.initialStep);
            double step = this.solver.solve(maxEval, lineSearchFunction, 0.0d, uB, 1.0E-15d);
            maxEval -= this.solver.getEvaluations();
            for (int i4 = 0; i4 < this.point.length; i4++) {
                double[] dArr = this.point;
                int i5 = i4;
                dArr[i5] = dArr[i5] + (step * searchDirection[i4]);
            }
            double[] r3 = computeObjectiveGradient(this.point);
            if (goal == GoalType.MINIMIZE) {
                for (int i6 = 0; i6 < n2; i6++) {
                    r3[i6] = -r3[i6];
                }
            }
            double deltaOld = delta;
            double[] newSteepestDescent = this.preconditioner.precondition(this.point, r3);
            delta = 0.0d;
            for (int i7 = 0; i7 < n2; i7++) {
                delta += r3[i7] * newSteepestDescent[i7];
            }
            if (this.updateFormula == ConjugateGradientFormula.FLETCHER_REEVES) {
                beta = delta / deltaOld;
            } else {
                double deltaMid = 0.0d;
                for (int i8 = 0; i8 < r3.length; i8++) {
                    deltaMid += r3[i8] * steepestDescent[i8];
                }
                beta = (delta - deltaMid) / deltaOld;
            }
            steepestDescent = newSteepestDescent;
            if (iter % n2 == 0 || beta < 0.0d) {
                searchDirection = (double[]) steepestDescent.clone();
            } else {
                for (int i9 = 0; i9 < n2; i9++) {
                    searchDirection[i9] = steepestDescent[i9] + (beta * searchDirection[i9]);
                }
            }
        }
    }

    private double findUpperBound(UnivariateFunction f2, double a2, double h2) {
        double yA = f2.value(a2);
        double dMax = h2;
        while (true) {
            double step = dMax;
            if (step < Double.MAX_VALUE) {
                double b2 = a2 + step;
                double yB = f2.value(b2);
                if (yA * yB > 0.0d) {
                    dMax = step * FastMath.max(2.0d, yA / yB);
                } else {
                    return b2;
                }
            } else {
                throw new MathIllegalStateException(LocalizedFormats.UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH, new Object[0]);
            }
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/general/NonLinearConjugateGradientOptimizer$IdentityPreconditioner.class */
    public static class IdentityPreconditioner implements Preconditioner {
        @Override // org.apache.commons.math3.optimization.general.Preconditioner
        public double[] precondition(double[] variables, double[] r2) {
            return (double[]) r2.clone();
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/general/NonLinearConjugateGradientOptimizer$LineSearchFunction.class */
    private class LineSearchFunction implements UnivariateFunction {
        private final double[] searchDirection;

        LineSearchFunction(double[] searchDirection) {
            this.searchDirection = searchDirection;
        }

        @Override // org.apache.commons.math3.analysis.UnivariateFunction
        public double value(double x2) {
            double[] shiftedPoint = (double[]) NonLinearConjugateGradientOptimizer.this.point.clone();
            for (int i2 = 0; i2 < shiftedPoint.length; i2++) {
                int i3 = i2;
                shiftedPoint[i3] = shiftedPoint[i3] + (x2 * this.searchDirection[i2]);
            }
            double[] gradient = NonLinearConjugateGradientOptimizer.this.computeObjectiveGradient(shiftedPoint);
            double dotProduct = 0.0d;
            for (int i4 = 0; i4 < gradient.length; i4++) {
                dotProduct += gradient[i4] * this.searchDirection[i4];
            }
            return dotProduct;
        }
    }
}
