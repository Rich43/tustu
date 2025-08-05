package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.linear.RealVector;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/ParameterValidator.class */
public interface ParameterValidator {
    RealVector validate(RealVector realVector);
}
