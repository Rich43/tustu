package org.apache.commons.math3.ode;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/ParameterizedODE.class */
public interface ParameterizedODE extends Parameterizable {
    double getParameter(String str) throws UnknownParameterException;

    void setParameter(String str, double d2) throws UnknownParameterException;
}
