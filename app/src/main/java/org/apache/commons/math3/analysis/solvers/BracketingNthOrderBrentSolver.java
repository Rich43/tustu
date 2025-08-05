package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BracketingNthOrderBrentSolver.class */
public class BracketingNthOrderBrentSolver extends AbstractUnivariateSolver implements BracketedUnivariateSolver<UnivariateFunction> {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    private static final int DEFAULT_MAXIMAL_ORDER = 5;
    private static final int MAXIMAL_AGING = 2;
    private static final double REDUCTION_FACTOR = 0.0625d;
    private final int maximalOrder;
    private AllowedSolution allowed;

    public BracketingNthOrderBrentSolver() {
        this(1.0E-6d, 5);
    }

    public BracketingNthOrderBrentSolver(double absoluteAccuracy, int maximalOrder) throws NumberIsTooSmallException {
        super(absoluteAccuracy);
        if (maximalOrder < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder), 2, true);
        }
        this.maximalOrder = maximalOrder;
        this.allowed = AllowedSolution.ANY_SIDE;
    }

    public BracketingNthOrderBrentSolver(double relativeAccuracy, double absoluteAccuracy, int maximalOrder) throws NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy);
        if (maximalOrder < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder), 2, true);
        }
        this.maximalOrder = maximalOrder;
        this.allowed = AllowedSolution.ANY_SIDE;
    }

    public BracketingNthOrderBrentSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, int maximalOrder) throws NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        if (maximalOrder < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder), 2, true);
        }
        this.maximalOrder = maximalOrder;
        this.allowed = AllowedSolution.ANY_SIDE;
    }

    public int getMaximalOrder() {
        return this.maximalOrder;
    }

    /* JADX WARN: Incorrect condition in loop: B:24:0x0112 */
    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected double doSolve() throws org.apache.commons.math3.exception.TooManyEvaluationsException, org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.NoBracketingException {
        /*
            Method dump skipped, instructions count: 849
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver.doSolve():double");
    }

    private double guessX(double targetY, double[] x2, double[] y2, int start, int end) {
        for (int i2 = start; i2 < end - 1; i2++) {
            int delta = (i2 + 1) - start;
            for (int j2 = end - 1; j2 > i2; j2--) {
                x2[j2] = (x2[j2] - x2[j2 - 1]) / (y2[j2] - y2[j2 - delta]);
            }
        }
        double x0 = 0.0d;
        for (int j3 = end - 1; j3 >= start; j3--) {
            x0 = x2[j3] + (x0 * (targetY - y2[j3]));
        }
        return x0;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver
    public double solve(int maxEval, UnivariateFunction f2, double min, double max, AllowedSolution allowedSolution) throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        this.allowed = allowedSolution;
        return super.solve(maxEval, f2, min, max);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver
    public double solve(int maxEval, UnivariateFunction f2, double min, double max, double startValue, AllowedSolution allowedSolution) throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        this.allowed = allowedSolution;
        return super.solve(maxEval, (int) f2, min, max, startValue);
    }
}
