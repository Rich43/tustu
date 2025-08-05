package com.sun.javafx.image;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/IntToBytePixelConverter.class */
public interface IntToBytePixelConverter extends PixelConverter<IntBuffer, ByteBuffer> {
    void convert(int[] iArr, int i2, int i3, byte[] bArr, int i4, int i5, int i6, int i7);

    void convert(IntBuffer intBuffer, int i2, int i3, byte[] bArr, int i4, int i5, int i6, int i7);

    void convert(int[] iArr, int i2, int i3, ByteBuffer byteBuffer, int i4, int i5, int i6, int i7);
}
