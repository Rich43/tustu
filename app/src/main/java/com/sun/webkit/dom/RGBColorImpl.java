package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/RGBColorImpl.class */
public class RGBColorImpl implements RGBColor {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native long getRedImpl(long j2);

    static native long getGreenImpl(long j2);

    static native long getBlueImpl(long j2);

    static native long getAlphaImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/RGBColorImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            RGBColorImpl.dispose(this.peer);
        }
    }

    RGBColorImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static RGBColor create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new RGBColorImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof RGBColorImpl) && this.peer == ((RGBColorImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(RGBColor arg) {
        if (arg == null) {
            return 0L;
        }
        return ((RGBColorImpl) arg).getPeer();
    }

    static RGBColor getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.css.RGBColor
    public CSSPrimitiveValue getRed() {
        return CSSPrimitiveValueImpl.getImpl(getRedImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.RGBColor
    public CSSPrimitiveValue getGreen() {
        return CSSPrimitiveValueImpl.getImpl(getGreenImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.RGBColor
    public CSSPrimitiveValue getBlue() {
        return CSSPrimitiveValueImpl.getImpl(getBlueImpl(getPeer()));
    }

    public CSSPrimitiveValue getAlpha() {
        return CSSPrimitiveValueImpl.getImpl(getAlphaImpl(getPeer()));
    }
}
