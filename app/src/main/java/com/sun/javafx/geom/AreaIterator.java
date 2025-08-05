package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;
import java.util.Vector;

/* compiled from: Area.java */
/* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaIterator.class */
class AreaIterator implements PathIterator {
    private BaseTransform transform;
    private Vector curves;
    private int index;
    private Curve prevcurve;
    private Curve thiscurve;

    public AreaIterator(Vector curves, BaseTransform tx) {
        this.curves = curves;
        this.transform = tx;
        if (curves.size() >= 1) {
            this.thiscurve = (Curve) curves.get(0);
        }
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public boolean isDone() {
        return this.prevcurve == null && this.thiscurve == null;
    }

    @Override // com.sun.javafx.geom.PathIterator
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

    @Override // com.sun.javafx.geom.PathIterator
    public int currentSegment(float[] coords) {
        int segtype;
        int numpoints;
        if (this.prevcurve != null) {
            if (this.thiscurve == null || this.thiscurve.getOrder() == 0) {
                return 4;
            }
            coords[0] = (float) this.thiscurve.getX0();
            coords[1] = (float) this.thiscurve.getY0();
            segtype = 1;
            numpoints = 1;
        } else {
            if (this.thiscurve == null) {
                throw new NoSuchElementException("area iterator out of bounds");
            }
            segtype = this.thiscurve.getSegment(coords);
            numpoints = this.thiscurve.getOrder();
            if (numpoints == 0) {
                numpoints = 1;
            }
        }
        if (this.transform != null) {
            this.transform.transform(coords, 0, coords, 0, numpoints);
        }
        return segtype;
    }
}
