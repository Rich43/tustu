package javax.imageio.event;

import java.util.EventListener;
import javax.imageio.ImageWriter;

/* loaded from: rt.jar:javax/imageio/event/IIOWriteWarningListener.class */
public interface IIOWriteWarningListener extends EventListener {
    void warningOccurred(ImageWriter imageWriter, int i2, String str);
}
