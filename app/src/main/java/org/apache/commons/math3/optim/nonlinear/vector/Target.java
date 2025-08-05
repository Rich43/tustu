package org.apache.commons.math3.optim.nonlinear.vector;

import org.apache.commons.math3.optim.OptimizationData;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/vector/Target.class */
public class Target implements OptimizationData {
    private final double[] target;

    public Target(double[] observations) {
        this.target = (double[]) observations.clone();
    }

    public double[] getTarget() {
        return (double[]) this.target.clone();
    }
}
