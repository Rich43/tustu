package org.apache.commons.math3.optimization.direct;

import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/SimplexOptimizer.class */
public class SimplexOptimizer extends BaseAbstractMultivariateOptimizer<MultivariateFunction> implements MultivariateOptimizer {
    private AbstractSimplex simplex;

    @Deprecated
    public SimplexOptimizer() {
        this(new SimpleValueChecker());
    }

    public SimplexOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    public SimplexOptimizer(double rel, double abs) {
        this(new SimpleValueChecker(rel, abs));
    }

    @Deprecated
    public void setSimplex(AbstractSimplex simplex) {
        parseOptimizationData(simplex);
    }

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    protected PointValuePair optimizeInternal(int maxEval, MultivariateFunction f2, GoalType goalType, OptimizationData... optData) {
        parseOptimizationData(optData);
        return super.optimizeInternal(maxEval, (int) f2, goalType, optData);
    }

    private void parseOptimizationData(OptimizationData... optData) {
        for (OptimizationData data : optData) {
            if (data instanceof AbstractSimplex) {
                this.simplex = (AbstractSimplex) data;
            }
        }
    }

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    protected PointValuePair doOptimize() {
        if (this.simplex == null) {
            throw new NullArgumentException();
        }
        MultivariateFunction evalFunc = new MultivariateFunction() { // from class: org.apache.commons.math3.optimization.direct.SimplexOptimizer.1
            @Override // org.apache.commons.math3.analysis.MultivariateFunction
            public double value(double[] point) {
                return SimplexOptimizer.this.computeObjectiveValue(point);
            }
        };
        final boolean isMinim = getGoalType() == GoalType.MINIMIZE;
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() { // from class: org.apache.commons.math3.optimization.direct.SimplexOptimizer.2
            @Override // java.util.Comparator
            public int compare(PointValuePair o1, PointValuePair o2) {
                double v1 = o1.getValue().doubleValue();
                double v2 = o2.getValue().doubleValue();
                return isMinim ? Double.compare(v1, v2) : Double.compare(v2, v1);
            }
        };
        this.simplex.build(getStartPoint());
        this.simplex.evaluate(evalFunc, comparator);
        PointValuePair[] previous = null;
        int iteration = 0;
        ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        while (true) {
            if (iteration > 0) {
                boolean converged = true;
                for (int i2 = 0; i2 < this.simplex.getSize(); i2++) {
                    PointValuePair prev = previous[i2];
                    converged = converged && checker.converged(iteration, prev, this.simplex.getPoint(i2));
                }
                if (converged) {
                    return this.simplex.getPoint(0);
                }
            }
            previous = this.simplex.getPoints();
            this.simplex.iterate(evalFunc, comparator);
            iteration++;
        }
    }
}
