package javafx.scene.image;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:javafx/scene/image/PixelWriter.class */
public interface PixelWriter {
    PixelFormat getPixelFormat();

    void setArgb(int i2, int i3, int i4);

    void setColor(int i2, int i3, Color color);

    <T extends Buffer> void setPixels(int i2, int i3, int i4, int i5, PixelFormat<T> pixelFormat, T t2, int i6);

    void setPixels(int i2, int i3, int i4, int i5, PixelFormat<ByteBuffer> pixelFormat, byte[] bArr, int i6, int i7);

    void setPixels(int i2, int i3, int i4, int i5, PixelFormat<IntBuffer> pixelFormat, int[] iArr, int i6, int i7);

    void setPixels(int i2, int i3, int i4, int i5, PixelReader pixelReader, int i6, int i7);
}
