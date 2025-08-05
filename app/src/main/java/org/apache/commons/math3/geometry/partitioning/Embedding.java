package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/Embedding.class */
public interface Embedding<S extends Space, T extends Space> {
    Point<T> toSubSpace(Point<S> point);

    Point<S> toSpace(Point<T> point);
}
