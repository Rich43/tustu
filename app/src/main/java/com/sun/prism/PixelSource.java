package com.sun.prism;

import com.sun.glass.ui.Pixels;

/* loaded from: jfxrt.jar:com/sun/prism/PixelSource.class */
public interface PixelSource {
    Pixels getLatestPixels();

    void doneWithPixels(Pixels pixels);

    void skipLatestPixels();
}
