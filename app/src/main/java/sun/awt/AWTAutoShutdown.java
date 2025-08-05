package sun.awt;

import java.awt.AWTEvent;
import java.security.AccessController;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import sun.misc.ThreadGroupUtils;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/AWTAutoShutdown.class */
public final class AWTAutoShutdown implements Runnable {
    private static final AWTAutoShutdown theInstance = new AWTAutoShutdown();
    private final Object mainLock = new Object();
    private final Object activationLock = new Object();
    private final Set<Thread> busyThreadSet = new HashSet(7);
    private boolean toolkitThreadBusy = false;
    private final Map<Object, Object> peerMap = new IdentityHashMap();
    private Thread blockerThread = null;
    private boolean timeoutPassed = false;
    private static final int SAFETY_TIMEOUT = 1000;

    private AWTAutoShutdown() {
    }

    public static AWTAutoShutdown getInstance() {
        return theInstance;
    }

    public static void notifyToolkitThreadBusy() {
        getInstance().setToolkitBusy(true);
    }

    public static void notifyToolkitThreadFree() {
        getInstance().setToolkitBusy(false);
    }

    public void notifyThreadBusy(Thread thread) {
        if (thread == null) {
            return;
        }
        synchronized (this.activationLock) {
            synchronized (this.mainLock) {
                if (this.blockerThread == null) {
                    activateBlockerThread();
                } else if (isReadyToShutdown()) {
                    this.mainLock.notifyAll();
                    this.timeoutPassed = false;
                }
                this.busyThreadSet.add(thread);
            }
        }
    }

    public void notifyThreadFree(Thread thread) {
        if (thread == null) {
            return;
        }
        synchronized (this.activationLock) {
            synchronized (this.mainLock) {
                this.busyThreadSet.remove(thread);
                if (isReadyToShutdown()) {
                    this.mainLock.notifyAll();
                    this.timeoutPassed = false;
                }
            }
        }
    }

    void notifyPeerMapUpdated() {
        synchronized (this.activationLock) {
            synchronized (this.mainLock) {
                if (!isReadyToShutdown() && this.blockerThread == null) {
                    AccessController.doPrivileged(() -> {
                        activateBlockerThread();
                        return null;
                    });
                } else {
                    this.mainLock.notifyAll();
                    this.timeoutPassed = false;
                }
            }
        }
    }

    private boolean isReadyToShutdown() {
        return !this.toolkitThreadBusy && this.peerMap.isEmpty() && this.busyThreadSet.isEmpty();
    }

    private void setToolkitBusy(boolean z2) {
        if (z2 != this.toolkitThreadBusy) {
            synchronized (this.activationLock) {
                synchronized (this.mainLock) {
                    if (z2 != this.toolkitThreadBusy) {
                        if (z2) {
                            if (this.blockerThread == null) {
                                activateBlockerThread();
                            } else if (isReadyToShutdown()) {
                                this.mainLock.notifyAll();
                                this.timeoutPassed = false;
                            }
                            this.toolkitThreadBusy = z2;
                        } else {
                            this.toolkitThreadBusy = z2;
                            if (isReadyToShutdown()) {
                                this.mainLock.notifyAll();
                                this.timeoutPassed = false;
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.lang.Runnable
    public void run() {
        Thread threadCurrentThread = Thread.currentThread();
        boolean z2 = false;
        synchronized (this.mainLock) {
            try {
                try {
                    this.mainLock.notifyAll();
                    while (this.blockerThread == threadCurrentThread) {
                        this.mainLock.wait();
                        this.timeoutPassed = false;
                        while (true) {
                            if (!isReadyToShutdown()) {
                                break;
                            }
                            if (this.timeoutPassed) {
                                this.timeoutPassed = false;
                                this.blockerThread = null;
                                break;
                            } else {
                                this.timeoutPassed = true;
                                this.mainLock.wait(1000L);
                            }
                        }
                    }
                    if (this.blockerThread == threadCurrentThread) {
                        this.blockerThread = null;
                    }
                } catch (Throwable th) {
                    if (this.blockerThread == threadCurrentThread) {
                        this.blockerThread = null;
                    }
                    throw th;
                }
            } catch (InterruptedException e2) {
                z2 = true;
                if (this.blockerThread == threadCurrentThread) {
                    this.blockerThread = null;
                }
            }
        }
        if (!z2) {
            AppContext.stopEventDispatchThreads();
        }
    }

    static AWTEvent getShutdownEvent() {
        return new AWTEvent(getInstance(), 0) { // from class: sun.awt.AWTAutoShutdown.1
        };
    }

    private void activateBlockerThread() {
        Thread thread = new Thread(ThreadGroupUtils.getRootThreadGroup(), this, "AWT-Shutdown");
        thread.setContextClassLoader(null);
        thread.setDaemon(false);
        this.blockerThread = thread;
        thread.start();
        try {
            this.mainLock.wait();
        } catch (InterruptedException e2) {
            System.err.println("AWT blocker activation interrupted:");
            e2.printStackTrace();
        }
    }

    final void registerPeer(Object obj, Object obj2) {
        synchronized (this.activationLock) {
            synchronized (this.mainLock) {
                this.peerMap.put(obj, obj2);
                notifyPeerMapUpdated();
            }
        }
    }

    final void unregisterPeer(Object obj, Object obj2) {
        synchronized (this.activationLock) {
            synchronized (this.mainLock) {
                if (this.peerMap.get(obj) == obj2) {
                    this.peerMap.remove(obj);
                    notifyPeerMapUpdated();
                }
            }
        }
    }

    final Object getPeer(Object obj) {
        Object obj2;
        synchronized (this.activationLock) {
            synchronized (this.mainLock) {
                obj2 = this.peerMap.get(obj);
            }
        }
        return obj2;
    }

    final void dumpPeers(PlatformLogger platformLogger) {
        if (platformLogger.isLoggable(PlatformLogger.Level.FINE)) {
            synchronized (this.activationLock) {
                synchronized (this.mainLock) {
                    platformLogger.fine("Mapped peers:");
                    for (Object obj : this.peerMap.keySet()) {
                        platformLogger.fine(obj + "->" + this.peerMap.get(obj));
                    }
                }
            }
        }
    }
}
