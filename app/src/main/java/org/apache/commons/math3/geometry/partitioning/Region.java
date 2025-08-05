package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/Region.class */
public interface Region<S extends Space> {

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/Region$Location.class */
    public enum Location {
        INSIDE,
        OUTSIDE,
        BOUNDARY
    }

    Region<S> buildNew(BSPTree<S> bSPTree);

    Region<S> copySelf();

    boolean isEmpty();

    boolean isEmpty(BSPTree<S> bSPTree);

    boolean isFull();

    boolean isFull(BSPTree<S> bSPTree);

    boolean contains(Region<S> region);

    Location checkPoint(Point<S> point);

    BoundaryProjection<S> projectToBoundary(Point<S> point);

    BSPTree<S> getTree(boolean z2);

    double getBoundarySize();

    double getSize();

    Point<S> getBarycenter();

    @Deprecated
    Side side(Hyperplane<S> hyperplane);

    SubHyperplane<S> intersection(SubHyperplane<S> subHyperplane);
}
