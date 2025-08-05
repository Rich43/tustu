package org.apache.commons.math3.stat.inference;

import java.util.Arrays;
import java.util.HashSet;
import org.apache.commons.math3.distribution.EnumeratedRealDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.BigFractionField;
import org.apache.commons.math3.fraction.FractionConversionException;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/inference/KolmogorovSmirnovTest.class */
public class KolmogorovSmirnovTest {
    protected static final int MAXIMUM_PARTIAL_SUM_COUNT = 100000;
    protected static final double KS_SUM_CAUCHY_CRITERION = 1.0E-20d;
    protected static final double PG_SUM_RELATIVE_ERROR = 1.0E-10d;

    @Deprecated
    protected static final int SMALL_SAMPLE_PRODUCT = 200;
    protected static final int LARGE_SAMPLE_PRODUCT = 10000;

    @Deprecated
    protected static final int MONTE_CARLO_ITERATIONS = 1000000;
    private final RandomGenerator rng;

    public KolmogorovSmirnovTest() {
        this.rng = new Well19937c();
    }

    @Deprecated
    public KolmogorovSmirnovTest(RandomGenerator rng) {
        this.rng = rng;
    }

    public double kolmogorovSmirnovTest(RealDistribution distribution, double[] data, boolean exact) {
        return 1.0d - cdf(kolmogorovSmirnovStatistic(distribution, data), data.length, exact);
    }

    public double kolmogorovSmirnovStatistic(RealDistribution distribution, double[] data) {
        checkArray(data);
        int n2 = data.length;
        double nd = n2;
        double[] dataCopy = new double[n2];
        System.arraycopy(data, 0, dataCopy, 0, n2);
        Arrays.sort(dataCopy);
        double d2 = 0.0d;
        for (int i2 = 1; i2 <= n2; i2++) {
            double yi = distribution.cumulativeProbability(dataCopy[i2 - 1]);
            double currD = FastMath.max(yi - ((i2 - 1) / nd), (i2 / nd) - yi);
            if (currD > d2) {
                d2 = currD;
            }
        }
        return d2;
    }

    public double kolmogorovSmirnovTest(double[] x2, double[] y2, boolean strict) {
        double[] xa;
        double[] ya;
        long lengthProduct = x2.length * y2.length;
        if (lengthProduct < 10000 && hasTies(x2, y2)) {
            xa = MathArrays.copyOf(x2);
            ya = MathArrays.copyOf(y2);
            fixTies(xa, ya);
        } else {
            xa = x2;
            ya = y2;
        }
        if (lengthProduct < 10000) {
            return exactP(kolmogorovSmirnovStatistic(xa, ya), x2.length, y2.length, strict);
        }
        return approximateP(kolmogorovSmirnovStatistic(x2, y2), x2.length, y2.length);
    }

    public double kolmogorovSmirnovTest(double[] x2, double[] y2) {
        return kolmogorovSmirnovTest(x2, y2, true);
    }

    public double kolmogorovSmirnovStatistic(double[] x2, double[] y2) {
        return integralKolmogorovSmirnovStatistic(x2, y2) / (x2.length * y2.length);
    }

    private long integralKolmogorovSmirnovStatistic(double[] x2, double[] y2) {
        checkArray(x2);
        checkArray(y2);
        double[] sx = MathArrays.copyOf(x2);
        double[] sy = MathArrays.copyOf(y2);
        Arrays.sort(sx);
        Arrays.sort(sy);
        int n2 = sx.length;
        int m2 = sy.length;
        int rankX = 0;
        int rankY = 0;
        long curD = 0;
        long supD = 0;
        do {
            double z2 = Double.compare(sx[rankX], sy[rankY]) <= 0 ? sx[rankX] : sy[rankY];
            while (rankX < n2 && Double.compare(sx[rankX], z2) == 0) {
                rankX++;
                curD += m2;
            }
            while (rankY < m2 && Double.compare(sy[rankY], z2) == 0) {
                rankY++;
                curD -= n2;
            }
            if (curD > supD) {
                supD = curD;
            } else if ((-curD) > supD) {
                supD = -curD;
            }
            if (rankX >= n2) {
                break;
            }
        } while (rankY < m2);
        return supD;
    }

    public double kolmogorovSmirnovTest(RealDistribution distribution, double[] data) {
        return kolmogorovSmirnovTest(distribution, data, false);
    }

    public boolean kolmogorovSmirnovTest(RealDistribution distribution, double[] data, double alpha) {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), 0, Double.valueOf(0.5d));
        }
        return kolmogorovSmirnovTest(distribution, data) < alpha;
    }

    public double bootstrap(double[] x2, double[] y2, int iterations, boolean strict) {
        int xLength = x2.length;
        int yLength = y2.length;
        double[] combined = new double[xLength + yLength];
        System.arraycopy(x2, 0, combined, 0, xLength);
        System.arraycopy(y2, 0, combined, xLength, yLength);
        EnumeratedRealDistribution dist = new EnumeratedRealDistribution(this.rng, combined);
        long d2 = integralKolmogorovSmirnovStatistic(x2, y2);
        int greaterCount = 0;
        int equalCount = 0;
        for (int i2 = 0; i2 < iterations; i2++) {
            double[] curX = dist.sample(xLength);
            double[] curY = dist.sample(yLength);
            long curD = integralKolmogorovSmirnovStatistic(curX, curY);
            if (curD > d2) {
                greaterCount++;
            } else if (curD == d2) {
                equalCount++;
            }
        }
        return strict ? greaterCount / iterations : (greaterCount + equalCount) / iterations;
    }

    public double bootstrap(double[] x2, double[] y2, int iterations) {
        return bootstrap(x2, y2, iterations, true);
    }

    public double cdf(double d2, int n2) throws MathArithmeticException {
        return cdf(d2, n2, false);
    }

    public double cdfExact(double d2, int n2) throws MathArithmeticException {
        return cdf(d2, n2, true);
    }

    public double cdf(double d2, int n2, boolean exact) throws MathArithmeticException {
        double ninv = 1.0d / n2;
        double ninvhalf = 0.5d * ninv;
        if (d2 <= ninvhalf) {
            return 0.0d;
        }
        if (ninvhalf < d2 && d2 <= ninv) {
            double res = 1.0d;
            double f2 = (2.0d * d2) - ninv;
            for (int i2 = 1; i2 <= n2; i2++) {
                res *= i2 * f2;
            }
            return res;
        }
        if (1.0d - ninv <= d2 && d2 < 1.0d) {
            return 1.0d - (2.0d * Math.pow(1.0d - d2, n2));
        }
        if (1.0d <= d2) {
            return 1.0d;
        }
        if (exact) {
            return exactK(d2, n2);
        }
        if (n2 <= 140) {
            return roundedK(d2, n2);
        }
        return pelzGood(d2, n2);
    }

    private double exactK(double d2, int n2) throws FractionConversionException, NumberIsTooLargeException, MathArithmeticException {
        int k2 = (int) Math.ceil(n2 * d2);
        FieldMatrix<BigFraction> H2 = createExactH(d2, n2);
        BigFraction pFrac = (BigFraction) H2.power(n2).getEntry(k2 - 1, k2 - 1);
        for (int i2 = 1; i2 <= n2; i2++) {
            pFrac = pFrac.multiply(i2).divide(n2);
        }
        return pFrac.bigDecimalValue(20, 4).doubleValue();
    }

    private double roundedK(double d2, int n2) throws OutOfRangeException, NotPositiveException, NonSquareMatrixException, NumberIsTooLargeException {
        int k2 = (int) Math.ceil(n2 * d2);
        RealMatrix H2 = createRoundedH(d2, n2);
        RealMatrix Hpower = H2.power(n2);
        double pFrac = Hpower.getEntry(k2 - 1, k2 - 1);
        for (int i2 = 1; i2 <= n2; i2++) {
            pFrac *= i2 / n2;
        }
        return pFrac;
    }

    public double pelzGood(double d2, int n2) {
        double sqrtN = FastMath.sqrt(n2);
        double z2 = d2 * sqrtN;
        double z22 = d2 * d2 * n2;
        double z4 = z22 * z22;
        double z6 = z4 * z22;
        double z8 = z4 * z4;
        double sum = 0.0d;
        double z2Term = 9.869604401089358d / (8.0d * z22);
        int k2 = 1;
        while (k2 < 100000) {
            double kTerm = (2 * k2) - 1;
            double increment = FastMath.exp((-z2Term) * kTerm * kTerm);
            sum += increment;
            if (increment <= 1.0E-10d * sum) {
                break;
            }
            k2++;
        }
        if (k2 == 100000) {
            throw new TooManyIterationsException(100000);
        }
        double ret = (sum * FastMath.sqrt(6.283185307179586d)) / z2;
        double twoZ2 = 2.0d * z22;
        double sum2 = 0.0d;
        int k3 = 0;
        while (k3 < 100000) {
            double kTerm2 = k3 + 0.5d;
            double kTerm22 = kTerm2 * kTerm2;
            double increment2 = ((9.869604401089358d * kTerm22) - z22) * FastMath.exp(((-9.869604401089358d) * kTerm22) / twoZ2);
            sum2 += increment2;
            if (FastMath.abs(increment2) < 1.0E-10d * FastMath.abs(sum2)) {
                break;
            }
            k3++;
        }
        if (k3 == 100000) {
            throw new TooManyIterationsException(100000);
        }
        double sqrtHalfPi = FastMath.sqrt(1.5707963267948966d);
        double ret2 = ret + ((sum2 * sqrtHalfPi) / ((3.0d * z4) * sqrtN));
        double z4Term = 2.0d * z4;
        double z6Term = 6.0d * z6;
        double z2Term2 = 5.0d * z22;
        double sum3 = 0.0d;
        int k4 = 0;
        while (k4 < 100000) {
            double kTerm3 = k4 + 0.5d;
            double kTerm23 = kTerm3 * kTerm3;
            double increment3 = (z6Term + z4Term + (9.869604401089358d * (z4Term - z2Term2) * kTerm23) + (97.40909103400243d * (1.0d - twoZ2) * kTerm23 * kTerm23)) * FastMath.exp(((-9.869604401089358d) * kTerm23) / twoZ2);
            sum3 += increment3;
            if (FastMath.abs(increment3) < 1.0E-10d * FastMath.abs(sum3)) {
                break;
            }
            k4++;
        }
        if (k4 == 100000) {
            throw new TooManyIterationsException(100000);
        }
        double sum22 = 0.0d;
        int k5 = 1;
        while (k5 < 100000) {
            double kTerm24 = k5 * k5;
            double increment4 = 9.869604401089358d * kTerm24 * FastMath.exp(((-9.869604401089358d) * kTerm24) / twoZ2);
            sum22 += increment4;
            if (FastMath.abs(increment4) < 1.0E-10d * FastMath.abs(sum22)) {
                break;
            }
            k5++;
        }
        if (k5 == 100000) {
            throw new TooManyIterationsException(100000);
        }
        double ret3 = ret2 + ((sqrtHalfPi / n2) * ((sum3 / ((((36.0d * z22) * z22) * z22) * z2)) - (sum22 / ((18.0d * z22) * z2))));
        double sum4 = 0.0d;
        int k6 = 0;
        while (k6 < 100000) {
            double kTerm4 = k6 + 0.5d;
            double kTerm25 = kTerm4 * kTerm4;
            double kTerm42 = kTerm25 * kTerm25;
            double kTerm6 = kTerm42 * kTerm25;
            double increment5 = ((((((961.3891935753043d * kTerm6) * (5.0d - (30.0d * z22))) + ((97.40909103400243d * kTerm42) * (((-60.0d) * z22) + (212.0d * z4)))) + ((9.869604401089358d * kTerm25) * ((135.0d * z4) - (96.0d * z6)))) - (30.0d * z6)) - (90.0d * z8)) * FastMath.exp(((-9.869604401089358d) * kTerm25) / twoZ2);
            sum4 += increment5;
            if (FastMath.abs(increment5) < 1.0E-10d * FastMath.abs(sum4)) {
                break;
            }
            k6++;
        }
        if (k6 == 100000) {
            throw new TooManyIterationsException(100000);
        }
        double sum23 = 0.0d;
        int k7 = 1;
        while (k7 < 100000) {
            double kTerm26 = k7 * k7;
            double increment6 = (((-97.40909103400243d) * kTerm26 * kTerm26) + (29.608813203268074d * kTerm26 * z22)) * FastMath.exp(((-9.869604401089358d) * kTerm26) / twoZ2);
            sum23 += increment6;
            if (FastMath.abs(increment6) < 1.0E-10d * FastMath.abs(sum23)) {
                break;
            }
            k7++;
        }
        if (k7 == 100000) {
            throw new TooManyIterationsException(100000);
        }
        return ret3 + ((sqrtHalfPi / (sqrtN * n2)) * ((sum4 / ((3240.0d * z6) * z4)) + (sum23 / (108.0d * z6))));
    }

    private FieldMatrix<BigFraction> createExactH(double d2, int n2) throws FractionConversionException, NumberIsTooLargeException {
        BigFraction h2;
        int k2 = (int) Math.ceil(n2 * d2);
        int m2 = (2 * k2) - 1;
        double hDouble = k2 - (n2 * d2);
        if (hDouble >= 1.0d) {
            throw new NumberIsTooLargeException(Double.valueOf(hDouble), Double.valueOf(1.0d), false);
        }
        try {
            h2 = new BigFraction(hDouble, KS_SUM_CAUCHY_CRITERION, 10000);
        } catch (FractionConversionException e2) {
            try {
                h2 = new BigFraction(hDouble, 1.0E-10d, 10000);
            } catch (FractionConversionException e3) {
                h2 = new BigFraction(hDouble, 1.0E-5d, 10000);
            }
        }
        BigFraction[][] Hdata = new BigFraction[m2][m2];
        for (int i2 = 0; i2 < m2; i2++) {
            for (int j2 = 0; j2 < m2; j2++) {
                if ((i2 - j2) + 1 < 0) {
                    Hdata[i2][j2] = BigFraction.ZERO;
                } else {
                    Hdata[i2][j2] = BigFraction.ONE;
                }
            }
        }
        BigFraction[] hPowers = new BigFraction[m2];
        hPowers[0] = h2;
        for (int i3 = 1; i3 < m2; i3++) {
            hPowers[i3] = h2.multiply(hPowers[i3 - 1]);
        }
        for (int i4 = 0; i4 < m2; i4++) {
            Hdata[i4][0] = Hdata[i4][0].subtract(hPowers[i4]);
            Hdata[m2 - 1][i4] = Hdata[m2 - 1][i4].subtract(hPowers[(m2 - i4) - 1]);
        }
        if (h2.compareTo(BigFraction.ONE_HALF) == 1) {
            Hdata[m2 - 1][0] = Hdata[m2 - 1][0].add(h2.multiply(2).subtract(1).pow(m2));
        }
        for (int i5 = 0; i5 < m2; i5++) {
            for (int j3 = 0; j3 < i5 + 1; j3++) {
                if ((i5 - j3) + 1 > 0) {
                    for (int g2 = 2; g2 <= (i5 - j3) + 1; g2++) {
                        Hdata[i5][j3] = Hdata[i5][j3].divide(g2);
                    }
                }
            }
        }
        return new Array2DRowFieldMatrix(BigFractionField.getInstance(), Hdata);
    }

    private RealMatrix createRoundedH(double d2, int n2) throws NumberIsTooLargeException {
        int k2 = (int) Math.ceil(n2 * d2);
        int m2 = (2 * k2) - 1;
        double h2 = k2 - (n2 * d2);
        if (h2 >= 1.0d) {
            throw new NumberIsTooLargeException(Double.valueOf(h2), Double.valueOf(1.0d), false);
        }
        double[][] Hdata = new double[m2][m2];
        for (int i2 = 0; i2 < m2; i2++) {
            for (int j2 = 0; j2 < m2; j2++) {
                if ((i2 - j2) + 1 < 0) {
                    Hdata[i2][j2] = 0.0d;
                } else {
                    Hdata[i2][j2] = 1.0d;
                }
            }
        }
        double[] hPowers = new double[m2];
        hPowers[0] = h2;
        for (int i3 = 1; i3 < m2; i3++) {
            hPowers[i3] = h2 * hPowers[i3 - 1];
        }
        for (int i4 = 0; i4 < m2; i4++) {
            Hdata[i4][0] = Hdata[i4][0] - hPowers[i4];
            double[] dArr = Hdata[m2 - 1];
            int i5 = i4;
            dArr[i5] = dArr[i5] - hPowers[(m2 - i4) - 1];
        }
        if (Double.compare(h2, 0.5d) > 0) {
            double[] dArr2 = Hdata[m2 - 1];
            dArr2[0] = dArr2[0] + FastMath.pow((2.0d * h2) - 1.0d, m2);
        }
        for (int i6 = 0; i6 < m2; i6++) {
            for (int j3 = 0; j3 < i6 + 1; j3++) {
                if ((i6 - j3) + 1 > 0) {
                    for (int g2 = 2; g2 <= (i6 - j3) + 1; g2++) {
                        double[] dArr3 = Hdata[i6];
                        int i7 = j3;
                        dArr3[i7] = dArr3[i7] / g2;
                    }
                }
            }
        }
        return MatrixUtils.createRealMatrix(Hdata);
    }

    private void checkArray(double[] array) {
        if (array == null) {
            throw new NullArgumentException(LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
        }
        if (array.length < 2) {
            throw new InsufficientDataException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(array.length), 2);
        }
    }

    public double ksSum(double t2, double tolerance, int maxIterations) {
        if (t2 == 0.0d) {
            return 0.0d;
        }
        double x2 = (-2.0d) * t2 * t2;
        int sign = -1;
        long i2 = 1;
        double partialSum = 0.5d;
        double delta = 1.0d;
        while (delta > tolerance && i2 < maxIterations) {
            delta = FastMath.exp(x2 * i2 * i2);
            partialSum += sign * delta;
            sign *= -1;
            i2++;
        }
        if (i2 == maxIterations) {
            throw new TooManyIterationsException(Integer.valueOf(maxIterations));
        }
        return partialSum * 2.0d;
    }

    private static long calculateIntegralD(double d2, int n2, int m2, boolean strict) {
        long nm = n2 * m2;
        long upperBound = (long) FastMath.ceil((d2 - 1.0E-12d) * nm);
        long lowerBound = (long) FastMath.floor((d2 + 1.0E-12d) * nm);
        if (strict && lowerBound == upperBound) {
            return upperBound + 1;
        }
        return upperBound;
    }

    public double exactP(double d2, int n2, int m2, boolean strict) {
        return 1.0d - (n(m2, n2, m2, n2, calculateIntegralD(d2, m2, n2, strict), strict) / CombinatoricsUtils.binomialCoefficientDouble(n2 + m2, m2));
    }

    public double approximateP(double d2, int n2, int m2) {
        double dm = m2;
        double dn = n2;
        return 1.0d - ksSum(d2 * FastMath.sqrt((dm * dn) / (dm + dn)), KS_SUM_CAUCHY_CRITERION, 100000);
    }

    static void fillBooleanArrayRandomlyWithFixedNumberTrueValues(boolean[] b2, int numberOfTrueValues, RandomGenerator rng) {
        Arrays.fill(b2, true);
        for (int k2 = numberOfTrueValues; k2 < b2.length; k2++) {
            int r2 = rng.nextInt(k2 + 1);
            b2[b2[r2] ? r2 : k2] = false;
        }
    }

    public double monteCarloP(double d2, int n2, int m2, boolean strict, int iterations) {
        return integralMonteCarloP(calculateIntegralD(d2, n2, m2, strict), n2, m2, iterations);
    }

    private double integralMonteCarloP(long d2, int n2, int m2, int iterations) {
        int nn = FastMath.max(n2, m2);
        int mm = FastMath.min(n2, m2);
        int sum = nn + mm;
        int tail = 0;
        boolean[] b2 = new boolean[sum];
        for (int i2 = 0; i2 < iterations; i2++) {
            fillBooleanArrayRandomlyWithFixedNumberTrueValues(b2, nn, this.rng);
            long curD = 0;
            int j2 = 0;
            while (true) {
                if (j2 >= b2.length) {
                    break;
                }
                if (b2[j2]) {
                    curD += mm;
                    if (curD < d2) {
                        j2++;
                    } else {
                        tail++;
                        break;
                    }
                } else {
                    curD -= nn;
                    if (curD > (-d2)) {
                        j2++;
                    } else {
                        tail++;
                        break;
                    }
                }
            }
        }
        return tail / iterations;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [double[], double[][]] */
    private static void fixTies(double[] x2, double[] y2) {
        boolean ties;
        double[] values = MathArrays.unique(MathArrays.concatenate(new double[]{x2, y2}));
        if (values.length == x2.length + y2.length) {
            return;
        }
        double minDelta = 1.0d;
        double prev = values[0];
        for (int i2 = 1; i2 < values.length; i2++) {
            double delta = prev - values[i2];
            if (delta < minDelta) {
                minDelta = delta;
            }
            prev = values[i2];
        }
        double minDelta2 = minDelta / 2.0d;
        RealDistribution dist = new UniformRealDistribution(new JDKRandomGenerator(100), -minDelta2, minDelta2);
        int ct = 0;
        do {
            jitter(x2, dist);
            jitter(y2, dist);
            ties = hasTies(x2, y2);
            ct++;
            if (!ties) {
                break;
            }
        } while (ct < 1000);
        if (ties) {
            throw new MathInternalError();
        }
    }

    private static boolean hasTies(double[] x2, double[] y2) {
        HashSet<Double> values = new HashSet<>();
        for (double d2 : x2) {
            if (!values.add(Double.valueOf(d2))) {
                return true;
            }
        }
        for (double d3 : y2) {
            if (!values.add(Double.valueOf(d3))) {
                return true;
            }
        }
        return false;
    }

    private static void jitter(double[] data, RealDistribution dist) {
        for (int i2 = 0; i2 < data.length; i2++) {
            int i3 = i2;
            data[i3] = data[i3] + dist.sample();
        }
    }

    private static int c(int i2, int j2, int m2, int n2, long cmn, boolean strict) {
        return strict ? FastMath.abs((((long) i2) * ((long) n2)) - (((long) j2) * ((long) m2))) <= cmn ? 1 : 0 : FastMath.abs((((long) i2) * ((long) n2)) - (((long) j2) * ((long) m2))) < cmn ? 1 : 0;
    }

    private static double n(int i2, int j2, int m2, int n2, long cnm, boolean strict) {
        double[] lag = new double[n2];
        double last = 0.0d;
        for (int k2 = 0; k2 < n2; k2++) {
            lag[k2] = c(0, k2 + 1, m2, n2, cnm, strict);
        }
        for (int k3 = 1; k3 <= i2; k3++) {
            last = c(k3, 0, m2, n2, cnm, strict);
            for (int l2 = 1; l2 <= j2; l2++) {
                lag[l2 - 1] = c(k3, l2, m2, n2, cnm, strict) * (last + lag[l2 - 1]);
                last = lag[l2 - 1];
            }
        }
        return last;
    }
}
