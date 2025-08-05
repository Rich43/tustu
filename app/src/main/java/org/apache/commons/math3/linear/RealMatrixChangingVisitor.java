package org.apache.commons.math3.linear;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealMatrixChangingVisitor.class */
public interface RealMatrixChangingVisitor {
    void start(int i2, int i3, int i4, int i5, int i6, int i7);

    double visit(int i2, int i3, double d2);

    double end();
}
