package org.apache.commons.math3.geometry.euclidean.threed;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/Segment.class */
public class Segment {
    private final Vector3D start;
    private final Vector3D end;
    private final Line line;

    public Segment(Vector3D start, Vector3D end, Line line) {
        this.start = start;
        this.end = end;
        this.line = line;
    }

    public Vector3D getStart() {
        return this.start;
    }

    public Vector3D getEnd() {
        return this.end;
    }

    public Line getLine() {
        return this.line;
    }
}
