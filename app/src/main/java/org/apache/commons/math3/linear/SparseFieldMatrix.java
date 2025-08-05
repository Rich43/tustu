package org.apache.commons.math3.linear;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.util.OpenIntToFieldHashMap;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/SparseFieldMatrix.class */
public class SparseFieldMatrix<T extends FieldElement<T>> extends AbstractFieldMatrix<T> {
    private final OpenIntToFieldHashMap<T> entries;
    private final int rows;
    private final int columns;

    public SparseFieldMatrix(Field<T> field) {
        super(field);
        this.rows = 0;
        this.columns = 0;
        this.entries = new OpenIntToFieldHashMap<>(field);
    }

    public SparseFieldMatrix(Field<T> field, int rowDimension, int columnDimension) {
        super(field, rowDimension, columnDimension);
        this.rows = rowDimension;
        this.columns = columnDimension;
        this.entries = new OpenIntToFieldHashMap<>(field);
    }

    public SparseFieldMatrix(SparseFieldMatrix<T> other) {
        super(other.getField(), other.getRowDimension(), other.getColumnDimension());
        this.rows = other.getRowDimension();
        this.columns = other.getColumnDimension();
        this.entries = new OpenIntToFieldHashMap<>(other.entries);
    }

    public SparseFieldMatrix(FieldMatrix<T> other) {
        super(other.getField(), other.getRowDimension(), other.getColumnDimension());
        this.rows = other.getRowDimension();
        this.columns = other.getColumnDimension();
        this.entries = new OpenIntToFieldHashMap<>(getField());
        for (int i2 = 0; i2 < this.rows; i2++) {
            for (int j2 = 0; j2 < this.columns; j2++) {
                setEntry(i2, j2, other.getEntry(i2, j2));
            }
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void addToEntry(int row, int column, T increment) {
        checkRowIndex(row);
        checkColumnIndex(column);
        int key = computeKey(row, column);
        FieldElement fieldElement = (FieldElement) this.entries.get(key).add(increment);
        if (getField().getZero().equals(fieldElement)) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, fieldElement);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> copy() {
        return new SparseFieldMatrix((SparseFieldMatrix) this);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> createMatrix(int rowDimension, int columnDimension) {
        return new SparseFieldMatrix(getField(), rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.AnyMatrix
    public int getColumnDimension() {
        return this.columns;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T getEntry(int i2, int i3) {
        checkRowIndex(i2);
        checkColumnIndex(i3);
        return (T) this.entries.get(computeKey(i2, i3));
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.AnyMatrix
    public int getRowDimension() {
        return this.rows;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void multiplyEntry(int row, int column, T factor) {
        checkRowIndex(row);
        checkColumnIndex(column);
        int key = computeKey(row, column);
        FieldElement fieldElement = (FieldElement) this.entries.get(key).multiply(factor);
        if (getField().getZero().equals(fieldElement)) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, fieldElement);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setEntry(int row, int column, T value) {
        checkRowIndex(row);
        checkColumnIndex(column);
        if (getField().getZero().equals(value)) {
            this.entries.remove(computeKey(row, column));
        } else {
            this.entries.put(computeKey(row, column), value);
        }
    }

    private int computeKey(int row, int column) {
        return (row * this.columns) + column;
    }
}
