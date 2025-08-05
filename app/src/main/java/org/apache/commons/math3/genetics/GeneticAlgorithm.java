package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/GeneticAlgorithm.class */
public class GeneticAlgorithm {
    private static RandomGenerator randomGenerator = new JDKRandomGenerator();
    private final CrossoverPolicy crossoverPolicy;
    private final double crossoverRate;
    private final MutationPolicy mutationPolicy;
    private final double mutationRate;
    private final SelectionPolicy selectionPolicy;
    private int generationsEvolved = 0;

    public GeneticAlgorithm(CrossoverPolicy crossoverPolicy, double crossoverRate, MutationPolicy mutationPolicy, double mutationRate, SelectionPolicy selectionPolicy) throws OutOfRangeException {
        if (crossoverRate < 0.0d || crossoverRate > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.CROSSOVER_RATE, Double.valueOf(crossoverRate), 0, 1);
        }
        if (mutationRate < 0.0d || mutationRate > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.MUTATION_RATE, Double.valueOf(mutationRate), 0, 1);
        }
        this.crossoverPolicy = crossoverPolicy;
        this.crossoverRate = crossoverRate;
        this.mutationPolicy = mutationPolicy;
        this.mutationRate = mutationRate;
        this.selectionPolicy = selectionPolicy;
    }

    public static synchronized void setRandomGenerator(RandomGenerator random) {
        randomGenerator = random;
    }

    public static synchronized RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    public Population evolve(Population initial, StoppingCondition condition) throws MathIllegalArgumentException {
        Population current = initial;
        this.generationsEvolved = 0;
        while (!condition.isSatisfied(current)) {
            current = nextGeneration(current);
            this.generationsEvolved++;
        }
        return current;
    }

    public Population nextGeneration(Population current) throws MathIllegalArgumentException {
        Population nextGeneration = current.nextGeneration();
        RandomGenerator randGen = getRandomGenerator();
        while (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
            ChromosomePair pair = getSelectionPolicy().select(current);
            if (randGen.nextDouble() < getCrossoverRate()) {
                pair = getCrossoverPolicy().crossover(pair.getFirst(), pair.getSecond());
            }
            if (randGen.nextDouble() < getMutationRate()) {
                pair = new ChromosomePair(getMutationPolicy().mutate(pair.getFirst()), getMutationPolicy().mutate(pair.getSecond()));
            }
            nextGeneration.addChromosome(pair.getFirst());
            if (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
                nextGeneration.addChromosome(pair.getSecond());
            }
        }
        return nextGeneration;
    }

    public CrossoverPolicy getCrossoverPolicy() {
        return this.crossoverPolicy;
    }

    public double getCrossoverRate() {
        return this.crossoverRate;
    }

    public MutationPolicy getMutationPolicy() {
        return this.mutationPolicy;
    }

    public double getMutationRate() {
        return this.mutationRate;
    }

    public SelectionPolicy getSelectionPolicy() {
        return this.selectionPolicy;
    }

    public int getGenerationsEvolved() {
        return this.generationsEvolved;
    }
}
