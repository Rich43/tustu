package com.sun.prism;

import com.sun.javafx.geom.Rectangle;

/* loaded from: jfxrt.jar:com/sun/prism/Presentable.class */
public interface Presentable extends RenderTarget {
    boolean lockResources(PresentableState presentableState);

    boolean prepare(Rectangle rectangle);

    boolean present();

    float getPixelScaleFactor();
}
