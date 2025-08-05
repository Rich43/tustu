package com.sun.javafx.image;

import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/BytePixelSetter.class */
public interface BytePixelSetter extends PixelSetter<ByteBuffer> {
    void setArgb(byte[] bArr, int i2, int i3);

    void setArgbPre(byte[] bArr, int i2, int i3);
}
