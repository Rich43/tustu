package org.apache.commons.math3.geometry.euclidean.twod.hull;

import java.io.Serializable;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.hull.ConvexHull;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/hull/ConvexHull2D.class */
public class ConvexHull2D implements ConvexHull<Euclidean2D, Vector2D>, Serializable {
    private static final long serialVersionUID = 20140129;
    private final Vector2D[] vertices;
    private final double tolerance;
    private transient Segment[] lineSegments;

    public ConvexHull2D(Vector2D[] vertices, double tolerance) throws MathIllegalArgumentException {
        this.tolerance = tolerance;
        if (!isConvex(vertices)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_CONVEX, new Object[0]);
        }
        this.vertices = (Vector2D[]) vertices.clone();
    }

    private boolean isConvex(Vector2D[] hullVertices) {
        if (hullVertices.length < 3) {
            return true;
        }
        int sign = 0;
        int i2 = 0;
        while (i2 < hullVertices.length) {
            Vector2D p1 = hullVertices[i2 == 0 ? hullVertices.length - 1 : i2 - 1];
            Vector2D p2 = hullVertices[i2];
            Vector2D p3 = hullVertices[i2 == hullVertices.length - 1 ? 0 : i2 + 1];
            Vector2D d1 = p2.subtract((Vector<Euclidean2D>) p1);
            Vector2D d2 = p3.subtract((Vector<Euclidean2D>) p2);
            double crossProduct = MathArrays.linearCombination(d1.getX(), d2.getY(), -d1.getY(), d2.getX());
            int cmp = Precision.compareTo(crossProduct, 0.0d, this.tolerance);
            if (cmp != 0.0d) {
                if (sign != 0.0d && cmp != sign) {
                    return false;
                }
                sign = cmp;
            }
            i2++;
        }
        return true;
    }

    @Override // org.apache.commons.math3.geometry.hull.ConvexHull
    public Vector2D[] getVertices() {
        return (Vector2D[]) this.vertices.clone();
    }

    public Segment[] getLineSegments() {
        return (Segment[]) retrieveLineSegments().clone();
    }

    private Segment[] retrieveLineSegments() {
        if (this.lineSegments == null) {
            int size = this.vertices.length;
            if (size <= 1) {
                this.lineSegments = new Segment[0];
            } else if (size == 2) {
                this.lineSegments = new Segment[1];
                Vector2D p1 = this.vertices[0];
                Vector2D p2 = this.vertices[1];
                this.lineSegments[0] = new Segment(p1, p2, new Line(p1, p2, this.tolerance));
            } else {
                this.lineSegments = new Segment[size];
                Vector2D firstPoint = null;
                Vector2D lastPoint = null;
                int index = 0;
                Vector2D[] arr$ = this.vertices;
                for (Vector2D point : arr$) {
                    if (lastPoint == null) {
                        firstPoint = point;
                    } else {
                        int i2 = index;
                        index++;
                        this.lineSegments[i2] = new Segment(lastPoint, point, new Line(lastPoint, point, this.tolerance));
                    }
                    lastPoint = point;
                }
                this.lineSegments[index] = new Segment(lastPoint, firstPoint, new Line(lastPoint, firstPoint, this.tolerance));
            }
        }
        return this.lineSegments;
    }

    @Override // org.apache.commons.math3.geometry.hull.ConvexHull
    public Region<Euclidean2D> createRegion() throws InsufficientDataException {
        if (this.vertices.length < 3) {
            throw new InsufficientDataException();
        }
        RegionFactory<Euclidean2D> factory = new RegionFactory<>();
        Segment[] segments = retrieveLineSegments();
        Line[] lineArray = new Line[segments.length];
        for (int i2 = 0; i2 < segments.length; i2++) {
            lineArray[i2] = segments[i2].getLine();
        }
        return factory.buildConvex(lineArray);
    }
}
