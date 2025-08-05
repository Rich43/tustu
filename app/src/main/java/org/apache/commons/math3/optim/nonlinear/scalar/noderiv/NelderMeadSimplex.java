package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/NelderMeadSimplex.class */
public class NelderMeadSimplex extends AbstractSimplex {
    private static final double DEFAULT_RHO = 1.0d;
    private static final double DEFAULT_KHI = 2.0d;
    private static final double DEFAULT_GAMMA = 0.5d;
    private static final double DEFAULT_SIGMA = 0.5d;
    private final double rho;
    private final double khi;
    private final double gamma;
    private final double sigma;

    public NelderMeadSimplex(int n2) {
        this(n2, 1.0d);
    }

    public NelderMeadSimplex(int n2, double sideLength) {
        this(n2, sideLength, 1.0d, 2.0d, 0.5d, 0.5d);
    }

    public NelderMeadSimplex(int n2, double sideLength, double rho, double khi, double gamma, double sigma) {
        super(n2, sideLength);
        this.rho = rho;
        this.khi = khi;
        this.gamma = gamma;
        this.sigma = sigma;
    }

    public NelderMeadSimplex(int n2, double rho, double khi, double gamma, double sigma) {
        this(n2, 1.0d, rho, khi, gamma, sigma);
    }

    public NelderMeadSimplex(double[] steps) {
        this(steps, 1.0d, 2.0d, 0.5d, 0.5d);
    }

    public NelderMeadSimplex(double[] steps, double rho, double khi, double gamma, double sigma) {
        super(steps);
        this.rho = rho;
        this.khi = khi;
        this.gamma = gamma;
        this.sigma = sigma;
    }

    public NelderMeadSimplex(double[][] referenceSimplex) {
        this(referenceSimplex, 1.0d, 2.0d, 0.5d, 0.5d);
    }

    public NelderMeadSimplex(double[][] referenceSimplex, double rho, double khi, double gamma, double sigma) {
        super(referenceSimplex);
        this.rho = rho;
        this.khi = khi;
        this.gamma = gamma;
        this.sigma = sigma;
    }

    @Override // org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex
    public void iterate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator) {
        int n2 = getDimension();
        PointValuePair best = getPoint(0);
        PointValuePair secondBest = getPoint(n2 - 1);
        PointValuePair worst = getPoint(n2);
        double[] xWorst = worst.getPointRef();
        double[] centroid = new double[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            double[] x2 = getPoint(i2).getPointRef();
            for (int j2 = 0; j2 < n2; j2++) {
                int i3 = j2;
                centroid[i3] = centroid[i3] + x2[j2];
            }
        }
        double scaling = 1.0d / n2;
        for (int j3 = 0; j3 < n2; j3++) {
            int i4 = j3;
            centroid[i4] = centroid[i4] * scaling;
        }
        double[] xR = new double[n2];
        for (int j4 = 0; j4 < n2; j4++) {
            xR[j4] = centroid[j4] + (this.rho * (centroid[j4] - xWorst[j4]));
        }
        PointValuePair reflected = new PointValuePair(xR, evaluationFunction.value(xR), false);
        if (comparator.compare(best, reflected) <= 0 && comparator.compare(reflected, secondBest) < 0) {
            replaceWorstPoint(reflected, comparator);
            return;
        }
        if (comparator.compare(reflected, best) < 0) {
            double[] xE = new double[n2];
            for (int j5 = 0; j5 < n2; j5++) {
                xE[j5] = centroid[j5] + (this.khi * (xR[j5] - centroid[j5]));
            }
            PointValuePair expanded = new PointValuePair(xE, evaluationFunction.value(xE), false);
            if (comparator.compare(expanded, reflected) < 0) {
                replaceWorstPoint(expanded, comparator);
                return;
            } else {
                replaceWorstPoint(reflected, comparator);
                return;
            }
        }
        if (comparator.compare(reflected, worst) < 0) {
            double[] xC = new double[n2];
            for (int j6 = 0; j6 < n2; j6++) {
                xC[j6] = centroid[j6] + (this.gamma * (xR[j6] - centroid[j6]));
            }
            PointValuePair outContracted = new PointValuePair(xC, evaluationFunction.value(xC), false);
            if (comparator.compare(outContracted, reflected) <= 0) {
                replaceWorstPoint(outContracted, comparator);
                return;
            }
        } else {
            double[] xC2 = new double[n2];
            for (int j7 = 0; j7 < n2; j7++) {
                xC2[j7] = centroid[j7] - (this.gamma * (centroid[j7] - xWorst[j7]));
            }
            PointValuePair inContracted = new PointValuePair(xC2, evaluationFunction.value(xC2), false);
            if (comparator.compare(inContracted, worst) < 0) {
                replaceWorstPoint(inContracted, comparator);
                return;
            }
        }
        double[] xSmallest = getPoint(0).getPointRef();
        for (int i5 = 1; i5 <= n2; i5++) {
            double[] x3 = getPoint(i5).getPoint();
            for (int j8 = 0; j8 < n2; j8++) {
                x3[j8] = xSmallest[j8] + (this.sigma * (x3[j8] - xSmallest[j8]));
            }
            setPoint(i5, new PointValuePair(x3, Double.NaN, false));
        }
        evaluate(evaluationFunction, comparator);
    }
}
