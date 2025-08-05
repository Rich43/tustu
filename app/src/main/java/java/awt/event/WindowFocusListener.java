package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/WindowFocusListener.class */
public interface WindowFocusListener extends EventListener {
    void windowGainedFocus(WindowEvent windowEvent);

    void windowLostFocus(WindowEvent windowEvent);
}
