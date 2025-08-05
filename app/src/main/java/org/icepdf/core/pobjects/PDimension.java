package org.icepdf.core.pobjects;

import java.awt.Dimension;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/PDimension.class */
public class PDimension {
    private float width;
    private float height;

    public PDimension(float w2, float h2) {
        set(w2, h2);
    }

    public PDimension(int w2, int h2) {
        set(w2, h2);
    }

    public void set(float w2, float h2) {
        this.width = w2;
        this.height = h2;
    }

    public void set(int w2, int h2) {
        this.width = w2;
        this.height = h2;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public Dimension toDimension() {
        return new Dimension((int) this.width, (int) this.height);
    }

    public String toString() {
        return "PDimension { width=" + this.width + ", height=" + this.height + " }";
    }
}
