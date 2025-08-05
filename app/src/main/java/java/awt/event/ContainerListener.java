package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/ContainerListener.class */
public interface ContainerListener extends EventListener {
    void componentAdded(ContainerEvent containerEvent);

    void componentRemoved(ContainerEvent containerEvent);
}
