package java.awt.peer;

import java.awt.Menu;

/* loaded from: rt.jar:java/awt/peer/MenuBarPeer.class */
public interface MenuBarPeer extends MenuComponentPeer {
    void addMenu(Menu menu);

    void delMenu(int i2);

    void addHelpMenu(Menu menu);
}
