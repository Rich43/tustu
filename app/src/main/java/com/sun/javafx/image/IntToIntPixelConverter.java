package com.sun.javafx.image;

import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/IntToIntPixelConverter.class */
public interface IntToIntPixelConverter extends PixelConverter<IntBuffer, IntBuffer> {
    void convert(int[] iArr, int i2, int i3, int[] iArr2, int i4, int i5, int i6, int i7);

    void convert(IntBuffer intBuffer, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7);

    void convert(int[] iArr, int i2, int i3, IntBuffer intBuffer, int i4, int i5, int i6, int i7);
}
