package org.apache.commons.math3.ml.neuralnet.sofm;

import org.apache.commons.math3.ml.neuralnet.sofm.util.ExponentialDecayFunction;
import org.apache.commons.math3.ml.neuralnet.sofm.util.QuasiSigmoidDecayFunction;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/sofm/NeighbourhoodSizeFunctionFactory.class */
public class NeighbourhoodSizeFunctionFactory {
    private NeighbourhoodSizeFunctionFactory() {
    }

    public static NeighbourhoodSizeFunction exponentialDecay(final double initValue, final double valueAtNumCall, final long numCall) {
        return new NeighbourhoodSizeFunction() { // from class: org.apache.commons.math3.ml.neuralnet.sofm.NeighbourhoodSizeFunctionFactory.1
            private final ExponentialDecayFunction decay;

            {
                this.decay = new ExponentialDecayFunction(initValue, valueAtNumCall, numCall);
            }

            @Override // org.apache.commons.math3.ml.neuralnet.sofm.NeighbourhoodSizeFunction
            public int value(long n2) {
                return (int) FastMath.rint(this.decay.value(n2));
            }
        };
    }

    public static NeighbourhoodSizeFunction quasiSigmoidDecay(final double initValue, final double slope, final long numCall) {
        return new NeighbourhoodSizeFunction() { // from class: org.apache.commons.math3.ml.neuralnet.sofm.NeighbourhoodSizeFunctionFactory.2
            private final QuasiSigmoidDecayFunction decay;

            {
                this.decay = new QuasiSigmoidDecayFunction(initValue, slope, numCall);
            }

            @Override // org.apache.commons.math3.ml.neuralnet.sofm.NeighbourhoodSizeFunction
            public int value(long n2) {
                return (int) FastMath.rint(this.decay.value(n2));
            }
        };
    }
}
