package com.sun.prism.image;

/* loaded from: jfxrt.jar:com/sun/prism/image/ViewPort.class */
public class ViewPort {
    public float u0;
    public float v0;
    public float u1;
    public float v1;

    public ViewPort(float u2, float v2, float du, float dv) {
        this.u0 = u2;
        this.u1 = u2 + du;
        this.v0 = v2;
        this.v1 = v2 + dv;
    }

    public ViewPort getScaledVersion(float pixelScale) {
        if (pixelScale == 1.0f) {
            return this;
        }
        float newu0 = this.u0 * pixelScale;
        float newv0 = this.v0 * pixelScale;
        float newu1 = this.u1 * pixelScale;
        float newv1 = this.v1 * pixelScale;
        return new ViewPort(newu0, newv0, newu1 - newu0, newv1 - newv0);
    }

    public float getRelX(float u2) {
        return (u2 - this.u0) / (this.u1 - this.u0);
    }

    public float getRelY(float v2) {
        return (v2 - this.v0) / (this.v1 - this.v0);
    }

    public Coords getClippedCoords(float iw, float ih, float w2, float h2) {
        Coords cr = new Coords(w2, h2, this);
        if (this.u1 > iw || this.u0 < 0.0f) {
            if (this.u0 >= iw || this.u1 <= 0.0f) {
                return null;
            }
            if (this.u1 > iw) {
                cr.x1 = w2 * getRelX(iw);
                cr.u1 = iw;
            }
            if (this.u0 < 0.0f) {
                cr.x0 = w2 * getRelX(0.0f);
                cr.u0 = 0.0f;
            }
        }
        if (this.v1 > ih || this.v0 < 0.0f) {
            if (this.v0 >= ih || this.v1 <= 0.0f) {
                return null;
            }
            if (this.v1 > ih) {
                cr.y1 = h2 * getRelY(ih);
                cr.v1 = ih;
            }
            if (this.v0 < 0.0f) {
                cr.y0 = h2 * getRelY(0.0f);
                cr.v0 = 0.0f;
            }
        }
        return cr;
    }
}
