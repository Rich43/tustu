package com.sun.javafx.image;

import java.nio.Buffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/PixelSetter.class */
public interface PixelSetter<T extends Buffer> {
    AlphaType getAlphaType();

    int getNumElements();

    void setArgb(T t2, int i2, int i3);

    void setArgbPre(T t2, int i2, int i3);
}
