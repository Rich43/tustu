package org.apache.commons.math3.analysis.polynomials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/polynomials/PolynomialsUtils.class */
public class PolynomialsUtils {
    private static final List<BigFraction> CHEBYSHEV_COEFFICIENTS = new ArrayList();
    private static final List<BigFraction> HERMITE_COEFFICIENTS;
    private static final List<BigFraction> LAGUERRE_COEFFICIENTS;
    private static final List<BigFraction> LEGENDRE_COEFFICIENTS;
    private static final Map<JacobiKey, List<BigFraction>> JACOBI_COEFFICIENTS;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/polynomials/PolynomialsUtils$RecurrenceCoefficientsGenerator.class */
    private interface RecurrenceCoefficientsGenerator {
        BigFraction[] generate(int i2);
    }

    static {
        CHEBYSHEV_COEFFICIENTS.add(BigFraction.ONE);
        CHEBYSHEV_COEFFICIENTS.add(BigFraction.ZERO);
        CHEBYSHEV_COEFFICIENTS.add(BigFraction.ONE);
        HERMITE_COEFFICIENTS = new ArrayList();
        HERMITE_COEFFICIENTS.add(BigFraction.ONE);
        HERMITE_COEFFICIENTS.add(BigFraction.ZERO);
        HERMITE_COEFFICIENTS.add(BigFraction.TWO);
        LAGUERRE_COEFFICIENTS = new ArrayList();
        LAGUERRE_COEFFICIENTS.add(BigFraction.ONE);
        LAGUERRE_COEFFICIENTS.add(BigFraction.ONE);
        LAGUERRE_COEFFICIENTS.add(BigFraction.MINUS_ONE);
        LEGENDRE_COEFFICIENTS = new ArrayList();
        LEGENDRE_COEFFICIENTS.add(BigFraction.ONE);
        LEGENDRE_COEFFICIENTS.add(BigFraction.ZERO);
        LEGENDRE_COEFFICIENTS.add(BigFraction.ONE);
        JACOBI_COEFFICIENTS = new HashMap();
    }

    private PolynomialsUtils() {
    }

    public static PolynomialFunction createChebyshevPolynomial(int degree) {
        return buildPolynomial(degree, CHEBYSHEV_COEFFICIENTS, new RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.1
            private final BigFraction[] coeffs = {BigFraction.ZERO, BigFraction.TWO, BigFraction.ONE};

            @Override // org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public BigFraction[] generate(int k2) {
                return this.coeffs;
            }
        });
    }

    public static PolynomialFunction createHermitePolynomial(int degree) {
        return buildPolynomial(degree, HERMITE_COEFFICIENTS, new RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.2
            @Override // org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public BigFraction[] generate(int k2) {
                return new BigFraction[]{BigFraction.ZERO, BigFraction.TWO, new BigFraction(2 * k2)};
            }
        });
    }

    public static PolynomialFunction createLaguerrePolynomial(int degree) {
        return buildPolynomial(degree, LAGUERRE_COEFFICIENTS, new RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.3
            @Override // org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public BigFraction[] generate(int k2) {
                int kP1 = k2 + 1;
                return new BigFraction[]{new BigFraction((2 * k2) + 1, kP1), new BigFraction(-1, kP1), new BigFraction(k2, kP1)};
            }
        });
    }

    public static PolynomialFunction createLegendrePolynomial(int degree) {
        return buildPolynomial(degree, LEGENDRE_COEFFICIENTS, new RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.4
            @Override // org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public BigFraction[] generate(int k2) {
                int kP1 = k2 + 1;
                return new BigFraction[]{BigFraction.ZERO, new BigFraction(k2 + kP1, kP1), new BigFraction(k2, kP1)};
            }
        });
    }

    public static PolynomialFunction createJacobiPolynomial(int degree, final int v2, final int w2) {
        JacobiKey key = new JacobiKey(v2, w2);
        if (!JACOBI_COEFFICIENTS.containsKey(key)) {
            List<BigFraction> list = new ArrayList<>();
            JACOBI_COEFFICIENTS.put(key, list);
            list.add(BigFraction.ONE);
            list.add(new BigFraction(v2 - w2, 2));
            list.add(new BigFraction(2 + v2 + w2, 2));
        }
        return buildPolynomial(degree, JACOBI_COEFFICIENTS.get(key), new RecurrenceCoefficientsGenerator() { // from class: org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.5
            @Override // org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.RecurrenceCoefficientsGenerator
            public BigFraction[] generate(int k2) {
                int k3 = k2 + 1;
                int kvw = k3 + v2 + w2;
                int twoKvw = kvw + k3;
                int twoKvwM1 = twoKvw - 1;
                int twoKvwM2 = twoKvw - 2;
                int den = 2 * k3 * kvw * twoKvwM2;
                return new BigFraction[]{new BigFraction(twoKvwM1 * ((v2 * v2) - (w2 * w2)), den), new BigFraction(twoKvwM1 * twoKvw * twoKvwM2, den), new BigFraction(2 * ((k3 + v2) - 1) * ((k3 + w2) - 1) * twoKvw, den)};
            }
        });
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/polynomials/PolynomialsUtils$JacobiKey.class */
    private static class JacobiKey {

        /* renamed from: v, reason: collision with root package name */
        private final int f12979v;

        /* renamed from: w, reason: collision with root package name */
        private final int f12980w;

        JacobiKey(int v2, int w2) {
            this.f12979v = v2;
            this.f12980w = w2;
        }

        public int hashCode() {
            return (this.f12979v << 16) ^ this.f12980w;
        }

        public boolean equals(Object key) {
            if (key == null || !(key instanceof JacobiKey)) {
                return false;
            }
            JacobiKey otherK = (JacobiKey) key;
            return this.f12979v == otherK.f12979v && this.f12980w == otherK.f12980w;
        }
    }

    public static double[] shift(double[] coefficients, double shift) {
        int dp1 = coefficients.length;
        double[] newCoefficients = new double[dp1];
        int[][] coeff = new int[dp1][dp1];
        for (int i2 = 0; i2 < dp1; i2++) {
            for (int j2 = 0; j2 <= i2; j2++) {
                coeff[i2][j2] = (int) CombinatoricsUtils.binomialCoefficient(i2, j2);
            }
        }
        for (int i3 = 0; i3 < dp1; i3++) {
            newCoefficients[0] = newCoefficients[0] + (coefficients[i3] * FastMath.pow(shift, i3));
        }
        int d2 = dp1 - 1;
        for (int i4 = 0; i4 < d2; i4++) {
            for (int j3 = i4; j3 < d2; j3++) {
                int i5 = i4 + 1;
                newCoefficients[i5] = newCoefficients[i5] + (coeff[j3 + 1][j3 - i4] * coefficients[j3 + 1] * FastMath.pow(shift, j3 - i4));
            }
        }
        return newCoefficients;
    }

    private static PolynomialFunction buildPolynomial(int degree, List<BigFraction> coefficients, RecurrenceCoefficientsGenerator generator) {
        synchronized (coefficients) {
            int maxDegree = ((int) FastMath.floor(FastMath.sqrt(2 * coefficients.size()))) - 1;
            if (degree > maxDegree) {
                computeUpToDegree(degree, maxDegree, generator, coefficients);
            }
        }
        int start = (degree * (degree + 1)) / 2;
        double[] a2 = new double[degree + 1];
        for (int i2 = 0; i2 <= degree; i2++) {
            a2[i2] = coefficients.get(start + i2).doubleValue();
        }
        return new PolynomialFunction(a2);
    }

    private static void computeUpToDegree(int degree, int maxDegree, RecurrenceCoefficientsGenerator generator, List<BigFraction> coefficients) {
        int startK = ((maxDegree - 1) * maxDegree) / 2;
        for (int k2 = maxDegree; k2 < degree; k2++) {
            int startKm1 = startK;
            startK += k2;
            BigFraction[] ai2 = generator.generate(k2);
            BigFraction ck = coefficients.get(startK);
            BigFraction ckm1 = coefficients.get(startKm1);
            coefficients.add(ck.multiply(ai2[0]).subtract(ckm1.multiply(ai2[2])));
            for (int i2 = 1; i2 < k2; i2++) {
                BigFraction ckPrev = ck;
                ck = coefficients.get(startK + i2);
                BigFraction ckm12 = coefficients.get(startKm1 + i2);
                coefficients.add(ck.multiply(ai2[0]).add(ckPrev.multiply(ai2[1])).subtract(ckm12.multiply(ai2[2])));
            }
            BigFraction ckPrev2 = ck;
            BigFraction ck2 = coefficients.get(startK + k2);
            coefficients.add(ck2.multiply(ai2[0]).add(ckPrev2.multiply(ai2[1])));
            coefficients.add(ck2.multiply(ai2[1]));
        }
    }
}
