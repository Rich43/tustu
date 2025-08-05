package org.apache.commons.math3.ode;

import java.io.Serializable;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/ParameterConfiguration.class */
class ParameterConfiguration implements Serializable {
    private static final long serialVersionUID = 2247518849090889379L;
    private String parameterName;
    private double hP;

    ParameterConfiguration(String parameterName, double hP) {
        this.parameterName = parameterName;
        this.hP = hP;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public double getHP() {
        return this.hP;
    }

    public void setHP(double hParam) {
        this.hP = hParam;
    }
}
