package org.apache.commons.math3.optimization;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomVectorGenerator;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/BaseMultivariateVectorMultiStartOptimizer.class */
public class BaseMultivariateVectorMultiStartOptimizer<FUNC extends MultivariateVectorFunction> implements BaseMultivariateVectorOptimizer<FUNC> {
    private final BaseMultivariateVectorOptimizer<FUNC> optimizer;
    private int maxEvaluations;
    private int totalEvaluations;
    private int starts;
    private RandomVectorGenerator generator;
    private PointVectorValuePair[] optima;

    protected BaseMultivariateVectorMultiStartOptimizer(BaseMultivariateVectorOptimizer<FUNC> optimizer, int starts, RandomVectorGenerator generator) {
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

    public PointVectorValuePair[] getOptima() {
        if (this.optima == null) {
            throw new MathIllegalStateException(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, new Object[0]);
        }
        return (PointVectorValuePair[]) this.optima.clone();
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
    public ConvergenceChecker<PointVectorValuePair> getConvergenceChecker() {
        return this.optimizer.getConvergenceChecker();
    }

    @Override // org.apache.commons.math3.optimization.BaseMultivariateVectorOptimizer
    public PointVectorValuePair optimize(int maxEval, FUNC f2, double[] target, double[] weights, double[] startPoint) {
        this.maxEvaluations = maxEval;
        RuntimeException lastException = null;
        this.optima = new PointVectorValuePair[this.starts];
        this.totalEvaluations = 0;
        int i2 = 0;
        while (i2 < this.starts) {
            try {
                this.optima[i2] = this.optimizer.optimize(maxEval - this.totalEvaluations, f2, target, weights, i2 == 0 ? startPoint : this.generator.nextVector());
            } catch (ConvergenceException e2) {
                this.optima[i2] = null;
            } catch (RuntimeException mue) {
                lastException = mue;
                this.optima[i2] = null;
            }
            this.totalEvaluations += this.optimizer.getEvaluations();
            i2++;
        }
        sortPairs(target, weights);
        if (this.optima[0] == null) {
            throw lastException;
        }
        return this.optima[0];
    }

    private void sortPairs(final double[] target, final double[] weights) {
        Arrays.sort(this.optima, new Comparator<PointVectorValuePair>() { // from class: org.apache.commons.math3.optimization.BaseMultivariateVectorMultiStartOptimizer.1
            @Override // java.util.Comparator
            public int compare(PointVectorValuePair o1, PointVectorValuePair o2) {
                if (o1 == null) {
                    return o2 == null ? 0 : 1;
                }
                if (o2 == null) {
                    return -1;
                }
                return Double.compare(weightedResidual(o1), weightedResidual(o2));
            }

            private double weightedResidual(PointVectorValuePair pv) {
                double[] value = pv.getValueRef();
                double sum = 0.0d;
                for (int i2 = 0; i2 < value.length; i2++) {
                    double ri = value[i2] - target[i2];
                    sum += weights[i2] * ri * ri;
                }
                return sum;
            }
        });
    }
}
