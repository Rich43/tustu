package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.IntegerSequence;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/BaseAbstractUnivariateIntegrator.class */
public abstract class BaseAbstractUnivariateIntegrator implements UnivariateIntegrator {
    public static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-15d;
    public static final double DEFAULT_RELATIVE_ACCURACY = 1.0E-6d;
    public static final int DEFAULT_MIN_ITERATIONS_COUNT = 3;
    public static final int DEFAULT_MAX_ITERATIONS_COUNT = Integer.MAX_VALUE;

    @Deprecated
    protected Incrementor iterations;
    private IntegerSequence.Incrementor count;
    private final double absoluteAccuracy;
    private final double relativeAccuracy;
    private final int minimalIterationCount;
    private IntegerSequence.Incrementor evaluations;
    private UnivariateFunction function;
    private double min;
    private double max;

    protected abstract double doIntegrate() throws MaxCountExceededException;

    protected BaseAbstractUnivariateIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException {
        this.relativeAccuracy = relativeAccuracy;
        this.absoluteAccuracy = absoluteAccuracy;
        if (minimalIterationCount <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(minimalIterationCount));
        }
        if (maximalIterationCount <= minimalIterationCount) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalIterationCount), Integer.valueOf(minimalIterationCount), false);
        }
        this.minimalIterationCount = minimalIterationCount;
        this.count = IntegerSequence.Incrementor.create().withMaximalCount(maximalIterationCount);
        Incrementor wrapped = Incrementor.wrap(this.count);
        this.iterations = wrapped;
        this.evaluations = IntegerSequence.Incrementor.create();
    }

    protected BaseAbstractUnivariateIntegrator(double relativeAccuracy, double absoluteAccuracy) {
        this(relativeAccuracy, absoluteAccuracy, 3, Integer.MAX_VALUE);
    }

    protected BaseAbstractUnivariateIntegrator(int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException {
        this(1.0E-6d, 1.0E-15d, minimalIterationCount, maximalIterationCount);
    }

    @Override // org.apache.commons.math3.analysis.integration.UnivariateIntegrator
    public double getRelativeAccuracy() {
        return this.relativeAccuracy;
    }

    @Override // org.apache.commons.math3.analysis.integration.UnivariateIntegrator
    public double getAbsoluteAccuracy() {
        return this.absoluteAccuracy;
    }

    @Override // org.apache.commons.math3.analysis.integration.UnivariateIntegrator
    public int getMinimalIterationCount() {
        return this.minimalIterationCount;
    }

    @Override // org.apache.commons.math3.analysis.integration.UnivariateIntegrator
    public int getMaximalIterationCount() {
        return this.count.getMaximalCount();
    }

    @Override // org.apache.commons.math3.analysis.integration.UnivariateIntegrator
    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    @Override // org.apache.commons.math3.analysis.integration.UnivariateIntegrator
    public int getIterations() {
        return this.count.getCount();
    }

    protected void incrementCount() throws MaxCountExceededException {
        this.count.increment();
    }

    protected double getMin() {
        return this.min;
    }

    protected double getMax() {
        return this.max;
    }

    protected double computeObjectiveValue(double point) throws TooManyEvaluationsException {
        try {
            this.evaluations.increment();
            return this.function.value(point);
        } catch (MaxCountExceededException e2) {
            throw new TooManyEvaluationsException(e2.getMax());
        }
    }

    protected void setup(int maxEval, UnivariateFunction f2, double lower, double upper) throws MathIllegalArgumentException {
        MathUtils.checkNotNull(f2);
        UnivariateSolverUtils.verifyInterval(lower, upper);
        this.min = lower;
        this.max = upper;
        this.function = f2;
        this.evaluations = this.evaluations.withMaximalCount(maxEval).withStart(0);
        this.count = this.count.withStart(0);
    }

    @Override // org.apache.commons.math3.analysis.integration.UnivariateIntegrator
    public double integrate(int maxEval, UnivariateFunction f2, double lower, double upper) throws MaxCountExceededException, MathIllegalArgumentException {
        setup(maxEval, f2, lower, upper);
        return doIntegrate();
    }
}
