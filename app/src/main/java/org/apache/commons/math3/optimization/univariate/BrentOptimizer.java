package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/univariate/BrentOptimizer.class */
public class BrentOptimizer extends BaseAbstractUnivariateOptimizer {
    private static final double GOLDEN_SECTION = 0.5d * (3.0d - FastMath.sqrt(5.0d));
    private static final double MIN_RELATIVE_TOLERANCE = 2.0d * FastMath.ulp(1.0d);
    private final double relativeThreshold;
    private final double absoluteThreshold;

    public BrentOptimizer(double rel, double abs, ConvergenceChecker<UnivariatePointValuePair> checker) {
        super(checker);
        if (rel < MIN_RELATIVE_TOLERANCE) {
            throw new NumberIsTooSmallException(Double.valueOf(rel), Double.valueOf(MIN_RELATIVE_TOLERANCE), true);
        }
        if (abs <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(abs));
        }
        this.relativeThreshold = rel;
        this.absoluteThreshold = abs;
    }

    public BrentOptimizer(double rel, double abs) {
        this(rel, abs, null);
    }

    @Override // org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer
    protected UnivariatePointValuePair doOptimize() {
        double a2;
        double b2;
        double u2;
        boolean isMinim = getGoalType() == GoalType.MINIMIZE;
        double lo = getMin();
        double mid = getStartValue();
        double hi = getMax();
        ConvergenceChecker<UnivariatePointValuePair> checker = getConvergenceChecker();
        if (lo < hi) {
            a2 = lo;
            b2 = hi;
        } else {
            a2 = hi;
            b2 = lo;
        }
        double x2 = mid;
        double v2 = x2;
        double w2 = x2;
        double d2 = 0.0d;
        double e2 = 0.0d;
        double fx = computeObjectiveValue(x2);
        if (!isMinim) {
            fx = -fx;
        }
        double fv = fx;
        double fw = fx;
        UnivariatePointValuePair previous = null;
        UnivariatePointValuePair current = new UnivariatePointValuePair(x2, isMinim ? fx : -fx);
        UnivariatePointValuePair best = current;
        int iter = 0;
        while (true) {
            double m2 = 0.5d * (a2 + b2);
            double tol1 = (this.relativeThreshold * FastMath.abs(x2)) + this.absoluteThreshold;
            double tol2 = 2.0d * tol1;
            boolean stop = FastMath.abs(x2 - m2) <= tol2 - (0.5d * (b2 - a2));
            if (!stop) {
                if (FastMath.abs(e2) > tol1) {
                    double r2 = (x2 - w2) * (fx - fv);
                    double q2 = (x2 - v2) * (fx - fw);
                    double p2 = ((x2 - v2) * q2) - ((x2 - w2) * r2);
                    double q3 = 2.0d * (q2 - r2);
                    if (q3 > 0.0d) {
                        p2 = -p2;
                    } else {
                        q3 = -q3;
                    }
                    double r3 = e2;
                    e2 = d2;
                    if (p2 > q3 * (a2 - x2) && p2 < q3 * (b2 - x2) && FastMath.abs(p2) < FastMath.abs(0.5d * q3 * r3)) {
                        d2 = p2 / q3;
                        double u3 = x2 + d2;
                        if (u3 - a2 < tol2 || b2 - u3 < tol2) {
                            if (x2 <= m2) {
                                d2 = tol1;
                            } else {
                                d2 = -tol1;
                            }
                        }
                    } else {
                        if (x2 < m2) {
                            e2 = b2 - x2;
                        } else {
                            e2 = a2 - x2;
                        }
                        d2 = GOLDEN_SECTION * e2;
                    }
                } else {
                    if (x2 < m2) {
                        e2 = b2 - x2;
                    } else {
                        e2 = a2 - x2;
                    }
                    d2 = GOLDEN_SECTION * e2;
                }
                if (FastMath.abs(d2) >= tol1) {
                    u2 = x2 + d2;
                } else if (d2 >= 0.0d) {
                    u2 = x2 + tol1;
                } else {
                    u2 = x2 - tol1;
                }
                double fu = computeObjectiveValue(u2);
                if (!isMinim) {
                    fu = -fu;
                }
                previous = current;
                current = new UnivariatePointValuePair(u2, isMinim ? fu : -fu);
                best = best(best, best(previous, current, isMinim), isMinim);
                if (checker != null && checker.converged(iter, previous, current)) {
                    return best;
                }
                if (fu <= fx) {
                    if (u2 < x2) {
                        b2 = x2;
                    } else {
                        a2 = x2;
                    }
                    v2 = w2;
                    fv = fw;
                    w2 = x2;
                    fw = fx;
                    x2 = u2;
                    fx = fu;
                } else {
                    if (u2 < x2) {
                        a2 = u2;
                    } else {
                        b2 = u2;
                    }
                    if (fu <= fw || Precision.equals(w2, x2)) {
                        v2 = w2;
                        fv = fw;
                        w2 = u2;
                        fw = fu;
                    } else if (fu <= fv || Precision.equals(v2, x2) || Precision.equals(v2, w2)) {
                        v2 = u2;
                        fv = fu;
                    }
                }
                iter++;
            } else {
                return best(best, best(previous, current, isMinim), isMinim);
            }
        }
    }

    private UnivariatePointValuePair best(UnivariatePointValuePair a2, UnivariatePointValuePair b2, boolean isMinim) {
        if (a2 == null) {
            return b2;
        }
        if (b2 == null) {
            return a2;
        }
        return isMinim ? a2.getValue() <= b2.getValue() ? a2 : b2 : a2.getValue() >= b2.getValue() ? a2 : b2;
    }
}
