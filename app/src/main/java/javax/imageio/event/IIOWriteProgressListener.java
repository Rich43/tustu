package javax.imageio.event;

import java.util.EventListener;
import javax.imageio.ImageWriter;

/* loaded from: rt.jar:javax/imageio/event/IIOWriteProgressListener.class */
public interface IIOWriteProgressListener extends EventListener {
    void imageStarted(ImageWriter imageWriter, int i2);

    void imageProgress(ImageWriter imageWriter, float f2);

    void imageComplete(ImageWriter imageWriter);

    void thumbnailStarted(ImageWriter imageWriter, int i2, int i3);

    void thumbnailProgress(ImageWriter imageWriter, float f2);

    void thumbnailComplete(ImageWriter imageWriter);

    void writeAborted(ImageWriter imageWriter);
}
