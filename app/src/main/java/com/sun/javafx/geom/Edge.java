package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Edge.class */
public final class Edge {
    static final int INIT_PARTS = 4;
    static final int GROW_PARTS = 10;
    Curve curve;
    int ctag;
    int etag;
    double activey;
    int equivalence;
    private Edge lastEdge;
    private int lastResult;
    private double lastLimit;

    public Edge(Curve c2, int ctag) {
        this(c2, ctag, 0);
    }

    public Edge(Curve c2, int ctag, int etag) {
        this.curve = c2;
        this.ctag = ctag;
        this.etag = etag;
    }

    public Curve getCurve() {
        return this.curve;
    }

    public int getCurveTag() {
        return this.ctag;
    }

    public int getEdgeTag() {
        return this.etag;
    }

    public void setEdgeTag(int etag) {
        this.etag = etag;
    }

    public int getEquivalence() {
        return this.equivalence;
    }

    public void setEquivalence(int eq) {
        this.equivalence = eq;
    }

    public int compareTo(Edge other, double[] yrange) {
        if (other == this.lastEdge && yrange[0] < this.lastLimit) {
            if (yrange[1] > this.lastLimit) {
                yrange[1] = this.lastLimit;
            }
            return this.lastResult;
        }
        if (this == other.lastEdge && yrange[0] < other.lastLimit) {
            if (yrange[1] > other.lastLimit) {
                yrange[1] = other.lastLimit;
            }
            return 0 - other.lastResult;
        }
        int ret = this.curve.compareTo(other.curve, yrange);
        this.lastEdge = other;
        this.lastLimit = yrange[1];
        this.lastResult = ret;
        return ret;
    }

    public void record(double yend, int etag) {
        this.activey = yend;
        this.etag = etag;
    }

    public boolean isActiveFor(double y2, int etag) {
        return this.etag == etag && this.activey >= y2;
    }

    public String toString() {
        return "Edge[" + ((Object) this.curve) + ", " + (this.ctag == 0 ? "L" : "R") + ", " + (this.etag == 1 ? "I" : this.etag == -1 ? "O" : "N") + "]";
    }
}
