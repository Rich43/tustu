package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/LegendreGaussIntegrator.class */
public class LegendreGaussIntegrator extends BaseAbstractUnivariateIntegrator {
    private static final double[] ABSCISSAS_2 = {(-1.0d) / FastMath.sqrt(3.0d), 1.0d / FastMath.sqrt(3.0d)};
    private static final double[] WEIGHTS_2 = {1.0d, 1.0d};
    private static final double[] ABSCISSAS_3 = {-FastMath.sqrt(0.6d), 0.0d, FastMath.sqrt(0.6d)};
    private static final double[] WEIGHTS_3 = {0.5555555555555556d, 0.8888888888888888d, 0.5555555555555556d};
    private static final double[] ABSCISSAS_4 = {-FastMath.sqrt((15.0d + (2.0d * FastMath.sqrt(30.0d))) / 35.0d), -FastMath.sqrt((15.0d - (2.0d * FastMath.sqrt(30.0d))) / 35.0d), FastMath.sqrt((15.0d - (2.0d * FastMath.sqrt(30.0d))) / 35.0d), FastMath.sqrt((15.0d + (2.0d * FastMath.sqrt(30.0d))) / 35.0d)};
    private static final double[] WEIGHTS_4 = {(90.0d - (5.0d * FastMath.sqrt(30.0d))) / 180.0d, (90.0d + (5.0d * FastMath.sqrt(30.0d))) / 180.0d, (90.0d + (5.0d * FastMath.sqrt(30.0d))) / 180.0d, (90.0d - (5.0d * FastMath.sqrt(30.0d))) / 180.0d};
    private static final double[] ABSCISSAS_5 = {-FastMath.sqrt((35.0d + (2.0d * FastMath.sqrt(70.0d))) / 63.0d), -FastMath.sqrt((35.0d - (2.0d * FastMath.sqrt(70.0d))) / 63.0d), 0.0d, FastMath.sqrt((35.0d - (2.0d * FastMath.sqrt(70.0d))) / 63.0d), FastMath.sqrt((35.0d + (2.0d * FastMath.sqrt(70.0d))) / 63.0d)};
    private static final double[] WEIGHTS_5 = {(322.0d - (13.0d * FastMath.sqrt(70.0d))) / 900.0d, (322.0d + (13.0d * FastMath.sqrt(70.0d))) / 900.0d, 0.5688888888888889d, (322.0d + (13.0d * FastMath.sqrt(70.0d))) / 900.0d, (322.0d - (13.0d * FastMath.sqrt(70.0d))) / 900.0d};
    private final double[] abscissas;
    private final double[] weights;

    public LegendreGaussIntegrator(int n2, double relativeAccuracy, double absoluteAccuracy, int minimalIterationCount, int maximalIterationCount) throws MathIllegalArgumentException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        switch (n2) {
            case 2:
                this.abscissas = ABSCISSAS_2;
                this.weights = WEIGHTS_2;
                return;
            case 3:
                this.abscissas = ABSCISSAS_3;
                this.weights = WEIGHTS_3;
                return;
            case 4:
                this.abscissas = ABSCISSAS_4;
                this.weights = WEIGHTS_4;
                return;
            case 5:
                this.abscissas = ABSCISSAS_5;
                this.weights = WEIGHTS_5;
                return;
            default:
                throw new MathIllegalArgumentException(LocalizedFormats.N_POINTS_GAUSS_LEGENDRE_INTEGRATOR_NOT_SUPPORTED, Integer.valueOf(n2), 2, 5);
        }
    }

    public LegendreGaussIntegrator(int n2, double relativeAccuracy, double absoluteAccuracy) throws MathIllegalArgumentException {
        this(n2, relativeAccuracy, absoluteAccuracy, 3, Integer.MAX_VALUE);
    }

    public LegendreGaussIntegrator(int n2, int minimalIterationCount, int maximalIterationCount) throws MathIllegalArgumentException {
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
            double ratio = FastMath.min(4.0d, FastMath.pow(delta / limit, 0.5d / this.abscissas.length));
            n2 = FastMath.max((int) (ratio * n2), n2 + 1);
            oldt = t2;
            incrementCount();
        }
    }

    private double stage(int n2) throws TooManyEvaluationsException {
        double step = (getMax() - getMin()) / n2;
        double halfStep = step / 2.0d;
        double midPoint = getMin() + halfStep;
        double sum = 0.0d;
        for (int i2 = 0; i2 < n2; i2++) {
            for (int j2 = 0; j2 < this.abscissas.length; j2++) {
                sum += this.weights[j2] * computeObjectiveValue(midPoint + (halfStep * this.abscissas[j2]));
            }
            midPoint += step;
        }
        return halfStep * sum;
    }
}
