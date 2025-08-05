package org.apache.commons.math3.analysis.integration.gauss;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/gauss/LegendreRuleFactory.class */
public class LegendreRuleFactory extends BaseRuleFactory<Double> {
    @Override // org.apache.commons.math3.analysis.integration.gauss.BaseRuleFactory
    protected Pair<Double[], Double[]> computeRule(int numberOfPoints) throws DimensionMismatchException {
        if (numberOfPoints == 1) {
            return new Pair<>(new Double[]{Double.valueOf(0.0d)}, new Double[]{Double.valueOf(2.0d)});
        }
        Double[] previousPoints = getRuleInternal(numberOfPoints - 1).getFirst();
        Double[] points = new Double[numberOfPoints];
        Double[] weights = new Double[numberOfPoints];
        int iMax = numberOfPoints / 2;
        int i2 = 0;
        while (i2 < iMax) {
            double a2 = i2 == 0 ? -1.0d : previousPoints[i2 - 1].doubleValue();
            double b2 = iMax == 1 ? 1.0d : previousPoints[i2].doubleValue();
            double pma = 1.0d;
            double pa = a2;
            double pmb = 1.0d;
            double pb = b2;
            for (int j2 = 1; j2 < numberOfPoints; j2++) {
                int two_j_p_1 = (2 * j2) + 1;
                int j_p_1 = j2 + 1;
                double ppa = (((two_j_p_1 * a2) * pa) - (j2 * pma)) / j_p_1;
                double ppb = (((two_j_p_1 * b2) * pb) - (j2 * pmb)) / j_p_1;
                pma = pa;
                pa = ppa;
                pmb = pb;
                pb = ppb;
            }
            double c2 = 0.5d * (a2 + b2);
            double pmc = 1.0d;
            double pc = c2;
            boolean done = false;
            while (!done) {
                done = b2 - a2 <= Math.ulp(c2);
                pmc = 1.0d;
                pc = c2;
                for (int j3 = 1; j3 < numberOfPoints; j3++) {
                    double ppc = (((((2 * j3) + 1) * c2) * pc) - (j3 * pmc)) / (j3 + 1);
                    pmc = pc;
                    pc = ppc;
                }
                if (!done) {
                    if (pa * pc <= 0.0d) {
                        b2 = c2;
                    } else {
                        a2 = c2;
                        pa = pc;
                    }
                    c2 = 0.5d * (a2 + b2);
                }
            }
            double d2 = numberOfPoints * (pmc - (c2 * pc));
            double w2 = (2.0d * (1.0d - (c2 * c2))) / (d2 * d2);
            points[i2] = Double.valueOf(c2);
            weights[i2] = Double.valueOf(w2);
            int idx = (numberOfPoints - i2) - 1;
            points[idx] = Double.valueOf(-c2);
            weights[idx] = Double.valueOf(w2);
            i2++;
        }
        if (numberOfPoints % 2 != 0) {
            double pmc2 = 1.0d;
            for (int j4 = 1; j4 < numberOfPoints; j4 += 2) {
                pmc2 = ((-j4) * pmc2) / (j4 + 1);
            }
            double d3 = numberOfPoints * pmc2;
            points[iMax] = Double.valueOf(0.0d);
            weights[iMax] = Double.valueOf(2.0d / (d3 * d3));
        }
        return new Pair<>(points, weights);
    }
}
