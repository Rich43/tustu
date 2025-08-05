package sun.awt;

import java.awt.Point;
import java.awt.Window;
import java.awt.peer.MouseInfoPeer;

/* loaded from: rt.jar:sun/awt/DefaultMouseInfoPeer.class */
public class DefaultMouseInfoPeer implements MouseInfoPeer {
    @Override // java.awt.peer.MouseInfoPeer
    public native int fillPointWithCoords(Point point);

    @Override // java.awt.peer.MouseInfoPeer
    public native boolean isWindowUnderMouse(Window window);

    DefaultMouseInfoPeer() {
    }
}
