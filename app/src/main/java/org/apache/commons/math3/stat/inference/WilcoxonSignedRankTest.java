package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/inference/WilcoxonSignedRankTest.class */
public class WilcoxonSignedRankTest {
    private NaturalRanking naturalRanking;

    public WilcoxonSignedRankTest() {
        this.naturalRanking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
    }

    public WilcoxonSignedRankTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy) {
        this.naturalRanking = new NaturalRanking(nanStrategy, tiesStrategy);
    }

    private void ensureDataConformance(double[] x2, double[] y2) throws NullArgumentException, NoDataException, DimensionMismatchException {
        if (x2 == null || y2 == null) {
            throw new NullArgumentException();
        }
        if (x2.length == 0 || y2.length == 0) {
            throw new NoDataException();
        }
        if (y2.length != x2.length) {
            throw new DimensionMismatchException(y2.length, x2.length);
        }
    }

    private double[] calculateDifferences(double[] x2, double[] y2) {
        double[] z2 = new double[x2.length];
        for (int i2 = 0; i2 < x2.length; i2++) {
            z2[i2] = y2[i2] - x2[i2];
        }
        return z2;
    }

    private double[] calculateAbsoluteDifferences(double[] z2) throws NullArgumentException, NoDataException {
        if (z2 == null) {
            throw new NullArgumentException();
        }
        if (z2.length == 0) {
            throw new NoDataException();
        }
        double[] zAbs = new double[z2.length];
        for (int i2 = 0; i2 < z2.length; i2++) {
            zAbs[i2] = FastMath.abs(z2[i2]);
        }
        return zAbs;
    }

    public double wilcoxonSignedRank(double[] x2, double[] y2) throws NullArgumentException, NoDataException, DimensionMismatchException {
        ensureDataConformance(x2, y2);
        double[] z2 = calculateDifferences(x2, y2);
        double[] zAbs = calculateAbsoluteDifferences(z2);
        double[] ranks = this.naturalRanking.rank(zAbs);
        double Wplus = 0.0d;
        for (int i2 = 0; i2 < z2.length; i2++) {
            if (z2[i2] > 0.0d) {
                Wplus += ranks[i2];
            }
        }
        int N2 = x2.length;
        double Wminus = ((N2 * (N2 + 1)) / 2.0d) - Wplus;
        return FastMath.max(Wplus, Wminus);
    }

    private double calculateExactPValue(double Wmax, int N2) {
        int m2 = 1 << N2;
        int largerRankSums = 0;
        for (int i2 = 0; i2 < m2; i2++) {
            int rankSum = 0;
            for (int j2 = 0; j2 < N2; j2++) {
                if (((i2 >> j2) & 1) == 1) {
                    rankSum += j2 + 1;
                }
            }
            if (rankSum >= Wmax) {
                largerRankSums++;
            }
        }
        return (2.0d * largerRankSums) / m2;
    }

    private double calculateAsymptoticPValue(double Wmin, int N2) {
        double ES = (N2 * (N2 + 1)) / 4.0d;
        double VarS = ES * (((2 * N2) + 1) / 6.0d);
        double z2 = ((Wmin - ES) - 0.5d) / FastMath.sqrt(VarS);
        NormalDistribution standardNormal = new NormalDistribution((RandomGenerator) null, 0.0d, 1.0d);
        return 2.0d * standardNormal.cumulativeProbability(z2);
    }

    public double wilcoxonSignedRankTest(double[] x2, double[] y2, boolean exactPValue) throws NullArgumentException, NoDataException, ConvergenceException, DimensionMismatchException, MaxCountExceededException, NumberIsTooLargeException {
        ensureDataConformance(x2, y2);
        int N2 = x2.length;
        double Wmax = wilcoxonSignedRank(x2, y2);
        if (exactPValue && N2 > 30) {
            throw new NumberIsTooLargeException(Integer.valueOf(N2), 30, true);
        }
        if (exactPValue) {
            return calculateExactPValue(Wmax, N2);
        }
        double Wmin = ((N2 * (N2 + 1)) / 2.0d) - Wmax;
        return calculateAsymptoticPValue(Wmin, N2);
    }
}
