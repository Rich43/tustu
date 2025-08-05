package org.apache.commons.math3.util;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.RandomGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/RandomPivotingStrategy.class */
public class RandomPivotingStrategy implements PivotingStrategyInterface, Serializable {
    private static final long serialVersionUID = 20140713;
    private final RandomGenerator random;

    public RandomPivotingStrategy(RandomGenerator random) {
        this.random = random;
    }

    @Override // org.apache.commons.math3.util.PivotingStrategyInterface
    public int pivotIndex(double[] work, int begin, int end) throws MathIllegalArgumentException {
        MathArrays.verifyValues(work, begin, end - begin);
        return begin + this.random.nextInt((end - begin) - 1);
    }
}
