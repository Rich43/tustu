package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/PowellOptimizer.class */
public class PowellOptimizer extends BaseAbstractMultivariateOptimizer<MultivariateFunction> implements MultivariateOptimizer {
    private static final double MIN_RELATIVE_TOLERANCE = 2.0d * FastMath.ulp(1.0d);
    private final double relativeThreshold;
    private final double absoluteThreshold;
    private final LineSearch line;

    public PowellOptimizer(double rel, double abs, ConvergenceChecker<PointValuePair> checker) {
        this(rel, abs, FastMath.sqrt(rel), FastMath.sqrt(abs), checker);
    }

    public PowellOptimizer(double rel, double abs, double lineRel, double lineAbs, ConvergenceChecker<PointValuePair> checker) {
        super(checker);
        if (rel < MIN_RELATIVE_TOLERANCE) {
            throw new NumberIsTooSmallException(Double.valueOf(rel), Double.valueOf(MIN_RELATIVE_TOLERANCE), true);
        }
        if (abs <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(abs));
        }
        this.relativeThreshold = rel;
        this.absoluteThreshold = abs;
        this.line = new LineSearch(lineRel, lineAbs);
    }

    public PowellOptimizer(double rel, double abs) {
        this(rel, abs, null);
    }

    public PowellOptimizer(double rel, double abs, double lineRel, double lineAbs) {
        this(rel, abs, lineRel, lineAbs, null);
    }

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    protected PointValuePair doOptimize() {
        double fX;
        PointValuePair previous;
        PointValuePair current;
        GoalType goal = getGoalType();
        double[] guess = getStartPoint();
        int n2 = guess.length;
        double[][] direc = new double[n2][n2];
        for (int i2 = 0; i2 < n2; i2++) {
            direc[i2][i2] = 1.0d;
        }
        ConvergenceChecker<PointValuePair> checker = getConvergenceChecker();
        double[] x2 = guess;
        double fVal = computeObjectiveValue(x2);
        double[] x1 = (double[]) x2.clone();
        int iter = 0;
        while (true) {
            iter++;
            fX = fVal;
            double delta = 0.0d;
            int bigInd = 0;
            for (int i3 = 0; i3 < n2; i3++) {
                double[] d2 = MathArrays.copyOf(direc[i3]);
                double fX2 = fVal;
                UnivariatePointValuePair optimum = this.line.search(x2, d2);
                fVal = optimum.getValue();
                double alphaMin = optimum.getPoint();
                x2 = newPointAndDirection(x2, d2, alphaMin)[0];
                if (fX2 - fVal > delta) {
                    delta = fX2 - fVal;
                    bigInd = i3;
                }
            }
            boolean stop = 2.0d * (fX - fVal) <= (this.relativeThreshold * (FastMath.abs(fX) + FastMath.abs(fVal))) + this.absoluteThreshold;
            previous = new PointValuePair(x1, fX);
            current = new PointValuePair(x2, fVal);
            if (!stop && checker != null) {
                stop = checker.converged(iter, previous, current);
            }
            if (stop) {
                break;
            }
            double[] d3 = new double[n2];
            double[] x22 = new double[n2];
            for (int i4 = 0; i4 < n2; i4++) {
                d3[i4] = x2[i4] - x1[i4];
                x22[i4] = (2.0d * x2[i4]) - x1[i4];
            }
            x1 = (double[]) x2.clone();
            double fX22 = computeObjectiveValue(x22);
            if (fX > fX22) {
                double t2 = 2.0d * ((fX + fX22) - (2.0d * fVal));
                double temp = (fX - fVal) - delta;
                double t3 = t2 * temp * temp;
                double temp2 = fX - fX22;
                if (t3 - ((delta * temp2) * temp2) < 0.0d) {
                    UnivariatePointValuePair optimum2 = this.line.search(x2, d3);
                    fVal = optimum2.getValue();
                    double alphaMin2 = optimum2.getPoint();
                    double[][] result = newPointAndDirection(x2, d3, alphaMin2);
                    x2 = result[0];
                    int lastInd = n2 - 1;
                    direc[bigInd] = direc[lastInd];
                    direc[lastInd] = result[1];
                }
            }
        }
        return goal == GoalType.MINIMIZE ? fVal < fX ? current : previous : fVal > fX ? current : previous;
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [double[], double[][]] */
    private double[][] newPointAndDirection(double[] p2, double[] d2, double optimum) {
        int n2 = p2.length;
        double[] nP = new double[n2];
        double[] nD = new double[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            nD[i2] = d2[i2] * optimum;
            nP[i2] = p2[i2] + nD[i2];
        }
        return new double[]{nP, nD};
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/PowellOptimizer$LineSearch.class */
    private class LineSearch extends BrentOptimizer {
        private static final double REL_TOL_UNUSED = 1.0E-15d;
        private static final double ABS_TOL_UNUSED = Double.MIN_VALUE;
        private final BracketFinder bracket;

        LineSearch(double rel, double abs) {
            super(1.0E-15d, Double.MIN_VALUE, new SimpleUnivariateValueChecker(rel, abs));
            this.bracket = new BracketFinder();
        }

        public UnivariatePointValuePair search(final double[] p2, final double[] d2) {
            final int n2 = p2.length;
            UnivariateFunction f2 = new UnivariateFunction() { // from class: org.apache.commons.math3.optimization.direct.PowellOptimizer.LineSearch.1
                @Override // org.apache.commons.math3.analysis.UnivariateFunction
                public double value(double alpha) {
                    double[] x2 = new double[n2];
                    for (int i2 = 0; i2 < n2; i2++) {
                        x2[i2] = p2[i2] + (alpha * d2[i2]);
                    }
                    double obj = PowellOptimizer.this.computeObjectiveValue(x2);
                    return obj;
                }
            };
            GoalType goal = PowellOptimizer.this.getGoalType();
            this.bracket.search(f2, goal, 0.0d, 1.0d);
            return optimize(Integer.MAX_VALUE, f2, goal, this.bracket.getLo(), this.bracket.getHi(), this.bracket.getMid());
        }
    }
}
