package org.apache.commons.math3.geometry.spherical.twod;

import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.spherical.oned.Arc;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/Edge.class */
public class Edge {
    private final Vertex start;
    private Vertex end;
    private final double length;
    private final Circle circle;

    Edge(Vertex start, Vertex end, double length, Circle circle) {
        this.start = start;
        this.end = end;
        this.length = length;
        this.circle = circle;
        start.setOutgoing(this);
        end.setIncoming(this);
    }

    public Vertex getStart() {
        return this.start;
    }

    public Vertex getEnd() {
        return this.end;
    }

    public double getLength() {
        return this.length;
    }

    public Circle getCircle() {
        return this.circle;
    }

    public Vector3D getPointAt(double alpha) {
        return this.circle.getPointAt(alpha + this.circle.getPhase(this.start.getLocation().getVector()));
    }

    void setNextEdge(Edge next) {
        this.end = next.getStart();
        this.end.setIncoming(this);
        this.end.bindWith(getCircle());
    }

    void split(Circle splitCircle, List<Edge> outsideList, List<Edge> insideList) {
        double edgeStart = this.circle.getPhase(this.start.getLocation().getVector());
        Arc arc = this.circle.getInsideArc(splitCircle);
        double arcRelativeStart = MathUtils.normalizeAngle(arc.getInf(), edgeStart + 3.141592653589793d) - edgeStart;
        double arcRelativeEnd = arcRelativeStart + arc.getSize();
        double unwrappedEnd = arcRelativeEnd - 6.283185307179586d;
        double tolerance = this.circle.getTolerance();
        Vertex previousVertex = this.start;
        if (unwrappedEnd >= this.length - tolerance) {
            insideList.add(this);
            return;
        }
        double alreadyManagedLength = 0.0d;
        if (unwrappedEnd >= 0.0d) {
            previousVertex = addSubEdge(previousVertex, new Vertex(new S2Point(this.circle.getPointAt(edgeStart + unwrappedEnd))), unwrappedEnd, insideList, splitCircle);
            alreadyManagedLength = unwrappedEnd;
        }
        if (arcRelativeStart >= this.length - tolerance) {
            if (unwrappedEnd >= 0.0d) {
                addSubEdge(previousVertex, this.end, this.length - alreadyManagedLength, outsideList, splitCircle);
                return;
            } else {
                outsideList.add(this);
                return;
            }
        }
        Vertex previousVertex2 = addSubEdge(previousVertex, new Vertex(new S2Point(this.circle.getPointAt(edgeStart + arcRelativeStart))), arcRelativeStart - alreadyManagedLength, outsideList, splitCircle);
        if (arcRelativeEnd >= this.length - tolerance) {
            addSubEdge(previousVertex2, this.end, this.length - arcRelativeStart, insideList, splitCircle);
        } else {
            addSubEdge(addSubEdge(previousVertex2, new Vertex(new S2Point(this.circle.getPointAt(edgeStart + arcRelativeStart))), arcRelativeStart - arcRelativeStart, insideList, splitCircle), this.end, this.length - arcRelativeStart, outsideList, splitCircle);
        }
    }

    private Vertex addSubEdge(Vertex subStart, Vertex subEnd, double subLength, List<Edge> list, Circle splitCircle) {
        if (subLength <= this.circle.getTolerance()) {
            return subStart;
        }
        subEnd.bindWith(splitCircle);
        Edge edge = new Edge(subStart, subEnd, subLength, this.circle);
        list.add(edge);
        return subEnd;
    }
}
