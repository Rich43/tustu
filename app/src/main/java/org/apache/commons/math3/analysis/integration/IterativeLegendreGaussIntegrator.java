package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.gauss.GaussIntegrator;
import org.apache.commons.math3.analysis.integration.gauss.GaussIntegratorFactory;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/IterativeLegendreGaussIntegrator.class */
public class IterativeLegendreGaussIntegrator extends BaseAbstractUnivariateIntegrator {
    private static final GaussIntegratorFactory FACTORY = new GaussIntegratorFactory();
    private final int numberOfPoints;

    public IterativeLegendreGaussIntegrator(int n2, double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        if (n2 <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(n2));
        }
        this.numberOfPoints = n2;
    }

    public IterativeLegendreGaussIntegrator(int n2, double relativeAccuracy, double absoluteAccuracy) throws NotStrictlyPositiveException {
        this(n2, relativeAccuracy, absoluteAccuracy, 3, Integer.MAX_VALUE);
    }

    public IterativeLegendreGaussIntegrator(int n2, int minimalIterationCount, int maximalIterationCount) throws NumberIsTooSmallException {
        this(n2, 1.0E-6d, 1.0E-15d, minimalIterationCount, maximalIterationCount);
    }

    @Override // org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator
    protected double doIntegrate() throws MaxCountExceededException, MathIllegalArgumentException {
        double oldt = stage(1);
        int n2 = 2;
        while (true) {
            double t2 = stage(n2);
            double delta = FastMath.abs(t2 - oldt);
            double limit = FastMath.max(getAbsoluteAccuracy(), getRelativeAccuracy() * (FastMath.abs(oldt) + FastMath.abs(t2)) * 0.5d);
            if (getIterations() + 1 >= getMinimalIterationCount() && delta <= limit) {
                return t2;
            }
            double ratio = FastMath.min(4.0d, FastMath.pow(delta / limit, 0.5d / this.numberOfPoints));
            n2 = FastMath.max((int) (ratio * n2), n2 + 1);
            oldt = t2;
            incrementCount();
        }
    }

    private double stage(int n2) throws NotStrictlyPositiveException, TooManyEvaluationsException {
        UnivariateFunction f2 = new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator.1
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) throws TooManyEvaluationsException, MathIllegalArgumentException {
                return IterativeLegendreGaussIntegrator.this.computeObjectiveValue(x2);
            }
        };
        double min = getMin();
        double max = getMax();
        double step = (max - min) / n2;
        double sum = 0.0d;
        for (int i2 = 0; i2 < n2; i2++) {
            double a2 = min + (i2 * step);
            double b2 = a2 + step;
            GaussIntegrator g2 = FACTORY.legendreHighPrecision(this.numberOfPoints, a2, b2);
            sum += g2.integrate(f2);
        }
        return sum;
    }
}
