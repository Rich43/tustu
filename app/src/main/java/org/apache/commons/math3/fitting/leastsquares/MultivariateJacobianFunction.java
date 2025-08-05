package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/MultivariateJacobianFunction.class */
public interface MultivariateJacobianFunction {
    Pair<RealVector, RealMatrix> value(RealVector realVector);
}
