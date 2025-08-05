package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCPageBackBuffer.class */
public abstract class WCPageBackBuffer extends Ref {
    public abstract WCGraphicsContext createGraphics();

    public abstract void disposeGraphics(WCGraphicsContext wCGraphicsContext);

    public abstract void flush(WCGraphicsContext wCGraphicsContext, int i2, int i3, int i4, int i5);

    protected abstract void copyArea(int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract boolean validate(int i2, int i3);
}
