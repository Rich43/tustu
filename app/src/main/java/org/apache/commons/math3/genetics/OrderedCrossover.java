package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/OrderedCrossover.class */
public class OrderedCrossover<T> implements CrossoverPolicy {
    @Override // org.apache.commons.math3.genetics.CrossoverPolicy
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {
        if (!(first instanceof AbstractListChromosome) || !(second instanceof AbstractListChromosome)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
        }
        return mate((AbstractListChromosome) first, (AbstractListChromosome) second);
    }

    protected ChromosomePair mate(AbstractListChromosome<T> first, AbstractListChromosome<T> second) throws DimensionMismatchException {
        int b2;
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        List<T> parent1Rep = first.getRepresentation();
        List<T> parent2Rep = second.getRepresentation();
        ArrayList arrayList = new ArrayList(length);
        ArrayList arrayList2 = new ArrayList(length);
        Set<T> child1Set = new HashSet<>(length);
        Set<T> child2Set = new HashSet<>(length);
        RandomGenerator random = GeneticAlgorithm.getRandomGenerator();
        int a2 = random.nextInt(length);
        do {
            b2 = random.nextInt(length);
        } while (a2 == b2);
        int lb = FastMath.min(a2, b2);
        int ub = FastMath.max(a2, b2);
        arrayList.addAll(parent1Rep.subList(lb, ub + 1));
        child1Set.addAll(arrayList);
        arrayList2.addAll(parent2Rep.subList(lb, ub + 1));
        child2Set.addAll(arrayList2);
        for (int i2 = 1; i2 <= length; i2++) {
            int idx = (ub + i2) % length;
            T item1 = parent1Rep.get(idx);
            T item2 = parent2Rep.get(idx);
            if (!child1Set.contains(item2)) {
                arrayList.add(item2);
                child1Set.add(item2);
            }
            if (!child2Set.contains(item1)) {
                arrayList2.add(item1);
                child2Set.add(item1);
            }
        }
        Collections.rotate(arrayList, lb);
        Collections.rotate(arrayList2, lb);
        return new ChromosomePair(first.newFixedLengthChromosome(arrayList), second.newFixedLengthChromosome(arrayList2));
    }
}
