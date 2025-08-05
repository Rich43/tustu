package com.sun.webkit;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: jfxrt.jar:com/sun/webkit/Disposer.class */
public final class Disposer implements Runnable {
    private static final ReferenceQueue queue = new ReferenceQueue();
    private static final Disposer disposerInstance = new Disposer();
    private static final Set<WeakDisposerRecord> records = new HashSet();

    static {
        AccessController.doPrivileged(() -> {
            ThreadGroup tg = Thread.currentThread().getThreadGroup();
            ThreadGroup parent = tg;
            while (true) {
                ThreadGroup tgn = parent;
                if (tgn != null) {
                    tg = tgn;
                    parent = tg.getParent();
                } else {
                    Thread t2 = new Thread(tg, disposerInstance, "Disposer");
                    t2.setDaemon(true);
                    t2.setPriority(10);
                    t2.start();
                    return null;
                }
            }
        });
    }

    public static void addRecord(Object target, DisposerRecord rec) {
        disposerInstance.add(target, rec);
    }

    private synchronized void add(Object target, DisposerRecord rec) {
        records.add(new WeakDisposerRecord(target, rec));
    }

    public static void addRecord(WeakDisposerRecord rec) {
        disposerInstance.add(rec);
    }

    private synchronized void add(WeakDisposerRecord rec) {
        records.add(rec);
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                WeakDisposerRecord obj = (WeakDisposerRecord) queue.remove();
                obj.clear();
                DisposerRunnable.getInstance().enqueue(obj);
            } catch (Exception e2) {
                System.out.println("Exception while removing reference: " + ((Object) e2));
                e2.printStackTrace();
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/Disposer$DisposerRunnable.class */
    private static final class DisposerRunnable implements Runnable {
        private static final DisposerRunnable theInstance = new DisposerRunnable();
        private boolean isRunning = false;
        private final Object disposerLock = new Object();
        private final LinkedBlockingQueue<WeakDisposerRecord> disposerQueue = new LinkedBlockingQueue<>();

        private DisposerRunnable() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static DisposerRunnable getInstance() {
            return theInstance;
        }

        private void enqueueAll(Collection<WeakDisposerRecord> objs) {
            synchronized (this.disposerLock) {
                this.disposerQueue.addAll(objs);
                if (!this.isRunning) {
                    Invoker.getInvoker().invokeOnEventThread(this);
                    this.isRunning = true;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enqueue(WeakDisposerRecord obj) {
            enqueueAll(Arrays.asList(obj));
        }

        @Override // java.lang.Runnable
        public void run() {
            WeakDisposerRecord obj;
            while (true) {
                synchronized (this.disposerLock) {
                    obj = this.disposerQueue.poll();
                    if (obj == null) {
                        this.isRunning = false;
                        return;
                    }
                }
                if (Disposer.records.contains(obj)) {
                    Disposer.records.remove(obj);
                    obj.dispose();
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/Disposer$WeakDisposerRecord.class */
    public static class WeakDisposerRecord extends WeakReference implements DisposerRecord {
        private final DisposerRecord record;

        protected WeakDisposerRecord(Object referent) {
            super(referent, Disposer.queue);
            this.record = null;
        }

        private WeakDisposerRecord(Object referent, DisposerRecord record) {
            super(referent, Disposer.queue);
            this.record = record;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            this.record.dispose();
        }
    }
}
