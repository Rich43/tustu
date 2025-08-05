package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/RombergIntegrator.class */
public class RombergIntegrator extends BaseAbstractUnivariateIntegrator {
    public static final int ROMBERG_MAX_ITERATIONS_COUNT = 32;

    public RombergIntegrator(double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 32) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), 32, false);
        }
    }

    public RombergIntegrator(int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(minimalIterationCount, maximalIterationCount);
        if (maximalIterationCount > 32) {
            throw new NumberIsTooLargeException(Integer.valueOf(maximalIterationCount), 32, false);
        }
    }

    public RombergIntegrator() {
        super(3, 32);
    }

    @Override // org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator
    protected double doIntegrate() throws MaxCountExceededException {
        double s2;
        int m2 = getMaximalIterationCount() + 1;
        double[] previousRow = new double[m2];
        double[] currentRow = new double[m2];
        TrapezoidIntegrator qtrap = new TrapezoidIntegrator();
        currentRow[0] = qtrap.stage(this, 0);
        incrementCount();
        double d2 = currentRow[0];
        while (true) {
            double olds = d2;
            int i2 = getIterations();
            double[] tmpRow = previousRow;
            previousRow = currentRow;
            currentRow = tmpRow;
            currentRow[0] = qtrap.stage(this, i2);
            incrementCount();
            for (int j2 = 1; j2 <= i2; j2++) {
                double r2 = (1 << (2 * j2)) - 1;
                double tIJm1 = currentRow[j2 - 1];
                currentRow[j2] = tIJm1 + ((tIJm1 - previousRow[j2 - 1]) / r2);
            }
            s2 = currentRow[i2];
            if (i2 >= getMinimalIterationCount()) {
                double delta = FastMath.abs(s2 - olds);
                double rLimit = getRelativeAccuracy() * (FastMath.abs(olds) + FastMath.abs(s2)) * 0.5d;
                if (delta <= rLimit || delta <= getAbsoluteAccuracy()) {
                    break;
                }
            }
            d2 = s2;
        }
        return s2;
    }
}
