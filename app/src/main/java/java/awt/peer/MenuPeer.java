package java.awt.peer;

import java.awt.MenuItem;

/* loaded from: rt.jar:java/awt/peer/MenuPeer.class */
public interface MenuPeer extends MenuItemPeer {
    void addSeparator();

    void addItem(MenuItem menuItem);

    void delItem(int i2);
}
