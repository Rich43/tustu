package sun.awt.windows;

import java.io.FileInputStream;
import java.io.IOException;
import sun.awt.PeerEvent;
import sun.awt.SunToolkit;
import sun.awt.dnd.SunDropTargetContextPeer;
import sun.awt.dnd.SunDropTargetEvent;

/* loaded from: rt.jar:sun/awt/windows/WDropTargetContextPeer.class */
final class WDropTargetContextPeer extends SunDropTargetContextPeer {
    private native Object getData(long j2, long j3);

    private native void dropDone(long j2, boolean z2, int i2);

    static WDropTargetContextPeer getWDropTargetContextPeer() {
        return new WDropTargetContextPeer();
    }

    private WDropTargetContextPeer() {
    }

    private static FileInputStream getFileStream(String str, long j2) throws IOException {
        return new WDropTargetContextPeerFileStream(str, j2);
    }

    private static Object getIStream(long j2) throws IOException {
        return new WDropTargetContextPeerIStream(j2);
    }

    @Override // sun.awt.dnd.SunDropTargetContextPeer
    protected Object getNativeData(long j2) {
        return getData(getNativeDragContext(), j2);
    }

    @Override // sun.awt.dnd.SunDropTargetContextPeer
    protected void doDropDone(boolean z2, int i2, boolean z3) {
        dropDone(getNativeDragContext(), z2, i2);
    }

    @Override // sun.awt.dnd.SunDropTargetContextPeer
    protected void eventPosted(final SunDropTargetEvent sunDropTargetEvent) {
        if (sunDropTargetEvent.getID() != 502) {
            SunToolkit.executeOnEventHandlerThread(new PeerEvent(sunDropTargetEvent.getSource(), new Runnable() { // from class: sun.awt.windows.WDropTargetContextPeer.1
                @Override // java.lang.Runnable
                public void run() {
                    sunDropTargetEvent.getDispatcher().unregisterAllEvents();
                }
            }, 0L));
        }
    }
}
