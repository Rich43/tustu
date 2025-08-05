package org.apache.commons.math3.util;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/DoubleArray.class */
public interface DoubleArray {
    int getNumElements();

    double getElement(int i2);

    void setElement(int i2, double d2);

    void addElement(double d2);

    void addElements(double[] dArr);

    double addElementRolling(double d2);

    double[] getElements();

    void clear();
}
