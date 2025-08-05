package com.sun.glass.ui;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Size.class */
public final class Size {
    public int width;
    public int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Size() {
        this(0, 0);
    }

    public String toString() {
        return "Size(" + this.width + ", " + this.height + ")";
    }
}
