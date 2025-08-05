package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/QuadIterator.class */
class QuadIterator implements PathIterator {
    QuadCurve2D quad;
    BaseTransform transform;
    int index;

    QuadIterator(QuadCurve2D q2, BaseTransform tx) {
        this.quad = q2;
        this.transform = tx;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public boolean isDone() {
        return this.index > 1;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public void next() {
        this.index++;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int currentSegment(float[] coords) {
        int type;
        if (isDone()) {
            throw new NoSuchElementException("quad iterator iterator out of bounds");
        }
        if (this.index == 0) {
            coords[0] = this.quad.x1;
            coords[1] = this.quad.y1;
            type = 0;
        } else {
            coords[0] = this.quad.ctrlx;
            coords[1] = this.quad.ctrly;
            coords[2] = this.quad.x2;
            coords[3] = this.quad.y2;
            type = 2;
        }
        if (this.transform != null) {
            this.transform.transform(coords, 0, coords, 0, this.index == 0 ? 1 : 2);
        }
        return type;
    }
}
