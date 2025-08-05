package com.sun.prism.impl;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.prism.PixelSource;
import java.lang.ref.WeakReference;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/prism/impl/QueuedPixelSource.class */
public class QueuedPixelSource implements PixelSource {
    private volatile Pixels beingConsumed;
    private volatile Pixels enqueued;
    private final List<WeakReference<Pixels>> saved = new ArrayList(3);
    private final boolean useDirectBuffers;

    public QueuedPixelSource(boolean useDirectBuffers) {
        this.useDirectBuffers = useDirectBuffers;
    }

    @Override // com.sun.prism.PixelSource
    public synchronized Pixels getLatestPixels() {
        if (this.beingConsumed != null) {
            throw new IllegalStateException("already consuming pixels: " + ((Object) this.beingConsumed));
        }
        if (this.enqueued != null) {
            this.beingConsumed = this.enqueued;
            this.enqueued = null;
        }
        return this.beingConsumed;
    }

    @Override // com.sun.prism.PixelSource
    public synchronized void doneWithPixels(Pixels used) {
        if (this.beingConsumed != used) {
            throw new IllegalStateException("wrong pixels buffer: " + ((Object) used) + " != " + ((Object) this.beingConsumed));
        }
        this.beingConsumed = null;
    }

    @Override // com.sun.prism.PixelSource
    public synchronized void skipLatestPixels() {
        if (this.beingConsumed != null) {
            throw new IllegalStateException("cannot skip while processing: " + ((Object) this.beingConsumed));
        }
        this.enqueued = null;
    }

    private boolean usesSameBuffer(Pixels p1, Pixels p2) {
        if (p1 == p2) {
            return true;
        }
        return (p1 == null || p2 == null || p1.getPixels() != p2.getPixels()) ? false : true;
    }

    public synchronized Pixels getUnusedPixels(int w2, int h2, float scale) {
        int i2 = 0;
        IntBuffer reuseBuffer = null;
        while (i2 < this.saved.size()) {
            WeakReference<Pixels> ref = this.saved.get(i2);
            Pixels p2 = ref.get();
            if (p2 == null) {
                this.saved.remove(i2);
            } else if (usesSameBuffer(p2, this.beingConsumed) || usesSameBuffer(p2, this.enqueued)) {
                i2++;
            } else {
                if (p2.getWidthUnsafe() == w2 && p2.getHeightUnsafe() == h2 && p2.getScaleUnsafe() == scale) {
                    return p2;
                }
                this.saved.remove(i2);
                reuseBuffer = (IntBuffer) p2.getPixels();
                if (reuseBuffer.capacity() >= w2 * h2) {
                    break;
                }
                reuseBuffer = null;
            }
        }
        if (reuseBuffer == null) {
            int bufsize = w2 * h2;
            if (this.useDirectBuffers) {
                reuseBuffer = BufferUtil.newIntBuffer(bufsize);
            } else {
                reuseBuffer = IntBuffer.allocate(bufsize);
            }
        }
        Pixels p3 = Application.GetApplication().createPixels(w2, h2, reuseBuffer, scale);
        this.saved.add(new WeakReference<>(p3));
        return p3;
    }

    public synchronized void enqueuePixels(Pixels pixels) {
        this.enqueued = pixels;
    }
}
