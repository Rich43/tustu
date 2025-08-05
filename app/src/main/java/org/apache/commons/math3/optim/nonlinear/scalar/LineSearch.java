package org.apache.commons.math3.optim.nonlinear.scalar;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SearchInterval;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/LineSearch.class */
public class LineSearch {
    private static final double REL_TOL_UNUSED = 1.0E-15d;
    private static final double ABS_TOL_UNUSED = Double.MIN_VALUE;
    private final UnivariateOptimizer lineOptimizer;
    private final BracketFinder bracket = new BracketFinder();
    private final double initialBracketingRange;
    private final MultivariateOptimizer mainOptimizer;

    public LineSearch(MultivariateOptimizer optimizer, double relativeTolerance, double absoluteTolerance, double initialBracketingRange) {
        this.mainOptimizer = optimizer;
        this.lineOptimizer = new BrentOptimizer(1.0E-15d, Double.MIN_VALUE, new SimpleUnivariateValueChecker(relativeTolerance, absoluteTolerance));
        this.initialBracketingRange = initialBracketingRange;
    }

    public UnivariatePointValuePair search(final double[] startPoint, final double[] direction) {
        final int n2 = startPoint.length;
        UnivariateFunction f2 = new UnivariateFunction() { // from class: org.apache.commons.math3.optim.nonlinear.scalar.LineSearch.1
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double alpha) {
                double[] x2 = new double[n2];
                for (int i2 = 0; i2 < n2; i2++) {
                    x2[i2] = startPoint[i2] + (alpha * direction[i2]);
                }
                double obj = LineSearch.this.mainOptimizer.computeObjectiveValue(x2);
                return obj;
            }
        };
        GoalType goal = this.mainOptimizer.getGoalType();
        this.bracket.search(f2, goal, 0.0d, this.initialBracketingRange);
        return this.lineOptimizer.optimize(new MaxEval(Integer.MAX_VALUE), new UnivariateObjectiveFunction(f2), goal, new SearchInterval(this.bracket.getLo(), this.bracket.getHi(), this.bracket.getMid()));
    }
}
