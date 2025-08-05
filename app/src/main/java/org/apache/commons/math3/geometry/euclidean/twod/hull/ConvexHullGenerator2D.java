package org.apache.commons.math3.geometry.euclidean.twod.hull;

import java.util.Collection;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.hull.ConvexHullGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/hull/ConvexHullGenerator2D.class */
public interface ConvexHullGenerator2D extends ConvexHullGenerator<Euclidean2D, Vector2D> {
    @Override // org.apache.commons.math3.geometry.hull.ConvexHullGenerator
    ConvexHull2D generate(Collection<Vector2D> collection) throws NullArgumentException, ConvergenceException;
}
