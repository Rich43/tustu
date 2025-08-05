package org.apache.commons.math3.geometry.euclidean.twod;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/Segment.class */
public class Segment {
    private final Vector2D start;
    private final Vector2D end;
    private final Line line;

    public Segment(Vector2D start, Vector2D end, Line line) {
        this.start = start;
        this.end = end;
        this.line = line;
    }

    public Vector2D getStart() {
        return this.start;
    }

    public Vector2D getEnd() {
        return this.end;
    }

    public Line getLine() {
        return this.line;
    }

    public double distance(Vector2D p2) {
        double deltaX = this.end.getX() - this.start.getX();
        double deltaY = this.end.getY() - this.start.getY();
        double r2 = (((p2.getX() - this.start.getX()) * deltaX) + ((p2.getY() - this.start.getY()) * deltaY)) / ((deltaX * deltaX) + (deltaY * deltaY));
        if (r2 < 0.0d || r2 > 1.0d) {
            double dist1 = getStart().distance((Point<Euclidean2D>) p2);
            double dist2 = getEnd().distance((Point<Euclidean2D>) p2);
            return FastMath.min(dist1, dist2);
        }
        double px = this.start.getX() + (r2 * deltaX);
        double py = this.start.getY() + (r2 * deltaY);
        Vector2D interPt = new Vector2D(px, py);
        return interPt.distance((Point<Euclidean2D>) p2);
    }
}
