package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/RectangularShape.class */
public abstract class RectangularShape extends Shape {
    public abstract float getX();

    public abstract float getY();

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract boolean isEmpty();

    public abstract void setFrame(float f2, float f3, float f4, float f5);

    protected RectangularShape() {
    }

    public float getMinX() {
        return getX();
    }

    public float getMinY() {
        return getY();
    }

    public float getMaxX() {
        return getX() + getWidth();
    }

    public float getMaxY() {
        return getY() + getHeight();
    }

    public float getCenterX() {
        return getX() + (getWidth() / 2.0f);
    }

    public float getCenterY() {
        return getY() + (getHeight() / 2.0f);
    }

    public void setFrame(Point2D loc, Dimension2D size) {
        setFrame(loc.f11907x, loc.f11908y, size.width, size.height);
    }

    public void setFrameFromDiagonal(float x1, float y1, float x2, float y2) {
        if (x2 < x1) {
            x1 = x2;
            x2 = x1;
        }
        if (y2 < y1) {
            y1 = y2;
            y2 = y1;
        }
        setFrame(x1, y1, x2 - x1, y2 - y1);
    }

    public void setFrameFromDiagonal(Point2D p1, Point2D p2) {
        setFrameFromDiagonal(p1.f11907x, p1.f11908y, p2.f11907x, p2.f11908y);
    }

    public void setFrameFromCenter(float centerX, float centerY, float cornerX, float cornerY) {
        float halfW = Math.abs(cornerX - centerX);
        float halfH = Math.abs(cornerY - centerY);
        setFrame(centerX - halfW, centerY - halfH, halfW * 2.0f, halfH * 2.0f);
    }

    public void setFrameFromCenter(Point2D center, Point2D corner) {
        setFrameFromCenter(center.f11907x, center.f11908y, corner.f11907x, corner.f11908y);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(Point2D p2) {
        return contains(p2.f11907x, p2.f11908y);
    }

    @Override // com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        float width = getWidth();
        float height = getHeight();
        if (width < 0.0f || height < 0.0f) {
            return new RectBounds();
        }
        float x2 = getX();
        float y2 = getY();
        float x1 = (float) Math.floor(x2);
        float y1 = (float) Math.floor(y2);
        float x22 = (float) Math.ceil(x2 + width);
        float y22 = (float) Math.ceil(y2 + height);
        return new RectBounds(x1, y1, x22, y22);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx, float flatness) {
        return new FlatteningPathIterator(getPathIterator(tx), flatness);
    }

    public String toString() {
        return getClass().getName() + "[x=" + getX() + ",y=" + getY() + ",w=" + getWidth() + ",h=" + getHeight() + "]";
    }
}
