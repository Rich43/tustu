package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Rect;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/RectImpl.class */
public class RectImpl implements Rect {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native long getTopImpl(long j2);

    static native long getRightImpl(long j2);

    static native long getBottomImpl(long j2);

    static native long getLeftImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/RectImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            RectImpl.dispose(this.peer);
        }
    }

    RectImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static Rect create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new RectImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof RectImpl) && this.peer == ((RectImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(Rect arg) {
        if (arg == null) {
            return 0L;
        }
        return ((RectImpl) arg).getPeer();
    }

    static Rect getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.css.Rect
    public CSSPrimitiveValue getTop() {
        return CSSPrimitiveValueImpl.getImpl(getTopImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.Rect
    public CSSPrimitiveValue getRight() {
        return CSSPrimitiveValueImpl.getImpl(getRightImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.Rect
    public CSSPrimitiveValue getBottom() {
        return CSSPrimitiveValueImpl.getImpl(getBottomImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.Rect
    public CSSPrimitiveValue getLeft() {
        return CSSPrimitiveValueImpl.getImpl(getLeftImpl(getPeer()));
    }
}
