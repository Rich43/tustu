package org.apache.commons.math3.genetics;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/Chromosome.class */
public abstract class Chromosome implements Comparable<Chromosome>, Fitness {
    private static final double NO_FITNESS = Double.NEGATIVE_INFINITY;
    private double fitness = Double.NEGATIVE_INFINITY;

    public double getFitness() {
        if (this.fitness == Double.NEGATIVE_INFINITY) {
            this.fitness = fitness();
        }
        return this.fitness;
    }

    @Override // java.lang.Comparable
    public int compareTo(Chromosome another) {
        return Double.compare(getFitness(), another.getFitness());
    }

    protected boolean isSame(Chromosome another) {
        return false;
    }

    protected Chromosome findSameChromosome(Population population) {
        for (Chromosome anotherChr : population) {
            if (isSame(anotherChr)) {
                return anotherChr;
            }
        }
        return null;
    }

    public void searchForFitnessUpdate(Population population) {
        Chromosome sameChromosome = findSameChromosome(population);
        if (sameChromosome != null) {
            this.fitness = sameChromosome.getFitness();
        }
    }
}
