package sun.java2d.d3d;

import sun.java2d.ScreenUpdateManager;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.RenderQueue;

/* loaded from: rt.jar:sun/java2d/d3d/D3DRenderQueue.class */
public class D3DRenderQueue extends RenderQueue {
    private static D3DRenderQueue theInstance;
    private static Thread rqThread;

    private native void flushBuffer(long j2, int i2, Runnable runnable);

    private D3DRenderQueue() {
    }

    public static synchronized D3DRenderQueue getInstance() {
        if (theInstance == null) {
            theInstance = new D3DRenderQueue();
            theInstance.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DRenderQueue.1
                @Override // java.lang.Runnable
                public void run() {
                    Thread unused = D3DRenderQueue.rqThread = Thread.currentThread();
                }
            });
        }
        return theInstance;
    }

    public static void sync() {
        if (theInstance != null) {
            ((D3DScreenUpdateManager) ScreenUpdateManager.getInstance()).runUpdateNow();
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

    public static void restoreDevices() {
        D3DRenderQueue d3DRenderQueue = getInstance();
        d3DRenderQueue.lock();
        try {
            d3DRenderQueue.ensureCapacity(4);
            d3DRenderQueue.getBuffer().putInt(77);
            d3DRenderQueue.flushNow();
        } finally {
            d3DRenderQueue.unlock();
        }
    }

    public static boolean isRenderQueueThread() {
        return Thread.currentThread() == rqThread;
    }

    public static void disposeGraphicsConfig(long j2) {
        D3DRenderQueue d3DRenderQueue = getInstance();
        d3DRenderQueue.lock();
        try {
            RenderBuffer buffer = d3DRenderQueue.getBuffer();
            d3DRenderQueue.ensureCapacityAndAlignment(12, 4);
            buffer.putInt(74);
            buffer.putLong(j2);
            d3DRenderQueue.flushNow();
            d3DRenderQueue.unlock();
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    @Override // sun.java2d.pipe.RenderQueue
    public void flushNow() {
        flushBuffer(null);
    }

    @Override // sun.java2d.pipe.RenderQueue
    public void flushAndInvokeNow(Runnable runnable) {
        flushBuffer(runnable);
    }

    private void flushBuffer(Runnable runnable) {
        int iPosition = this.buf.position();
        if (iPosition > 0 || runnable != null) {
            flushBuffer(this.buf.getAddress(), iPosition, runnable);
        }
        this.buf.clear();
        this.refSet.clear();
    }
}
