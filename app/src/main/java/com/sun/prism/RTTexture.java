package com.sun.prism;

import java.nio.Buffer;

/* loaded from: jfxrt.jar:com/sun/prism/RTTexture.class */
public interface RTTexture extends Texture, RenderTarget {
    int[] getPixels();

    boolean readPixels(Buffer buffer);

    boolean readPixels(Buffer buffer, int i2, int i3, int i4, int i5);

    boolean isVolatile();
}
