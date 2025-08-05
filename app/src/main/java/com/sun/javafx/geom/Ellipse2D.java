package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Ellipse2D.class */
public class Ellipse2D extends RectangularShape {

    /* renamed from: x, reason: collision with root package name */
    public float f11899x;

    /* renamed from: y, reason: collision with root package name */
    public float f11900y;
    public float width;
    public float height;

    public Ellipse2D() {
    }

    public Ellipse2D(float x2, float y2, float w2, float h2) {
        setFrame(x2, y2, w2, h2);
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getX() {
        return this.f11899x;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getY() {
        return this.f11900y;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getWidth() {
        return this.width;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getHeight() {
        return this.height;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public boolean isEmpty() {
        return this.width <= 0.0f || this.height <= 0.0f;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public void setFrame(float x2, float y2, float w2, float h2) {
        this.f11899x = x2;
        this.f11900y = y2;
        this.width = w2;
        this.height = h2;
    }

    @Override // com.sun.javafx.geom.RectangularShape, com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        return new RectBounds(this.f11899x, this.f11900y, this.f11899x + this.width, this.f11900y + this.height);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        float ellw = this.width;
        if (ellw <= 0.0f) {
            return false;
        }
        float normx = ((x2 - this.f11899x) / ellw) - 0.5f;
        float ellh = this.height;
        if (ellh <= 0.0f) {
            return false;
        }
        float normy = ((y2 - this.f11900y) / ellh) - 0.5f;
        return (normx * normx) + (normy * normy) < 0.25f;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        float nearx;
        float neary;
        if (w2 <= 0.0f || h2 <= 0.0f) {
            return false;
        }
        float ellw = this.width;
        if (ellw <= 0.0f) {
            return false;
        }
        float normx0 = ((x2 - this.f11899x) / ellw) - 0.5f;
        float normx1 = normx0 + (w2 / ellw);
        float ellh = this.height;
        if (ellh <= 0.0f) {
            return false;
        }
        float normy0 = ((y2 - this.f11900y) / ellh) - 0.5f;
        float normy1 = normy0 + (h2 / ellh);
        if (normx0 > 0.0f) {
            nearx = normx0;
        } else if (normx1 < 0.0f) {
            nearx = normx1;
        } else {
            nearx = 0.0f;
        }
        if (normy0 > 0.0f) {
            neary = normy0;
        } else if (normy1 < 0.0f) {
            neary = normy1;
        } else {
            neary = 0.0f;
        }
        return (nearx * nearx) + (neary * neary) < 0.25f;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        return contains(x2, y2) && contains(x2 + w2, y2) && contains(x2, y2 + h2) && contains(x2 + w2, y2 + h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        return new EllipseIterator(this, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public Ellipse2D copy() {
        return new Ellipse2D(this.f11899x, this.f11900y, this.width, this.height);
    }

    public int hashCode() {
        int bits = Float.floatToIntBits(this.f11899x);
        return bits + (Float.floatToIntBits(this.f11900y) * 37) + (Float.floatToIntBits(this.width) * 43) + (Float.floatToIntBits(this.height) * 47);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Ellipse2D) {
            Ellipse2D e2d = (Ellipse2D) obj;
            return this.f11899x == e2d.f11899x && this.f11900y == e2d.f11900y && this.width == e2d.width && this.height == e2d.height;
        }
        return false;
    }
}
