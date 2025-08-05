package sun.awt.windows;

import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.peer.SystemTrayPeer;

/* loaded from: rt.jar:sun/awt/windows/WSystemTrayPeer.class */
final class WSystemTrayPeer extends WObjectPeer implements SystemTrayPeer {
    WSystemTrayPeer(SystemTray systemTray) {
        this.target = systemTray;
    }

    @Override // java.awt.peer.SystemTrayPeer
    public Dimension getTrayIconSize() {
        return new Dimension(16, 16);
    }

    public boolean isSupported() {
        return ((WToolkit) Toolkit.getDefaultToolkit()).isTraySupported();
    }

    @Override // sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
    }
}
