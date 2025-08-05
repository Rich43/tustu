package org.apache.commons.math3.util;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/CombinatoricsUtils.class */
public final class CombinatoricsUtils {
    static final long[] FACTORIALS = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    static final AtomicReference<long[][]> STIRLING_S2 = new AtomicReference<>(null);

    private CombinatoricsUtils() {
    }

    public static long binomialCoefficient(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        checkBinomial(n2, k2);
        if (n2 == k2 || k2 == 0) {
            return 1L;
        }
        if (k2 == 1 || k2 == n2 - 1) {
            return n2;
        }
        if (k2 > n2 / 2) {
            return binomialCoefficient(n2, n2 - k2);
        }
        long result = 1;
        if (n2 <= 61) {
            int i2 = (n2 - k2) + 1;
            for (int j2 = 1; j2 <= k2; j2++) {
                result = (result * i2) / j2;
                i2++;
            }
        } else if (n2 <= 66) {
            int i3 = (n2 - k2) + 1;
            for (int j3 = 1; j3 <= k2; j3++) {
                long d2 = ArithmeticUtils.gcd(i3, j3);
                result = (result / (j3 / d2)) * (i3 / d2);
                i3++;
            }
        } else {
            int i4 = (n2 - k2) + 1;
            for (int j4 = 1; j4 <= k2; j4++) {
                long d3 = ArithmeticUtils.gcd(i4, j4);
                result = ArithmeticUtils.mulAndCheck(result / (j4 / d3), i4 / d3);
                i4++;
            }
        }
        return result;
    }

    public static double binomialCoefficientDouble(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        checkBinomial(n2, k2);
        if (n2 == k2 || k2 == 0) {
            return 1.0d;
        }
        if (k2 == 1 || k2 == n2 - 1) {
            return n2;
        }
        if (k2 > n2 / 2) {
            return binomialCoefficientDouble(n2, n2 - k2);
        }
        if (n2 < 67) {
            return binomialCoefficient(n2, k2);
        }
        double result = 1.0d;
        for (int i2 = 1; i2 <= k2; i2++) {
            result *= ((n2 - k2) + i2) / i2;
        }
        return FastMath.floor(result + 0.5d);
    }

    public static double binomialCoefficientLog(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        checkBinomial(n2, k2);
        if (n2 == k2 || k2 == 0) {
            return 0.0d;
        }
        if (k2 == 1 || k2 == n2 - 1) {
            return FastMath.log(n2);
        }
        if (n2 < 67) {
            return FastMath.log(binomialCoefficient(n2, k2));
        }
        if (n2 < 1030) {
            return FastMath.log(binomialCoefficientDouble(n2, k2));
        }
        if (k2 > n2 / 2) {
            return binomialCoefficientLog(n2, n2 - k2);
        }
        double logSum = 0.0d;
        for (int i2 = (n2 - k2) + 1; i2 <= n2; i2++) {
            logSum += FastMath.log(i2);
        }
        for (int i3 = 2; i3 <= k2; i3++) {
            logSum -= FastMath.log(i3);
        }
        return logSum;
    }

    public static long factorial(int n2) throws NotPositiveException, MathArithmeticException {
        if (n2 < 0) {
            throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n2));
        }
        if (n2 > 20) {
            throw new MathArithmeticException();
        }
        return FACTORIALS[n2];
    }

    public static double factorialDouble(int n2) throws NotPositiveException {
        if (n2 < 0) {
            throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n2));
        }
        if (n2 < 21) {
            return FACTORIALS[n2];
        }
        return FastMath.floor(FastMath.exp(factorialLog(n2)) + 0.5d);
    }

    public static double factorialLog(int n2) throws NotPositiveException {
        if (n2 < 0) {
            throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n2));
        }
        if (n2 < 21) {
            return FastMath.log(FACTORIALS[n2]);
        }
        double logSum = 0.0d;
        for (int i2 = 2; i2 <= n2; i2++) {
            logSum += FastMath.log(i2);
        }
        return logSum;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v42, types: [long[]] */
    public static long stirlingS2(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        if (k2 < 0) {
            throw new NotPositiveException(Integer.valueOf(k2));
        }
        if (k2 > n2) {
            throw new NumberIsTooLargeException(Integer.valueOf(k2), Integer.valueOf(n2), true);
        }
        long[][] stirlingS2 = STIRLING_S2.get();
        if (stirlingS2 == null) {
            stirlingS2 = new long[26];
            stirlingS2[0] = new long[]{1};
            for (int i2 = 1; i2 < stirlingS2.length; i2++) {
                stirlingS2[i2] = new long[i2 + 1];
                stirlingS2[i2][0] = 0;
                stirlingS2[i2][1] = 1;
                stirlingS2[i2][i2] = 1;
                for (int j2 = 2; j2 < i2; j2++) {
                    stirlingS2[i2][j2] = (j2 * stirlingS2[i2 - 1][j2]) + stirlingS2[i2 - 1][j2 - 1];
                }
            }
            STIRLING_S2.compareAndSet(null, stirlingS2);
        }
        if (n2 < stirlingS2.length) {
            return stirlingS2[n2][k2];
        }
        if (k2 == 0) {
            return 0L;
        }
        if (k2 == 1 || k2 == n2) {
            return 1L;
        }
        if (k2 == 2) {
            return (1 << (n2 - 1)) - 1;
        }
        if (k2 == n2 - 1) {
            return binomialCoefficient(n2, 2);
        }
        long sum = 0;
        long sign = (k2 & 1) == 0 ? 1L : -1L;
        for (int j3 = 1; j3 <= k2; j3++) {
            sign = -sign;
            sum += sign * binomialCoefficient(k2, j3) * ArithmeticUtils.pow(j3, n2);
            if (sum < 0) {
                throw new MathArithmeticException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN, Integer.valueOf(n2), 0, Integer.valueOf(stirlingS2.length - 1));
            }
        }
        return sum / factorial(k2);
    }

    public static Iterator<int[]> combinationsIterator(int n2, int k2) {
        return new Combinations(n2, k2).iterator();
    }

    public static void checkBinomial(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException {
        if (n2 < k2) {
            throw new NumberIsTooLargeException(LocalizedFormats.BINOMIAL_INVALID_PARAMETERS_ORDER, Integer.valueOf(k2), Integer.valueOf(n2), true);
        }
        if (n2 < 0) {
            throw new NotPositiveException(LocalizedFormats.BINOMIAL_NEGATIVE_PARAMETER, Integer.valueOf(n2));
        }
    }
}
