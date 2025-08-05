package sun.awt.geom;

/* loaded from: rt.jar:sun/awt/geom/Edge.class */
final class Edge {
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

    public Edge(Curve curve, int i2) {
        this(curve, i2, 0);
    }

    public Edge(Curve curve, int i2, int i3) {
        this.curve = curve;
        this.ctag = i2;
        this.etag = i3;
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

    public void setEdgeTag(int i2) {
        this.etag = i2;
    }

    public int getEquivalence() {
        return this.equivalence;
    }

    public void setEquivalence(int i2) {
        this.equivalence = i2;
    }

    public int compareTo(Edge edge, double[] dArr) {
        if (edge == this.lastEdge && dArr[0] < this.lastLimit) {
            if (dArr[1] > this.lastLimit) {
                dArr[1] = this.lastLimit;
            }
            return this.lastResult;
        }
        if (this == edge.lastEdge && dArr[0] < edge.lastLimit) {
            if (dArr[1] > edge.lastLimit) {
                dArr[1] = edge.lastLimit;
            }
            return 0 - edge.lastResult;
        }
        int iCompareTo = this.curve.compareTo(edge.curve, dArr);
        this.lastEdge = edge;
        this.lastLimit = dArr[1];
        this.lastResult = iCompareTo;
        return iCompareTo;
    }

    public void record(double d2, int i2) {
        this.activey = d2;
        this.etag = i2;
    }

    public boolean isActiveFor(double d2, int i2) {
        return this.etag == i2 && this.activey >= d2;
    }

    public String toString() {
        return "Edge[" + ((Object) this.curve) + ", " + (this.ctag == 0 ? "L" : "R") + ", " + (this.etag == 1 ? "I" : this.etag == -1 ? "O" : "N") + "]";
    }
}
