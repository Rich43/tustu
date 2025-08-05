package com.sun.prism;

import com.sun.javafx.geom.Rectangle;

/* loaded from: jfxrt.jar:com/sun/prism/ReadbackGraphics.class */
public interface ReadbackGraphics extends Graphics {
    boolean canReadBack();

    RTTexture readBack(Rectangle rectangle);

    void releaseReadBackBuffer(RTTexture rTTexture);
}
