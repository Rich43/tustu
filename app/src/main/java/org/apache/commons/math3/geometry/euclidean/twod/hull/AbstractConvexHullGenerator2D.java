package org.apache.commons.math3.geometry.euclidean.twod.hull;

import java.util.Collection;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.hull.ConvexHull;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/hull/AbstractConvexHullGenerator2D.class */
abstract class AbstractConvexHullGenerator2D implements ConvexHullGenerator2D {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private final double tolerance;
    private final boolean includeCollinearPoints;

    protected abstract Collection<Vector2D> findHullVertices(Collection<Vector2D> collection);

    @Override // org.apache.commons.math3.geometry.hull.ConvexHullGenerator
    public /* bridge */ /* synthetic */ ConvexHull generate(Collection collection) throws NullArgumentException, ConvergenceException {
        return generate((Collection<Vector2D>) collection);
    }

    protected AbstractConvexHullGenerator2D(boolean includeCollinearPoints) {
        this(includeCollinearPoints, 1.0E-10d);
    }

    protected AbstractConvexHullGenerator2D(boolean includeCollinearPoints, double tolerance) {
        this.includeCollinearPoints = includeCollinearPoints;
        this.tolerance = tolerance;
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public boolean isIncludeCollinearPoints() {
        return this.includeCollinearPoints;
    }

    @Override // org.apache.commons.math3.geometry.euclidean.twod.hull.ConvexHullGenerator2D, org.apache.commons.math3.geometry.hull.ConvexHullGenerator
    public ConvexHull2D generate(Collection<Vector2D> points) throws NullArgumentException, ConvergenceException {
        Collection<Vector2D> hullVertices;
        MathUtils.checkNotNull(points);
        if (points.size() < 2) {
            hullVertices = points;
        } else {
            hullVertices = findHullVertices(points);
        }
        try {
            return new ConvexHull2D((Vector2D[]) hullVertices.toArray(new Vector2D[hullVertices.size()]), this.tolerance);
        } catch (MathIllegalArgumentException e2) {
            throw new ConvergenceException();
        }
    }
}
