package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NumberIsTooLargeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/Population.class */
public interface Population extends Iterable<Chromosome> {
    int getPopulationSize();

    int getPopulationLimit();

    Population nextGeneration();

    void addChromosome(Chromosome chromosome) throws NumberIsTooLargeException;

    Chromosome getFittestChromosome();
}
