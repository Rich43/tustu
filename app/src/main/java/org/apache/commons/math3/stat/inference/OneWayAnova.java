package org.apache.commons.math3.stat.inference;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/inference/OneWayAnova.class */
public class OneWayAnova {
    public double anovaFValue(Collection<double[]> categoryData) throws NullArgumentException, DimensionMismatchException {
        AnovaStats a2 = anovaStats(categoryData);
        return a2.f13108F;
    }

    public double anovaPValue(Collection<double[]> categoryData) throws NullArgumentException, ConvergenceException, DimensionMismatchException, MaxCountExceededException {
        AnovaStats a2 = anovaStats(categoryData);
        FDistribution fdist = new FDistribution((RandomGenerator) null, a2.dfbg, a2.dfwg);
        return 1.0d - fdist.cumulativeProbability(a2.f13108F);
    }

    public double anovaPValue(Collection<SummaryStatistics> categoryData, boolean allowOneElementData) throws NullArgumentException, ConvergenceException, DimensionMismatchException, MaxCountExceededException {
        AnovaStats a2 = anovaStats(categoryData, allowOneElementData);
        FDistribution fdist = new FDistribution((RandomGenerator) null, a2.dfbg, a2.dfwg);
        return 1.0d - fdist.cumulativeProbability(a2.f13108F);
    }

    private AnovaStats anovaStats(Collection<double[]> categoryData) throws NullArgumentException, DimensionMismatchException {
        MathUtils.checkNotNull(categoryData);
        Collection<SummaryStatistics> categoryDataSummaryStatistics = new ArrayList<>(categoryData.size());
        for (double[] data : categoryData) {
            SummaryStatistics dataSummaryStatistics = new SummaryStatistics();
            categoryDataSummaryStatistics.add(dataSummaryStatistics);
            for (double val : data) {
                dataSummaryStatistics.addValue(val);
            }
        }
        return anovaStats(categoryDataSummaryStatistics, false);
    }

    public boolean anovaTest(Collection<double[]> categoryData, double alpha) throws OutOfRangeException, NullArgumentException, ConvergenceException, DimensionMismatchException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), 0, Double.valueOf(0.5d));
        }
        return anovaPValue(categoryData) < alpha;
    }

    private AnovaStats anovaStats(Collection<SummaryStatistics> categoryData, boolean allowOneElementData) throws NullArgumentException, DimensionMismatchException {
        MathUtils.checkNotNull(categoryData);
        if (!allowOneElementData) {
            if (categoryData.size() < 2) {
                throw new DimensionMismatchException(LocalizedFormats.TWO_OR_MORE_CATEGORIES_REQUIRED, categoryData.size(), 2);
            }
            for (SummaryStatistics array : categoryData) {
                if (array.getN() <= 1) {
                    throw new DimensionMismatchException(LocalizedFormats.TWO_OR_MORE_VALUES_IN_CATEGORY_REQUIRED, (int) array.getN(), 2);
                }
            }
        }
        int dfwg = 0;
        double sswg = 0.0d;
        double totsum = 0.0d;
        double totsumsq = 0.0d;
        int totnum = 0;
        for (SummaryStatistics data : categoryData) {
            double sum = data.getSum();
            double sumsq = data.getSumsq();
            int num = (int) data.getN();
            totnum += num;
            totsum += sum;
            totsumsq += sumsq;
            dfwg += num - 1;
            double ss = sumsq - ((sum * sum) / num);
            sswg += ss;
        }
        double sst = totsumsq - ((totsum * totsum) / totnum);
        double ssbg = sst - sswg;
        int dfbg = categoryData.size() - 1;
        double msbg = ssbg / dfbg;
        double mswg = sswg / dfwg;
        double F2 = msbg / mswg;
        return new AnovaStats(dfbg, dfwg, F2);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/inference/OneWayAnova$AnovaStats.class */
    private static class AnovaStats {
        private final int dfbg;
        private final int dfwg;

        /* renamed from: F, reason: collision with root package name */
        private final double f13108F;

        private AnovaStats(int dfbg, int dfwg, double F2) {
            this.dfbg = dfbg;
            this.dfwg = dfwg;
            this.f13108F = F2;
        }
    }
}
