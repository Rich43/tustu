package javafx.scene.image;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:javafx/scene/image/PixelReader.class */
public interface PixelReader {
    PixelFormat getPixelFormat();

    int getArgb(int i2, int i3);

    Color getColor(int i2, int i3);

    <T extends Buffer> void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<T> writablePixelFormat, T t2, int i6);

    void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] bArr, int i6, int i7);

    void getPixels(int i2, int i3, int i4, int i5, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] iArr, int i6, int i7);
}
