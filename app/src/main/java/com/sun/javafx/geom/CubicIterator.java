package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/CubicIterator.class */
class CubicIterator implements PathIterator {
    CubicCurve2D cubic;
    BaseTransform transform;
    int index;

    CubicIterator(CubicCurve2D q2, BaseTransform tx) {
        this.cubic = q2;
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
            throw new NoSuchElementException("cubic iterator iterator out of bounds");
        }
        if (this.index == 0) {
            coords[0] = this.cubic.x1;
            coords[1] = this.cubic.y1;
            type = 0;
        } else {
            coords[0] = this.cubic.ctrlx1;
            coords[1] = this.cubic.ctrly1;
            coords[2] = this.cubic.ctrlx2;
            coords[3] = this.cubic.ctrly2;
            coords[4] = this.cubic.x2;
            coords[5] = this.cubic.y2;
            type = 3;
        }
        if (this.transform != null) {
            this.transform.transform(coords, 0, coords, 0, this.index == 0 ? 1 : 3);
        }
        return type;
    }
}
