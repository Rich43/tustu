package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/Hyperplane.class */
public interface Hyperplane<S extends Space> {
    Hyperplane<S> copySelf();

    double getOffset(Point<S> point);

    Point<S> project(Point<S> point);

    double getTolerance();

    boolean sameOrientationAs(Hyperplane<S> hyperplane);

    SubHyperplane<S> wholeHyperplane();

    Region<S> wholeSpace();
}
