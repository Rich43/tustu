package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/MutationPolicy.class */
public interface MutationPolicy {
    Chromosome mutate(Chromosome chromosome) throws MathIllegalArgumentException;
}
