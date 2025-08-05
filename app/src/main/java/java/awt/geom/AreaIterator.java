package java.awt.geom;

import java.util.NoSuchElementException;
import java.util.Vector;
import sun.awt.geom.Curve;

/* compiled from: Area.java */
/* loaded from: rt.jar:java/awt/geom/AreaIterator.class */
class AreaIterator implements PathIterator {
    private AffineTransform transform;
    private Vector curves;
    private int index;
    private Curve prevcurve;
    private Curve thiscurve;

    public AreaIterator(Vector vector, AffineTransform affineTransform) {
        this.curves = vector;
        this.transform = affineTransform;
        if (vector.size() >= 1) {
            this.thiscurve = (Curve) vector.get(0);
        }
    }

    @Override // java.awt.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // java.awt.geom.PathIterator
    public boolean isDone() {
        return this.prevcurve == null && this.thiscurve == null;
    }

    @Override // java.awt.geom.PathIterator
    public void next() {
        if (this.prevcurve != null) {
            this.prevcurve = null;
            return;
        }
        this.prevcurve = this.thiscurve;
        this.index++;
        if (this.index < this.curves.size()) {
            this.thiscurve = (Curve) this.curves.get(this.index);
            if (this.thiscurve.getOrder() != 0 && this.prevcurve.getX1() == this.thiscurve.getX0() && this.prevcurve.getY1() == this.thiscurve.getY0()) {
                this.prevcurve = null;
                return;
            }
            return;
        }
        this.thiscurve = null;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(float[] fArr) {
        double[] dArr = new double[6];
        int iCurrentSegment = currentSegment(dArr);
        int i2 = iCurrentSegment == 4 ? 0 : iCurrentSegment == 2 ? 2 : iCurrentSegment == 3 ? 3 : 1;
        for (int i3 = 0; i3 < i2 * 2; i3++) {
            fArr[i3] = (float) dArr[i3];
        }
        return iCurrentSegment;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        int segment;
        int order;
        if (this.prevcurve != null) {
            if (this.thiscurve == null || this.thiscurve.getOrder() == 0) {
                return 4;
            }
            dArr[0] = this.thiscurve.getX0();
            dArr[1] = this.thiscurve.getY0();
            segment = 1;
            order = 1;
        } else {
            if (this.thiscurve == null) {
                throw new NoSuchElementException("area iterator out of bounds");
            }
            segment = this.thiscurve.getSegment(dArr);
            order = this.thiscurve.getOrder();
            if (order == 0) {
                order = 1;
            }
        }
        if (this.transform != null) {
            this.transform.transform(dArr, 0, dArr, 0, order);
        }
        return segment;
    }
}
