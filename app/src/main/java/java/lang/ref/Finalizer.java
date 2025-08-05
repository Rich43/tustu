package java.lang.ref;

import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;
import sun.misc.VM;

/* loaded from: rt.jar:java/lang/ref/Finalizer.class */
final class Finalizer extends FinalReference<Object> {
    private static ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private static Finalizer unfinalized = null;
    private static final Object lock = new Object();
    private Finalizer next;
    private Finalizer prev;

    static {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parent = threadGroup;
        while (true) {
            ThreadGroup threadGroup2 = parent;
            if (threadGroup2 != null) {
                threadGroup = threadGroup2;
                parent = threadGroup.getParent();
            } else {
                FinalizerThread finalizerThread = new FinalizerThread(threadGroup);
                finalizerThread.setPriority(8);
                finalizerThread.setDaemon(true);
                finalizerThread.start();
                return;
            }
        }
    }

    private boolean hasBeenFinalized() {
        return this.next == this;
    }

    private void add() {
        synchronized (lock) {
            if (unfinalized != null) {
                this.next = unfinalized;
                unfinalized.prev = this;
            }
            unfinalized = this;
        }
    }

    private void remove() {
        synchronized (lock) {
            if (unfinalized == this) {
                if (this.next != null) {
                    unfinalized = this.next;
                } else {
                    unfinalized = this.prev;
                }
            }
            if (this.next != null) {
                this.next.prev = this.prev;
            }
            if (this.prev != null) {
                this.prev.next = this.next;
            }
            this.next = this;
            this.prev = this;
        }
    }

    private Finalizer(Object obj) {
        super(obj, queue);
        this.next = null;
        this.prev = null;
        add();
    }

    static ReferenceQueue<Object> getQueue() {
        return queue;
    }

    static void register(Object obj) {
        new Finalizer(obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runFinalizer(JavaLangAccess javaLangAccess) {
        synchronized (this) {
            if (hasBeenFinalized()) {
                return;
            }
            remove();
            try {
                Object obj = get();
                if (obj != null && !(obj instanceof Enum)) {
                    javaLangAccess.invokeFinalize(obj);
                }
            } catch (Throwable th) {
            }
            super.clear();
        }
    }

    private static void forkSecondaryFinalizer(final Runnable runnable) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.ref.Finalizer.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                ThreadGroup parent = threadGroup;
                while (true) {
                    ThreadGroup threadGroup2 = parent;
                    if (threadGroup2 != null) {
                        threadGroup = threadGroup2;
                        parent = threadGroup.getParent();
                    } else {
                        Thread thread = new Thread(threadGroup, runnable, "Secondary finalizer");
                        thread.start();
                        try {
                            thread.join();
                            return null;
                        } catch (InterruptedException e2) {
                            Thread.currentThread().interrupt();
                            return null;
                        }
                    }
                }
            }
        });
    }

    static void runFinalization() {
        if (!VM.isBooted()) {
            return;
        }
        forkSecondaryFinalizer(new Runnable() { // from class: java.lang.ref.Finalizer.2
            private volatile boolean running;

            @Override // java.lang.Runnable
            public void run() {
                if (this.running) {
                    return;
                }
                JavaLangAccess javaLangAccess = SharedSecrets.getJavaLangAccess();
                this.running = true;
                while (true) {
                    Finalizer finalizer = (Finalizer) Finalizer.queue.poll();
                    if (finalizer != null) {
                        finalizer.runFinalizer(javaLangAccess);
                    } else {
                        return;
                    }
                }
            }
        });
    }

    /* loaded from: rt.jar:java/lang/ref/Finalizer$FinalizerThread.class */
    private static class FinalizerThread extends Thread {
        private volatile boolean running;

        FinalizerThread(ThreadGroup threadGroup) {
            super(threadGroup, "Finalizer");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (this.running) {
                return;
            }
            while (!VM.isBooted()) {
                try {
                    VM.awaitBooted();
                } catch (InterruptedException e2) {
                }
            }
            JavaLangAccess javaLangAccess = SharedSecrets.getJavaLangAccess();
            this.running = true;
            while (true) {
                try {
                    ((Finalizer) Finalizer.queue.remove()).runFinalizer(javaLangAccess);
                } catch (InterruptedException e3) {
                }
            }
        }
    }
}
