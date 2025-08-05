package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/NPointCrossover.class */
public class NPointCrossover<T> implements CrossoverPolicy {
    private final int crossoverPoints;

    public NPointCrossover(int crossoverPoints) throws NotStrictlyPositiveException {
        if (crossoverPoints <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(crossoverPoints));
        }
        this.crossoverPoints = crossoverPoints;
    }

    public int getCrossoverPoints() {
        return this.crossoverPoints;
    }

    @Override // org.apache.commons.math3.genetics.CrossoverPolicy
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {
        if (!(first instanceof AbstractListChromosome) || !(second instanceof AbstractListChromosome)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
        }
        return mate((AbstractListChromosome) first, (AbstractListChromosome) second);
    }

    private ChromosomePair mate(AbstractListChromosome<T> first, AbstractListChromosome<T> second) throws DimensionMismatchException, NumberIsTooLargeException {
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        if (this.crossoverPoints >= length) {
            throw new NumberIsTooLargeException(Integer.valueOf(this.crossoverPoints), Integer.valueOf(length), false);
        }
        List<T> parent1Rep = first.getRepresentation();
        List<T> parent2Rep = second.getRepresentation();
        List<T> child1Rep = new ArrayList<>(length);
        List<T> child2Rep = new ArrayList<>(length);
        RandomGenerator random = GeneticAlgorithm.getRandomGenerator();
        List<T> c1 = child1Rep;
        List<T> c2 = child2Rep;
        int remainingPoints = this.crossoverPoints;
        int lastIndex = 0;
        int i2 = 0;
        while (i2 < this.crossoverPoints) {
            int crossoverIndex = 1 + lastIndex + random.nextInt((length - lastIndex) - remainingPoints);
            for (int j2 = lastIndex; j2 < crossoverIndex; j2++) {
                c1.add(parent1Rep.get(j2));
                c2.add(parent2Rep.get(j2));
            }
            List<T> tmp = c1;
            c1 = c2;
            c2 = tmp;
            lastIndex = crossoverIndex;
            i2++;
            remainingPoints--;
        }
        for (int j3 = lastIndex; j3 < length; j3++) {
            c1.add(parent1Rep.get(j3));
            c2.add(parent2Rep.get(j3));
        }
        return new ChromosomePair(first.newFixedLengthChromosome(child1Rep), second.newFixedLengthChromosome(child2Rep));
    }
}
