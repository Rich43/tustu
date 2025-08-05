package org.apache.commons.math3.geometry;

import java.io.Serializable;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/Point.class */
public interface Point<S extends Space> extends Serializable {
    Space getSpace();

    boolean isNaN();

    double distance(Point<S> point);
}
