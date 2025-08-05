package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/ValueAndJacobianFunction.class */
public interface ValueAndJacobianFunction extends MultivariateJacobianFunction {
    RealVector computeValue(double[] dArr);

    RealMatrix computeJacobian(double[] dArr);
}
