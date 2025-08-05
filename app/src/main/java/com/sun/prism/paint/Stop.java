package com.sun.prism.paint;

/* loaded from: jfxrt.jar:com/sun/prism/paint/Stop.class */
public class Stop {
    private final Color color;
    private final float offset;

    public Stop(Color color, float offset) {
        this.color = color;
        this.offset = offset;
    }

    public Color getColor() {
        return this.color;
    }

    public float getOffset() {
        return this.offset;
    }
}
