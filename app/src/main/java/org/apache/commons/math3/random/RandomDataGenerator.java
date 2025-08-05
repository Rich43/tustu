package org.apache.commons.math3.random;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Collection;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.PascalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/RandomDataGenerator.class */
public class RandomDataGenerator implements RandomData, Serializable {
    private static final long serialVersionUID = -626730818244969716L;
    private RandomGenerator rand;
    private RandomGenerator secRand;

    public RandomDataGenerator() {
        this.rand = null;
        this.secRand = null;
    }

    public RandomDataGenerator(RandomGenerator rand) {
        this.rand = null;
        this.secRand = null;
        this.rand = rand;
    }

    @Override // org.apache.commons.math3.random.RandomData
    public String nextHexString(int len) throws NotStrictlyPositiveException {
        if (len <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(len));
        }
        RandomGenerator ran = getRandomGenerator();
        StringBuilder outBuffer = new StringBuilder();
        byte[] randomBytes = new byte[(len / 2) + 1];
        ran.nextBytes(randomBytes);
        for (byte b2 : randomBytes) {
            Integer c2 = Integer.valueOf(b2);
            String hex = Integer.toHexString(c2.intValue() + 128);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            outBuffer.append(hex);
        }
        return outBuffer.toString().substring(0, len);
    }

    @Override // org.apache.commons.math3.random.RandomData
    public int nextInt(int lower, int upper) throws NumberIsTooLargeException {
        return new UniformIntegerDistribution(getRandomGenerator(), lower, upper).sample();
    }

    @Override // org.apache.commons.math3.random.RandomData
    public long nextLong(long lower, long upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Long.valueOf(lower), Long.valueOf(upper), false);
        }
        long max = (upper - lower) + 1;
        if (max > 0) {
            if (max < 2147483647L) {
                return lower + getRandomGenerator().nextInt((int) max);
            }
            return lower + nextLong(getRandomGenerator(), max);
        }
        RandomGenerator rng = getRandomGenerator();
        while (true) {
            long r2 = rng.nextLong();
            if (r2 >= lower && r2 <= upper) {
                return r2;
            }
        }
    }

    private static long nextLong(RandomGenerator rng, long n2) throws IllegalArgumentException {
        long bits;
        long val;
        if (n2 > 0) {
            byte[] byteArray = new byte[8];
            do {
                rng.nextBytes(byteArray);
                long bits2 = 0;
                for (byte b2 : byteArray) {
                    bits2 = (bits2 << 8) | (b2 & 255);
                }
                bits = bits2 & Long.MAX_VALUE;
                val = bits % n2;
            } while ((bits - val) + (n2 - 1) < 0);
            return val;
        }
        throw new NotStrictlyPositiveException(Long.valueOf(n2));
    }

    @Override // org.apache.commons.math3.random.RandomData
    public String nextSecureHexString(int len) throws NotStrictlyPositiveException {
        if (len <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(len));
        }
        RandomGenerator secRan = getSecRan();
        try {
            MessageDigest alg = MessageDigest.getInstance("SHA-1");
            alg.reset();
            int numIter = (len / 40) + 1;
            StringBuilder outBuffer = new StringBuilder();
            for (int iter = 1; iter < numIter + 1; iter++) {
                byte[] randomBytes = new byte[40];
                secRan.nextBytes(randomBytes);
                alg.update(randomBytes);
                byte[] hash = alg.digest();
                for (byte b2 : hash) {
                    Integer c2 = Integer.valueOf(b2);
                    String hex = Integer.toHexString(c2.intValue() + 128);
                    if (hex.length() == 1) {
                        hex = "0" + hex;
                    }
                    outBuffer.append(hex);
                }
            }
            return outBuffer.toString().substring(0, len);
        } catch (NoSuchAlgorithmException ex) {
            throw new MathInternalError(ex);
        }
    }

    @Override // org.apache.commons.math3.random.RandomData
    public int nextSecureInt(int lower, int upper) throws NumberIsTooLargeException {
        return new UniformIntegerDistribution(getSecRan(), lower, upper).sample();
    }

    @Override // org.apache.commons.math3.random.RandomData
    public long nextSecureLong(long lower, long upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Long.valueOf(lower), Long.valueOf(upper), false);
        }
        RandomGenerator rng = getSecRan();
        long max = (upper - lower) + 1;
        if (max > 0) {
            if (max < 2147483647L) {
                return lower + rng.nextInt((int) max);
            }
            return lower + nextLong(rng, max);
        }
        while (true) {
            long r2 = rng.nextLong();
            if (r2 >= lower && r2 <= upper) {
                return r2;
            }
        }
    }

    @Override // org.apache.commons.math3.random.RandomData
    public long nextPoisson(double mean) throws NotStrictlyPositiveException {
        return new PoissonDistribution(getRandomGenerator(), mean, 1.0E-12d, PoissonDistribution.DEFAULT_MAX_ITERATIONS).sample();
    }

    @Override // org.apache.commons.math3.random.RandomData
    public double nextGaussian(double mu, double sigma) throws NotStrictlyPositiveException {
        if (sigma <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.STANDARD_DEVIATION, Double.valueOf(sigma));
        }
        return (sigma * getRandomGenerator().nextGaussian()) + mu;
    }

    @Override // org.apache.commons.math3.random.RandomData
    public double nextExponential(double mean) throws NotStrictlyPositiveException {
        return new ExponentialDistribution(getRandomGenerator(), mean, 1.0E-9d).sample();
    }

    public double nextGamma(double shape, double scale) throws NotStrictlyPositiveException {
        return new GammaDistribution(getRandomGenerator(), shape, scale, 1.0E-9d).sample();
    }

    public int nextHypergeometric(int populationSize, int numberOfSuccesses, int sampleSize) throws NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException {
        return new HypergeometricDistribution(getRandomGenerator(), populationSize, numberOfSuccesses, sampleSize).sample();
    }

    public int nextPascal(int r2, double p2) throws OutOfRangeException, NotStrictlyPositiveException {
        return new PascalDistribution(getRandomGenerator(), r2, p2).sample();
    }

    public double nextT(double df) throws NotStrictlyPositiveException {
        return new TDistribution(getRandomGenerator(), df, 1.0E-9d).sample();
    }

    public double nextWeibull(double shape, double scale) throws NotStrictlyPositiveException {
        return new WeibullDistribution(getRandomGenerator(), shape, scale, 1.0E-9d).sample();
    }

    public int nextZipf(int numberOfElements, double exponent) throws NotStrictlyPositiveException {
        return new ZipfDistribution(getRandomGenerator(), numberOfElements, exponent).sample();
    }

    public double nextBeta(double alpha, double beta) {
        return new BetaDistribution(getRandomGenerator(), alpha, beta, 1.0E-9d).sample();
    }

    public int nextBinomial(int numberOfTrials, double probabilityOfSuccess) {
        return new BinomialDistribution(getRandomGenerator(), numberOfTrials, probabilityOfSuccess).sample();
    }

    public double nextCauchy(double median, double scale) {
        return new CauchyDistribution(getRandomGenerator(), median, scale, 1.0E-9d).sample();
    }

    public double nextChiSquare(double df) {
        return new ChiSquaredDistribution(getRandomGenerator(), df, 1.0E-9d).sample();
    }

    public double nextF(double numeratorDf, double denominatorDf) throws NotStrictlyPositiveException {
        return new FDistribution(getRandomGenerator(), numeratorDf, denominatorDf, 1.0E-9d).sample();
    }

    @Override // org.apache.commons.math3.random.RandomData
    public double nextUniform(double lower, double upper) throws NotANumberException, NotFiniteNumberException, NumberIsTooLargeException {
        return nextUniform(lower, upper, false);
    }

    @Override // org.apache.commons.math3.random.RandomData
    public double nextUniform(double lower, double upper, boolean lowerInclusive) throws NotANumberException, NotFiniteNumberException, NumberIsTooLargeException {
        double u2;
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(lower), Double.valueOf(upper), false);
        }
        if (Double.isInfinite(lower)) {
            throw new NotFiniteNumberException(LocalizedFormats.INFINITE_BOUND, Double.valueOf(lower), new Object[0]);
        }
        if (Double.isInfinite(upper)) {
            throw new NotFiniteNumberException(LocalizedFormats.INFINITE_BOUND, Double.valueOf(upper), new Object[0]);
        }
        if (Double.isNaN(lower) || Double.isNaN(upper)) {
            throw new NotANumberException();
        }
        RandomGenerator generator = getRandomGenerator();
        double dNextDouble = generator.nextDouble();
        while (true) {
            u2 = dNextDouble;
            if (lowerInclusive || u2 > 0.0d) {
                break;
            }
            dNextDouble = generator.nextDouble();
        }
        return (u2 * upper) + ((1.0d - u2) * lower);
    }

    @Override // org.apache.commons.math3.random.RandomData
    public int[] nextPermutation(int n2, int k2) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        if (k2 > n2) {
            throw new NumberIsTooLargeException(LocalizedFormats.PERMUTATION_EXCEEDS_N, Integer.valueOf(k2), Integer.valueOf(n2), true);
        }
        if (k2 <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.PERMUTATION_SIZE, Integer.valueOf(k2));
        }
        int[] index = MathArrays.natural(n2);
        MathArrays.shuffle(index, getRandomGenerator());
        return MathArrays.copyOf(index, k2);
    }

    @Override // org.apache.commons.math3.random.RandomData
    public Object[] nextSample(Collection<?> c2, int k2) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        int len = c2.size();
        if (k2 > len) {
            throw new NumberIsTooLargeException(LocalizedFormats.SAMPLE_SIZE_EXCEEDS_COLLECTION_SIZE, Integer.valueOf(k2), Integer.valueOf(len), true);
        }
        if (k2 <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(k2));
        }
        Object[] objects = c2.toArray();
        int[] index = nextPermutation(len, k2);
        Object[] result = new Object[k2];
        for (int i2 = 0; i2 < k2; i2++) {
            result[i2] = objects[index[i2]];
        }
        return result;
    }

    public void reSeed(long seed) {
        getRandomGenerator().setSeed(seed);
    }

    public void reSeedSecure() {
        getSecRan().setSeed(System.currentTimeMillis());
    }

    public void reSeedSecure(long seed) {
        getSecRan().setSeed(seed);
    }

    public void reSeed() {
        getRandomGenerator().setSeed(System.currentTimeMillis() + System.identityHashCode(this));
    }

    public void setSecureAlgorithm(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.secRand = RandomGeneratorFactory.createRandomGenerator(SecureRandom.getInstance(algorithm, provider));
    }

    public RandomGenerator getRandomGenerator() {
        if (this.rand == null) {
            initRan();
        }
        return this.rand;
    }

    private void initRan() {
        this.rand = new Well19937c(System.currentTimeMillis() + System.identityHashCode(this));
    }

    private RandomGenerator getSecRan() {
        if (this.secRand == null) {
            this.secRand = RandomGeneratorFactory.createRandomGenerator(new SecureRandom());
            this.secRand.setSeed(System.currentTimeMillis() + System.identityHashCode(this));
        }
        return this.secRand;
    }
}
