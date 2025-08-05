package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/inference/TTest.class */
public class TTest {
    public double pairedT(double[] sample1, double[] sample2) throws NumberIsTooSmallException, NullArgumentException, NoDataException, DimensionMismatchException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        double meanDifference = StatUtils.meanDifference(sample1, sample2);
        return t(meanDifference, 0.0d, StatUtils.varianceDifference(sample1, sample2, meanDifference), sample1.length);
    }

    public double pairedTTest(double[] sample1, double[] sample2) throws NumberIsTooSmallException, NullArgumentException, NoDataException, DimensionMismatchException, MaxCountExceededException {
        double meanDifference = StatUtils.meanDifference(sample1, sample2);
        return tTest(meanDifference, 0.0d, StatUtils.varianceDifference(sample1, sample2, meanDifference), sample1.length);
    }

    public boolean pairedTTest(double[] sample1, double[] sample2, double alpha) throws NumberIsTooSmallException, OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException, MaxCountExceededException {
        checkSignificanceLevel(alpha);
        return pairedTTest(sample1, sample2) < alpha;
    }

    public double t(double mu, double[] observed) throws NumberIsTooSmallException, NullArgumentException {
        checkSampleData(observed);
        return t(StatUtils.mean(observed), mu, StatUtils.variance(observed), observed.length);
    }

    public double t(double mu, StatisticalSummary sampleStats) throws NumberIsTooSmallException, NullArgumentException {
        checkSampleData(sampleStats);
        return t(sampleStats.getMean(), mu, sampleStats.getVariance(), sampleStats.getN());
    }

    public double homoscedasticT(double[] sample1, double[] sample2) throws NumberIsTooSmallException, NullArgumentException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        return homoscedasticT(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
    }

    public double t(double[] sample1, double[] sample2) throws NumberIsTooSmallException, NullArgumentException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        return t(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
    }

    public double t(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2) throws NumberIsTooSmallException, NullArgumentException {
        checkSampleData(sampleStats1);
        checkSampleData(sampleStats2);
        return t(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
    }

    public double homoscedasticT(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2) throws NumberIsTooSmallException, NullArgumentException {
        checkSampleData(sampleStats1);
        checkSampleData(sampleStats2);
        return homoscedasticT(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
    }

    public double tTest(double mu, double[] sample) throws NumberIsTooSmallException, NullArgumentException, MaxCountExceededException {
        checkSampleData(sample);
        return tTest(StatUtils.mean(sample), mu, StatUtils.variance(sample), sample.length);
    }

    public boolean tTest(double mu, double[] sample, double alpha) throws NumberIsTooSmallException, OutOfRangeException, NullArgumentException, MaxCountExceededException {
        checkSignificanceLevel(alpha);
        return tTest(mu, sample) < alpha;
    }

    public double tTest(double mu, StatisticalSummary sampleStats) throws NumberIsTooSmallException, NullArgumentException, MaxCountExceededException {
        checkSampleData(sampleStats);
        return tTest(sampleStats.getMean(), mu, sampleStats.getVariance(), sampleStats.getN());
    }

    public boolean tTest(double mu, StatisticalSummary sampleStats, double alpha) throws NumberIsTooSmallException, OutOfRangeException, NullArgumentException, MaxCountExceededException {
        checkSignificanceLevel(alpha);
        return tTest(mu, sampleStats) < alpha;
    }

    public double tTest(double[] sample1, double[] sample2) throws NumberIsTooSmallException, NullArgumentException, MaxCountExceededException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        return tTest(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
    }

    public double homoscedasticTTest(double[] sample1, double[] sample2) throws NumberIsTooSmallException, NullArgumentException, MaxCountExceededException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        return homoscedasticTTest(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
    }

    public boolean tTest(double[] sample1, double[] sample2, double alpha) throws NumberIsTooSmallException, OutOfRangeException, NullArgumentException, MaxCountExceededException {
        checkSignificanceLevel(alpha);
        return tTest(sample1, sample2) < alpha;
    }

    public boolean homoscedasticTTest(double[] sample1, double[] sample2, double alpha) throws NumberIsTooSmallException, OutOfRangeException, NullArgumentException, MaxCountExceededException {
        checkSignificanceLevel(alpha);
        return homoscedasticTTest(sample1, sample2) < alpha;
    }

    public double tTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2) throws NumberIsTooSmallException, NullArgumentException, MaxCountExceededException {
        checkSampleData(sampleStats1);
        checkSampleData(sampleStats2);
        return tTest(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
    }

    public double homoscedasticTTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2) throws NumberIsTooSmallException, NullArgumentException, MaxCountExceededException {
        checkSampleData(sampleStats1);
        checkSampleData(sampleStats2);
        return homoscedasticTTest(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
    }

    public boolean tTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2, double alpha) throws NumberIsTooSmallException, OutOfRangeException, NullArgumentException, MaxCountExceededException {
        checkSignificanceLevel(alpha);
        return tTest(sampleStats1, sampleStats2) < alpha;
    }

    protected double df(double v1, double v2, double n1, double n2) {
        return (((v1 / n1) + (v2 / n2)) * ((v1 / n1) + (v2 / n2))) / (((v1 * v1) / ((n1 * n1) * (n1 - 1.0d))) + ((v2 * v2) / ((n2 * n2) * (n2 - 1.0d))));
    }

    protected double t(double m2, double mu, double v2, double n2) {
        return (m2 - mu) / FastMath.sqrt(v2 / n2);
    }

    protected double t(double m1, double m2, double v1, double v2, double n1, double n2) {
        return (m1 - m2) / FastMath.sqrt((v1 / n1) + (v2 / n2));
    }

    protected double homoscedasticT(double m1, double m2, double v1, double v2, double n1, double n2) {
        double pooledVariance = (((n1 - 1.0d) * v1) + ((n2 - 1.0d) * v2)) / ((n1 + n2) - 2.0d);
        return (m1 - m2) / FastMath.sqrt(pooledVariance * ((1.0d / n1) + (1.0d / n2)));
    }

    protected double tTest(double m2, double mu, double v2, double n2) throws MaxCountExceededException, MathIllegalArgumentException {
        double t2 = FastMath.abs(t(m2, mu, v2, n2));
        TDistribution distribution = new TDistribution((RandomGenerator) null, n2 - 1.0d);
        return 2.0d * distribution.cumulativeProbability(-t2);
    }

    protected double tTest(double m1, double m2, double v1, double v2, double n1, double n2) throws NotStrictlyPositiveException, MaxCountExceededException {
        double t2 = FastMath.abs(t(m1, m2, v1, v2, n1, n2));
        double degreesOfFreedom = df(v1, v2, n1, n2);
        TDistribution distribution = new TDistribution((RandomGenerator) null, degreesOfFreedom);
        return 2.0d * distribution.cumulativeProbability(-t2);
    }

    protected double homoscedasticTTest(double m1, double m2, double v1, double v2, double n1, double n2) throws NotStrictlyPositiveException, MaxCountExceededException {
        double t2 = FastMath.abs(homoscedasticT(m1, m2, v1, v2, n1, n2));
        double degreesOfFreedom = (n1 + n2) - 2.0d;
        TDistribution distribution = new TDistribution((RandomGenerator) null, degreesOfFreedom);
        return 2.0d * distribution.cumulativeProbability(-t2);
    }

    private void checkSignificanceLevel(double alpha) throws OutOfRangeException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Double.valueOf(0.0d), Double.valueOf(0.5d));
        }
    }

    private void checkSampleData(double[] data) throws NumberIsTooSmallException, NullArgumentException {
        if (data == null) {
            throw new NullArgumentException();
        }
        if (data.length < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DATA_FOR_T_STATISTIC, Integer.valueOf(data.length), 2, true);
        }
    }

    private void checkSampleData(StatisticalSummary stat) throws NumberIsTooSmallException, NullArgumentException {
        if (stat == null) {
            throw new NullArgumentException();
        }
        if (stat.getN() < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DATA_FOR_T_STATISTIC, Long.valueOf(stat.getN()), 2, true);
        }
    }
}
