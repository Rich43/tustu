package org.apache.commons.math3.optim.univariate;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/univariate/MultiStartUnivariateOptimizer.class */
public class MultiStartUnivariateOptimizer extends UnivariateOptimizer {
    private final UnivariateOptimizer optimizer;
    private int totalEvaluations;
    private int starts;
    private RandomGenerator generator;
    private UnivariatePointValuePair[] optima;
    private OptimizationData[] optimData;
    private int maxEvalIndex;
    private int searchIntervalIndex;

    public MultiStartUnivariateOptimizer(UnivariateOptimizer optimizer, int starts, RandomGenerator generator) {
        super(optimizer.getConvergenceChecker());
        this.maxEvalIndex = -1;
        this.searchIntervalIndex = -1;
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

    public UnivariatePointValuePair[] getOptima() {
        if (this.optima == null) {
            throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
        }
        return (UnivariatePointValuePair[]) this.optima.clone();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.optim.univariate.UnivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    public UnivariatePointValuePair optimize(OptimizationData... optData) {
        this.optimData = optData;
        return super.optimize(optData);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.optim.BaseOptimizer
    public UnivariatePointValuePair doOptimize() {
        for (int i2 = 0; i2 < this.optimData.length; i2++) {
            if (this.optimData[i2] instanceof MaxEval) {
                this.optimData[i2] = null;
                this.maxEvalIndex = i2;
            } else if (this.optimData[i2] instanceof SearchInterval) {
                this.optimData[i2] = null;
                this.searchIntervalIndex = i2;
            }
        }
        if (this.maxEvalIndex == -1) {
            throw new MathIllegalStateException();
        }
        if (this.searchIntervalIndex == -1) {
            throw new MathIllegalStateException();
        }
        RuntimeException lastException = null;
        this.optima = new UnivariatePointValuePair[this.starts];
        this.totalEvaluations = 0;
        int maxEval = getMaxEvaluations();
        double min = getMin();
        double max = getMax();
        double startValue = getStartValue();
        int i3 = 0;
        while (i3 < this.starts) {
            try {
                this.optimData[this.maxEvalIndex] = new MaxEval(maxEval - this.totalEvaluations);
                double s2 = i3 == 0 ? startValue : min + (this.generator.nextDouble() * (max - min));
                this.optimData[this.searchIntervalIndex] = new SearchInterval(min, max, s2);
                this.optima[i3] = this.optimizer.optimize(this.optimData);
            } catch (RuntimeException mue) {
                lastException = mue;
                this.optima[i3] = null;
            }
            this.totalEvaluations += this.optimizer.getEvaluations();
            i3++;
        }
        sortPairs(getGoalType());
        if (this.optima[0] == null) {
            throw lastException;
        }
        return this.optima[0];
    }

    private void sortPairs(final GoalType goal) {
        Arrays.sort(this.optima, new Comparator<UnivariatePointValuePair>() { // from class: org.apache.commons.math3.optim.univariate.MultiStartUnivariateOptimizer.1
            @Override // java.util.Comparator
            public int compare(UnivariatePointValuePair o1, UnivariatePointValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                }
                if (o2 == null) {
                    return -1;
                }
                double v1 = o1.getValue();
                double v2 = o2.getValue();
                return goal == GoalType.MINIMIZE ? Double.compare(v1, v2) : Double.compare(v2, v1);
            }
        });
    }
}
