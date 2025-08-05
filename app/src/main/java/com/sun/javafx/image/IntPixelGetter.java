package com.sun.javafx.image;

import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/IntPixelGetter.class */
public interface IntPixelGetter extends PixelGetter<IntBuffer> {
    int getArgb(int[] iArr, int i2);

    int getArgbPre(int[] iArr, int i2);
}
