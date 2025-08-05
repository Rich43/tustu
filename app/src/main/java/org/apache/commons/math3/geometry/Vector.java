package org.apache.commons.math3.geometry;

import java.text.NumberFormat;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/Vector.class */
public interface Vector<S extends Space> extends Point<S> {
    Vector<S> getZero();

    double getNorm1();

    double getNorm();

    double getNormSq();

    double getNormInf();

    Vector<S> add(Vector<S> vector);

    Vector<S> add(double d2, Vector<S> vector);

    Vector<S> subtract(Vector<S> vector);

    Vector<S> subtract(double d2, Vector<S> vector);

    Vector<S> negate();

    Vector<S> normalize() throws MathArithmeticException;

    Vector<S> scalarMultiply(double d2);

    boolean isInfinite();

    double distance1(Vector<S> vector);

    double distance(Vector<S> vector);

    double distanceInf(Vector<S> vector);

    double distanceSq(Vector<S> vector);

    double dotProduct(Vector<S> vector);

    String toString(NumberFormat numberFormat);
}
