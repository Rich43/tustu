package javax.imageio.event;

import java.util.EventListener;
import javax.imageio.ImageReader;

/* loaded from: rt.jar:javax/imageio/event/IIOReadProgressListener.class */
public interface IIOReadProgressListener extends EventListener {
    void sequenceStarted(ImageReader imageReader, int i2);

    void sequenceComplete(ImageReader imageReader);

    void imageStarted(ImageReader imageReader, int i2);

    void imageProgress(ImageReader imageReader, float f2);

    void imageComplete(ImageReader imageReader);

    void thumbnailStarted(ImageReader imageReader, int i2, int i3);

    void thumbnailProgress(ImageReader imageReader, float f2);

    void thumbnailComplete(ImageReader imageReader);

    void readAborted(ImageReader imageReader);
}
