package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/RoundRectangle2D.class */
public class RoundRectangle2D extends RectangularShape {

    /* renamed from: x, reason: collision with root package name */
    public float f11924x;

    /* renamed from: y, reason: collision with root package name */
    public float f11925y;
    public float width;
    public float height;
    public float arcWidth;
    public float arcHeight;

    public RoundRectangle2D() {
    }

    public RoundRectangle2D(float x2, float y2, float w2, float h2, float arcw, float arch) {
        setRoundRect(x2, y2, w2, h2, arcw, arch);
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getX() {
        return this.f11924x;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getY() {
        return this.f11925y;
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

    public void setRoundRect(float x2, float y2, float w2, float h2, float arcw, float arch) {
        this.f11924x = x2;
        this.f11925y = y2;
        this.width = w2;
        this.height = h2;
        this.arcWidth = arcw;
        this.arcHeight = arch;
    }

    @Override // com.sun.javafx.geom.RectangularShape, com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        return new RectBounds(this.f11924x, this.f11925y, this.f11924x + this.width, this.f11925y + this.height);
    }

    public void setRoundRect(RoundRectangle2D rr) {
        setRoundRect(rr.f11924x, rr.f11925y, rr.width, rr.height, rr.arcWidth, rr.arcHeight);
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public void setFrame(float x2, float y2, float w2, float h2) {
        setRoundRect(x2, y2, w2, h2, this.arcWidth, this.arcHeight);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        if (isEmpty()) {
            return false;
        }
        float rrx0 = this.f11924x;
        float rry0 = this.f11925y;
        float rrx1 = rrx0 + this.width;
        float rry1 = rry0 + this.height;
        if (x2 < rrx0 || y2 < rry0 || x2 >= rrx1 || y2 >= rry1) {
            return false;
        }
        float aw2 = Math.min(this.width, Math.abs(this.arcWidth)) / 2.0f;
        float ah2 = Math.min(this.height, Math.abs(this.arcHeight)) / 2.0f;
        float f2 = rrx0 + aw2;
        float rrx02 = f2;
        if (x2 >= f2) {
            float f3 = rrx1 - aw2;
            rrx02 = f3;
            if (x2 < f3) {
                return true;
            }
        }
        float f4 = rry0 + ah2;
        float rry02 = f4;
        if (y2 >= f4) {
            float f5 = rry1 - ah2;
            rry02 = f5;
            if (y2 < f5) {
                return true;
            }
        }
        float x3 = (x2 - rrx02) / aw2;
        float y3 = (y2 - rry02) / ah2;
        return ((double) ((x3 * x3) + (y3 * y3))) <= 1.0d;
    }

    private int classify(float coord, float left, float right, float arcsize) {
        if (coord < left) {
            return 0;
        }
        if (coord < left + arcsize) {
            return 1;
        }
        if (coord < right - arcsize) {
            return 2;
        }
        if (coord < right) {
            return 3;
        }
        return 4;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        if (isEmpty() || w2 <= 0.0f || h2 <= 0.0f) {
            return false;
        }
        float rrx0 = this.f11924x;
        float rry0 = this.f11925y;
        float rrx1 = rrx0 + this.width;
        float rry1 = rry0 + this.height;
        if (x2 + w2 <= rrx0 || x2 >= rrx1 || y2 + h2 <= rry0 || y2 >= rry1) {
            return false;
        }
        float aw2 = Math.min(this.width, Math.abs(this.arcWidth)) / 2.0f;
        float ah2 = Math.min(this.height, Math.abs(this.arcHeight)) / 2.0f;
        int x0class = classify(x2, rrx0, rrx1, aw2);
        int x1class = classify(x2 + w2, rrx0, rrx1, aw2);
        int y0class = classify(y2, rry0, rry1, ah2);
        int y1class = classify(y2 + h2, rry0, rry1, ah2);
        if (x0class == 2 || x1class == 2 || y0class == 2 || y1class == 2) {
            return true;
        }
        if (x0class < 2 && x1class > 2) {
            return true;
        }
        if (y0class < 2 && y1class > 2) {
            return true;
        }
        float x3 = x1class == 1 ? (x2 + w2) - (rrx0 + aw2) : x2 - (rrx1 - aw2);
        float y3 = y1class == 1 ? (y2 + h2) - (rry0 + ah2) : y2 - (rry1 - ah2);
        float x4 = x3 / aw2;
        float y4 = y3 / ah2;
        return (x4 * x4) + (y4 * y4) <= 1.0f;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        return !isEmpty() && w2 > 0.0f && h2 > 0.0f && contains(x2, y2) && contains(x2 + w2, y2) && contains(x2, y2 + h2) && contains(x2 + w2, y2 + h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        return new RoundRectIterator(this, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public RoundRectangle2D copy() {
        return new RoundRectangle2D(this.f11924x, this.f11925y, this.width, this.height, this.arcWidth, this.arcHeight);
    }

    public int hashCode() {
        int bits = Float.floatToIntBits(this.f11924x);
        return bits + (Float.floatToIntBits(this.f11925y) * 37) + (Float.floatToIntBits(this.width) * 43) + (Float.floatToIntBits(this.height) * 47) + (Float.floatToIntBits(this.arcWidth) * 53) + (Float.floatToIntBits(this.arcHeight) * 59);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof RoundRectangle2D) {
            RoundRectangle2D rr2d = (RoundRectangle2D) obj;
            return this.f11924x == rr2d.f11924x && this.f11925y == rr2d.f11925y && this.width == rr2d.width && this.height == rr2d.height && this.arcWidth == rr2d.arcWidth && this.arcHeight == rr2d.arcHeight;
        }
        return false;
    }
}
