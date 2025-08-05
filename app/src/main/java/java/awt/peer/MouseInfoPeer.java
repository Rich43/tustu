package java.awt.peer;

import java.awt.Point;
import java.awt.Window;

/* loaded from: rt.jar:java/awt/peer/MouseInfoPeer.class */
public interface MouseInfoPeer {
    int fillPointWithCoords(Point point);

    boolean isWindowUnderMouse(Window window);
}
