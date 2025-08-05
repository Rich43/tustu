package java.lang;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/* loaded from: rt.jar:java/lang/ApplicationShutdownHooks.class */
class ApplicationShutdownHooks {
    private static IdentityHashMap<Thread, Thread> hooks;

    static {
        try {
            Shutdown.add(1, false, new Runnable() { // from class: java.lang.ApplicationShutdownHooks.1
                @Override // java.lang.Runnable
                public void run() {
                    ApplicationShutdownHooks.runHooks();
                }
            });
            hooks = new IdentityHashMap<>();
        } catch (IllegalStateException e2) {
            hooks = null;
        }
    }

    private ApplicationShutdownHooks() {
    }

    static synchronized void add(Thread thread) {
        if (hooks == null) {
            throw new IllegalStateException("Shutdown in progress");
        }
        if (thread.isAlive()) {
            throw new IllegalArgumentException("Hook already running");
        }
        if (hooks.containsKey(thread)) {
            throw new IllegalArgumentException("Hook previously registered");
        }
        hooks.put(thread, thread);
    }

    static synchronized boolean remove(Thread thread) {
        if (hooks == null) {
            throw new IllegalStateException("Shutdown in progress");
        }
        if (thread == null) {
            throw new NullPointerException();
        }
        return hooks.remove(thread) != null;
    }

    static void runHooks() {
        Set<Thread> setKeySet;
        synchronized (ApplicationShutdownHooks.class) {
            setKeySet = hooks.keySet();
            hooks = null;
        }
        Iterator<Thread> it = setKeySet.iterator();
        while (it.hasNext()) {
            it.next().start();
        }
        Iterator<Thread> it2 = setKeySet.iterator();
        while (it2.hasNext()) {
            while (true) {
                try {
                    it2.next().join();
                    break;
                } catch (InterruptedException e2) {
                }
            }
        }
    }
}
