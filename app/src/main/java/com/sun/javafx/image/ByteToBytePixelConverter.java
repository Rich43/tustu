package com.sun.javafx.image;

import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/ByteToBytePixelConverter.class */
public interface ByteToBytePixelConverter extends PixelConverter<ByteBuffer, ByteBuffer> {
    void convert(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5, int i6, int i7);

    void convert(ByteBuffer byteBuffer, int i2, int i3, byte[] bArr, int i4, int i5, int i6, int i7);

    void convert(byte[] bArr, int i2, int i3, ByteBuffer byteBuffer, int i4, int i5, int i6, int i7);
}
