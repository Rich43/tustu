package sun.awt.windows;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.peer.MenuBarPeer;

/* loaded from: rt.jar:sun/awt/windows/WMenuBarPeer.class */
final class WMenuBarPeer extends WMenuPeer implements MenuBarPeer {
    final WFramePeer framePeer;

    @Override // java.awt.peer.MenuBarPeer
    public native void addMenu(Menu menu);

    @Override // java.awt.peer.MenuBarPeer
    public native void delMenu(int i2);

    native void create(WFramePeer wFramePeer);

    @Override // java.awt.peer.MenuBarPeer
    public void addHelpMenu(Menu menu) {
        addMenu(menu);
    }

    WMenuBarPeer(MenuBar menuBar) {
        this.target = menuBar;
        this.framePeer = (WFramePeer) WToolkit.targetToPeer(menuBar.getParent());
        if (this.framePeer != null) {
            this.framePeer.addChildPeer(this);
        }
        create(this.framePeer);
        checkMenuCreation();
    }
}
