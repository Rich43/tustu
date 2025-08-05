package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCSize.class */
public final class WCSize {
    private final float width;
    private final float height;

    public WCSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public int getIntWidth() {
        return (int) this.width;
    }

    public int getIntHeight() {
        return (int) this.height;
    }
}
