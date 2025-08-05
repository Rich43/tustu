package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/ConcentricShapePair.class */
public final class ConcentricShapePair extends ShapePair {
    private final Shape outer;
    private final Shape inner;

    public ConcentricShapePair(Shape outer, Shape inner) {
        this.outer = outer;
        this.inner = inner;
    }

    @Override // com.sun.javafx.geom.ShapePair
    public int getCombinationType() {
        return 1;
    }

    @Override // com.sun.javafx.geom.ShapePair
    public Shape getOuterShape() {
        return this.outer;
    }

    @Override // com.sun.javafx.geom.ShapePair
    public Shape getInnerShape() {
        return this.inner;
    }

    @Override // com.sun.javafx.geom.Shape
    public Shape copy() {
        return new ConcentricShapePair(this.outer.copy(), this.inner.copy());
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        return this.outer.contains(x2, y2) && !this.inner.contains(x2, y2);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        return this.outer.intersects(x2, y2, w2, h2) && !this.inner.contains(x2, y2, w2, h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        return this.outer.contains(x2, y2, w2, h2) && !this.inner.intersects(x2, y2, w2, h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        return this.outer.getBounds();
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        return new PairIterator(this.outer.getPathIterator(tx), this.inner.getPathIterator(tx));
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx, float flatness) {
        return new PairIterator(this.outer.getPathIterator(tx, flatness), this.inner.getPathIterator(tx, flatness));
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/ConcentricShapePair$PairIterator.class */
    static class PairIterator implements PathIterator {
        PathIterator outer;
        PathIterator inner;

        PairIterator(PathIterator outer, PathIterator inner) {
            this.outer = outer;
            this.inner = inner;
        }

        @Override // com.sun.javafx.geom.PathIterator
        public int getWindingRule() {
            return 0;
        }

        @Override // com.sun.javafx.geom.PathIterator
        public int currentSegment(float[] coords) {
            if (this.outer.isDone()) {
                return this.inner.currentSegment(coords);
            }
            return this.outer.currentSegment(coords);
        }

        @Override // com.sun.javafx.geom.PathIterator
        public boolean isDone() {
            return this.outer.isDone() && this.inner.isDone();
        }

        @Override // com.sun.javafx.geom.PathIterator
        public void next() {
            if (this.outer.isDone()) {
                this.inner.next();
            } else {
                this.outer.next();
            }
        }
    }
}
