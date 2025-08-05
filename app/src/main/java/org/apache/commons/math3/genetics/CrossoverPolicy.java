package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/CrossoverPolicy.class */
public interface CrossoverPolicy {
    ChromosomePair crossover(Chromosome chromosome, Chromosome chromosome2) throws MathIllegalArgumentException;
}
