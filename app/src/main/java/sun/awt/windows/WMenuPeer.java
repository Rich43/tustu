package sun.awt.windows;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuContainer;
import java.awt.MenuItem;
import java.awt.peer.MenuPeer;

/* loaded from: rt.jar:sun/awt/windows/WMenuPeer.class */
class WMenuPeer extends WMenuItemPeer implements MenuPeer {
    @Override // java.awt.peer.MenuPeer
    public native void addSeparator();

    @Override // java.awt.peer.MenuPeer
    public native void delItem(int i2);

    native void createMenu(WMenuBarPeer wMenuBarPeer);

    native void createSubMenu(WMenuPeer wMenuPeer);

    @Override // java.awt.peer.MenuPeer
    public void addItem(MenuItem menuItem) {
    }

    WMenuPeer() {
    }

    WMenuPeer(Menu menu) {
        this.target = menu;
        MenuContainer parent = menu.getParent();
        if (parent instanceof MenuBar) {
            WMenuBarPeer wMenuBarPeer = (WMenuBarPeer) WToolkit.targetToPeer(parent);
            this.parent = wMenuBarPeer;
            wMenuBarPeer.addChildPeer(this);
            createMenu(wMenuBarPeer);
        } else if (parent instanceof Menu) {
            this.parent = (WMenuPeer) WToolkit.targetToPeer(parent);
            this.parent.addChildPeer(this);
            createSubMenu(this.parent);
        } else {
            throw new IllegalArgumentException("unknown menu container class");
        }
        checkMenuCreation();
    }
}
