package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/OnePointCrossover.class */
public class OnePointCrossover<T> implements CrossoverPolicy {
    @Override // org.apache.commons.math3.genetics.CrossoverPolicy
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {
        if (!(first instanceof AbstractListChromosome) || !(second instanceof AbstractListChromosome)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
        }
        return crossover((AbstractListChromosome) first, (AbstractListChromosome) second);
    }

    private ChromosomePair crossover(AbstractListChromosome<T> first, AbstractListChromosome<T> second) throws DimensionMismatchException {
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        List<T> parent1Rep = first.getRepresentation();
        List<T> parent2Rep = second.getRepresentation();
        List<T> child1Rep = new ArrayList<>(length);
        List<T> child2Rep = new ArrayList<>(length);
        int crossoverIndex = 1 + GeneticAlgorithm.getRandomGenerator().nextInt(length - 2);
        for (int i2 = 0; i2 < crossoverIndex; i2++) {
            child1Rep.add(parent1Rep.get(i2));
            child2Rep.add(parent2Rep.get(i2));
        }
        for (int i3 = crossoverIndex; i3 < length; i3++) {
            child1Rep.add(parent2Rep.get(i3));
            child2Rep.add(parent1Rep.get(i3));
        }
        return new ChromosomePair(first.newFixedLengthChromosome(child1Rep), second.newFixedLengthChromosome(child2Rep));
    }
}
