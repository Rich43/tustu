package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/FieldVectorPreservingVisitor.class */
public interface FieldVectorPreservingVisitor<T extends FieldElement<?>> {
    void start(int i2, int i3, int i4);

    void visit(int i2, T t2);

    T end();
}
