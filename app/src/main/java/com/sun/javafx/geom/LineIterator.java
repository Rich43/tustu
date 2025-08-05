package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/LineIterator.class */
class LineIterator implements PathIterator {
    Line2D line;
    BaseTransform transform;
    int index;

    LineIterator(Line2D l2, BaseTransform tx) {
        this.line = l2;
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
            throw new NoSuchElementException("line iterator out of bounds");
        }
        if (this.index == 0) {
            coords[0] = this.line.x1;
            coords[1] = this.line.y1;
            type = 0;
        } else {
            coords[0] = this.line.x2;
            coords[1] = this.line.y2;
            type = 1;
        }
        if (this.transform != null) {
            this.transform.transform(coords, 0, coords, 0, 1);
        }
        return type;
    }
}
