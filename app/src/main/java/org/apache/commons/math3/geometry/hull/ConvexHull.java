package org.apache.commons.math3.geometry.hull;

import java.io.Serializable;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.partitioning.Region;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/hull/ConvexHull.class */
public interface ConvexHull<S extends Space, P extends Point<S>> extends Serializable {
    P[] getVertices();

    Region<S> createRegion() throws InsufficientDataException;
}
