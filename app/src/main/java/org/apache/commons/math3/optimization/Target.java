package org.apache.commons.math3.optimization;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/Target.class */
public class Target implements OptimizationData {
    private final double[] target;

    public Target(double[] observations) {
        this.target = (double[]) observations.clone();
    }

    public double[] getTarget() {
        return (double[]) this.target.clone();
    }
}
