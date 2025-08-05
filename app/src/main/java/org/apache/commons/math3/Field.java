package org.apache.commons.math3;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/Field.class */
public interface Field<T> {
    T getZero();

    T getOne();

    Class<? extends FieldElement<T>> getRuntimeClass();
}
