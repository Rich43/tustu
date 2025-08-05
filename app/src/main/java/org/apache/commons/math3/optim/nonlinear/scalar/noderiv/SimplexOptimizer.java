package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/SimplexOptimizer.class */
public class SimplexOptimizer extends MultivariateOptimizer {
    private AbstractSimplex simplex;

    public SimplexOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    public SimplexOptimizer(double rel, double abs) {
        this(new SimpleValueChecker(rel, abs));
    }

    @Override // org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    public PointValuePair optimize(OptimizationData... optData) {
        return super.optimize(optData);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.optim.BaseOptimizer
    public PointValuePair doOptimize() {
        checkParameters();
        MultivariateFunction evalFunc = new MultivariateFunction() { // from class: org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer.1
            @Override // org.apache.commons.math3.analysis.MultivariateFunction
            public double value(double[] point) {
                return SimplexOptimizer.this.computeObjectiveValue(point);
            }
        };
        final boolean isMinim = getGoalType() == GoalType.MINIMIZE;
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() { // from class: org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer.2
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
        ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        while (true) {
            if (getIterations() > 0) {
                boolean converged = true;
                for (int i2 = 0; i2 < this.simplex.getSize(); i2++) {
                    PointValuePair prev = previous[i2];
                    converged = converged && checker.converged(0, prev, this.simplex.getPoint(i2));
                }
                if (converged) {
                    return this.simplex.getPoint(0);
                }
            }
            previous = this.simplex.getPoints();
            this.simplex.iterate(evalFunc, comparator);
            incrementIterationCount();
        }
    }

    @Override // org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    protected void parseOptimizationData(OptimizationData... optData) {
        super.parseOptimizationData(optData);
        for (OptimizationData data : optData) {
            if (data instanceof AbstractSimplex) {
                this.simplex = (AbstractSimplex) data;
                return;
            }
        }
    }

    private void checkParameters() {
        if (this.simplex == null) {
            throw new NullArgumentException();
        }
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new MathUnsupportedOperationException(LocalizedFormats.CONSTRAINT, new Object[0]);
        }
    }
}
