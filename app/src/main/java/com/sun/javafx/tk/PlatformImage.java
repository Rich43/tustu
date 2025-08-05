package com.sun.javafx.tk;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/PlatformImage.class */
public interface PlatformImage {
    float getPixelScale();

    int getArgb(int i2, int i3);

    void setArgb(int i2, int i3, int i4);

    PixelFormat getPlatformPixelFormat();

    boolean isWritable();

    PlatformImage promoteToWritableImage();

    <T extends Buffer> void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<T> writablePixelFormat, T t2, int i6);

    void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] bArr, int i6, int i7);

    void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] iArr, int i6, int i7);

    <T extends Buffer> void setPixels(int i2, int i3, int i4, int i5, PixelFormat<T> pixelFormat, T t2, int i6);

    void setPixels(int i2, int i3, int i4, int i5, PixelFormat<ByteBuffer> pixelFormat, byte[] bArr, int i6, int i7);

    void setPixels(int i2, int i3, int i4, int i5, PixelFormat<IntBuffer> pixelFormat, int[] iArr, int i6, int i7);

    void setPixels(int i2, int i3, int i4, int i5, PixelReader pixelReader, int i6, int i7);
}
