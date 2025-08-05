package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/inference/ChiSquareTest.class */
public class ChiSquareTest {
    public double chiSquare(double[] expected, long[] observed) throws NotStrictlyPositiveException, NotPositiveException, DimensionMismatchException {
        double d2;
        double d3;
        double d4;
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
        double sumSq = 0.0d;
        for (int i3 = 0; i3 < observed.length; i3++) {
            if (rescale) {
                double dev = observed[i3] - (ratio * expected[i3]);
                d2 = sumSq;
                d3 = dev * dev;
                d4 = ratio * expected[i3];
            } else {
                double dev2 = observed[i3] - expected[i3];
                d2 = sumSq;
                d3 = dev2 * dev2;
                d4 = expected[i3];
            }
            sumSq = d2 + (d3 / d4);
        }
        return sumSq;
    }

    public double chiSquareTest(double[] expected, long[] observed) throws NotStrictlyPositiveException, NotPositiveException, DimensionMismatchException, MaxCountExceededException {
        ChiSquaredDistribution distribution = new ChiSquaredDistribution((RandomGenerator) null, expected.length - 1.0d);
        return 1.0d - distribution.cumulativeProbability(chiSquare(expected, observed));
    }

    public boolean chiSquareTest(double[] expected, long[] observed, double alpha) throws OutOfRangeException, NotStrictlyPositiveException, NotPositiveException, DimensionMismatchException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), 0, Double.valueOf(0.5d));
        }
        return chiSquareTest(expected, observed) < alpha;
    }

    public double chiSquare(long[][] counts) throws NotPositiveException, NullArgumentException, DimensionMismatchException {
        checkArray(counts);
        int nRows = counts.length;
        int nCols = counts[0].length;
        double[] rowSum = new double[nRows];
        double[] colSum = new double[nCols];
        double total = 0.0d;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                int i2 = row;
                rowSum[i2] = rowSum[i2] + counts[row][col];
                int i3 = col;
                colSum[i3] = colSum[i3] + counts[row][col];
                total += counts[row][col];
            }
        }
        double sumSq = 0.0d;
        for (int row2 = 0; row2 < nRows; row2++) {
            for (int col2 = 0; col2 < nCols; col2++) {
                double expected = (rowSum[row2] * colSum[col2]) / total;
                sumSq += ((counts[row2][col2] - expected) * (counts[row2][col2] - expected)) / expected;
            }
        }
        return sumSq;
    }

    public double chiSquareTest(long[][] counts) throws NotPositiveException, NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        checkArray(counts);
        double df = (counts.length - 1.0d) * (counts[0].length - 1.0d);
        ChiSquaredDistribution distribution = new ChiSquaredDistribution(df);
        return 1.0d - distribution.cumulativeProbability(chiSquare(counts));
    }

    public boolean chiSquareTest(long[][] counts, double alpha) throws OutOfRangeException, NotPositiveException, NullArgumentException, DimensionMismatchException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), 0, Double.valueOf(0.5d));
        }
        return chiSquareTest(counts) < alpha;
    }

    public double chiSquareDataSetsComparison(long[] observed1, long[] observed2) throws NotPositiveException, ZeroException, DimensionMismatchException {
        double d2;
        double d3;
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
        double weight = 0.0d;
        for (int i2 = 0; i2 < observed1.length; i2++) {
            countSum1 += observed1[i2];
            countSum2 += observed2[i2];
        }
        if (countSum1 == 0 || countSum2 == 0) {
            throw new ZeroException();
        }
        boolean unequalCounts = countSum1 != countSum2;
        if (unequalCounts) {
            weight = FastMath.sqrt(countSum1 / countSum2);
        }
        double sumSq = 0.0d;
        for (int i3 = 0; i3 < observed1.length; i3++) {
            if (observed1[i3] == 0 && observed2[i3] == 0) {
                throw new ZeroException(LocalizedFormats.OBSERVED_COUNTS_BOTTH_ZERO_FOR_ENTRY, Integer.valueOf(i3));
            }
            double obs1 = observed1[i3];
            double obs2 = observed2[i3];
            if (unequalCounts) {
                d2 = obs1 / weight;
                d3 = obs2 * weight;
            } else {
                d2 = obs1;
                d3 = obs2;
            }
            double dev = d2 - d3;
            sumSq += (dev * dev) / (obs1 + obs2);
        }
        return sumSq;
    }

    public double chiSquareTestDataSetsComparison(long[] observed1, long[] observed2) throws NotPositiveException, ZeroException, DimensionMismatchException, MaxCountExceededException {
        ChiSquaredDistribution distribution = new ChiSquaredDistribution((RandomGenerator) null, observed1.length - 1.0d);
        return 1.0d - distribution.cumulativeProbability(chiSquareDataSetsComparison(observed1, observed2));
    }

    public boolean chiSquareTestDataSetsComparison(long[] observed1, long[] observed2, double alpha) throws OutOfRangeException, NotPositiveException, ZeroException, DimensionMismatchException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), 0, Double.valueOf(0.5d));
        }
        return chiSquareTestDataSetsComparison(observed1, observed2) < alpha;
    }

    private void checkArray(long[][] in) throws NotPositiveException, NullArgumentException, DimensionMismatchException {
        if (in.length < 2) {
            throw new DimensionMismatchException(in.length, 2);
        }
        if (in[0].length < 2) {
            throw new DimensionMismatchException(in[0].length, 2);
        }
        MathArrays.checkRectangular(in);
        MathArrays.checkNonNegative(in);
    }
}
