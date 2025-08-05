package org.apache.commons.math3.ml.neuralnet.sofm.util;

import org.apache.commons.math3.analysis.function.Logistic;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/sofm/util/QuasiSigmoidDecayFunction.class */
public class QuasiSigmoidDecayFunction {
    private final Logistic sigmoid;
    private final double scale;

    public QuasiSigmoidDecayFunction(double initValue, double slope, long numCall) {
        if (initValue <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(initValue));
        }
        if (slope >= 0.0d) {
            throw new NumberIsTooLargeException(Double.valueOf(slope), 0, false);
        }
        if (numCall <= 1) {
            throw new NotStrictlyPositiveException(Long.valueOf(numCall));
        }
        double m2 = numCall;
        double b2 = (4.0d * slope) / initValue;
        this.sigmoid = new Logistic(initValue, m2, b2, 1.0d, 0.0d, 1.0d);
        double y0 = this.sigmoid.value(0.0d);
        this.scale = initValue / y0;
    }

    public double value(long numCall) {
        return this.scale * this.sigmoid.value(numCall);
    }
}
