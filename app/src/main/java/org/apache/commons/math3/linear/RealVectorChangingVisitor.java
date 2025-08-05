package org.apache.commons.math3.linear;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealVectorChangingVisitor.class */
public interface RealVectorChangingVisitor {
    void start(int i2, int i3, int i4);

    double visit(int i2, double d2);

    double end();
}
