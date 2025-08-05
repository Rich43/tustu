package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/UniformCrossover.class */
public class UniformCrossover<T> implements CrossoverPolicy {
    private final double ratio;

    public UniformCrossover(double ratio) throws OutOfRangeException {
        if (ratio < 0.0d || ratio > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.CROSSOVER_RATE, Double.valueOf(ratio), Double.valueOf(0.0d), Double.valueOf(1.0d));
        }
        this.ratio = ratio;
    }

    public double getRatio() {
        return this.ratio;
    }

    @Override // org.apache.commons.math3.genetics.CrossoverPolicy
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {
        if (!(first instanceof AbstractListChromosome) || !(second instanceof AbstractListChromosome)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME, new Object[0]);
        }
        return mate((AbstractListChromosome) first, (AbstractListChromosome) second);
    }

    private ChromosomePair mate(AbstractListChromosome<T> first, AbstractListChromosome<T> second) throws DimensionMismatchException {
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        List<T> parent1Rep = first.getRepresentation();
        List<T> parent2Rep = second.getRepresentation();
        List<T> child1Rep = new ArrayList<>(length);
        List<T> child2Rep = new ArrayList<>(length);
        RandomGenerator random = GeneticAlgorithm.getRandomGenerator();
        for (int index = 0; index < length; index++) {
            if (random.nextDouble() < this.ratio) {
                child1Rep.add(parent2Rep.get(index));
                child2Rep.add(parent1Rep.get(index));
            } else {
                child1Rep.add(parent1Rep.get(index));
                child2Rep.add(parent2Rep.get(index));
            }
        }
        return new ChromosomePair(first.newFixedLengthChromosome(child1Rep), second.newFixedLengthChromosome(child2Rep));
    }
}
