package java.awt.peer;

import java.awt.Component;
import java.awt.Window;

/* loaded from: rt.jar:java/awt/peer/KeyboardFocusManagerPeer.class */
public interface KeyboardFocusManagerPeer {
    void setCurrentFocusedWindow(Window window);

    Window getCurrentFocusedWindow();

    void setCurrentFocusOwner(Component component);

    Component getCurrentFocusOwner();

    void clearGlobalFocusOwner(Window window);
}
