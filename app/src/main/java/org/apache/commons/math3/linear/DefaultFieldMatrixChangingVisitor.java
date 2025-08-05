package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/DefaultFieldMatrixChangingVisitor.class */
public class DefaultFieldMatrixChangingVisitor<T extends FieldElement<T>> implements FieldMatrixChangingVisitor<T> {
    private final T zero;

    public DefaultFieldMatrixChangingVisitor(T zero) {
        this.zero = zero;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrixChangingVisitor
    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    @Override // org.apache.commons.math3.linear.FieldMatrixChangingVisitor
    public T visit(int row, int column, T value) {
        return value;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrixChangingVisitor
    public T end() {
        return this.zero;
    }
}
