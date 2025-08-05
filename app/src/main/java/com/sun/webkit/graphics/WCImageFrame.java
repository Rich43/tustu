package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCImageFrame.class */
public abstract class WCImageFrame extends Ref {
    public abstract WCImage getFrame();

    public abstract int[] getSize();

    protected void destroyDecodedData() {
    }
}
