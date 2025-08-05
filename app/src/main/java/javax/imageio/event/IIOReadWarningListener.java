package javax.imageio.event;

import java.util.EventListener;
import javax.imageio.ImageReader;

/* loaded from: rt.jar:javax/imageio/event/IIOReadWarningListener.class */
public interface IIOReadWarningListener extends EventListener {
    void warningOccurred(ImageReader imageReader, String str);
}
