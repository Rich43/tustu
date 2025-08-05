package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NumberIsTooSmallException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/FixedGenerationCount.class */
public class FixedGenerationCount implements StoppingCondition {
    private int numGenerations = 0;
    private final int maxGenerations;

    public FixedGenerationCount(int maxGenerations) throws NumberIsTooSmallException {
        if (maxGenerations <= 0) {
            throw new NumberIsTooSmallException(Integer.valueOf(maxGenerations), 1, true);
        }
        this.maxGenerations = maxGenerations;
    }

    @Override // org.apache.commons.math3.genetics.StoppingCondition
    public boolean isSatisfied(Population population) {
        if (this.numGenerations < this.maxGenerations) {
            this.numGenerations++;
            return false;
        }
        return true;
    }

    public int getNumGenerations() {
        return this.numGenerations;
    }
}
