package org.apache.commons.math3;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/FieldElement.class */
public interface FieldElement<T> {
    T add(T t2) throws NullArgumentException;

    T subtract(T t2) throws NullArgumentException;

    T negate();

    T multiply(int i2);

    T multiply(T t2) throws NullArgumentException;

    T divide(T t2) throws NullArgumentException, MathArithmeticException;

    T reciprocal() throws MathArithmeticException;

    Field<T> getField();
}
