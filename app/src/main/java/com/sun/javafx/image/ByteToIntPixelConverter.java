package com.sun.javafx.image;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/ByteToIntPixelConverter.class */
public interface ByteToIntPixelConverter extends PixelConverter<ByteBuffer, IntBuffer> {
    void convert(byte[] bArr, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7);

    void convert(ByteBuffer byteBuffer, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7);

    void convert(byte[] bArr, int i2, int i3, IntBuffer intBuffer, int i4, int i5, int i6, int i7);
}
