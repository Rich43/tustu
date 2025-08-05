package org.apache.commons.math3.stat.correlation;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/correlation/KendallsCorrelation.class */
public class KendallsCorrelation {
    private final RealMatrix correlationMatrix;

    public KendallsCorrelation() {
        this.correlationMatrix = null;
    }

    public KendallsCorrelation(double[][] data) {
        this(MatrixUtils.createRealMatrix(data));
    }

    public KendallsCorrelation(RealMatrix matrix) {
        this.correlationMatrix = computeCorrelationMatrix(matrix);
    }

    public RealMatrix getCorrelationMatrix() {
        return this.correlationMatrix;
    }

    public RealMatrix computeCorrelationMatrix(RealMatrix matrix) throws OutOfRangeException, DimensionMismatchException {
        int nVars = matrix.getColumnDimension();
        RealMatrix outMatrix = new BlockRealMatrix(nVars, nVars);
        for (int i2 = 0; i2 < nVars; i2++) {
            for (int j2 = 0; j2 < i2; j2++) {
                double corr = correlation(matrix.getColumn(i2), matrix.getColumn(j2));
                outMatrix.setEntry(i2, j2, corr);
                outMatrix.setEntry(j2, i2, corr);
            }
            outMatrix.setEntry(i2, i2, 1.0d);
        }
        return outMatrix;
    }

    public RealMatrix computeCorrelationMatrix(double[][] matrix) {
        return computeCorrelationMatrix(new BlockRealMatrix(matrix));
    }

    public double correlation(double[] xArray, double[] yArray) throws DimensionMismatchException {
        long j2;
        long j3;
        if (xArray.length != yArray.length) {
            throw new DimensionMismatchException(xArray.length, yArray.length);
        }
        int n2 = xArray.length;
        long numPairs = sum(n2 - 1);
        Pair<Double, Double>[] pairs = new Pair[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            pairs[i2] = new Pair<>(Double.valueOf(xArray[i2]), Double.valueOf(yArray[i2]));
        }
        Arrays.sort(pairs, new Comparator<Pair<Double, Double>>() { // from class: org.apache.commons.math3.stat.correlation.KendallsCorrelation.1
            @Override // java.util.Comparator
            public int compare(Pair<Double, Double> pair1, Pair<Double, Double> pair2) {
                int compareFirst = pair1.getFirst().compareTo(pair2.getFirst());
                return compareFirst != 0 ? compareFirst : pair1.getSecond().compareTo(pair2.getSecond());
            }
        });
        long tiedXPairs = 0;
        long tiedXYPairs = 0;
        long consecutiveXTies = 1;
        long consecutiveXYTies = 1;
        Pair<Double, Double> prev = pairs[0];
        for (int i3 = 1; i3 < n2; i3++) {
            Pair<Double, Double> curr = pairs[i3];
            if (curr.getFirst().equals(prev.getFirst())) {
                consecutiveXTies++;
                if (curr.getSecond().equals(prev.getSecond())) {
                    j3 = consecutiveXYTies + 1;
                } else {
                    tiedXYPairs += sum(consecutiveXYTies - 1);
                    j3 = 1;
                }
            } else {
                tiedXPairs += sum(consecutiveXTies - 1);
                consecutiveXTies = 1;
                tiedXYPairs += sum(consecutiveXYTies - 1);
                j3 = 1;
            }
            consecutiveXYTies = j3;
            prev = curr;
        }
        long tiedXPairs2 = tiedXPairs + sum(consecutiveXTies - 1);
        long tiedXYPairs2 = tiedXYPairs + sum(consecutiveXYTies - 1);
        long swaps = 0;
        Pair<Double, Double>[] pairsDestination = new Pair[n2];
        int i4 = 1;
        while (true) {
            int segmentSize = i4;
            if (segmentSize >= n2) {
                break;
            }
            int i5 = 0;
            while (true) {
                int offset = i5;
                if (offset < n2) {
                    int i6 = offset;
                    int iEnd = FastMath.min(i6 + segmentSize, n2);
                    int j4 = iEnd;
                    int jEnd = FastMath.min(j4 + segmentSize, n2);
                    int copyLocation = offset;
                    while (true) {
                        if (i6 < iEnd || j4 < jEnd) {
                            if (i6 < iEnd) {
                                if (j4 < jEnd) {
                                    if (pairs[i6].getSecond().compareTo(pairs[j4].getSecond()) <= 0) {
                                        pairsDestination[copyLocation] = pairs[i6];
                                        i6++;
                                    } else {
                                        pairsDestination[copyLocation] = pairs[j4];
                                        j4++;
                                        swaps += iEnd - i6;
                                    }
                                } else {
                                    pairsDestination[copyLocation] = pairs[i6];
                                    i6++;
                                }
                            } else {
                                pairsDestination[copyLocation] = pairs[j4];
                                j4++;
                            }
                            copyLocation++;
                        }
                    }
                    i5 = offset + (2 * segmentSize);
                }
            }
            Pair<Double, Double>[] pairsTemp = pairs;
            pairs = pairsDestination;
            pairsDestination = pairsTemp;
            i4 = segmentSize << 1;
        }
        long tiedYPairs = 0;
        long consecutiveYTies = 1;
        Pair<Double, Double> prev2 = pairs[0];
        for (int i7 = 1; i7 < n2; i7++) {
            Pair<Double, Double> curr2 = pairs[i7];
            if (curr2.getSecond().equals(prev2.getSecond())) {
                j2 = consecutiveYTies + 1;
            } else {
                tiedYPairs += sum(consecutiveYTies - 1);
                j2 = 1;
            }
            consecutiveYTies = j2;
            prev2 = curr2;
        }
        long concordantMinusDiscordant = (((numPairs - tiedXPairs2) - (tiedYPairs + sum(consecutiveYTies - 1))) + tiedXYPairs2) - (2 * swaps);
        double nonTiedPairsMultiplied = (numPairs - tiedXPairs2) * (numPairs - r0);
        return concordantMinusDiscordant / FastMath.sqrt(nonTiedPairsMultiplied);
    }

    private static long sum(long n2) {
        return (n2 * (n2 + 1)) / 2;
    }
}
