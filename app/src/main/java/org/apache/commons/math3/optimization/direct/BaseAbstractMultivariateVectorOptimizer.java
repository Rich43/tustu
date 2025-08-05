package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.BaseMultivariateVectorOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.Target;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.util.Incrementor;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/BaseAbstractMultivariateVectorOptimizer.class */
public abstract class BaseAbstractMultivariateVectorOptimizer<FUNC extends MultivariateVectorFunction> implements BaseMultivariateVectorOptimizer<FUNC> {
    protected final Incrementor evaluations;
    private ConvergenceChecker<PointVectorValuePair> checker;
    private double[] target;
    private RealMatrix weightMatrix;

    @Deprecated
    private double[] weight;
    private double[] start;
    private FUNC function;

    protected abstract PointVectorValuePair doOptimize();

    @Deprecated
    protected BaseAbstractMultivariateVectorOptimizer() {
        this(new SimpleVectorValueChecker());
    }

    protected BaseAbstractMultivariateVectorOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        this.evaluations = new Incrementor();
        this.checker = checker;
    }

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public ConvergenceChecker<PointVectorValuePair> getConvergenceChecker() {
        return this.checker;
    }

    protected double[] computeObjectiveValue(double[] point) {
        try {
            this.evaluations.incrementCount();
            return this.function.value(point);
        } catch (MaxCountExceededException e2) {
            throw new TooManyEvaluationsException(e2.getMax());
        }
    }

    @Override // org.apache.commons.math3.optimization.BaseMultivariateVectorOptimizer
    @Deprecated
    public PointVectorValuePair optimize(int maxEval, FUNC f2, double[] t2, double[] w2, double[] startPoint) {
        return optimizeInternal(maxEval, f2, t2, w2, startPoint);
    }

    protected PointVectorValuePair optimize(int maxEval, FUNC f2, OptimizationData... optData) throws TooManyEvaluationsException, DimensionMismatchException {
        return optimizeInternal(maxEval, f2, optData);
    }

    @Deprecated
    protected PointVectorValuePair optimizeInternal(int maxEval, FUNC f2, double[] t2, double[] w2, double[] startPoint) {
        if (f2 == null) {
            throw new NullArgumentException();
        }
        if (t2 == null) {
            throw new NullArgumentException();
        }
        if (w2 == null) {
            throw new NullArgumentException();
        }
        if (startPoint == null) {
            throw new NullArgumentException();
        }
        if (t2.length != w2.length) {
            throw new DimensionMismatchException(t2.length, w2.length);
        }
        return optimizeInternal(maxEval, f2, new Target(t2), new Weight(w2), new InitialGuess(startPoint));
    }

    protected PointVectorValuePair optimizeInternal(int maxEval, FUNC f2, OptimizationData... optData) throws TooManyEvaluationsException, DimensionMismatchException {
        this.evaluations.setMaximalCount(maxEval);
        this.evaluations.resetCount();
        this.function = f2;
        parseOptimizationData(optData);
        checkParameters();
        setUp();
        return doOptimize();
    }

    public double[] getStartPoint() {
        return (double[]) this.start.clone();
    }

    public RealMatrix getWeight() {
        return this.weightMatrix.copy();
    }

    public double[] getTarget() {
        return (double[]) this.target.clone();
    }

    protected FUNC getObjectiveFunction() {
        return this.function;
    }

    @Deprecated
    protected double[] getTargetRef() {
        return this.target;
    }

    @Deprecated
    protected double[] getWeightRef() {
        return this.weight;
    }

    protected void setUp() {
        int dim = this.target.length;
        this.weight = new double[dim];
        for (int i2 = 0; i2 < dim; i2++) {
            this.weight[i2] = this.weightMatrix.getEntry(i2, i2);
        }
    }

    private void parseOptimizationData(OptimizationData... optData) {
        for (OptimizationData data : optData) {
            if (data instanceof Target) {
                this.target = ((Target) data).getTarget();
            } else if (data instanceof Weight) {
                this.weightMatrix = ((Weight) data).getWeight();
            } else if (data instanceof InitialGuess) {
                this.start = ((InitialGuess) data).getInitialGuess();
            }
        }
    }

    private void checkParameters() {
        if (this.target.length != this.weightMatrix.getColumnDimension()) {
            throw new DimensionMismatchException(this.target.length, this.weightMatrix.getColumnDimension());
        }
    }
}
