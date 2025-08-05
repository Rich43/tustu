package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/inference/GTest.class */
public class GTest {
    public double g(double[] expected, long[] observed) throws NotStrictlyPositiveException, NotPositiveException, DimensionMismatchException {
        if (expected.length < 2) {
            throw new DimensionMismatchException(expected.length, 2);
        }
        if (expected.length != observed.length) {
            throw new DimensionMismatchException(expected.length, observed.length);
        }
        MathArrays.checkPositive(expected);
        MathArrays.checkNonNegative(observed);
        double sumExpected = 0.0d;
        double sumObserved = 0.0d;
        for (int i2 = 0; i2 < observed.length; i2++) {
            sumExpected += expected[i2];
            sumObserved += observed[i2];
        }
        double ratio = 1.0d;
        boolean rescale = false;
        if (FastMath.abs(sumExpected - sumObserved) > 1.0E-5d) {
            ratio = sumObserved / sumExpected;
            rescale = true;
        }
        double sum = 0.0d;
        for (int i3 = 0; i3 < observed.length; i3++) {
            double dev = rescale ? FastMath.log(observed[i3] / (ratio * expected[i3])) : FastMath.log(observed[i3] / expected[i3]);
            sum += observed[i3] * dev;
        }
        return 2.0d * sum;
    }

    public double gTest(double[] expected, long[] observed) throws NotStrictlyPositiveException, NotPositiveException, DimensionMismatchException, MaxCountExceededException {
        ChiSquaredDistribution distribution = new ChiSquaredDistribution((RandomGenerator) null, expected.length - 1.0d);
        return 1.0d - distribution.cumulativeProbability(g(expected, observed));
    }

    public double gTestIntrinsic(double[] expected, long[] observed) throws NotStrictlyPositiveException, NotPositiveException, DimensionMismatchException, MaxCountExceededException {
        ChiSquaredDistribution distribution = new ChiSquaredDistribution((RandomGenerator) null, expected.length - 2.0d);
        return 1.0d - distribution.cumulativeProbability(g(expected, observed));
    }

    public boolean gTest(double[] expected, long[] observed, double alpha) throws OutOfRangeException, NotStrictlyPositiveException, NotPositiveException, DimensionMismatchException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), 0, Double.valueOf(0.5d));
        }
        return gTest(expected, observed) < alpha;
    }

    private double entropy(long[][] k2) {
        double h2 = 0.0d;
        double sum_k = 0.0d;
        for (int i2 = 0; i2 < k2.length; i2++) {
            for (int j2 = 0; j2 < k2[i2].length; j2++) {
                sum_k += k2[i2][j2];
            }
        }
        for (int i3 = 0; i3 < k2.length; i3++) {
            for (int j3 = 0; j3 < k2[i3].length; j3++) {
                if (k2[i3][j3] != 0) {
                    double p_ij = k2[i3][j3] / sum_k;
                    h2 += p_ij * FastMath.log(p_ij);
                }
            }
        }
        return -h2;
    }

    private double entropy(long[] k2) {
        double h2 = 0.0d;
        double sum_k = 0.0d;
        for (long j2 : k2) {
            sum_k += j2;
        }
        for (int i2 = 0; i2 < k2.length; i2++) {
            if (k2[i2] != 0) {
                double p_i = k2[i2] / sum_k;
                h2 += p_i * FastMath.log(p_i);
            }
        }
        return -h2;
    }

    public double gDataSetsComparison(long[] observed1, long[] observed2) throws NotPositiveException, ZeroException, DimensionMismatchException {
        if (observed1.length < 2) {
            throw new DimensionMismatchException(observed1.length, 2);
        }
        if (observed1.length != observed2.length) {
            throw new DimensionMismatchException(observed1.length, observed2.length);
        }
        MathArrays.checkNonNegative(observed1);
        MathArrays.checkNonNegative(observed2);
        long countSum1 = 0;
        long countSum2 = 0;
        long[] collSums = new long[observed1.length];
        long[][] k2 = new long[2][observed1.length];
        for (int i2 = 0; i2 < observed1.length; i2++) {
            if (observed1[i2] == 0 && observed2[i2] == 0) {
                throw new ZeroException(LocalizedFormats.OBSERVED_COUNTS_BOTTH_ZERO_FOR_ENTRY, Integer.valueOf(i2));
            }
            countSum1 += observed1[i2];
            countSum2 += observed2[i2];
            collSums[i2] = observed1[i2] + observed2[i2];
            k2[0][i2] = observed1[i2];
            k2[1][i2] = observed2[i2];
        }
        if (countSum1 == 0 || countSum2 == 0) {
            throw new ZeroException();
        }
        long[] rowSums = {countSum1, countSum2};
        double sum = countSum1 + countSum2;
        return 2.0d * sum * ((entropy(rowSums) + entropy(collSums)) - entropy(k2));
    }

    public double rootLogLikelihoodRatio(long k11, long k12, long k21, long k22) throws NotPositiveException, ZeroException, DimensionMismatchException {
        double llr = gDataSetsComparison(new long[]{k11, k12}, new long[]{k21, k22});
        double sqrt = FastMath.sqrt(llr);
        if (k11 / (k11 + k12) < k21 / (k21 + k22)) {
            sqrt = -sqrt;
        }
        return sqrt;
    }

    public double gTestDataSetsComparison(long[] observed1, long[] observed2) throws NotPositiveException, ZeroException, DimensionMismatchException, MaxCountExceededException {
        ChiSquaredDistribution distribution = new ChiSquaredDistribution((RandomGenerator) null, observed1.length - 1.0d);
        return 1.0d - distribution.cumulativeProbability(gDataSetsComparison(observed1, observed2));
    }

    public boolean gTestDataSetsComparison(long[] observed1, long[] observed2, double alpha) throws OutOfRangeException, NotPositiveException, ZeroException, DimensionMismatchException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), 0, Double.valueOf(0.5d));
        }
        return gTestDataSetsComparison(observed1, observed2) < alpha;
    }
}
