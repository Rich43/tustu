package org.apache.commons.math3.distribution;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/EnumeratedDistribution.class */
public class EnumeratedDistribution<T> implements Serializable {
    private static final long serialVersionUID = 20123308;
    protected final RandomGenerator random;
    private final List<T> singletons;
    private final double[] probabilities;
    private final double[] cumulativeProbabilities;

    public EnumeratedDistribution(List<Pair<T, Double>> pmf) throws NotPositiveException, NotANumberException, NotFiniteNumberException, MathArithmeticException {
        this(new Well19937c(), pmf);
    }

    public EnumeratedDistribution(RandomGenerator rng, List<Pair<T, Double>> pmf) throws NotPositiveException, NotANumberException, NotFiniteNumberException, MathArithmeticException {
        this.random = rng;
        this.singletons = new ArrayList(pmf.size());
        double[] probs = new double[pmf.size()];
        for (int i2 = 0; i2 < pmf.size(); i2++) {
            Pair<T, Double> sample = pmf.get(i2);
            this.singletons.add(sample.getKey());
            double p2 = sample.getValue().doubleValue();
            if (p2 < 0.0d) {
                throw new NotPositiveException(sample.getValue());
            }
            if (Double.isInfinite(p2)) {
                throw new NotFiniteNumberException(Double.valueOf(p2), new Object[0]);
            }
            if (Double.isNaN(p2)) {
                throw new NotANumberException();
            }
            probs[i2] = p2;
        }
        this.probabilities = MathArrays.normalizeArray(probs, 1.0d);
        this.cumulativeProbabilities = new double[this.probabilities.length];
        double sum = 0.0d;
        for (int i3 = 0; i3 < this.probabilities.length; i3++) {
            sum += this.probabilities[i3];
            this.cumulativeProbabilities[i3] = sum;
        }
    }

    public void reseedRandomGenerator(long seed) {
        this.random.setSeed(seed);
    }

    double probability(T x2) {
        double probability = 0.0d;
        for (int i2 = 0; i2 < this.probabilities.length; i2++) {
            if ((x2 == null && this.singletons.get(i2) == null) || (x2 != null && x2.equals(this.singletons.get(i2)))) {
                probability += this.probabilities[i2];
            }
        }
        return probability;
    }

    public List<Pair<T, Double>> getPmf() {
        List<Pair<T, Double>> samples = new ArrayList<>(this.probabilities.length);
        for (int i2 = 0; i2 < this.probabilities.length; i2++) {
            samples.add(new Pair<>(this.singletons.get(i2), Double.valueOf(this.probabilities[i2])));
        }
        return samples;
    }

    public T sample() {
        double randomValue = this.random.nextDouble();
        int index = Arrays.binarySearch(this.cumulativeProbabilities, randomValue);
        if (index < 0) {
            index = (-index) - 1;
        }
        if (index >= 0 && index < this.probabilities.length && randomValue < this.cumulativeProbabilities[index]) {
            return this.singletons.get(index);
        }
        return this.singletons.get(this.singletons.size() - 1);
    }

    public Object[] sample(int sampleSize) throws NotStrictlyPositiveException {
        if (sampleSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
        }
        Object[] out = new Object[sampleSize];
        for (int i2 = 0; i2 < sampleSize; i2++) {
            out[i2] = sample();
        }
        return out;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [java.lang.Object[]] */
    public T[] sample(int sampleSize, T[] array) throws NotStrictlyPositiveException {
        T[] out;
        if (sampleSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
        }
        if (array == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        if (array.length < sampleSize) {
            out = (Object[]) Array.newInstance(array.getClass().getComponentType(), sampleSize);
        } else {
            out = array;
        }
        for (int i2 = 0; i2 < sampleSize; i2++) {
            out[i2] = sample();
        }
        return out;
    }
}
