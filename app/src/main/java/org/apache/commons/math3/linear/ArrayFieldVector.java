package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/ArrayFieldVector.class */
public class ArrayFieldVector<T extends FieldElement<T>> implements FieldVector<T>, Serializable {
    private static final long serialVersionUID = 7648186910365927050L;
    private T[] data;
    private final Field<T> field;

    public ArrayFieldVector(Field<T> field) {
        this(field, 0);
    }

    public ArrayFieldVector(Field<T> field, int i2) {
        this.field = field;
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(field, i2));
    }

    public ArrayFieldVector(int size, T preset) {
        this(preset.getField2(), size);
        Arrays.fill(this.data, preset);
    }

    public ArrayFieldVector(T[] tArr) throws NullArgumentException, ZeroException {
        MathUtils.checkNotNull(tArr);
        try {
            this.field = tArr[0].getField2();
            this.data = (T[]) ((FieldElement[]) tArr.clone());
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
        }
    }

    public ArrayFieldVector(Field<T> field, T[] tArr) throws NullArgumentException {
        MathUtils.checkNotNull(tArr);
        this.field = field;
        this.data = (T[]) ((FieldElement[]) tArr.clone());
    }

    public ArrayFieldVector(T[] tArr, boolean z2) throws NullArgumentException, ZeroException {
        MathUtils.checkNotNull(tArr);
        if (tArr.length == 0) {
            throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
        }
        this.field = tArr[0].getField2();
        this.data = z2 ? (T[]) ((FieldElement[]) tArr.clone()) : tArr;
    }

    public ArrayFieldVector(Field<T> field, T[] tArr, boolean z2) throws NullArgumentException {
        MathUtils.checkNotNull(tArr);
        this.field = field;
        this.data = z2 ? (T[]) ((FieldElement[]) tArr.clone()) : tArr;
    }

    public ArrayFieldVector(T[] tArr, int i2, int i3) throws NullArgumentException, NumberIsTooLargeException {
        MathUtils.checkNotNull(tArr);
        if (tArr.length < i2 + i3) {
            throw new NumberIsTooLargeException(Integer.valueOf(i2 + i3), Integer.valueOf(tArr.length), true);
        }
        this.field = tArr[0].getField2();
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, i3));
        System.arraycopy(tArr, i2, this.data, 0, i3);
    }

    public ArrayFieldVector(Field<T> field, T[] tArr, int i2, int i3) throws NullArgumentException, NumberIsTooLargeException {
        MathUtils.checkNotNull(tArr);
        if (tArr.length < i2 + i3) {
            throw new NumberIsTooLargeException(Integer.valueOf(i2 + i3), Integer.valueOf(tArr.length), true);
        }
        this.field = field;
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(field, i3));
        System.arraycopy(tArr, i2, this.data, 0, i3);
    }

    public ArrayFieldVector(FieldVector<T> fieldVector) throws NullArgumentException {
        MathUtils.checkNotNull(fieldVector);
        this.field = fieldVector.getField();
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, fieldVector.getDimension()));
        for (int i2 = 0; i2 < this.data.length; i2++) {
            ((T[]) this.data)[i2] = fieldVector.getEntry(i2);
        }
    }

    public ArrayFieldVector(ArrayFieldVector<T> arrayFieldVector) throws NullArgumentException {
        MathUtils.checkNotNull(arrayFieldVector);
        this.field = arrayFieldVector.getField();
        this.data = (T[]) ((FieldElement[]) arrayFieldVector.data.clone());
    }

    public ArrayFieldVector(ArrayFieldVector<T> arrayFieldVector, boolean z2) throws NullArgumentException {
        MathUtils.checkNotNull(arrayFieldVector);
        this.field = arrayFieldVector.getField();
        this.data = z2 ? (T[]) ((FieldElement[]) arrayFieldVector.data.clone()) : arrayFieldVector.data;
    }

    @Deprecated
    public ArrayFieldVector(ArrayFieldVector<T> v1, ArrayFieldVector<T> v2) throws NullArgumentException {
        this((FieldVector) v1, (FieldVector) v2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArrayFieldVector(FieldVector<T> fieldVector, FieldVector<T> fieldVector2) throws NullArgumentException {
        MathUtils.checkNotNull(fieldVector);
        MathUtils.checkNotNull(fieldVector2);
        this.field = fieldVector.getField();
        Object[] objArr = fieldVector instanceof ArrayFieldVector ? ((ArrayFieldVector) fieldVector).data : (T[]) fieldVector.toArray();
        Object[] objArr2 = fieldVector2 instanceof ArrayFieldVector ? ((ArrayFieldVector) fieldVector2).data : (T[]) fieldVector2.toArray();
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, objArr.length + objArr2.length));
        System.arraycopy(objArr, 0, this.data, 0, objArr.length);
        System.arraycopy(objArr2, 0, this.data, objArr.length, objArr2.length);
    }

    @Deprecated
    public ArrayFieldVector(ArrayFieldVector<T> v1, T[] v2) throws NullArgumentException {
        this((FieldVector) v1, (FieldElement[]) v2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArrayFieldVector(FieldVector<T> fieldVector, T[] tArr) throws NullArgumentException {
        MathUtils.checkNotNull(fieldVector);
        MathUtils.checkNotNull(tArr);
        this.field = fieldVector.getField();
        Object[] objArr = fieldVector instanceof ArrayFieldVector ? ((ArrayFieldVector) fieldVector).data : (T[]) fieldVector.toArray();
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, objArr.length + tArr.length));
        System.arraycopy(objArr, 0, this.data, 0, objArr.length);
        System.arraycopy(tArr, 0, this.data, objArr.length, tArr.length);
    }

    @Deprecated
    public ArrayFieldVector(T[] v1, ArrayFieldVector<T> v2) throws NullArgumentException {
        this((FieldElement[]) v1, (FieldVector) v2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArrayFieldVector(T[] tArr, FieldVector<T> fieldVector) throws NullArgumentException {
        MathUtils.checkNotNull(tArr);
        MathUtils.checkNotNull(fieldVector);
        this.field = fieldVector.getField();
        Object[] objArr = fieldVector instanceof ArrayFieldVector ? ((ArrayFieldVector) fieldVector).data : (T[]) fieldVector.toArray();
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, tArr.length + objArr.length));
        System.arraycopy(tArr, 0, this.data, 0, tArr.length);
        System.arraycopy(objArr, 0, this.data, tArr.length, objArr.length);
    }

    public ArrayFieldVector(T[] tArr, T[] tArr2) throws NullArgumentException, ZeroException {
        MathUtils.checkNotNull(tArr);
        MathUtils.checkNotNull(tArr2);
        if (tArr.length + tArr2.length == 0) {
            throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
        }
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(tArr[0].getField2(), tArr.length + tArr2.length));
        System.arraycopy(tArr, 0, this.data, 0, tArr.length);
        System.arraycopy(tArr2, 0, this.data, tArr.length, tArr2.length);
        this.field = this.data[0].getField2();
    }

    public ArrayFieldVector(Field<T> field, T[] tArr, T[] tArr2) throws NullArgumentException, ZeroException {
        MathUtils.checkNotNull(tArr);
        MathUtils.checkNotNull(tArr2);
        if (tArr.length + tArr2.length == 0) {
            throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
        }
        this.data = (T[]) ((FieldElement[]) MathArrays.buildArray(field, tArr.length + tArr2.length));
        System.arraycopy(tArr, 0, this.data, 0, tArr.length);
        System.arraycopy(tArr2, 0, this.data, tArr.length, tArr2.length);
        this.field = field;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public Field<T> getField() {
        return this.field;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> copy() {
        return new ArrayFieldVector((ArrayFieldVector) this, true);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> add(FieldVector<T> v2) throws DimensionMismatchException {
        try {
            return add((ArrayFieldVector) v2);
        } catch (ClassCastException e2) {
            checkVectorDimensions(v2);
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
            for (int i2 = 0; i2 < this.data.length; i2++) {
                fieldElementArr[i2] = (FieldElement) this.data[i2].add(v2.getEntry(i2));
            }
            return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
        }
    }

    public ArrayFieldVector<T> add(ArrayFieldVector<T> v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.data.length);
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            fieldElementArr[i2] = (FieldElement) this.data[i2].add(v2.data[i2]);
        }
        return new ArrayFieldVector<>((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> subtract(FieldVector<T> v2) throws DimensionMismatchException {
        try {
            return subtract((ArrayFieldVector) v2);
        } catch (ClassCastException e2) {
            checkVectorDimensions(v2);
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
            for (int i2 = 0; i2 < this.data.length; i2++) {
                fieldElementArr[i2] = (FieldElement) this.data[i2].subtract(v2.getEntry(i2));
            }
            return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
        }
    }

    public ArrayFieldVector<T> subtract(ArrayFieldVector<T> v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.data.length);
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            fieldElementArr[i2] = (FieldElement) this.data[i2].subtract(v2.data[i2]);
        }
        return new ArrayFieldVector<>((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapAdd(T d2) throws NullArgumentException {
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            fieldElementArr[i2] = (FieldElement) this.data[i2].add(d2);
        }
        return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapAddToSelf(T t2) throws NullArgumentException {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            ((T[]) this.data)[i2] = (FieldElement) this.data[i2].add(t2);
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapSubtract(T d2) throws NullArgumentException {
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            fieldElementArr[i2] = (FieldElement) this.data[i2].subtract(d2);
        }
        return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapSubtractToSelf(T t2) throws NullArgumentException {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            ((T[]) this.data)[i2] = (FieldElement) this.data[i2].subtract(t2);
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapMultiply(T d2) throws NullArgumentException {
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            fieldElementArr[i2] = (FieldElement) this.data[i2].multiply(d2);
        }
        return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapMultiplyToSelf(T t2) throws NullArgumentException {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            ((T[]) this.data)[i2] = (FieldElement) this.data[i2].multiply(t2);
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapDivide(T d2) throws NullArgumentException, MathArithmeticException {
        MathUtils.checkNotNull(d2);
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            fieldElementArr[i2] = (FieldElement) this.data[i2].divide(d2);
        }
        return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapDivideToSelf(T t2) throws NullArgumentException, MathArithmeticException {
        MathUtils.checkNotNull(t2);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            ((T[]) this.data)[i2] = (FieldElement) this.data[i2].divide(t2);
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapInv() throws MathArithmeticException {
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        T one = this.field.getOne();
        for (int i2 = 0; i2 < this.data.length; i2++) {
            try {
                fieldElementArr[i2] = (FieldElement) one.divide(this.data[i2]);
            } catch (MathArithmeticException e2) {
                throw new MathArithmeticException(LocalizedFormats.INDEX, Integer.valueOf(i2));
            }
        }
        return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> mapInvToSelf() throws MathArithmeticException {
        T one = this.field.getOne();
        for (int i2 = 0; i2 < this.data.length; i2++) {
            try {
                ((T[]) this.data)[i2] = (FieldElement) one.divide(this.data[i2]);
            } catch (MathArithmeticException e2) {
                throw new MathArithmeticException(LocalizedFormats.INDEX, Integer.valueOf(i2));
            }
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> ebeMultiply(FieldVector<T> v2) throws DimensionMismatchException {
        try {
            return ebeMultiply((ArrayFieldVector) v2);
        } catch (ClassCastException e2) {
            checkVectorDimensions(v2);
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
            for (int i2 = 0; i2 < this.data.length; i2++) {
                fieldElementArr[i2] = (FieldElement) this.data[i2].multiply(v2.getEntry(i2));
            }
            return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
        }
    }

    public ArrayFieldVector<T> ebeMultiply(ArrayFieldVector<T> v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.data.length);
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            fieldElementArr[i2] = (FieldElement) this.data[i2].multiply(v2.data[i2]);
        }
        return new ArrayFieldVector<>((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> ebeDivide(FieldVector<T> v2) throws DimensionMismatchException, MathArithmeticException {
        try {
            return ebeDivide((ArrayFieldVector) v2);
        } catch (ClassCastException e2) {
            checkVectorDimensions(v2);
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
            for (int i2 = 0; i2 < this.data.length; i2++) {
                try {
                    fieldElementArr[i2] = (FieldElement) this.data[i2].divide(v2.getEntry(i2));
                } catch (MathArithmeticException e3) {
                    throw new MathArithmeticException(LocalizedFormats.INDEX, Integer.valueOf(i2));
                }
            }
            return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
        }
    }

    public ArrayFieldVector<T> ebeDivide(ArrayFieldVector<T> v2) throws DimensionMismatchException, MathArithmeticException {
        checkVectorDimensions(v2.data.length);
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            try {
                fieldElementArr[i2] = (FieldElement) this.data[i2].divide(v2.data[i2]);
            } catch (MathArithmeticException e2) {
                throw new MathArithmeticException(LocalizedFormats.INDEX, Integer.valueOf(i2));
            }
        }
        return new ArrayFieldVector<>((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public T[] getData() {
        return (T[]) ((FieldElement[]) this.data.clone());
    }

    public T[] getDataRef() {
        return this.data;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.FieldVector
    public T dotProduct(FieldVector<T> fieldVector) throws DimensionMismatchException {
        try {
            return (T) dotProduct((ArrayFieldVector) fieldVector);
        } catch (ClassCastException e2) {
            checkVectorDimensions(fieldVector);
            T zero = this.field.getZero();
            for (int i2 = 0; i2 < this.data.length; i2++) {
                zero = (FieldElement) zero.add(this.data[i2].multiply(fieldVector.getEntry(i2)));
            }
            return zero;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [org.apache.commons.math3.FieldElement] */
    public T dotProduct(ArrayFieldVector<T> v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.data.length);
        T dot = this.field.getZero();
        for (int i2 = 0; i2 < this.data.length; i2++) {
            dot = (FieldElement) dot.add(this.data[i2].multiply(v2.data[i2]));
        }
        return dot;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> projection(FieldVector<T> v2) throws DimensionMismatchException, MathArithmeticException {
        return v2.mapMultiply((FieldElement) dotProduct(v2).divide(v2.dotProduct(v2)));
    }

    public ArrayFieldVector<T> projection(ArrayFieldVector<T> v2) throws DimensionMismatchException, MathArithmeticException {
        return (ArrayFieldVector) v2.mapMultiply((FieldElement) dotProduct((ArrayFieldVector) v2).divide(v2.dotProduct((ArrayFieldVector) v2)));
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldMatrix<T> outerProduct(FieldVector<T> v2) throws OutOfRangeException {
        try {
            return outerProduct((ArrayFieldVector) v2);
        } catch (ClassCastException e2) {
            int m2 = this.data.length;
            int n2 = v2.getDimension();
            FieldMatrix<T> out = new Array2DRowFieldMatrix<>(this.field, m2, n2);
            for (int i2 = 0; i2 < m2; i2++) {
                for (int j2 = 0; j2 < n2; j2++) {
                    out.setEntry(i2, j2, (FieldElement) this.data[i2].multiply(v2.getEntry(j2)));
                }
            }
            return out;
        }
    }

    public FieldMatrix<T> outerProduct(ArrayFieldVector<T> v2) throws OutOfRangeException {
        int m2 = this.data.length;
        int n2 = v2.data.length;
        FieldMatrix<T> out = new Array2DRowFieldMatrix<>(this.field, m2, n2);
        for (int i2 = 0; i2 < m2; i2++) {
            for (int j2 = 0; j2 < n2; j2++) {
                out.setEntry(i2, j2, (FieldElement) this.data[i2].multiply(v2.data[j2]));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public T getEntry(int index) {
        return this.data[index];
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public int getDimension() {
        return this.data.length;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> append(FieldVector<T> v2) {
        try {
            return append((ArrayFieldVector) v2);
        } catch (ClassCastException e2) {
            return new ArrayFieldVector((ArrayFieldVector) this, new ArrayFieldVector(v2));
        }
    }

    public ArrayFieldVector<T> append(ArrayFieldVector<T> v2) {
        return new ArrayFieldVector<>((ArrayFieldVector) this, (ArrayFieldVector) v2);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> append(T in) {
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, this.data.length + 1);
        System.arraycopy(this.data, 0, fieldElementArr, 0, this.data.length);
        fieldElementArr[this.data.length] = in;
        return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public FieldVector<T> getSubVector(int index, int n2) throws OutOfRangeException, NotPositiveException {
        if (n2 < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_ELEMENTS_SHOULD_BE_POSITIVE, Integer.valueOf(n2));
        }
        ArrayFieldVector<T> out = new ArrayFieldVector<>(this.field, n2);
        try {
            System.arraycopy(this.data, index, out.data, 0, n2);
        } catch (IndexOutOfBoundsException e2) {
            checkIndex(index);
            checkIndex((index + n2) - 1);
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public void setEntry(int index, T value) throws OutOfRangeException {
        try {
            this.data[index] = value;
        } catch (IndexOutOfBoundsException e2) {
            checkIndex(index);
        }
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public void setSubVector(int i2, FieldVector<T> fieldVector) throws OutOfRangeException {
        try {
            try {
                set(i2, (ArrayFieldVector) fieldVector);
            } catch (ClassCastException e2) {
                for (int i3 = i2; i3 < i2 + fieldVector.getDimension(); i3++) {
                    ((T[]) this.data)[i3] = fieldVector.getEntry(i3 - i2);
                }
            }
        } catch (IndexOutOfBoundsException e3) {
            checkIndex(i2);
            checkIndex((i2 + fieldVector.getDimension()) - 1);
        }
    }

    public void set(int index, ArrayFieldVector<T> v2) throws OutOfRangeException {
        try {
            System.arraycopy(v2.data, 0, this.data, index, v2.data.length);
        } catch (IndexOutOfBoundsException e2) {
            checkIndex(index);
            checkIndex((index + v2.data.length) - 1);
        }
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public void set(T value) {
        Arrays.fill(this.data, value);
    }

    @Override // org.apache.commons.math3.linear.FieldVector
    public T[] toArray() {
        return (T[]) ((FieldElement[]) this.data.clone());
    }

    protected void checkVectorDimensions(FieldVector<T> v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
    }

    protected void checkVectorDimensions(int n2) throws DimensionMismatchException {
        if (this.data.length != n2) {
            throw new DimensionMismatchException(this.data.length, n2);
        }
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

    public T walkInDefaultOrder(FieldVectorChangingVisitor<T> fieldVectorChangingVisitor) throws OutOfRangeException {
        int dimension = getDimension();
        fieldVectorChangingVisitor.start(dimension, 0, dimension - 1);
        for (int i2 = 0; i2 < dimension; i2++) {
            setEntry(i2, fieldVectorChangingVisitor.visit(i2, getEntry(i2)));
        }
        return (T) fieldVectorChangingVisitor.end();
    }

    public T walkInDefaultOrder(FieldVectorChangingVisitor<T> fieldVectorChangingVisitor, int i2, int i3) throws NumberIsTooSmallException, OutOfRangeException {
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

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        try {
            FieldVector<T> rhs = (FieldVector) other;
            if (this.data.length != rhs.getDimension()) {
                return false;
            }
            for (int i2 = 0; i2 < this.data.length; i2++) {
                if (!this.data[i2].equals(rhs.getEntry(i2))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public int hashCode() {
        int h2 = 3542;
        T[] arr$ = this.data;
        for (T a2 : arr$) {
            h2 ^= a2.hashCode();
        }
        return h2;
    }

    private void checkIndex(int index) throws OutOfRangeException {
        if (index < 0 || index >= getDimension()) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(index), 0, Integer.valueOf(getDimension() - 1));
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
}
