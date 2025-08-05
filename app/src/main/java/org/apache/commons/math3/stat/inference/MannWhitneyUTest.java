package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/inference/MannWhitneyUTest.class */
public class MannWhitneyUTest {
    private NaturalRanking naturalRanking;

    public MannWhitneyUTest() {
        this.naturalRanking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
    }

    public MannWhitneyUTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy) {
        this.naturalRanking = new NaturalRanking(nanStrategy, tiesStrategy);
    }

    private void ensureDataConformance(double[] x2, double[] y2) throws NullArgumentException, NoDataException {
        if (x2 == null || y2 == null) {
            throw new NullArgumentException();
        }
        if (x2.length == 0 || y2.length == 0) {
            throw new NoDataException();
        }
    }

    private double[] concatenateSamples(double[] x2, double[] y2) {
        double[] z2 = new double[x2.length + y2.length];
        System.arraycopy(x2, 0, z2, 0, x2.length);
        System.arraycopy(y2, 0, z2, x2.length, y2.length);
        return z2;
    }

    public double mannWhitneyU(double[] x2, double[] y2) throws NullArgumentException, NoDataException {
        ensureDataConformance(x2, y2);
        double[] z2 = concatenateSamples(x2, y2);
        double[] ranks = this.naturalRanking.rank(z2);
        double sumRankX = 0.0d;
        for (int i2 = 0; i2 < x2.length; i2++) {
            sumRankX += ranks[i2];
        }
        double U1 = sumRankX - ((x2.length * (x2.length + 1)) / 2);
        double U2 = (x2.length * y2.length) - U1;
        return FastMath.max(U1, U2);
    }

    private double calculateAsymptoticPValue(double Umin, int n1, int n2) throws ConvergenceException, MaxCountExceededException {
        long n1n2prod = n1 * n2;
        double EU = n1n2prod / 2.0d;
        double VarU = (n1n2prod * ((n1 + n2) + 1)) / 12.0d;
        double z2 = (Umin - EU) / FastMath.sqrt(VarU);
        NormalDistribution standardNormal = new NormalDistribution((RandomGenerator) null, 0.0d, 1.0d);
        return 2.0d * standardNormal.cumulativeProbability(z2);
    }

    public double mannWhitneyUTest(double[] x2, double[] y2) throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        ensureDataConformance(x2, y2);
        double Umax = mannWhitneyU(x2, y2);
        double Umin = (x2.length * y2.length) - Umax;
        return calculateAsymptoticPValue(Umin, x2.length, y2.length);
    }
}
