package org.apache.commons.math3.optim;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.random.RandomVectorGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/BaseMultiStartMultivariateOptimizer.class */
public abstract class BaseMultiStartMultivariateOptimizer<PAIR> extends BaseMultivariateOptimizer<PAIR> {
    private final BaseMultivariateOptimizer<PAIR> optimizer;
    private int totalEvaluations;
    private int starts;
    private RandomVectorGenerator generator;
    private OptimizationData[] optimData;
    private int maxEvalIndex;
    private int initialGuessIndex;

    public abstract PAIR[] getOptima();

    protected abstract void store(PAIR pair);

    protected abstract void clear();

    public BaseMultiStartMultivariateOptimizer(BaseMultivariateOptimizer<PAIR> optimizer, int starts, RandomVectorGenerator generator) {
        super(optimizer.getConvergenceChecker());
        this.maxEvalIndex = -1;
        this.initialGuessIndex = -1;
        if (starts < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(starts));
        }
        this.optimizer = optimizer;
        this.starts = starts;
        this.generator = generator;
    }

    @Override // org.apache.commons.math3.optim.BaseOptimizer
    public int getEvaluations() {
        return this.totalEvaluations;
    }

    @Override // org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    public PAIR optimize(OptimizationData... optimizationDataArr) {
        this.optimData = optimizationDataArr;
        return (PAIR) super.optimize(optimizationDataArr);
    }

    @Override // org.apache.commons.math3.optim.BaseOptimizer
    protected PAIR doOptimize() {
        for (int i2 = 0; i2 < this.optimData.length; i2++) {
            if (this.optimData[i2] instanceof MaxEval) {
                this.optimData[i2] = null;
                this.maxEvalIndex = i2;
            }
            if (this.optimData[i2] instanceof InitialGuess) {
                this.optimData[i2] = null;
                this.initialGuessIndex = i2;
            }
        }
        if (this.maxEvalIndex == -1) {
            throw new MathIllegalStateException();
        }
        if (this.initialGuessIndex == -1) {
            throw new MathIllegalStateException();
        }
        RuntimeException lastException = null;
        this.totalEvaluations = 0;
        clear();
        int maxEval = getMaxEvaluations();
        double[] min = getLowerBound();
        double[] max = getUpperBound();
        double[] startPoint = getStartPoint();
        for (int i3 = 0; i3 < this.starts; i3++) {
            try {
                this.optimData[this.maxEvalIndex] = new MaxEval(maxEval - this.totalEvaluations);
                double[] s2 = null;
                if (i3 == 0) {
                    s2 = startPoint;
                } else {
                    int attempts = 0;
                    while (s2 == null) {
                        int i4 = attempts;
                        attempts++;
                        if (i4 >= getMaxEvaluations()) {
                            throw new TooManyEvaluationsException(Integer.valueOf(getMaxEvaluations()));
                        }
                        s2 = this.generator.nextVector();
                        for (int k2 = 0; s2 != null && k2 < s2.length; k2++) {
                            if ((min != null && s2[k2] < min[k2]) || (max != null && s2[k2] > max[k2])) {
                                s2 = null;
                            }
                        }
                    }
                }
                this.optimData[this.initialGuessIndex] = new InitialGuess(s2);
                PAIR result = this.optimizer.optimize(this.optimData);
                store(result);
            } catch (RuntimeException mue) {
                lastException = mue;
            }
            this.totalEvaluations += this.optimizer.getEvaluations();
        }
        PAIR[] optima = getOptima();
        if (optima.length == 0) {
            throw lastException;
        }
        return optima[0];
    }
}
