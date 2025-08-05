package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/MidPointIntegrator.class */
public class MidPointIntegrator extends BaseAbstractUnivariateIntegrator {
    public static final int MIDPOINT_MAX_ITERATIONS_COUNT = 64;

    public MidPointIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), 64, false);
        }
    }

    public MidPointIntegrator(int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 64) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), 64, false);
        }
    }

    public MidPointIntegrator() {
        super(3, 64);
    }

    private double stage(int n2, double previousStageResult, double min, double diffMaxMin) throws TooManyEvaluationsException {
        long np = 1 << (n2 - 1);
        double sum = 0.0d;
        double spacing = diffMaxMin / np;
        double x2 = min + (0.5d * spacing);
        long j2 = 0;
        while (true) {
            long i2 = j2;
            if (i2 < np) {
                sum += computeObjectiveValue(x2);
                x2 += spacing;
                j2 = i2 + 1;
            } else {
                return 0.5d * (previousStageResult + (sum * spacing));
            }
        }
    }

    @Override // org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator
    protected double doIntegrate() throws MaxCountExceededException, MathIllegalArgumentException {
        double t2;
        double min = getMin();
        double diff = getMax() - min;
        double midPoint = min + (0.5d * diff);
        double dComputeObjectiveValue = diff * computeObjectiveValue(midPoint);
        while (true) {
            double oldt = dComputeObjectiveValue;
            incrementCount();
            int i2 = getIterations();
            t2 = stage(i2, oldt, min, diff);
            if (i2 >= getMinimalIterationCount()) {
                double delta = FastMath.abs(t2 - oldt);
                double rLimit = getRelativeAccuracy() * (FastMath.abs(oldt) + FastMath.abs(t2)) * 0.5d;
                if (delta <= rLimit || delta <= getAbsoluteAccuracy()) {
                    break;
                }
            }
            dComputeObjectiveValue = t2;
        }
        return t2;
    }
}
