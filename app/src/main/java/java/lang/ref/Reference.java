package java.lang.ref;

import sun.misc.JavaLangRefAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/lang/ref/Reference.class */
public abstract class Reference<T> {
    private T referent;
    volatile ReferenceQueue<? super T> queue;
    volatile Reference next;
    private transient Reference<T> discovered;
    private static Lock lock = new Lock();
    private static Reference<Object> pending = null;

    /* loaded from: rt.jar:java/lang/ref/Reference$Lock.class */
    private static class Lock {
        private Lock() {
        }
    }

    static {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parent = threadGroup;
        while (true) {
            ThreadGroup threadGroup2 = parent;
            if (threadGroup2 != null) {
                threadGroup = threadGroup2;
                parent = threadGroup.getParent();
            } else {
                ReferenceHandler referenceHandler = new ReferenceHandler(threadGroup, "Reference Handler");
                referenceHandler.setPriority(10);
                referenceHandler.setDaemon(true);
                referenceHandler.start();
                SharedSecrets.setJavaLangRefAccess(new JavaLangRefAccess() { // from class: java.lang.ref.Reference.1
                    @Override // sun.misc.JavaLangRefAccess
                    public boolean tryHandlePendingReference() {
                        return Reference.tryHandlePending(false);
                    }
                });
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/lang/ref/Reference$ReferenceHandler.class */
    private static class ReferenceHandler extends Thread {
        private static void ensureClassInitialized(Class<?> cls) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
            } catch (ClassNotFoundException e2) {
                throw ((Error) new NoClassDefFoundError(e2.getMessage()).initCause(e2));
            }
        }

        static {
            ensureClassInitialized(InterruptedException.class);
            ensureClassInitialized(sun.misc.Cleaner.class);
        }

        ReferenceHandler(ThreadGroup threadGroup, String str) {
            super(threadGroup, str);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                Reference.tryHandlePending(true);
            }
        }
    }

    static boolean tryHandlePending(boolean z2) {
        try {
            synchronized (lock) {
                if (pending != null) {
                    Reference<? extends Object> reference = pending;
                    sun.misc.Cleaner cleaner = reference instanceof sun.misc.Cleaner ? (sun.misc.Cleaner) reference : null;
                    pending = ((Reference) reference).discovered;
                    ((Reference) reference).discovered = null;
                    if (cleaner != null) {
                        cleaner.clean();
                        return true;
                    }
                    ReferenceQueue<? super Object> referenceQueue = reference.queue;
                    if (referenceQueue != ReferenceQueue.NULL) {
                        referenceQueue.enqueue(reference);
                        return true;
                    }
                    return true;
                }
                if (z2) {
                    lock.wait();
                }
                return z2;
            }
        } catch (InterruptedException e2) {
            return true;
        } catch (OutOfMemoryError e3) {
            Thread.yield();
            return true;
        }
    }

    public T get() {
        return this.referent;
    }

    public void clear() {
        this.referent = null;
    }

    public boolean isEnqueued() {
        return this.queue == ReferenceQueue.ENQUEUED;
    }

    public boolean enqueue() {
        this.referent = null;
        return this.queue.enqueue(this);
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    Reference(T t2) {
        this(t2, null);
    }

    Reference(T t2, ReferenceQueue<? super T> referenceQueue) {
        this.referent = t2;
        this.queue = referenceQueue == null ? ReferenceQueue.NULL : referenceQueue;
    }
}
