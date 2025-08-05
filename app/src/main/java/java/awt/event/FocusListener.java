package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/FocusListener.class */
public interface FocusListener extends EventListener {
    void focusGained(FocusEvent focusEvent);

    void focusLost(FocusEvent focusEvent);
}
