package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.Counter;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CounterImpl.class */
public class CounterImpl implements Counter {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native String getIdentifierImpl(long j2);

    static native String getListStyleImpl(long j2);

    static native String getSeparatorImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/CounterImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            CounterImpl.dispose(this.peer);
        }
    }

    CounterImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static Counter create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new CounterImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof CounterImpl) && this.peer == ((CounterImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(Counter arg) {
        if (arg == null) {
            return 0L;
        }
        return ((CounterImpl) arg).getPeer();
    }

    static Counter getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.css.Counter
    public String getIdentifier() {
        return getIdentifierImpl(getPeer());
    }

    @Override // org.w3c.dom.css.Counter
    public String getListStyle() {
        return getListStyleImpl(getPeer());
    }

    @Override // org.w3c.dom.css.Counter
    public String getSeparator() {
        return getSeparatorImpl(getPeer());
    }
}
