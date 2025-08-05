package com.sun.corba.se.impl.orbutil.concurrent;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/concurrent/SyncUtil.class */
public class SyncUtil {
    private SyncUtil() {
    }

    public static void acquire(Sync sync) {
        boolean z2 = false;
        while (!z2) {
            try {
                sync.acquire();
                z2 = true;
            } catch (InterruptedException e2) {
                z2 = false;
            }
        }
    }
}
