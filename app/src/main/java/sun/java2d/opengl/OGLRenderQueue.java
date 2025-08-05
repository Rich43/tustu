package sun.java2d.opengl;

import java.security.AccessController;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.RenderQueue;
import sun.misc.ThreadGroupUtils;

/* loaded from: rt.jar:sun/java2d/opengl/OGLRenderQueue.class */
public class OGLRenderQueue extends RenderQueue {
    private static OGLRenderQueue theInstance;
    private final QueueFlusher flusher = (QueueFlusher) AccessController.doPrivileged(() -> {
        return new QueueFlusher(ThreadGroupUtils.getRootThreadGroup());
    });

    private native void flushBuffer(long j2, int i2);

    private OGLRenderQueue() {
    }

    public static synchronized OGLRenderQueue getInstance() {
        if (theInstance == null) {
            theInstance = new OGLRenderQueue();
        }
        return theInstance;
    }

    public static void sync() {
        if (theInstance != null) {
            theInstance.lock();
            try {
                theInstance.ensureCapacity(4);
                theInstance.getBuffer().putInt(76);
                theInstance.flushNow();
                theInstance.unlock();
            } catch (Throwable th) {
                theInstance.unlock();
                throw th;
            }
        }
    }

    public static void disposeGraphicsConfig(long j2) {
        OGLRenderQueue oGLRenderQueue = getInstance();
        oGLRenderQueue.lock();
        try {
            OGLContext.setScratchSurface(j2);
            RenderBuffer buffer = oGLRenderQueue.getBuffer();
            oGLRenderQueue.ensureCapacityAndAlignment(12, 4);
            buffer.putInt(74);
            buffer.putLong(j2);
            oGLRenderQueue.flushNow();
            oGLRenderQueue.unlock();
        } catch (Throwable th) {
            oGLRenderQueue.unlock();
            throw th;
        }
    }

    public static boolean isQueueFlusherThread() {
        return Thread.currentThread() == getInstance().flusher;
    }

    @Override // sun.java2d.pipe.RenderQueue
    public void flushNow() {
        try {
            this.flusher.flushNow();
        } catch (Exception e2) {
            System.err.println("exception in flushNow:");
            e2.printStackTrace();
        }
    }

    @Override // sun.java2d.pipe.RenderQueue
    public void flushAndInvokeNow(Runnable runnable) {
        try {
            this.flusher.flushAndInvokeNow(runnable);
        } catch (Exception e2) {
            System.err.println("exception in flushAndInvokeNow:");
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void flushBuffer() {
        int iPosition = this.buf.position();
        if (iPosition > 0) {
            flushBuffer(this.buf.getAddress(), iPosition);
        }
        this.buf.clear();
        this.refSet.clear();
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLRenderQueue$QueueFlusher.class */
    private class QueueFlusher extends Thread {
        private boolean needsFlush;
        private Runnable task;
        private Error error;

        public QueueFlusher(ThreadGroup threadGroup) {
            super(threadGroup, "Java2D Queue Flusher");
            setDaemon(true);
            setPriority(10);
            start();
        }

        public synchronized void flushNow() {
            this.needsFlush = true;
            notify();
            while (this.needsFlush) {
                try {
                    wait();
                } catch (InterruptedException e2) {
                }
            }
            if (this.error != null) {
                throw this.error;
            }
        }

        public synchronized void flushAndInvokeNow(Runnable runnable) {
            this.task = runnable;
            flushNow();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public synchronized void run() {
            boolean z2 = false;
            while (true) {
                if (this.needsFlush) {
                    try {
                        try {
                            this.error = null;
                            OGLRenderQueue.this.flushBuffer();
                            if (this.task != null) {
                                this.task.run();
                            }
                            if (z2) {
                                OGLRenderQueue.this.unlock();
                            }
                            this.task = null;
                            this.needsFlush = false;
                            notify();
                        } catch (Error e2) {
                            this.error = e2;
                            if (z2) {
                                OGLRenderQueue.this.unlock();
                            }
                            this.task = null;
                            this.needsFlush = false;
                            notify();
                        } catch (Exception e3) {
                            System.err.println("exception in QueueFlusher:");
                            e3.printStackTrace();
                            if (z2) {
                                OGLRenderQueue.this.unlock();
                            }
                            this.task = null;
                            this.needsFlush = false;
                            notify();
                        }
                    } catch (Throwable th) {
                        if (z2) {
                            OGLRenderQueue.this.unlock();
                        }
                        this.task = null;
                        this.needsFlush = false;
                        notify();
                        throw th;
                    }
                } else {
                    try {
                        z2 = false;
                        wait(100L);
                        if (!this.needsFlush) {
                            boolean zTryLock = OGLRenderQueue.this.tryLock();
                            z2 = zTryLock;
                            if (zTryLock) {
                                if (OGLRenderQueue.this.buf.position() > 0) {
                                    this.needsFlush = true;
                                } else {
                                    OGLRenderQueue.this.unlock();
                                }
                            }
                        }
                    } catch (InterruptedException e4) {
                    }
                }
            }
        }
    }
}
