package javax.imageio.event;

import java.awt.image.BufferedImage;
import java.util.EventListener;
import javax.imageio.ImageReader;

/* loaded from: rt.jar:javax/imageio/event/IIOReadUpdateListener.class */
public interface IIOReadUpdateListener extends EventListener {
    void passStarted(ImageReader imageReader, BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int[] iArr);

    void imageUpdate(ImageReader imageReader, BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr);

    void passComplete(ImageReader imageReader, BufferedImage bufferedImage);

    void thumbnailPassStarted(ImageReader imageReader, BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int[] iArr);

    void thumbnailUpdate(ImageReader imageReader, BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr);

    void thumbnailPassComplete(ImageReader imageReader, BufferedImage bufferedImage);
}
