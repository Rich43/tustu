package org.apache.commons.math3.optimization;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomVectorGenerator;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/BaseMultivariateMultiStartOptimizer.class */
public class BaseMultivariateMultiStartOptimizer<FUNC extends MultivariateFunction> implements BaseMultivariateOptimizer<FUNC> {
    private final BaseMultivariateOptimizer<FUNC> optimizer;
    private int maxEvaluations;
    private int totalEvaluations;
    private int starts;
    private RandomVectorGenerator generator;
    private PointValuePair[] optima;

    protected BaseMultivariateMultiStartOptimizer(BaseMultivariateOptimizer<FUNC> optimizer, int starts, RandomVectorGenerator generator) {
        if (optimizer == null || generator == null) {
            throw new NullArgumentException();
        }
        if (starts < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(starts));
        }
        this.optimizer = optimizer;
        this.starts = starts;
        this.generator = generator;
    }

    public PointValuePair[] getOptima() {
        if (this.optima == null) {
            throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
        }
        return (PointValuePair[]) this.optima.clone();
    }

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public int getEvaluations() {
        return this.totalEvaluations;
    }

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return this.optimizer.getConvergenceChecker();
    }

    @Override // org.apache.commons.math3.optimization.BaseMultivariateOptimizer
    public PointValuePair optimize(int maxEval, FUNC f2, GoalType goal, double[] startPoint) {
        this.maxEvaluations = maxEval;
        RuntimeException lastException = null;
        this.optima = new PointValuePair[this.starts];
        this.totalEvaluations = 0;
        int i2 = 0;
        while (i2 < this.starts) {
            try {
                this.optima[i2] = this.optimizer.optimize(maxEval - this.totalEvaluations, f2, goal, i2 == 0 ? startPoint : this.generator.nextVector());
            } catch (RuntimeException mue) {
                lastException = mue;
                this.optima[i2] = null;
            }
            this.totalEvaluations += this.optimizer.getEvaluations();
            i2++;
        }
        sortPairs(goal);
        if (this.optima[0] == null) {
            throw lastException;
        }
        return this.optima[0];
    }

    private void sortPairs(final GoalType goal) {
        Arrays.sort(this.optima, new Comparator<PointValuePair>() { // from class: org.apache.commons.math3.optimization.BaseMultivariateMultiStartOptimizer.1
            @Override // java.util.Comparator
            public int compare(PointValuePair o1, PointValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                }
                if (o2 == null) {
                    return -1;
                }
                double v1 = o1.getValue().doubleValue();
                double v2 = o2.getValue().doubleValue();
                return goal == GoalType.MINIMIZE ? Double.compare(v1, v2) : Double.compare(v2, v1);
            }
        });
    }
}
