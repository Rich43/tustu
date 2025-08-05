package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCRectangle.class */
public final class WCRectangle {

    /* renamed from: x, reason: collision with root package name */
    float f12058x;

    /* renamed from: y, reason: collision with root package name */
    float f12059y;

    /* renamed from: w, reason: collision with root package name */
    float f12060w;

    /* renamed from: h, reason: collision with root package name */
    float f12061h;

    public WCRectangle(float x2, float y2, float w2, float h2) {
        this.f12058x = x2;
        this.f12059y = y2;
        this.f12060w = w2;
        this.f12061h = h2;
    }

    public WCRectangle(WCRectangle r2) {
        this.f12058x = r2.f12058x;
        this.f12059y = r2.f12059y;
        this.f12060w = r2.f12060w;
        this.f12061h = r2.f12061h;
    }

    public WCRectangle() {
    }

    public float getX() {
        return this.f12058x;
    }

    public int getIntX() {
        return (int) this.f12058x;
    }

    public float getY() {
        return this.f12059y;
    }

    public int getIntY() {
        return (int) this.f12059y;
    }

    public float getWidth() {
        return this.f12060w;
    }

    public int getIntWidth() {
        return (int) this.f12060w;
    }

    public float getHeight() {
        return this.f12061h;
    }

    public int getIntHeight() {
        return (int) this.f12061h;
    }

    public boolean contains(WCRectangle r2) {
        return this.f12058x <= r2.f12058x && this.f12058x + this.f12060w >= r2.f12058x + r2.f12060w && this.f12059y <= r2.f12059y && this.f12059y + this.f12061h >= r2.f12059y + r2.f12061h;
    }

    public WCRectangle intersection(WCRectangle r2) {
        float tx1 = this.f12058x;
        float ty1 = this.f12059y;
        float rx1 = r2.f12058x;
        float ry1 = r2.f12059y;
        float tx2 = tx1 + this.f12060w;
        float ty2 = ty1 + this.f12061h;
        float rx2 = rx1 + r2.f12060w;
        float ry2 = ry1 + r2.f12061h;
        if (tx1 < rx1) {
            tx1 = rx1;
        }
        if (ty1 < ry1) {
            ty1 = ry1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        float tx22 = tx2 - tx1;
        float ty22 = ty2 - ty1;
        if (tx22 < Float.MIN_VALUE) {
            tx22 = Float.MIN_VALUE;
        }
        if (ty22 < Float.MIN_VALUE) {
            ty22 = Float.MIN_VALUE;
        }
        return new WCRectangle(tx1, ty1, tx22, ty22);
    }

    public void translate(float dx, float dy) {
        float oldv = this.f12058x;
        float newv = oldv + dx;
        if (dx < 0.0f) {
            if (newv > oldv) {
                if (this.f12060w >= 0.0f) {
                    this.f12060w += newv - Float.MIN_VALUE;
                }
                newv = Float.MIN_VALUE;
            }
        } else if (newv < oldv) {
            if (this.f12060w >= 0.0f) {
                this.f12060w += newv - Float.MAX_VALUE;
                if (this.f12060w < 0.0f) {
                    this.f12060w = Float.MAX_VALUE;
                }
            }
            newv = Float.MAX_VALUE;
        }
        this.f12058x = newv;
        float oldv2 = this.f12059y;
        float newv2 = oldv2 + dy;
        if (dy < 0.0f) {
            if (newv2 > oldv2) {
                if (this.f12061h >= 0.0f) {
                    this.f12061h += newv2 - Float.MIN_VALUE;
                }
                newv2 = Float.MIN_VALUE;
            }
        } else if (newv2 < oldv2) {
            if (this.f12061h >= 0.0f) {
                this.f12061h += newv2 - Float.MAX_VALUE;
                if (this.f12061h < 0.0f) {
                    this.f12061h = Float.MAX_VALUE;
                }
            }
            newv2 = Float.MAX_VALUE;
        }
        this.f12059y = newv2;
    }

    public WCRectangle createUnion(WCRectangle r2) {
        WCRectangle dest = new WCRectangle();
        union(this, r2, dest);
        return dest;
    }

    public static void union(WCRectangle src1, WCRectangle src2, WCRectangle dest) {
        float x1 = Math.min(src1.getMinX(), src2.getMinX());
        float y1 = Math.min(src1.getMinY(), src2.getMinY());
        float x2 = Math.max(src1.getMaxX(), src2.getMaxX());
        float y2 = Math.max(src1.getMaxY(), src2.getMaxY());
        dest.setFrameFromDiagonal(x1, y1, x2, y2);
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

    public void setFrame(float x2, float y2, float w2, float h2) {
        this.f12058x = x2;
        this.f12059y = y2;
        this.f12060w = w2;
        this.f12061h = h2;
    }

    public float getMinX() {
        return getX();
    }

    public float getMaxX() {
        return getX() + getWidth();
    }

    public float getMinY() {
        return getY();
    }

    public float getMaxY() {
        return getY() + getHeight();
    }

    public boolean isEmpty() {
        return this.f12060w <= 0.0f || this.f12061h <= 0.0f;
    }

    public boolean equals(Object obj) {
        if (obj instanceof WCRectangle) {
            WCRectangle rc = (WCRectangle) obj;
            return this.f12058x == rc.f12058x && this.f12059y == rc.f12059y && this.f12060w == rc.f12060w && this.f12061h == rc.f12061h;
        }
        return super.equals(obj);
    }

    public String toString() {
        return "WCRectangle{x:" + this.f12058x + " y:" + this.f12059y + " w:" + this.f12060w + " h:" + this.f12061h + "}";
    }
}
