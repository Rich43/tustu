package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.IntegerSequence;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BaseAbstractUnivariateSolver.class */
public abstract class BaseAbstractUnivariateSolver<FUNC extends UnivariateFunction> implements BaseUnivariateSolver<FUNC> {
    private static final double DEFAULT_RELATIVE_ACCURACY = 1.0E-14d;
    private static final double DEFAULT_FUNCTION_VALUE_ACCURACY = 1.0E-15d;
    private final double functionValueAccuracy;
    private final double absoluteAccuracy;
    private final double relativeAccuracy;
    private IntegerSequence.Incrementor evaluations;
    private double searchMin;
    private double searchMax;
    private double searchStart;
    private FUNC function;

    protected abstract double doSolve() throws TooManyEvaluationsException, NoBracketingException;

    protected BaseAbstractUnivariateSolver(double absoluteAccuracy) {
        this(DEFAULT_RELATIVE_ACCURACY, absoluteAccuracy, 1.0E-15d);
    }

    protected BaseAbstractUnivariateSolver(double relativeAccuracy, double absoluteAccuracy) {
        this(relativeAccuracy, absoluteAccuracy, 1.0E-15d);
    }

    protected BaseAbstractUnivariateSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        this.absoluteAccuracy = absoluteAccuracy;
        this.relativeAccuracy = relativeAccuracy;
        this.functionValueAccuracy = functionValueAccuracy;
        this.evaluations = IntegerSequence.Incrementor.create();
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    public double getMin() {
        return this.searchMin;
    }

    public double getMax() {
        return this.searchMax;
    }

    public double getStartValue() {
        return this.searchStart;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double getAbsoluteAccuracy() {
        return this.absoluteAccuracy;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double getRelativeAccuracy() {
        return this.relativeAccuracy;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double getFunctionValueAccuracy() {
        return this.functionValueAccuracy;
    }

    protected double computeObjectiveValue(double point) throws TooManyEvaluationsException {
        incrementEvaluationCount();
        return this.function.value(point);
    }

    protected void setup(int maxEval, FUNC f2, double min, double max, double startValue) throws NullArgumentException {
        MathUtils.checkNotNull(f2);
        this.searchMin = min;
        this.searchMax = max;
        this.searchStart = startValue;
        this.function = f2;
        this.evaluations = this.evaluations.withMaximalCount(maxEval).withStart(0);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double solve(int maxEval, FUNC f2, double min, double max, double startValue) throws NullArgumentException, TooManyEvaluationsException, NoBracketingException {
        setup(maxEval, f2, min, max, startValue);
        return doSolve();
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double solve(int maxEval, FUNC f2, double min, double max) {
        return solve(maxEval, f2, min, max, min + (0.5d * (max - min)));
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double solve(int maxEval, FUNC f2, double startValue) throws TooManyEvaluationsException, NoBracketingException {
        return solve(maxEval, f2, Double.NaN, Double.NaN, startValue);
    }

    protected boolean isBracketing(double lower, double upper) {
        return UnivariateSolverUtils.isBracketing(this.function, lower, upper);
    }

    protected boolean isSequence(double start, double mid, double end) {
        return UnivariateSolverUtils.isSequence(start, mid, end);
    }

    protected void verifyInterval(double lower, double upper) throws NumberIsTooLargeException {
        UnivariateSolverUtils.verifyInterval(lower, upper);
    }

    protected void verifySequence(double lower, double initial, double upper) throws NumberIsTooLargeException {
        UnivariateSolverUtils.verifySequence(lower, initial, upper);
    }

    protected void verifyBracketing(double lower, double upper) throws NullArgumentException, NoBracketingException {
        UnivariateSolverUtils.verifyBracketing(this.function, lower, upper);
    }

    protected void incrementEvaluationCount() throws TooManyEvaluationsException {
        try {
            this.evaluations.increment();
        } catch (MaxCountExceededException e2) {
            throw new TooManyEvaluationsException(e2.getMax());
        }
    }
}
