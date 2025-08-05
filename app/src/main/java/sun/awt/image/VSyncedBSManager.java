package sun.awt.image;

import java.awt.image.BufferStrategy;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/awt/image/VSyncedBSManager.class */
public abstract class VSyncedBSManager {
    private static VSyncedBSManager theInstance;
    private static final boolean vSyncLimit = Boolean.valueOf((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.vsynclimit", "true"))).booleanValue();

    abstract boolean checkAllowed(BufferStrategy bufferStrategy);

    abstract void relinquishVsync(BufferStrategy bufferStrategy);

    private static VSyncedBSManager getInstance(boolean z2) {
        if (theInstance == null && z2) {
            theInstance = vSyncLimit ? new SingleVSyncedBSMgr() : new NoLimitVSyncBSMgr();
        }
        return theInstance;
    }

    public static boolean vsyncAllowed(BufferStrategy bufferStrategy) {
        return getInstance(true).checkAllowed(bufferStrategy);
    }

    public static synchronized void releaseVsync(BufferStrategy bufferStrategy) {
        VSyncedBSManager vSyncedBSManager = getInstance(false);
        if (vSyncedBSManager != null) {
            vSyncedBSManager.relinquishVsync(bufferStrategy);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/VSyncedBSManager$NoLimitVSyncBSMgr.class */
    private static final class NoLimitVSyncBSMgr extends VSyncedBSManager {
        private NoLimitVSyncBSMgr() {
        }

        @Override // sun.awt.image.VSyncedBSManager
        boolean checkAllowed(BufferStrategy bufferStrategy) {
            return true;
        }

        @Override // sun.awt.image.VSyncedBSManager
        void relinquishVsync(BufferStrategy bufferStrategy) {
        }
    }

    /* loaded from: rt.jar:sun/awt/image/VSyncedBSManager$SingleVSyncedBSMgr.class */
    private static final class SingleVSyncedBSMgr extends VSyncedBSManager {
        private WeakReference<BufferStrategy> strategy;

        private SingleVSyncedBSMgr() {
        }

        @Override // sun.awt.image.VSyncedBSManager
        public synchronized boolean checkAllowed(BufferStrategy bufferStrategy) {
            BufferStrategy bufferStrategy2;
            if (this.strategy != null && (bufferStrategy2 = this.strategy.get()) != null) {
                return bufferStrategy2 == bufferStrategy;
            }
            this.strategy = new WeakReference<>(bufferStrategy);
            return true;
        }

        @Override // sun.awt.image.VSyncedBSManager
        public synchronized void relinquishVsync(BufferStrategy bufferStrategy) {
            if (this.strategy != null && this.strategy.get() == bufferStrategy) {
                this.strategy.clear();
                this.strategy = null;
            }
        }
    }
}
