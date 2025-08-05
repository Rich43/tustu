package com.sun.webkit.graphics;

import com.sun.webkit.Invoker;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCRenderQueue.class */
public abstract class WCRenderQueue extends Ref {
    private static final AtomicInteger idCountObj = new AtomicInteger(0);
    private static final Logger log = Logger.getLogger(WCRenderQueue.class.getName());
    public static final int MAX_QUEUE_SIZE = 524288;
    private final LinkedList<BufferData> buffers;
    private BufferData currentBuffer;
    private final WCRectangle clip;
    private int size;
    private final boolean opaque;
    protected final WCGraphicsContext gc;

    protected abstract void flush();

    protected abstract void disposeGraphics();

    private native void twkRelease(Object[] objArr);

    protected WCRenderQueue(WCGraphicsContext gc) {
        this.buffers = new LinkedList<>();
        this.currentBuffer = new BufferData();
        this.size = 0;
        this.clip = null;
        this.opaque = false;
        this.gc = gc;
    }

    protected WCRenderQueue(WCRectangle clip, boolean opaque) {
        this.buffers = new LinkedList<>();
        this.currentBuffer = new BufferData();
        this.size = 0;
        this.clip = clip;
        this.opaque = opaque;
        this.gc = null;
    }

    public synchronized int getSize() {
        return this.size;
    }

    public synchronized void addBuffer(ByteBuffer buffer) {
        if (log.isLoggable(Level.FINE) && this.buffers.isEmpty()) {
            log.log(Level.FINE, "'{'WCRenderQueue{0}[{1}]", new Object[]{Integer.valueOf(hashCode()), Integer.valueOf(idCountObj.incrementAndGet())});
        }
        this.currentBuffer.setBuffer(buffer);
        this.buffers.addLast(this.currentBuffer);
        this.currentBuffer = new BufferData();
        this.size += buffer.capacity();
        if (this.size > 524288 && this.gc != null) {
            flush();
        }
    }

    public synchronized boolean isEmpty() {
        return this.buffers.isEmpty();
    }

    public synchronized void decode(WCGraphicsContext gc) {
        if (gc == null || !gc.isValid()) {
            log.fine("WCRenderQueue::decode : GC is " + (gc == null ? FXMLLoader.NULL_KEYWORD : " invalid"));
            return;
        }
        Iterator<BufferData> it = this.buffers.iterator();
        while (it.hasNext()) {
            BufferData bdata = it.next();
            try {
                GraphicsDecoder.decode(WCGraphicsManager.getGraphicsManager(), gc, bdata);
            } catch (RuntimeException e2) {
                e2.printStackTrace(System.err);
            }
        }
        dispose();
    }

    public synchronized void decode() {
        if (this.gc == null || !this.gc.isValid()) {
            log.fine("WCRenderQueue::decode : GC is " + (this.gc == null ? FXMLLoader.NULL_KEYWORD : " invalid"));
        } else {
            decode(this.gc);
            this.gc.flush();
        }
    }

    public synchronized void decode(int fontSmoothingType) {
        if (this.gc == null || !this.gc.isValid()) {
            log.fine("WCRenderQueue::decode : GC is " + (this.gc == null ? FXMLLoader.NULL_KEYWORD : " invalid"));
        } else {
            this.gc.setFontSmoothingType(fontSmoothingType);
            decode();
        }
    }

    private void fwkFlush() {
        flush();
    }

    private void fwkAddBuffer(ByteBuffer buffer) {
        addBuffer(buffer);
    }

    public WCRectangle getClip() {
        return this.clip;
    }

    public synchronized void dispose() {
        int n2 = this.buffers.size();
        if (n2 > 0) {
            int i2 = 0;
            Object[] arr = new Object[n2];
            Iterator<BufferData> it = this.buffers.iterator();
            while (it.hasNext()) {
                BufferData bdata = it.next();
                int i3 = i2;
                i2++;
                arr[i3] = bdata.getBuffer();
            }
            this.buffers.clear();
            Invoker.getInvoker().invokeOnEventThread(() -> {
                twkRelease(arr);
            });
            this.size = 0;
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "'}'WCRenderQueue{0}[{1}]", new Object[]{Integer.valueOf(hashCode()), Integer.valueOf(idCountObj.decrementAndGet())});
            }
        }
    }

    private void fwkDisposeGraphics() {
        disposeGraphics();
    }

    private int refString(String str) {
        return this.currentBuffer.addString(str);
    }

    private int refIntArr(int[] arr) {
        return this.currentBuffer.addIntArray(arr);
    }

    private int refFloatArr(float[] arr) {
        return this.currentBuffer.addFloatArray(arr);
    }

    public boolean isOpaque() {
        return this.opaque;
    }

    public synchronized String toString() {
        return "WCRenderQueue{clip=" + ((Object) this.clip) + ", size=" + this.size + ", opaque=" + this.opaque + "}";
    }
}
