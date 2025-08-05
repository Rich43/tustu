package org.apache.commons.math3.optim.univariate;

import java.io.Serializable;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/univariate/UnivariatePointValuePair.class */
public class UnivariatePointValuePair implements Serializable {
    private static final long serialVersionUID = 1003888396256744753L;
    private final double point;
    private final double value;

    public UnivariatePointValuePair(double point, double value) {
        this.point = point;
        this.value = value;
    }

    public double getPoint() {
        return this.point;
    }

    public double getValue() {
        return this.value;
    }
}
