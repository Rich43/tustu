package org.apache.commons.math3.genetics;

import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/ElitisticListPopulation.class */
public class ElitisticListPopulation extends ListPopulation {
    private double elitismRate;

    public ElitisticListPopulation(List<Chromosome> chromosomes, int populationLimit, double elitismRate) throws OutOfRangeException, NotPositiveException, NullArgumentException, NumberIsTooLargeException {
        super(chromosomes, populationLimit);
        this.elitismRate = 0.9d;
        setElitismRate(elitismRate);
    }

    public ElitisticListPopulation(int populationLimit, double elitismRate) throws OutOfRangeException, NotPositiveException {
        super(populationLimit);
        this.elitismRate = 0.9d;
        setElitismRate(elitismRate);
    }

    @Override // org.apache.commons.math3.genetics.Population
    public Population nextGeneration() {
        ElitisticListPopulation nextGeneration = new ElitisticListPopulation(getPopulationLimit(), getElitismRate());
        List<Chromosome> oldChromosomes = getChromosomeList();
        Collections.sort(oldChromosomes);
        int boundIndex = (int) FastMath.ceil((1.0d - getElitismRate()) * oldChromosomes.size());
        for (int i2 = boundIndex; i2 < oldChromosomes.size(); i2++) {
            nextGeneration.addChromosome(oldChromosomes.get(i2));
        }
        return nextGeneration;
    }

    public void setElitismRate(double elitismRate) throws OutOfRangeException {
        if (elitismRate < 0.0d || elitismRate > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.ELITISM_RATE, Double.valueOf(elitismRate), 0, 1);
        }
        this.elitismRate = elitismRate;
    }

    public double getElitismRate() {
        return this.elitismRate;
    }
}
