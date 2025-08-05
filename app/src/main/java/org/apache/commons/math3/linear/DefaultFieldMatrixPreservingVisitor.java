package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/DefaultFieldMatrixPreservingVisitor.class */
public class DefaultFieldMatrixPreservingVisitor<T extends FieldElement<T>> implements FieldMatrixPreservingVisitor<T> {
    private final T zero;

    public DefaultFieldMatrixPreservingVisitor(T zero) {
        this.zero = zero;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    @Override // org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
    public void visit(int row, int column, T value) {
    }

    @Override // org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
    public T end() {
        return this.zero;
    }
}
