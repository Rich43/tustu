package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/ListPopulation.class */
public abstract class ListPopulation implements Population {
    private List<Chromosome> chromosomes;
    private int populationLimit;

    public ListPopulation(int populationLimit) throws NotPositiveException {
        this(Collections.emptyList(), populationLimit);
    }

    public ListPopulation(List<Chromosome> chromosomes, int populationLimit) throws NotPositiveException, NullArgumentException, NumberIsTooLargeException {
        if (chromosomes == null) {
            throw new NullArgumentException();
        }
        if (populationLimit <= 0) {
            throw new NotPositiveException(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, Integer.valueOf(populationLimit));
        }
        if (chromosomes.size() > populationLimit) {
            throw new NumberIsTooLargeException(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, Integer.valueOf(chromosomes.size()), Integer.valueOf(populationLimit), false);
        }
        this.populationLimit = populationLimit;
        this.chromosomes = new ArrayList(populationLimit);
        this.chromosomes.addAll(chromosomes);
    }

    @Deprecated
    public void setChromosomes(List<Chromosome> chromosomes) throws NullArgumentException, NumberIsTooLargeException {
        if (chromosomes == null) {
            throw new NullArgumentException();
        }
        if (chromosomes.size() > this.populationLimit) {
            throw new NumberIsTooLargeException(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, Integer.valueOf(chromosomes.size()), Integer.valueOf(this.populationLimit), false);
        }
        this.chromosomes.clear();
        this.chromosomes.addAll(chromosomes);
    }

    public void addChromosomes(Collection<Chromosome> chromosomeColl) throws NumberIsTooLargeException {
        if (this.chromosomes.size() + chromosomeColl.size() > this.populationLimit) {
            throw new NumberIsTooLargeException(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, Integer.valueOf(this.chromosomes.size()), Integer.valueOf(this.populationLimit), false);
        }
        this.chromosomes.addAll(chromosomeColl);
    }

    public List<Chromosome> getChromosomes() {
        return Collections.unmodifiableList(this.chromosomes);
    }

    protected List<Chromosome> getChromosomeList() {
        return this.chromosomes;
    }

    @Override // org.apache.commons.math3.genetics.Population
    public void addChromosome(Chromosome chromosome) throws NumberIsTooLargeException {
        if (this.chromosomes.size() >= this.populationLimit) {
            throw new NumberIsTooLargeException(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, Integer.valueOf(this.chromosomes.size()), Integer.valueOf(this.populationLimit), false);
        }
        this.chromosomes.add(chromosome);
    }

    @Override // org.apache.commons.math3.genetics.Population
    public Chromosome getFittestChromosome() {
        Chromosome bestChromosome = this.chromosomes.get(0);
        for (Chromosome chromosome : this.chromosomes) {
            if (chromosome.compareTo(bestChromosome) > 0) {
                bestChromosome = chromosome;
            }
        }
        return bestChromosome;
    }

    @Override // org.apache.commons.math3.genetics.Population
    public int getPopulationLimit() {
        return this.populationLimit;
    }

    public void setPopulationLimit(int populationLimit) throws NumberIsTooSmallException {
        if (populationLimit <= 0) {
            throw new NotPositiveException(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, Integer.valueOf(populationLimit));
        }
        if (populationLimit < this.chromosomes.size()) {
            throw new NumberIsTooSmallException(Integer.valueOf(populationLimit), Integer.valueOf(this.chromosomes.size()), true);
        }
        this.populationLimit = populationLimit;
    }

    @Override // org.apache.commons.math3.genetics.Population
    public int getPopulationSize() {
        return this.chromosomes.size();
    }

    public String toString() {
        return this.chromosomes.toString();
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<Chromosome> iterator() {
        return getChromosomes().iterator();
    }
}
