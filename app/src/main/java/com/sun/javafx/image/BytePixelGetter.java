package com.sun.javafx.image;

import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/BytePixelGetter.class */
public interface BytePixelGetter extends PixelGetter<ByteBuffer> {
    int getArgb(byte[] bArr, int i2);

    int getArgbPre(byte[] bArr, int i2);
}
