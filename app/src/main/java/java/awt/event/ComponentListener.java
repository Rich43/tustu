package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/ComponentListener.class */
public interface ComponentListener extends EventListener {
    void componentResized(ComponentEvent componentEvent);

    void componentMoved(ComponentEvent componentEvent);

    void componentShown(ComponentEvent componentEvent);

    void componentHidden(ComponentEvent componentEvent);
}
