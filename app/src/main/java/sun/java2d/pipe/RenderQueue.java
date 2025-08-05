package sun.java2d.pipe;

import java.util.HashSet;
import java.util.Set;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:sun/java2d/pipe/RenderQueue.class */
public abstract class RenderQueue {
    private static final int BUFFER_SIZE = 32000;
    protected Set refSet = new HashSet();
    protected RenderBuffer buf = RenderBuffer.allocate(BUFFER_SIZE);

    public abstract void flushNow();

    public abstract void flushAndInvokeNow(Runnable runnable);

    protected RenderQueue() {
    }

    public final void lock() {
        SunToolkit.awtLock();
    }

    public final boolean tryLock() {
        return SunToolkit.awtTryLock();
    }

    public final void unlock() {
        SunToolkit.awtUnlock();
    }

    public final void addReference(Object obj) {
        this.refSet.add(obj);
    }

    public final RenderBuffer getBuffer() {
        return this.buf;
    }

    public final void ensureCapacity(int i2) {
        if (this.buf.remaining() < i2) {
            flushNow();
        }
    }

    public final void ensureCapacityAndAlignment(int i2, int i3) {
        ensureCapacity(i2 + 4);
        ensureAlignment(i3);
    }

    public final void ensureAlignment(int i2) {
        if (((this.buf.position() + i2) & 7) != 0) {
            this.buf.putInt(90);
        }
    }

    public void flushNow(int i2) {
        this.buf.position(i2);
        flushNow();
    }
}
