package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/GeneralShapePair.class */
public class GeneralShapePair extends ShapePair {
    private final Shape outer;
    private final Shape inner;
    private final int combinationType;

    public GeneralShapePair(Shape outer, Shape inner, int combinationType) {
        this.outer = outer;
        this.inner = inner;
        this.combinationType = combinationType;
    }

    @Override // com.sun.javafx.geom.ShapePair
    public final int getCombinationType() {
        return this.combinationType;
    }

    @Override // com.sun.javafx.geom.ShapePair
    public final Shape getOuterShape() {
        return this.outer;
    }

    @Override // com.sun.javafx.geom.ShapePair
    public final Shape getInnerShape() {
        return this.inner;
    }

    @Override // com.sun.javafx.geom.Shape
    public Shape copy() {
        return new GeneralShapePair(this.outer.copy(), this.inner.copy(), this.combinationType);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        return this.combinationType == 4 ? this.outer.contains(x2, y2) && this.inner.contains(x2, y2) : this.outer.contains(x2, y2) && !this.inner.contains(x2, y2);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        return this.combinationType == 4 ? this.outer.intersects(x2, y2, w2, h2) && this.inner.intersects(x2, y2, w2, h2) : this.outer.intersects(x2, y2, w2, h2) && !this.inner.contains(x2, y2, w2, h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        return this.combinationType == 4 ? this.outer.contains(x2, y2, w2, h2) && this.inner.contains(x2, y2, w2, h2) : this.outer.contains(x2, y2, w2, h2) && !this.inner.intersects(x2, y2, w2, h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        RectBounds b2 = this.outer.getBounds();
        if (this.combinationType == 4) {
            b2.intersectWith(this.inner.getBounds());
        }
        return b2;
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx, float flatness) {
        return new FlatteningPathIterator(getPathIterator(tx), flatness);
    }
}
