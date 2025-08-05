package sun.awt.windows;

import sun.awt.Mutex;
import sun.awt.datatransfer.ToolkitThreadBlockedHandler;

/* compiled from: WDataTransferer.java */
/* loaded from: rt.jar:sun/awt/windows/WToolkitThreadBlockedHandler.class */
final class WToolkitThreadBlockedHandler extends Mutex implements ToolkitThreadBlockedHandler {
    private native void startSecondaryEventLoop();

    WToolkitThreadBlockedHandler() {
    }

    @Override // sun.awt.datatransfer.ToolkitThreadBlockedHandler
    public void enter() {
        if (!isOwned()) {
            throw new IllegalMonitorStateException();
        }
        unlock();
        startSecondaryEventLoop();
        lock();
    }

    @Override // sun.awt.datatransfer.ToolkitThreadBlockedHandler
    public void exit() {
        if (!isOwned()) {
            throw new IllegalMonitorStateException();
        }
        WToolkit.quitSecondaryEventLoop();
    }
}
