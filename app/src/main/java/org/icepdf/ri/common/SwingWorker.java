package org.icepdf.ri.common;

import javax.swing.SwingUtilities;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/SwingWorker.class */
public abstract class SwingWorker {
    private Object value;
    private Integer threadPriority;
    private ThreadVar threadVar;

    public abstract Object construct();

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/SwingWorker$ThreadVar.class */
    private static class ThreadVar {
        private Thread thread;

        ThreadVar(Thread t2) {
            this.thread = t2;
        }

        synchronized Thread get() {
            return this.thread;
        }

        synchronized void clear() {
            this.thread = null;
        }
    }

    protected synchronized Object getValue() {
        return this.value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setValue(Object x2) {
        this.value = x2;
    }

    public void finished() {
    }

    public void interrupt() {
        Thread t2 = this.threadVar.get();
        if (t2 != null) {
            t2.interrupt();
        }
        this.threadVar.clear();
    }

    public Object get() {
        while (true) {
            Thread t2 = this.threadVar.get();
            if (t2 == null) {
                return getValue();
            }
            try {
                t2.join();
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }

    public SwingWorker() {
        final Runnable doFinished = new Runnable() { // from class: org.icepdf.ri.common.SwingWorker.1
            @Override // java.lang.Runnable
            public void run() {
                SwingWorker.this.finished();
            }
        };
        Runnable doConstruct = new Runnable() { // from class: org.icepdf.ri.common.SwingWorker.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    SwingWorker.this.setValue(SwingWorker.this.construct());
                    SwingWorker.this.threadVar.clear();
                    SwingUtilities.invokeLater(doFinished);
                } catch (Throwable th) {
                    SwingWorker.this.threadVar.clear();
                    throw th;
                }
            }
        };
        Thread t2 = new Thread(doConstruct);
        this.threadVar = new ThreadVar(t2);
    }

    public void start() {
        Thread t2 = this.threadVar.get();
        if (t2 != null) {
            if (this.threadPriority != null) {
                t2.setPriority(this.threadPriority.intValue());
            }
            t2.start();
        }
    }

    public void setThreadPriority(int priority) {
        this.threadPriority = Integer.valueOf(priority);
    }
}
