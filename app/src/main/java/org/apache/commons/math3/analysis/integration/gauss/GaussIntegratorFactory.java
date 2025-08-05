package org.apache.commons.math3.analysis.integration.gauss;

import java.math.BigDecimal;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/gauss/GaussIntegratorFactory.class */
public class GaussIntegratorFactory {
    private final BaseRuleFactory<Double> legendre = new LegendreRuleFactory();
    private final BaseRuleFactory<BigDecimal> legendreHighPrecision = new LegendreHighPrecisionRuleFactory();
    private final BaseRuleFactory<Double> hermite = new HermiteRuleFactory();

    public GaussIntegrator legendre(int numberOfPoints) {
        return new GaussIntegrator(getRule(this.legendre, numberOfPoints));
    }

    public GaussIntegrator legendre(int numberOfPoints, double lowerBound, double upperBound) throws NotStrictlyPositiveException {
        return new GaussIntegrator(transform(getRule(this.legendre, numberOfPoints), lowerBound, upperBound));
    }

    public GaussIntegrator legendreHighPrecision(int numberOfPoints) throws NotStrictlyPositiveException {
        return new GaussIntegrator(getRule(this.legendreHighPrecision, numberOfPoints));
    }

    public GaussIntegrator legendreHighPrecision(int numberOfPoints, double lowerBound, double upperBound) throws NotStrictlyPositiveException {
        return new GaussIntegrator(transform(getRule(this.legendreHighPrecision, numberOfPoints), lowerBound, upperBound));
    }

    public SymmetricGaussIntegrator hermite(int numberOfPoints) {
        return new SymmetricGaussIntegrator(getRule(this.hermite, numberOfPoints));
    }

    private static Pair<double[], double[]> getRule(BaseRuleFactory<? extends Number> factory, int numberOfPoints) throws NotStrictlyPositiveException, DimensionMismatchException {
        return factory.getRule(numberOfPoints);
    }

    private static Pair<double[], double[]> transform(Pair<double[], double[]> rule, double a2, double b2) {
        double[] points = rule.getFirst();
        double[] weights = rule.getSecond();
        double scale = (b2 - a2) / 2.0d;
        double shift = a2 + scale;
        for (int i2 = 0; i2 < points.length; i2++) {
            points[i2] = (points[i2] * scale) + shift;
            int i3 = i2;
            weights[i3] = weights[i3] * scale;
        }
        return new Pair<>(points, weights);
    }
}
