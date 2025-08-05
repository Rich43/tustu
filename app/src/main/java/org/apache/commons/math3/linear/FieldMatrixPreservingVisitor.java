package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/FieldMatrixPreservingVisitor.class */
public interface FieldMatrixPreservingVisitor<T extends FieldElement<?>> {
    void start(int i2, int i3, int i4, int i5, int i6, int i7);

    void visit(int i2, int i3, T t2);

    T end();
}
