package com.sun.prism.impl;

import com.sun.prism.PixelFormat;

/* loaded from: jfxrt.jar:com/sun/prism/impl/TextureResourcePool.class */
public interface TextureResourcePool<T> extends ResourcePool<T> {
    long estimateTextureSize(int i2, int i3, PixelFormat pixelFormat);

    long estimateRTTextureSize(int i2, int i3, boolean z2);
}
