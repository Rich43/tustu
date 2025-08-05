package org.apache.commons.math3.optim.linear;

import org.apache.commons.math3.optim.OptimizationData;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/linear/NonNegativeConstraint.class */
public class NonNegativeConstraint implements OptimizationData {
    private final boolean isRestricted;

    public NonNegativeConstraint(boolean restricted) {
        this.isRestricted = restricted;
    }

    public boolean isRestrictedToNonNegative() {
        return this.isRestricted;
    }
}
