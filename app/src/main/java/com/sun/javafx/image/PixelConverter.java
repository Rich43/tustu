package com.sun.javafx.image;

import java.nio.Buffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/PixelConverter.class */
public interface PixelConverter<T extends Buffer, U extends Buffer> {
    void convert(T t2, int i2, int i3, U u2, int i4, int i5, int i6, int i7);

    PixelGetter<T> getGetter();

    PixelSetter<U> getSetter();
}
