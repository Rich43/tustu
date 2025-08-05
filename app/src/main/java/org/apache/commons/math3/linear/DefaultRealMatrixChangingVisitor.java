package org.apache.commons.math3.linear;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/DefaultRealMatrixChangingVisitor.class */
public class DefaultRealMatrixChangingVisitor implements RealMatrixChangingVisitor {
    @Override // org.apache.commons.math3.linear.RealMatrixChangingVisitor
    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    @Override // org.apache.commons.math3.linear.RealMatrixChangingVisitor
    public double visit(int row, int column, double value) {
        return value;
    }

    @Override // org.apache.commons.math3.linear.RealMatrixChangingVisitor
    public double end() {
        return 0.0d;
    }
}
