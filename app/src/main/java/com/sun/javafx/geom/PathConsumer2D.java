package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/PathConsumer2D.class */
public interface PathConsumer2D {
    void moveTo(float f2, float f3);

    void lineTo(float f2, float f3);

    void quadTo(float f2, float f3, float f4, float f5);

    void curveTo(float f2, float f3, float f4, float f5, float f6, float f7);

    void closePath();

    void pathDone();
}
