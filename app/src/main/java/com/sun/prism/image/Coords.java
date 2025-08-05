package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.Texture;

/* loaded from: jfxrt.jar:com/sun/prism/image/Coords.class */
public class Coords {
    float x0;
    float y0;
    float x1;
    float y1;
    float u0;
    float v0;
    float u1;
    float v1;

    public Coords(float w2, float h2, ViewPort v2) {
        this.x0 = 0.0f;
        this.x1 = w2;
        this.y0 = 0.0f;
        this.y1 = h2;
        this.u0 = v2.u0;
        this.u1 = v2.u1;
        this.v0 = v2.v0;
        this.v1 = v2.v1;
    }

    public Coords() {
    }

    public void draw(Texture t2, Graphics g2, float x2, float y2) {
        g2.drawTexture(t2, x2 + this.x0, y2 + this.y0, x2 + this.x1, y2 + this.y1, this.u0, this.v0, this.u1, this.v1);
    }

    public float getX(float u2) {
        return ((this.x0 * (this.u1 - u2)) + (this.x1 * (u2 - this.u0))) / (this.u1 - this.u0);
    }

    public float getY(float v2) {
        return ((this.y0 * (this.v1 - v2)) + (this.y1 * (v2 - this.v0))) / (this.v1 - this.v0);
    }
}
