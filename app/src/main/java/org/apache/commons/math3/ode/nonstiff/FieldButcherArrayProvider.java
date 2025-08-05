package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.RealFieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/FieldButcherArrayProvider.class */
public interface FieldButcherArrayProvider<T extends RealFieldElement<T>> {
    T[] getC();

    T[][] getA();

    T[] getB();
}
