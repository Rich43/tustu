package java.awt.peer;

import java.awt.Window;
import java.util.List;

/* loaded from: rt.jar:java/awt/peer/DialogPeer.class */
public interface DialogPeer extends WindowPeer {
    void setTitle(String str);

    void setResizable(boolean z2);

    void blockWindows(List<Window> list);
}
