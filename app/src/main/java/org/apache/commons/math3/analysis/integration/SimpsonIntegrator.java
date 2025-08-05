package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/SimpsonIntegrator.class */
public class SimpsonIntegrator extends BaseAbstractUnivariateIntegrator {
    public static final int SIMPSON_MAX_ITERATIONS_COUNT = 64;

    public SimpsonIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), 64, false);
        }
    }

    public SimpsonIntegrator(int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), 64, false);
        }
    }

    public SimpsonIntegrator() {
        super(3, 64);
    }

    @Override // org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator
    protected double doIntegrate() throws MaxCountExceededException {
        double s2;
        TrapezoidIntegrator qtrap = new TrapezoidIntegrator();
        if (getMinimalIterationCount() == 1) {
            return ((4.0d * qtrap.stage(this, 1)) - qtrap.stage(this, 0)) / 3.0d;
        }
        double olds = 0.0d;
        double dStage = qtrap.stage(this, 0);
        while (true) {
            double oldt = dStage;
            double t2 = qtrap.stage(this, getIterations());
            incrementCount();
            s2 = ((4.0d * t2) - oldt) / 3.0d;
            if (getIterations() >= getMinimalIterationCount()) {
                double delta = FastMath.abs(s2 - olds);
                double rLimit = getRelativeAccuracy() * (FastMath.abs(olds) + FastMath.abs(s2)) * 0.5d;
                if (delta <= rLimit || delta <= getAbsoluteAccuracy()) {
                    break;
                }
            }
            olds = s2;
            dStage = t2;
        }
        return s2;
    }
}
