package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.IntegerSequence;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/univariate/BracketFinder.class */
public class BracketFinder {
    private static final double EPS_MIN = 1.0E-21d;
    private static final double GOLD = 1.618034d;
    private final double growLimit;
    private IntegerSequence.Incrementor evaluations;
    private double lo;
    private double hi;
    private double mid;
    private double fLo;
    private double fHi;
    private double fMid;

    public BracketFinder() {
        this(100.0d, 500);
    }

    public BracketFinder(double growLimit, int maxEvaluations) {
        if (growLimit <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(growLimit));
        }
        if (maxEvaluations <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxEvaluations));
        }
        this.growLimit = growLimit;
        this.evaluations = IntegerSequence.Incrementor.create().withMaximalCount(maxEvaluations);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0119, code lost:
    
        r12 = r14;
        r14 = r33;
        r17 = r19;
        r19 = r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01b9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void search(org.apache.commons.math3.analysis.UnivariateFunction r10, org.apache.commons.math3.optim.nonlinear.scalar.GoalType r11, double r12, double r14) {
        /*
            Method dump skipped, instructions count: 620
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optim.univariate.BracketFinder.search(org.apache.commons.math3.analysis.UnivariateFunction, org.apache.commons.math3.optim.nonlinear.scalar.GoalType, double, double):void");
    }

    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    public double getLo() {
        return this.lo;
    }

    public double getFLo() {
        return this.fLo;
    }

    public double getHi() {
        return this.hi;
    }

    public double getFHi() {
        return this.fHi;
    }

    public double getMid() {
        return this.mid;
    }

    public double getFMid() {
        return this.fMid;
    }

    private double eval(UnivariateFunction f2, double x2) {
        try {
            this.evaluations.increment();
            return f2.value(x2);
        } catch (MaxCountExceededException e2) {
            throw new TooManyEvaluationsException(e2.getMax());
        }
    }
}
