package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/CycleCrossover.class */
public class CycleCrossover<T> implements CrossoverPolicy {
    private final boolean randomStart;

    public CycleCrossover() {
        this(false);
    }

    public CycleCrossover(boolean randomStart) {
        this.randomStart = randomStart;
    }

    public boolean isRandomStart() {
        return this.randomStart;
    }

    @Override // org.apache.commons.math3.genetics.CrossoverPolicy
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {
        if (!(first instanceof AbstractListChromosome) || !(second instanceof AbstractListChromosome)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
        }
        return mate((AbstractListChromosome) first, (AbstractListChromosome) second);
    }

    protected ChromosomePair mate(AbstractListChromosome<T> first, AbstractListChromosome<T> second) throws DimensionMismatchException {
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        List<T> parent1Rep = first.getRepresentation();
        List<T> parent2Rep = second.getRepresentation();
        ArrayList arrayList = new ArrayList(second.getRepresentation());
        ArrayList arrayList2 = new ArrayList(first.getRepresentation());
        Set<Integer> visitedIndices = new HashSet<>(length);
        List<Integer> indices = new ArrayList<>(length);
        int idx = this.randomStart ? GeneticAlgorithm.getRandomGenerator().nextInt(length) : 0;
        int cycle = 1;
        while (visitedIndices.size() < length) {
            indices.add(Integer.valueOf(idx));
            T item = parent2Rep.get(idx);
            int iIndexOf = parent1Rep.indexOf(item);
            while (true) {
                int idx2 = iIndexOf;
                if (idx2 == indices.get(0).intValue()) {
                    break;
                }
                indices.add(Integer.valueOf(idx2));
                T item2 = parent2Rep.get(idx2);
                iIndexOf = parent1Rep.indexOf(item2);
            }
            int i2 = cycle;
            cycle++;
            if (i2 % 2 != 0) {
                Iterator i$ = indices.iterator();
                while (i$.hasNext()) {
                    int i3 = i$.next().intValue();
                    E e2 = arrayList.get(i3);
                    arrayList.set(i3, arrayList2.get(i3));
                    arrayList2.set(i3, e2);
                }
            }
            visitedIndices.addAll(indices);
            idx = (indices.get(0).intValue() + 1) % length;
            while (visitedIndices.contains(Integer.valueOf(idx)) && visitedIndices.size() < length) {
                idx++;
                if (idx >= length) {
                    idx = 0;
                }
            }
            indices.clear();
        }
        return new ChromosomePair(first.newFixedLengthChromosome(arrayList), second.newFixedLengthChromosome(arrayList2));
    }
}
