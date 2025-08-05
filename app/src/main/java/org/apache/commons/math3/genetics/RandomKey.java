package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/RandomKey.class */
public abstract class RandomKey<T> extends AbstractListChromosome<Double> implements PermutationChromosome<T> {
    private final List<Double> sortedRepresentation;
    private final List<Integer> baseSeqPermutation;

    public RandomKey(List<Double> representation) throws InvalidRepresentationException {
        super(representation);
        List<Double> sortedRepr = new ArrayList<>(getRepresentation());
        Collections.sort(sortedRepr);
        this.sortedRepresentation = Collections.unmodifiableList(sortedRepr);
        this.baseSeqPermutation = Collections.unmodifiableList(decodeGeneric(baseSequence(getLength()), getRepresentation(), this.sortedRepresentation));
    }

    public RandomKey(Double[] representation) throws InvalidRepresentationException {
        this((List<Double>) Arrays.asList(representation));
    }

    @Override // org.apache.commons.math3.genetics.PermutationChromosome
    public List<T> decode(List<T> sequence) {
        return decodeGeneric(sequence, getRepresentation(), this.sortedRepresentation);
    }

    private static <S> List<S> decodeGeneric(List<S> sequence, List<Double> representation, List<Double> sortedRepr) throws DimensionMismatchException {
        int l2 = sequence.size();
        if (representation.size() != l2) {
            throw new DimensionMismatchException(representation.size(), l2);
        }
        if (sortedRepr.size() != l2) {
            throw new DimensionMismatchException(sortedRepr.size(), l2);
        }
        List<Double> reprCopy = new ArrayList<>(representation);
        List<S> res = new ArrayList<>(l2);
        for (int i2 = 0; i2 < l2; i2++) {
            int index = reprCopy.indexOf(sortedRepr.get(i2));
            res.add(sequence.get(index));
            reprCopy.set(index, null);
        }
        return res;
    }

    @Override // org.apache.commons.math3.genetics.Chromosome
    protected boolean isSame(Chromosome another) {
        if (!(another instanceof RandomKey)) {
            return false;
        }
        RandomKey<?> anotherRk = (RandomKey) another;
        if (getLength() != anotherRk.getLength()) {
            return false;
        }
        List<Integer> thisPerm = this.baseSeqPermutation;
        List<Integer> anotherPerm = anotherRk.baseSeqPermutation;
        for (int i2 = 0; i2 < getLength(); i2++) {
            if (thisPerm.get(i2) != anotherPerm.get(i2)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.apache.commons.math3.genetics.AbstractListChromosome
    protected void checkValidity(List<Double> chromosomeRepresentation) throws InvalidRepresentationException {
        Iterator i$ = chromosomeRepresentation.iterator();
        while (i$.hasNext()) {
            double val = i$.next().doubleValue();
            if (val < 0.0d || val > 1.0d) {
                throw new InvalidRepresentationException(LocalizedFormats.OUT_OF_RANGE_SIMPLE, Double.valueOf(val), 0, 1);
            }
        }
    }

    public static final List<Double> randomPermutation(int l2) {
        List<Double> repr = new ArrayList<>(l2);
        for (int i2 = 0; i2 < l2; i2++) {
            repr.add(Double.valueOf(GeneticAlgorithm.getRandomGenerator().nextDouble()));
        }
        return repr;
    }

    public static final List<Double> identityPermutation(int l2) {
        List<Double> repr = new ArrayList<>(l2);
        for (int i2 = 0; i2 < l2; i2++) {
            repr.add(Double.valueOf(i2 / l2));
        }
        return repr;
    }

    public static <S> List<Double> comparatorPermutation(List<S> data, Comparator<S> comparator) {
        List<S> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData, comparator);
        return inducedPermutation(data, sortedData);
    }

    public static <S> List<Double> inducedPermutation(List<S> originalData, List<S> permutedData) throws MathIllegalArgumentException {
        if (originalData.size() != permutedData.size()) {
            throw new DimensionMismatchException(permutedData.size(), originalData.size());
        }
        int l2 = originalData.size();
        List<S> origDataCopy = new ArrayList<>(originalData);
        Double[] res = new Double[l2];
        for (int i2 = 0; i2 < l2; i2++) {
            int index = origDataCopy.indexOf(permutedData.get(i2));
            if (index == -1) {
                throw new MathIllegalArgumentException(LocalizedFormats.DIFFERENT_ORIG_AND_PERMUTED_DATA, new Object[0]);
            }
            res[index] = Double.valueOf(i2 / l2);
            origDataCopy.set(index, null);
        }
        return Arrays.asList(res);
    }

    @Override // org.apache.commons.math3.genetics.AbstractListChromosome
    public String toString() {
        return String.format("(f=%s pi=(%s))", Double.valueOf(getFitness()), this.baseSeqPermutation);
    }

    private static List<Integer> baseSequence(int l2) {
        List<Integer> baseSequence = new ArrayList<>(l2);
        for (int i2 = 0; i2 < l2; i2++) {
            baseSequence.add(Integer.valueOf(i2));
        }
        return baseSequence;
    }
}
