package org.apache.commons.math3.stat.descriptive;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.ResizableDoubleArray;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/DescriptiveStatistics.class */
public class DescriptiveStatistics implements StatisticalSummary, Serializable {
    public static final int INFINITE_WINDOW = -1;
    private static final long serialVersionUID = 4133067267405273064L;
    private static final String SET_QUANTILE_METHOD_NAME = "setQuantile";
    protected int windowSize;
    private ResizableDoubleArray eDA;
    private UnivariateStatistic meanImpl;
    private UnivariateStatistic geometricMeanImpl;
    private UnivariateStatistic kurtosisImpl;
    private UnivariateStatistic maxImpl;
    private UnivariateStatistic minImpl;
    private UnivariateStatistic percentileImpl;
    private UnivariateStatistic skewnessImpl;
    private UnivariateStatistic varianceImpl;
    private UnivariateStatistic sumsqImpl;
    private UnivariateStatistic sumImpl;

    public DescriptiveStatistics() {
        this.windowSize = -1;
        this.eDA = new ResizableDoubleArray();
        this.meanImpl = new Mean();
        this.geometricMeanImpl = new GeometricMean();
        this.kurtosisImpl = new Kurtosis();
        this.maxImpl = new Max();
        this.minImpl = new Min();
        this.percentileImpl = new Percentile();
        this.skewnessImpl = new Skewness();
        this.varianceImpl = new Variance();
        this.sumsqImpl = new SumOfSquares();
        this.sumImpl = new Sum();
    }

    public DescriptiveStatistics(int window) throws MathIllegalArgumentException {
        this.windowSize = -1;
        this.eDA = new ResizableDoubleArray();
        this.meanImpl = new Mean();
        this.geometricMeanImpl = new GeometricMean();
        this.kurtosisImpl = new Kurtosis();
        this.maxImpl = new Max();
        this.minImpl = new Min();
        this.percentileImpl = new Percentile();
        this.skewnessImpl = new Skewness();
        this.varianceImpl = new Variance();
        this.sumsqImpl = new SumOfSquares();
        this.sumImpl = new Sum();
        setWindowSize(window);
    }

    public DescriptiveStatistics(double[] initialDoubleArray) {
        this.windowSize = -1;
        this.eDA = new ResizableDoubleArray();
        this.meanImpl = new Mean();
        this.geometricMeanImpl = new GeometricMean();
        this.kurtosisImpl = new Kurtosis();
        this.maxImpl = new Max();
        this.minImpl = new Min();
        this.percentileImpl = new Percentile();
        this.skewnessImpl = new Skewness();
        this.varianceImpl = new Variance();
        this.sumsqImpl = new SumOfSquares();
        this.sumImpl = new Sum();
        if (initialDoubleArray != null) {
            this.eDA = new ResizableDoubleArray(initialDoubleArray);
        }
    }

    public DescriptiveStatistics(DescriptiveStatistics original) throws NullArgumentException {
        this.windowSize = -1;
        this.eDA = new ResizableDoubleArray();
        this.meanImpl = new Mean();
        this.geometricMeanImpl = new GeometricMean();
        this.kurtosisImpl = new Kurtosis();
        this.maxImpl = new Max();
        this.minImpl = new Min();
        this.percentileImpl = new Percentile();
        this.skewnessImpl = new Skewness();
        this.varianceImpl = new Variance();
        this.sumsqImpl = new SumOfSquares();
        this.sumImpl = new Sum();
        copy(original, this);
    }

    public void addValue(double v2) {
        if (this.windowSize == -1) {
            this.eDA.addElement(v2);
        } else if (getN() == this.windowSize) {
            this.eDA.addElementRolling(v2);
        } else if (getN() < this.windowSize) {
            this.eDA.addElement(v2);
        }
    }

    public void removeMostRecentValue() throws MathIllegalStateException {
        try {
            this.eDA.discardMostRecentElements(1);
        } catch (MathIllegalArgumentException e2) {
            throw new MathIllegalStateException(LocalizedFormats.NO_DATA, new Object[0]);
        }
    }

    public double replaceMostRecentValue(double v2) throws MathIllegalStateException {
        return this.eDA.substituteMostRecentElement(v2);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMean() {
        return apply(this.meanImpl);
    }

    public double getGeometricMean() {
        return apply(this.geometricMeanImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getVariance() {
        return apply(this.varianceImpl);
    }

    public double getPopulationVariance() {
        return apply(new Variance(false));
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getStandardDeviation() {
        double stdDev = Double.NaN;
        if (getN() > 0) {
            if (getN() > 1) {
                stdDev = FastMath.sqrt(getVariance());
            } else {
                stdDev = 0.0d;
            }
        }
        return stdDev;
    }

    public double getQuadraticMean() {
        long n2 = getN();
        if (n2 > 0) {
            return FastMath.sqrt(getSumsq() / n2);
        }
        return Double.NaN;
    }

    public double getSkewness() {
        return apply(this.skewnessImpl);
    }

    public double getKurtosis() {
        return apply(this.kurtosisImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMax() {
        return apply(this.maxImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMin() {
        return apply(this.minImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public long getN() {
        return this.eDA.getNumElements();
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getSum() {
        return apply(this.sumImpl);
    }

    public double getSumsq() {
        return apply(this.sumsqImpl);
    }

    public void clear() {
        this.eDA.clear();
    }

    public int getWindowSize() {
        return this.windowSize;
    }

    public void setWindowSize(int windowSize) throws MathIllegalArgumentException {
        if (windowSize < 1 && windowSize != -1) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POSITIVE_WINDOW_SIZE, Integer.valueOf(windowSize));
        }
        this.windowSize = windowSize;
        if (windowSize != -1 && windowSize < this.eDA.getNumElements()) {
            this.eDA.discardFrontElements(this.eDA.getNumElements() - windowSize);
        }
    }

    public double[] getValues() {
        return this.eDA.getElements();
    }

    public double[] getSortedValues() {
        double[] sort = getValues();
        Arrays.sort(sort);
        return sort;
    }

    public double getElement(int index) {
        return this.eDA.getElement(index);
    }

    public double getPercentile(double p2) throws MathIllegalStateException, IllegalArgumentException {
        if (this.percentileImpl instanceof Percentile) {
            ((Percentile) this.percentileImpl).setQuantile(p2);
        } else {
            try {
                this.percentileImpl.getClass().getMethod(SET_QUANTILE_METHOD_NAME, Double.TYPE).invoke(this.percentileImpl, Double.valueOf(p2));
            } catch (IllegalAccessException e2) {
                throw new MathIllegalStateException(LocalizedFormats.PERCENTILE_IMPLEMENTATION_CANNOT_ACCESS_METHOD, SET_QUANTILE_METHOD_NAME, this.percentileImpl.getClass().getName());
            } catch (NoSuchMethodException e3) {
                throw new MathIllegalStateException(LocalizedFormats.PERCENTILE_IMPLEMENTATION_UNSUPPORTED_METHOD, this.percentileImpl.getClass().getName(), SET_QUANTILE_METHOD_NAME);
            } catch (InvocationTargetException e32) {
                throw new IllegalStateException(e32.getCause());
            }
        }
        return apply(this.percentileImpl);
    }

    public String toString() {
        StringBuilder outBuffer = new StringBuilder();
        outBuffer.append("DescriptiveStatistics:").append("\n");
        outBuffer.append("n: ").append(getN()).append("\n");
        outBuffer.append("min: ").append(getMin()).append("\n");
        outBuffer.append("max: ").append(getMax()).append("\n");
        outBuffer.append("mean: ").append(getMean()).append("\n");
        outBuffer.append("std dev: ").append(getStandardDeviation()).append("\n");
        try {
            outBuffer.append("median: ").append(getPercentile(50.0d)).append("\n");
        } catch (MathIllegalStateException e2) {
            outBuffer.append("median: unavailable").append("\n");
        }
        outBuffer.append("skewness: ").append(getSkewness()).append("\n");
        outBuffer.append("kurtosis: ").append(getKurtosis()).append("\n");
        return outBuffer.toString();
    }

    public double apply(UnivariateStatistic stat) {
        return this.eDA.compute(stat);
    }

    public synchronized UnivariateStatistic getMeanImpl() {
        return this.meanImpl;
    }

    public synchronized void setMeanImpl(UnivariateStatistic meanImpl) {
        this.meanImpl = meanImpl;
    }

    public synchronized UnivariateStatistic getGeometricMeanImpl() {
        return this.geometricMeanImpl;
    }

    public synchronized void setGeometricMeanImpl(UnivariateStatistic geometricMeanImpl) {
        this.geometricMeanImpl = geometricMeanImpl;
    }

    public synchronized UnivariateStatistic getKurtosisImpl() {
        return this.kurtosisImpl;
    }

    public synchronized void setKurtosisImpl(UnivariateStatistic kurtosisImpl) {
        this.kurtosisImpl = kurtosisImpl;
    }

    public synchronized UnivariateStatistic getMaxImpl() {
        return this.maxImpl;
    }

    public synchronized void setMaxImpl(UnivariateStatistic maxImpl) {
        this.maxImpl = maxImpl;
    }

    public synchronized UnivariateStatistic getMinImpl() {
        return this.minImpl;
    }

    public synchronized void setMinImpl(UnivariateStatistic minImpl) {
        this.minImpl = minImpl;
    }

    public synchronized UnivariateStatistic getPercentileImpl() {
        return this.percentileImpl;
    }

    public synchronized void setPercentileImpl(UnivariateStatistic percentileImpl) throws IllegalArgumentException {
        try {
            percentileImpl.getClass().getMethod(SET_QUANTILE_METHOD_NAME, Double.TYPE).invoke(percentileImpl, Double.valueOf(50.0d));
            this.percentileImpl = percentileImpl;
        } catch (IllegalAccessException e2) {
            throw new MathIllegalArgumentException(LocalizedFormats.PERCENTILE_IMPLEMENTATION_CANNOT_ACCESS_METHOD, SET_QUANTILE_METHOD_NAME, percentileImpl.getClass().getName());
        } catch (NoSuchMethodException e3) {
            throw new MathIllegalArgumentException(LocalizedFormats.PERCENTILE_IMPLEMENTATION_UNSUPPORTED_METHOD, percentileImpl.getClass().getName(), SET_QUANTILE_METHOD_NAME);
        } catch (InvocationTargetException e32) {
            throw new IllegalArgumentException(e32.getCause());
        }
    }

    public synchronized UnivariateStatistic getSkewnessImpl() {
        return this.skewnessImpl;
    }

    public synchronized void setSkewnessImpl(UnivariateStatistic skewnessImpl) {
        this.skewnessImpl = skewnessImpl;
    }

    public synchronized UnivariateStatistic getVarianceImpl() {
        return this.varianceImpl;
    }

    public synchronized void setVarianceImpl(UnivariateStatistic varianceImpl) {
        this.varianceImpl = varianceImpl;
    }

    public synchronized UnivariateStatistic getSumsqImpl() {
        return this.sumsqImpl;
    }

    public synchronized void setSumsqImpl(UnivariateStatistic sumsqImpl) {
        this.sumsqImpl = sumsqImpl;
    }

    public synchronized UnivariateStatistic getSumImpl() {
        return this.sumImpl;
    }

    public synchronized void setSumImpl(UnivariateStatistic sumImpl) {
        this.sumImpl = sumImpl;
    }

    public DescriptiveStatistics copy() throws NullArgumentException {
        DescriptiveStatistics result = new DescriptiveStatistics();
        copy(this, result);
        return result;
    }

    public static void copy(DescriptiveStatistics source, DescriptiveStatistics dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.eDA = source.eDA.copy();
        dest.windowSize = source.windowSize;
        dest.maxImpl = source.maxImpl.copy();
        dest.meanImpl = source.meanImpl.copy();
        dest.minImpl = source.minImpl.copy();
        dest.sumImpl = source.sumImpl.copy();
        dest.varianceImpl = source.varianceImpl.copy();
        dest.sumsqImpl = source.sumsqImpl.copy();
        dest.geometricMeanImpl = source.geometricMeanImpl.copy();
        dest.kurtosisImpl = source.kurtosisImpl;
        dest.skewnessImpl = source.skewnessImpl;
        dest.percentileImpl = source.percentileImpl;
    }
}
