package java.awt.peer;

import java.awt.MenuBar;
import java.awt.Rectangle;

/* loaded from: rt.jar:java/awt/peer/FramePeer.class */
public interface FramePeer extends WindowPeer {
    void setTitle(String str);

    void setMenuBar(MenuBar menuBar);

    void setResizable(boolean z2);

    void setState(int i2);

    int getState();

    void setMaximizedBounds(Rectangle rectangle);

    void setBoundsPrivate(int i2, int i3, int i4, int i5);

    Rectangle getBoundsPrivate();

    void emulateActivation(boolean z2);
}
