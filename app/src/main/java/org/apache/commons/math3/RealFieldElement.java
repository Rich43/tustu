package org.apache.commons.math3;

import org.apache.commons.math3.exception.DimensionMismatchException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/RealFieldElement.class */
public interface RealFieldElement<T> extends FieldElement<T> {
    double getReal();

    T add(double d2);

    T subtract(double d2);

    T multiply(double d2);

    T divide(double d2);

    T remainder(double d2);

    T remainder(T t2) throws DimensionMismatchException;

    T abs();

    T ceil();

    T floor();

    T rint();

    long round();

    T signum();

    T copySign(T t2);

    T copySign(double d2);

    T scalb(int i2);

    T hypot(T t2) throws DimensionMismatchException;

    @Override // org.apache.commons.math3.FieldElement
    T reciprocal();

    T sqrt();

    T cbrt();

    T rootN(int i2);

    T pow(double d2);

    T pow(int i2);

    T pow(T t2) throws DimensionMismatchException;

    T exp();

    T expm1();

    T log();

    T log1p();

    T cos();

    T sin();

    T tan();

    T acos();

    T asin();

    T atan();

    T atan2(T t2) throws DimensionMismatchException;

    T cosh();

    T sinh();

    T tanh();

    T acosh();

    T asinh();

    T atanh();

    T linearCombination(T[] tArr, T[] tArr2) throws DimensionMismatchException;

    T linearCombination(double[] dArr, T[] tArr) throws DimensionMismatchException;

    T linearCombination(T t2, T t3, T t4, T t5);

    T linearCombination(double d2, T t2, double d3, T t3);

    T linearCombination(T t2, T t3, T t4, T t5, T t6, T t7);

    T linearCombination(double d2, T t2, double d3, T t3, double d4, T t4);

    T linearCombination(T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9);

    T linearCombination(double d2, T t2, double d3, T t3, double d4, T t4, double d5, T t5);
}
