package java.awt.peer;

import java.awt.Dialog;

/* loaded from: rt.jar:java/awt/peer/WindowPeer.class */
public interface WindowPeer extends ContainerPeer {
    void toFront();

    void toBack();

    void updateAlwaysOnTopState();

    void updateFocusableWindowState();

    void setModalBlocked(Dialog dialog, boolean z2);

    void updateMinimumSize();

    void updateIconImages();

    void setOpacity(float f2);

    void setOpaque(boolean z2);

    void updateWindow();

    void repositionSecurityWarning();
}
