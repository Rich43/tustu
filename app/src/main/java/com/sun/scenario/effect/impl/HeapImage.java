package com.sun.scenario.effect.impl;

import com.sun.scenario.effect.Filterable;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/HeapImage.class */
public interface HeapImage extends Filterable {
    int getScanlineStride();

    int[] getPixelArray();
}
