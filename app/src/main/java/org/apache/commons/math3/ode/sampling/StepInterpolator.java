package org.apache.commons.math3.ode.sampling;

import java.io.Externalizable;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/StepInterpolator.class */
public interface StepInterpolator extends Externalizable {
    double getPreviousTime();

    double getCurrentTime();

    double getInterpolatedTime();

    void setInterpolatedTime(double d2);

    double[] getInterpolatedState() throws MaxCountExceededException;

    double[] getInterpolatedDerivatives() throws MaxCountExceededException;

    double[] getInterpolatedSecondaryState(int i2) throws MaxCountExceededException;

    double[] getInterpolatedSecondaryDerivatives(int i2) throws MaxCountExceededException;

    boolean isForward();

    StepInterpolator copy() throws MaxCountExceededException;
}
