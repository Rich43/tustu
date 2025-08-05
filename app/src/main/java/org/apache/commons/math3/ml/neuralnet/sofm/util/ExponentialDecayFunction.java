package org.apache.commons.math3.ml.neuralnet.sofm.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/sofm/util/ExponentialDecayFunction.class */
public class ExponentialDecayFunction {

    /* renamed from: a, reason: collision with root package name */
    private final double f13043a;
    private final double oneOverB;

    public ExponentialDecayFunction(double initValue, double valueAtNumCall, long numCall) {
        if (initValue <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(initValue));
        }
        if (valueAtNumCall <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(valueAtNumCall));
        }
        if (valueAtNumCall >= initValue) {
            throw new NumberIsTooLargeException(Double.valueOf(valueAtNumCall), Double.valueOf(initValue), false);
        }
        if (numCall <= 0) {
            throw new NotStrictlyPositiveException(Long.valueOf(numCall));
        }
        this.f13043a = initValue;
        this.oneOverB = (-FastMath.log(valueAtNumCall / initValue)) / numCall;
    }

    public double value(long numCall) {
        return this.f13043a * FastMath.exp((-numCall) * this.oneOverB);
    }
}
