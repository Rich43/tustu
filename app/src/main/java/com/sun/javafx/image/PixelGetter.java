package com.sun.javafx.image;

import java.nio.Buffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/PixelGetter.class */
public interface PixelGetter<T extends Buffer> {
    AlphaType getAlphaType();

    int getNumElements();

    int getArgb(T t2, int i2);

    int getArgbPre(T t2, int i2);
}
