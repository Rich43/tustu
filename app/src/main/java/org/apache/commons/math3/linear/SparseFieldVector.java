package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.OpenIntToFieldHashMap;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/SparseFieldVector.class */
public class SparseFieldVector<T extends FieldElement<T>> implements FieldVector<T>, Serializable {
    private static final long serialVersionUID = 7841233292190413362L;
    private final Field<T> field;
    private final OpenIntToFieldHashMap<T> entries;
    private final int virtualSize;

    public SparseFieldVector(Field<T> field) {
        this(field, 0);
    }

    public SparseFieldVector(Field<T> field, int dimension) {
        this.field = field;
        this.virtualSize = dimension;
        this.entries = new OpenIntToFieldHashMap<>(field);
    }

    protected SparseFieldVector(SparseFieldVector<T> v2, int resize) {
        this.field = v2.field;
        this.virtualSize = v2.getDimension() + resize;
        this.entries = new OpenIntToFieldHashMap<>(v2.entries);
    }

    public SparseFieldVector(Field<T> field, int dimension, int expectedSize) {
        this.field = field;
        this.virtualSize = dimension;
        this.entries = new OpenIntToFieldHashMap<>(field, expectedSize);
    }

    public SparseFieldVector(Field<T> field, T[] values) throws NullArgumentException {
        MathUtils.checkNotNull(values);
        this.field = field;
        this.virtualSize = values.length;
        this.entries = new OpenIntToFieldHashMap<>(field);
        for (int key = 0; key < values.length; key++) {
            T value = values[key];
            this.entries.put(key, value);
        }
    }

    public SparseFieldVector(SparseFieldVector<T> v2) {
        this.field = v2.field;
        this.virtualSize = v2.getDimension();
        this.entries = new OpenIntToFieldHashMap<>(v2.getEntries());
    }

    private OpenIntToFieldHashMap<T> getEntries() {
        return this.entries;
    }

    public FieldVector<T> add(SparseFieldVector<T> v2) throws OutOfRangeException, NullArgumentException, DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        SparseFieldVector<T> res = (SparseFieldVector) copy();
        OpenIntToFieldHashMap<T>.Iterator iter = v2.getEntries().iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            FieldElement fieldElementValue = iter.value();
            if (this.entries.containsKey(key)) {
                res.setEntry(key, (FieldElement) this.entries.get(key).add(fieldElementValue));
            } else {
                res.setEntry(key, fieldElementValue);
            }
        }
        return res;
    }

    public FieldVector<T> append(SparseFieldVector<T> v2) throws OutOfRangeException, NullArgumentException, NoSuchElementException, ConcurrentModificationException {
        SparseFieldVector<T> res = new SparseFieldVector<>(this, v2.getDimension());
        OpenIntToFieldHashMap<T>.Iterator iter = v2.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key() + this.virtualSize, iter.value());
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> append(FieldVector<T> v2) throws OutOfRangeException {
        if (v2 instanceof SparseFieldVector) {
            return append((SparseFieldVector) v2);
        }
        int n2 = v2.getDimension();
        FieldVector<T> res = new SparseFieldVector<>(this, n2);
        for (int i2 = 0; i2 < n2; i2++) {
            res.setEntry(i2 + this.virtualSize, v2.getEntry(i2));
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> append(T d2) throws OutOfRangeException, NullArgumentException {
        MathUtils.checkNotNull(d2);
        FieldVector<T> res = new SparseFieldVector<>(this, 1);
        res.setEntry(this.virtualSize, d2);
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> copy() {
        return new SparseFieldVector(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.FieldVector
    public T dotProduct(FieldVector<T> v2) throws DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        T res = this.field.getZero();
        OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res = (FieldElement) res.add(v2.getEntry(iter.key()).multiply(iter.value()));
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> ebeDivide(FieldVector<T> v2) throws OutOfRangeException, NullArgumentException, DimensionMismatchException, NoSuchElementException, ConcurrentModificationException, MathArithmeticException {
        checkVectorDimensions(v2.getDimension());
        SparseFieldVector<T> res = new SparseFieldVector<>(this);
        OpenIntToFieldHashMap<T>.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), (FieldElement) iter.value().divide(v2.getEntry(iter.key())));
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> ebeMultiply(FieldVector<T> v2) throws OutOfRangeException, NullArgumentException, DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        SparseFieldVector<T> res = new SparseFieldVector<>(this);
        OpenIntToFieldHashMap<T>.Iterator iter = res.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), (FieldElement) iter.value().multiply(v2.getEntry(iter.key())));
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    @Deprecated
    public T[] getData() {
        return (T[]) toArray();
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public int getDimension() {
        return this.virtualSize;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public T getEntry(int i2) throws OutOfRangeException {
        checkIndex(i2);
        return (T) this.entries.get(i2);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public Field<T> getField() {
        return this.field;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> getSubVector(int index, int n2) throws OutOfRangeException, NotPositiveException, NullArgumentException, NoSuchElementException, ConcurrentModificationException {
        if (n2 < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_ELEMENTS_SHOULD_BE_POSITIVE, Integer.valueOf(n2));
        }
        checkIndex(index);
        checkIndex((index + n2) - 1);
        SparseFieldVector<T> res = new SparseFieldVector<>(this.field, n2);
        int end = index + n2;
        OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (key >= index && key < end) {
                res.setEntry(key - index, iter.value());
            }
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapAdd(T d2) throws NullArgumentException {
        return copy().mapAddToSelf(d2);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapAddToSelf(T d2) throws OutOfRangeException, NullArgumentException {
        for (int i2 = 0; i2 < this.virtualSize; i2++) {
            setEntry(i2, (FieldElement) getEntry(i2).add(d2));
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapDivide(T d2) throws NullArgumentException, MathArithmeticException {
        return copy().mapDivideToSelf(d2);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapDivideToSelf(T d2) throws NullArgumentException, NoSuchElementException, ConcurrentModificationException, MathArithmeticException {
        OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            this.entries.put(iter.key(), (FieldElement) iter.value().divide(d2));
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapInv() throws MathArithmeticException {
        return copy().mapInvToSelf();
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapInvToSelf() throws OutOfRangeException, NullArgumentException, MathArithmeticException {
        for (int i2 = 0; i2 < this.virtualSize; i2++) {
            setEntry(i2, (FieldElement) this.field.getOne().divide(getEntry(i2)));
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapMultiply(T d2) throws NullArgumentException {
        return copy().mapMultiplyToSelf(d2);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapMultiplyToSelf(T d2) throws NullArgumentException, NoSuchElementException, ConcurrentModificationException {
        OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            this.entries.put(iter.key(), (FieldElement) iter.value().multiply(d2));
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapSubtract(T d2) throws NullArgumentException {
        return copy().mapSubtractToSelf(d2);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapSubtractToSelf(T d2) throws NullArgumentException {
        return mapAddToSelf((FieldElement) this.field.getZero().subtract(d2));
    }

    public FieldMatrix<T> outerProduct(SparseFieldVector<T> v2) throws NoSuchElementException, ConcurrentModificationException {
        int n2 = v2.getDimension();
        SparseFieldMatrix<T> res = new SparseFieldMatrix<>(this.field, this.virtualSize, n2);
        OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            OpenIntToFieldHashMap<T>.Iterator iter2 = v2.entries.iterator();
            while (iter2.hasNext()) {
                iter2.advance();
                res.setEntry(iter.key(), iter2.key(), (FieldElement) iter.value().multiply(iter2.value()));
            }
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldMatrix<T> outerProduct(FieldVector<T> v2) throws OutOfRangeException, NoSuchElementException, ConcurrentModificationException {
        if (v2 instanceof SparseFieldVector) {
            return outerProduct((SparseFieldVector) v2);
        }
        int n2 = v2.getDimension();
        FieldMatrix<T> res = new SparseFieldMatrix<>(this.field, this.virtualSize, n2);
        OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int row = iter.key();
            FieldElement fieldElementValue = iter.value();
            for (int col = 0; col < n2; col++) {
                res.setEntry(row, col, (FieldElement) fieldElementValue.multiply(v2.getEntry(col)));
            }
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> projection(FieldVector<T> v2) throws DimensionMismatchException, MathArithmeticException {
        checkVectorDimensions(v2.getDimension());
        return v2.mapMultiply((FieldElement) dotProduct(v2).divide(v2.dotProduct(v2)));
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public void set(T value) throws OutOfRangeException, NullArgumentException {
        MathUtils.checkNotNull(value);
        for (int i2 = 0; i2 < this.virtualSize; i2++) {
            setEntry(i2, value);
        }
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public void setEntry(int index, T value) throws OutOfRangeException, NullArgumentException {
        MathUtils.checkNotNull(value);
        checkIndex(index);
        this.entries.put(index, value);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public void setSubVector(int index, FieldVector<T> v2) throws OutOfRangeException, NullArgumentException {
        checkIndex(index);
        checkIndex((index + v2.getDimension()) - 1);
        int n2 = v2.getDimension();
        for (int i2 = 0; i2 < n2; i2++) {
            setEntry(i2 + index, v2.getEntry(i2));
        }
    }

    public SparseFieldVector<T> subtract(SparseFieldVector<T> v2) throws OutOfRangeException, NullArgumentException, DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        SparseFieldVector<T> res = (SparseFieldVector) copy();
        OpenIntToFieldHashMap<T>.Iterator iter = v2.getEntries().iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (this.entries.containsKey(key)) {
                res.setEntry(key, (FieldElement) this.entries.get(key).subtract(iter.value()));
            } else {
                res.setEntry(key, (FieldElement) this.field.getZero().subtract(iter.value()));
            }
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> subtract(FieldVector<T> v2) throws OutOfRangeException, NullArgumentException, DimensionMismatchException {
        if (v2 instanceof SparseFieldVector) {
            return subtract((SparseFieldVector) v2);
        }
        int n2 = v2.getDimension();
        checkVectorDimensions(n2);
        SparseFieldVector<T> res = new SparseFieldVector<>(this);
        for (int i2 = 0; i2 < n2; i2++) {
            if (this.entries.containsKey(i2)) {
                res.setEntry(i2, (FieldElement) this.entries.get(i2).subtract(v2.getEntry(i2)));
            } else {
                res.setEntry(i2, (FieldElement) this.field.getZero().subtract(v2.getEntry(i2)));
            }
        }
        return res;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.FieldVector
    public T[] toArray() throws NoSuchElementException, ConcurrentModificationException {
        T[] tArr = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, this.virtualSize));
        OpenIntToFieldHashMap<T>.Iterator it = this.entries.iterator();
        while (it.hasNext()) {
            it.advance();
            tArr[it.key()] = it.value();
        }
        return tArr;
    }

    private void checkIndex(int index) throws OutOfRangeException {
        if (index < 0 || index >= getDimension()) {
            throw new OutOfRangeException(Integer.valueOf(index), 0, Integer.valueOf(getDimension() - 1));
        }
    }

    private void checkIndices(int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        int dim = getDimension();
        if (start < 0 || start >= dim) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(start), 0, Integer.valueOf(dim - 1));
        }
        if (end < 0 || end >= dim) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(end), 0, Integer.valueOf(dim - 1));
        }
        if (end < start) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, Integer.valueOf(end), Integer.valueOf(start), false);
        }
    }

    protected void checkVectorDimensions(int n2) throws DimensionMismatchException {
        if (getDimension() != n2) {
            throw new DimensionMismatchException(getDimension(), n2);
        }
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> add(FieldVector<T> v2) throws OutOfRangeException, NullArgumentException, DimensionMismatchException {
        if (v2 instanceof SparseFieldVector) {
            return add((SparseFieldVector) v2);
        }
        int n2 = v2.getDimension();
        checkVectorDimensions(n2);
        SparseFieldVector<T> res = new SparseFieldVector<>(this.field, getDimension());
        for (int i2 = 0; i2 < n2; i2++) {
            res.setEntry(i2, (FieldElement) v2.getEntry(i2).add(getEntry(i2)));
        }
        return res;
    }

    public T walkInDefaultOrder(FieldVectorPreservingVisitor<T> fieldVectorPreservingVisitor) {
        int dimension = getDimension();
        fieldVectorPreservingVisitor.start(dimension, 0, dimension - 1);
        for (int i2 = 0; i2 < dimension; i2++) {
            fieldVectorPreservingVisitor.visit(i2, getEntry(i2));
        }
        return (T) fieldVectorPreservingVisitor.end();
    }

    public T walkInDefaultOrder(FieldVectorPreservingVisitor<T> fieldVectorPreservingVisitor, int i2, int i3) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(i2, i3);
        fieldVectorPreservingVisitor.start(getDimension(), i2, i3);
        for (int i4 = i2; i4 <= i3; i4++) {
            fieldVectorPreservingVisitor.visit(i4, getEntry(i4));
        }
        return (T) fieldVectorPreservingVisitor.end();
    }

    public T walkInOptimizedOrder(FieldVectorPreservingVisitor<T> fieldVectorPreservingVisitor) {
        return (T) walkInDefaultOrder(fieldVectorPreservingVisitor);
    }

    public T walkInOptimizedOrder(FieldVectorPreservingVisitor<T> fieldVectorPreservingVisitor, int i2, int i3) throws NumberIsTooSmallException, OutOfRangeException {
        return (T) walkInDefaultOrder(fieldVectorPreservingVisitor, i2, i3);
    }

    public T walkInDefaultOrder(FieldVectorChangingVisitor<T> fieldVectorChangingVisitor) throws OutOfRangeException, NullArgumentException {
        int dimension = getDimension();
        fieldVectorChangingVisitor.start(dimension, 0, dimension - 1);
        for (int i2 = 0; i2 < dimension; i2++) {
            setEntry(i2, fieldVectorChangingVisitor.visit(i2, getEntry(i2)));
        }
        return (T) fieldVectorChangingVisitor.end();
    }

    public T walkInDefaultOrder(FieldVectorChangingVisitor<T> fieldVectorChangingVisitor, int i2, int i3) throws NumberIsTooSmallException, OutOfRangeException, NullArgumentException {
        checkIndices(i2, i3);
        fieldVectorChangingVisitor.start(getDimension(), i2, i3);
        for (int i4 = i2; i4 <= i3; i4++) {
            setEntry(i4, fieldVectorChangingVisitor.visit(i4, getEntry(i4)));
        }
        return (T) fieldVectorChangingVisitor.end();
    }

    public T walkInOptimizedOrder(FieldVectorChangingVisitor<T> fieldVectorChangingVisitor) {
        return (T) walkInDefaultOrder(fieldVectorChangingVisitor);
    }

    public T walkInOptimizedOrder(FieldVectorChangingVisitor<T> fieldVectorChangingVisitor, int i2, int i3) throws NumberIsTooSmallException, OutOfRangeException {
        return (T) walkInDefaultOrder(fieldVectorChangingVisitor, i2, i3);
    }

    public int hashCode() throws NoSuchElementException, ConcurrentModificationException {
        int result = (31 * 1) + (this.field == null ? 0 : this.field.hashCode());
        int result2 = (31 * result) + this.virtualSize;
        OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int temp = iter.value().hashCode();
            result2 = (31 * result2) + temp;
        }
        return result2;
    }

    public boolean equals(Object obj) throws NoSuchElementException, ConcurrentModificationException {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SparseFieldVector)) {
            return false;
        }
        SparseFieldVector<T> other = (SparseFieldVector) obj;
        if (this.field == null) {
            if (other.field != null) {
                return false;
            }
        } else if (!this.field.equals(other.field)) {
            return false;
        }
        if (this.virtualSize != other.virtualSize) {
            return false;
        }
        OpenIntToFieldHashMap<T>.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            if (!other.getEntry(iter.key()).equals(iter.value())) {
                return false;
            }
        }
        OpenIntToFieldHashMap<T>.Iterator iter2 = other.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            if (!iter2.value().equals(getEntry(iter2.key()))) {
                return false;
            }
        }
        return true;
    }
}
