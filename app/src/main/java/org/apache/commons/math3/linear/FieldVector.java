package org.apache.commons.math3.linear;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/FieldVector.class */
public interface FieldVector<T extends FieldElement<T>> {
    Field<T> getField();

    FieldVector<T> copy();

    FieldVector<T> add(FieldVector<T> fieldVector) throws DimensionMismatchException;

    FieldVector<T> subtract(FieldVector<T> fieldVector) throws DimensionMismatchException;

    FieldVector<T> mapAdd(T t2) throws NullArgumentException;

    FieldVector<T> mapAddToSelf(T t2) throws NullArgumentException;

    FieldVector<T> mapSubtract(T t2) throws NullArgumentException;

    FieldVector<T> mapSubtractToSelf(T t2) throws NullArgumentException;

    FieldVector<T> mapMultiply(T t2) throws NullArgumentException;

    FieldVector<T> mapMultiplyToSelf(T t2) throws NullArgumentException;

    FieldVector<T> mapDivide(T t2) throws NullArgumentException, MathArithmeticException;

    FieldVector<T> mapDivideToSelf(T t2) throws NullArgumentException, MathArithmeticException;

    FieldVector<T> mapInv() throws MathArithmeticException;

    FieldVector<T> mapInvToSelf() throws MathArithmeticException;

    FieldVector<T> ebeMultiply(FieldVector<T> fieldVector) throws DimensionMismatchException;

    FieldVector<T> ebeDivide(FieldVector<T> fieldVector) throws DimensionMismatchException, MathArithmeticException;

    @Deprecated
    T[] getData();

    T dotProduct(FieldVector<T> fieldVector) throws DimensionMismatchException;

    FieldVector<T> projection(FieldVector<T> fieldVector) throws DimensionMismatchException, MathArithmeticException;

    FieldMatrix<T> outerProduct(FieldVector<T> fieldVector);

    T getEntry(int i2) throws OutOfRangeException;

    void setEntry(int i2, T t2) throws OutOfRangeException;

    int getDimension();

    FieldVector<T> append(FieldVector<T> fieldVector);

    FieldVector<T> append(T t2);

    FieldVector<T> getSubVector(int i2, int i3) throws OutOfRangeException, NotPositiveException;

    void setSubVector(int i2, FieldVector<T> fieldVector) throws OutOfRangeException;

    void set(T t2);

    T[] toArray();
}
