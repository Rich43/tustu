package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/WindowListener.class */
public interface WindowListener extends EventListener {
    void windowOpened(WindowEvent windowEvent);

    void windowClosing(WindowEvent windowEvent);

    void windowClosed(WindowEvent windowEvent);

    void windowIconified(WindowEvent windowEvent);

    void windowDeiconified(WindowEvent windowEvent);

    void windowActivated(WindowEvent windowEvent);

    void windowDeactivated(WindowEvent windowEvent);
}
