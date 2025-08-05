package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/TrapezoidIntegrator.class */
public class TrapezoidIntegrator extends BaseAbstractUnivariateIntegrator {
    public static final int TRAPEZOID_MAX_ITERATIONS_COUNT = 64;

    /* renamed from: s, reason: collision with root package name */
    private double f12965s;

    public TrapezoidIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), 64, false);
        }
    }

    public TrapezoidIntegrator(int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), 64, false);
        }
    }

    public TrapezoidIntegrator() {
        super(3, 64);
    }

    double stage(BaseAbstractUnivariateIntegrator baseIntegrator, int n2) throws TooManyEvaluationsException {
        if (n2 == 0) {
            double max = baseIntegrator.getMax();
            double min = baseIntegrator.getMin();
            this.f12965s = 0.5d * (max - min) * (baseIntegrator.computeObjectiveValue(min) + baseIntegrator.computeObjectiveValue(max));
            return this.f12965s;
        }
        long np = 1 << (n2 - 1);
        double sum = 0.0d;
        double max2 = baseIntegrator.getMax();
        double min2 = baseIntegrator.getMin();
        double spacing = (max2 - min2) / np;
        double x2 = min2 + (0.5d * spacing);
        long j2 = 0;
        while (true) {
            long i2 = j2;
            if (i2 < np) {
                sum += baseIntegrator.computeObjectiveValue(x2);
                x2 += spacing;
                j2 = i2 + 1;
            } else {
                this.f12965s = 0.5d * (this.f12965s + (sum * spacing));
                return this.f12965s;
            }
        }
    }

    @Override // org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator
    protected double doIntegrate() throws MaxCountExceededException, MathIllegalArgumentException {
        double t2;
        double oldt = stage(this, 0);
        incrementCount();
        while (true) {
            int i2 = getIterations();
            t2 = stage(this, i2);
            if (i2 >= getMinimalIterationCount()) {
                double delta = FastMath.abs(t2 - oldt);
                double rLimit = getRelativeAccuracy() * (FastMath.abs(oldt) + FastMath.abs(t2)) * 0.5d;
                if (delta <= rLimit || delta <= getAbsoluteAccuracy()) {
                    break;
                }
            }
            oldt = t2;
            incrementCount();
        }
        return t2;
    }
}
