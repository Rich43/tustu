package org.apache.commons.math3.optimization.univariate;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.random.RandomGenerator;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/univariate/UnivariateMultiStartOptimizer.class */
public class UnivariateMultiStartOptimizer<FUNC extends UnivariateFunction> implements BaseUnivariateOptimizer<FUNC> {
    private final BaseUnivariateOptimizer<FUNC> optimizer;
    private int maxEvaluations;
    private int totalEvaluations;
    private int starts;
    private RandomGenerator generator;
    private UnivariatePointValuePair[] optima;

    public UnivariateMultiStartOptimizer(BaseUnivariateOptimizer<FUNC> optimizer, int starts, RandomGenerator generator) {
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

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public ConvergenceChecker<UnivariatePointValuePair> getConvergenceChecker() {
        return this.optimizer.getConvergenceChecker();
    }

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    @Override // org.apache.commons.math3.optimization.BaseOptimizer
    public int getEvaluations() {
        return this.totalEvaluations;
    }

    public UnivariatePointValuePair[] getOptima() {
        if (this.optima == null) {
            throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
        }
        return (UnivariatePointValuePair[]) this.optima.clone();
    }

    @Override // org.apache.commons.math3.optimization.univariate.BaseUnivariateOptimizer
    public UnivariatePointValuePair optimize(int maxEval, FUNC f2, GoalType goal, double min, double max) {
        return optimize(maxEval, f2, goal, min, max, min + (0.5d * (max - min)));
    }

    @Override // org.apache.commons.math3.optimization.univariate.BaseUnivariateOptimizer
    public UnivariatePointValuePair optimize(int maxEval, FUNC f2, GoalType goal, double min, double max, double startValue) {
        double dNextDouble;
        RuntimeException lastException = null;
        this.optima = new UnivariatePointValuePair[this.starts];
        this.totalEvaluations = 0;
        for (int i2 = 0; i2 < this.starts; i2++) {
            if (i2 == 0) {
                dNextDouble = startValue;
            } else {
                try {
                    dNextDouble = min + (this.generator.nextDouble() * (max - min));
                } catch (RuntimeException mue) {
                    lastException = mue;
                    this.optima[i2] = null;
                }
            }
            double s2 = dNextDouble;
            this.optima[i2] = this.optimizer.optimize(maxEval - this.totalEvaluations, f2, goal, min, max, s2);
            this.totalEvaluations += this.optimizer.getEvaluations();
        }
        sortPairs(goal);
        if (this.optima[0] == null) {
            throw lastException;
        }
        return this.optima[0];
    }

    private void sortPairs(final GoalType goal) {
        Arrays.sort(this.optima, new Comparator<UnivariatePointValuePair>() { // from class: org.apache.commons.math3.optimization.univariate.UnivariateMultiStartOptimizer.1
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
