package org.apache.commons.math3.optimization;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/InitialGuess.class */
public class InitialGuess implements OptimizationData {
    private final double[] init;

    public InitialGuess(double[] startPoint) {
        this.init = (double[]) startPoint.clone();
    }

    public double[] getInitialGuess() {
        return (double[]) this.init.clone();
    }
}
