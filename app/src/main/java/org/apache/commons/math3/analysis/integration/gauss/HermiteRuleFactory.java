package org.apache.commons.math3.analysis.integration.gauss;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/gauss/HermiteRuleFactory.class */
public class HermiteRuleFactory extends BaseRuleFactory<Double> {
    private static final double SQRT_PI = 1.772453850905516d;
    private static final double H0 = 0.7511255444649425d;
    private static final double H1 = 1.0622519320271968d;

    @Override // org.apache.commons.math3.analysis.integration.gauss.BaseRuleFactory
    protected Pair<Double[], Double[]> computeRule(int numberOfPoints) throws DimensionMismatchException {
        if (numberOfPoints == 1) {
            return new Pair<>(new Double[]{Double.valueOf(0.0d)}, new Double[]{Double.valueOf(SQRT_PI)});
        }
        int lastNumPoints = numberOfPoints - 1;
        Double[] previousPoints = getRuleInternal(lastNumPoints).getFirst();
        Double[] points = new Double[numberOfPoints];
        Double[] weights = new Double[numberOfPoints];
        double sqrtTwoTimesLastNumPoints = FastMath.sqrt(2 * lastNumPoints);
        double sqrtTwoTimesNumPoints = FastMath.sqrt(2 * numberOfPoints);
        int iMax = numberOfPoints / 2;
        int i2 = 0;
        while (i2 < iMax) {
            double a2 = i2 == 0 ? -sqrtTwoTimesLastNumPoints : previousPoints[i2 - 1].doubleValue();
            double b2 = iMax == 1 ? -0.5d : previousPoints[i2].doubleValue();
            double hma = 0.7511255444649425d;
            double ha = H1 * a2;
            double hmb = 0.7511255444649425d;
            double hb = H1 * b2;
            for (int j2 = 1; j2 < numberOfPoints; j2++) {
                double jp1 = j2 + 1;
                double s2 = FastMath.sqrt(2.0d / jp1);
                double sm = FastMath.sqrt(j2 / jp1);
                double hpa = ((s2 * a2) * ha) - (sm * hma);
                double hpb = ((s2 * b2) * hb) - (sm * hmb);
                hma = ha;
                ha = hpa;
                hmb = hb;
                hb = hpb;
            }
            double c2 = 0.5d * (a2 + b2);
            double hmc = 0.7511255444649425d;
            double d2 = H1 * c2;
            boolean done = false;
            while (!done) {
                done = b2 - a2 <= Math.ulp(c2);
                hmc = 0.7511255444649425d;
                double hc = H1 * c2;
                for (int j3 = 1; j3 < numberOfPoints; j3++) {
                    double jp12 = j3 + 1;
                    double hpc = ((FastMath.sqrt(2.0d / jp12) * c2) * hc) - (FastMath.sqrt(j3 / jp12) * hmc);
                    hmc = hc;
                    hc = hpc;
                }
                if (!done) {
                    if (ha * hc < 0.0d) {
                        b2 = c2;
                    } else {
                        a2 = c2;
                        ha = hc;
                    }
                    c2 = 0.5d * (a2 + b2);
                }
            }
            double d3 = sqrtTwoTimesNumPoints * hmc;
            double w2 = 2.0d / (d3 * d3);
            points[i2] = Double.valueOf(c2);
            weights[i2] = Double.valueOf(w2);
            int idx = lastNumPoints - i2;
            points[idx] = Double.valueOf(-c2);
            weights[idx] = Double.valueOf(w2);
            i2++;
        }
        if (numberOfPoints % 2 != 0) {
            double hm = 0.7511255444649425d;
            for (int j4 = 1; j4 < numberOfPoints; j4 += 2) {
                hm = (-FastMath.sqrt(j4 / (j4 + 1))) * hm;
            }
            double d4 = sqrtTwoTimesNumPoints * hm;
            points[iMax] = Double.valueOf(0.0d);
            weights[iMax] = Double.valueOf(2.0d / (d4 * d4));
        }
        return new Pair<>(points, weights);
    }
}
