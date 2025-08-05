package java.lang;

/* loaded from: rt.jar:java/lang/Shutdown.class */
class Shutdown {
    private static final int MAX_SYSTEM_HOOKS = 10;
    private static boolean isShutdown;
    private static final Runnable[] hooks = new Runnable[10];
    private static int currentRunningHook = -1;
    private static Object lock = new Lock();
    private static Object haltLock = new Lock();

    static native void beforeHalt();

    static native void halt0(int i2);

    Shutdown() {
    }

    /* loaded from: rt.jar:java/lang/Shutdown$Lock.class */
    private static class Lock {
        private Lock() {
        }
    }

    static void add(int i2, boolean z2, Runnable runnable) {
        if (i2 < 0 || i2 >= 10) {
            throw new IllegalArgumentException("Invalid slot: " + i2);
        }
        synchronized (lock) {
            if (hooks[i2] != null) {
                throw new InternalError("Shutdown hook at slot " + i2 + " already registered");
            }
            if (!z2) {
                if (currentRunningHook >= 0) {
                    throw new IllegalStateException("Shutdown in progress");
                }
            } else if (isShutdown || i2 <= currentRunningHook) {
                throw new IllegalStateException("Shutdown in progress");
            }
            hooks[i2] = runnable;
        }
    }

    private static void runHooks() {
        boolean z2;
        ThreadDeath threadDeath;
        Runnable runnable;
        synchronized (lock) {
            if (isShutdown) {
                return;
            }
            for (int i2 = 0; i2 < 10; i2++) {
                try {
                    synchronized (lock) {
                        currentRunningHook = i2;
                        runnable = hooks[i2];
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                } finally {
                    if (z2) {
                    }
                }
            }
            synchronized (lock) {
                isShutdown = true;
            }
        }
    }

    static void halt(int i2) {
        synchronized (haltLock) {
            halt0(i2);
        }
    }

    static void exit(int i2) {
        synchronized (lock) {
            if (i2 != 0) {
                if (isShutdown) {
                    halt(i2);
                }
            }
        }
        synchronized (Shutdown.class) {
            beforeHalt();
            runHooks();
            halt(i2);
        }
    }

    static void shutdown() {
        synchronized (Shutdown.class) {
            runHooks();
        }
    }
}
