package sun.awt.windows;

import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: rt.jar:sun/awt/windows/WObjectPeer.class */
abstract class WObjectPeer {
    volatile long pData;
    private volatile boolean destroyed;
    volatile Object target;
    private volatile boolean disposed;
    volatile Error createError = null;
    private final Object stateLock = new Object();
    private volatile Map<WObjectPeer, WObjectPeer> childPeers;

    protected abstract void disposeImpl();

    private static native void initIDs();

    WObjectPeer() {
    }

    static {
        initIDs();
    }

    public static WObjectPeer getPeerForTarget(Object obj) {
        return (WObjectPeer) WToolkit.targetToPeer(obj);
    }

    public long getData() {
        return this.pData;
    }

    public Object getTarget() {
        return this.target;
    }

    public final Object getStateLock() {
        return this.stateLock;
    }

    public final void dispose() {
        boolean z2 = false;
        synchronized (this) {
            if (!this.disposed) {
                z2 = true;
                this.disposed = true;
            }
        }
        if (z2) {
            if (this.childPeers != null) {
                disposeChildPeers();
            }
            disposeImpl();
        }
    }

    protected final boolean isDisposed() {
        return this.disposed;
    }

    final void addChildPeer(WObjectPeer wObjectPeer) {
        synchronized (getStateLock()) {
            if (this.childPeers == null) {
                this.childPeers = new WeakHashMap();
            }
            if (isDisposed()) {
                throw new IllegalStateException("Parent peer is disposed");
            }
            this.childPeers.put(wObjectPeer, this);
        }
    }

    private void disposeChildPeers() {
        synchronized (getStateLock()) {
            for (WObjectPeer wObjectPeer : this.childPeers.keySet()) {
                if (wObjectPeer != null) {
                    try {
                        wObjectPeer.dispose();
                    } catch (Exception e2) {
                    }
                }
            }
        }
    }
}
