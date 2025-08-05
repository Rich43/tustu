package org.apache.commons.math3.optimization.general;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/general/Preconditioner.class */
public interface Preconditioner {
    double[] precondition(double[] dArr, double[] dArr2);
}
