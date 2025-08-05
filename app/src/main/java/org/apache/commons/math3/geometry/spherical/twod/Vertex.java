package org.apache.commons.math3.geometry.spherical.twod;

import java.util.ArrayList;
import java.util.List;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/Vertex.class */
public class Vertex {
    private final S2Point location;
    private Edge incoming = null;
    private Edge outgoing = null;
    private final List<Circle> circles = new ArrayList();

    Vertex(S2Point location) {
        this.location = location;
    }

    public S2Point getLocation() {
        return this.location;
    }

    void bindWith(Circle circle) {
        this.circles.add(circle);
    }

    Circle sharedCircleWith(Vertex vertex) {
        for (Circle circle1 : this.circles) {
            for (Circle circle2 : vertex.circles) {
                if (circle1 == circle2) {
                    return circle1;
                }
            }
        }
        return null;
    }

    void setIncoming(Edge incoming) {
        this.incoming = incoming;
        bindWith(incoming.getCircle());
    }

    public Edge getIncoming() {
        return this.incoming;
    }

    void setOutgoing(Edge outgoing) {
        this.outgoing = outgoing;
        bindWith(outgoing.getCircle());
    }

    public Edge getOutgoing() {
        return this.outgoing;
    }
}
