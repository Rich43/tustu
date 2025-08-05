package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMStringList;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/DOMStringListImpl.class */
public class DOMStringListImpl implements DOMStringList {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native int getLengthImpl(long j2);

    static native String itemImpl(long j2, int i2);

    static native boolean containsImpl(long j2, String str);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/DOMStringListImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            DOMStringListImpl.dispose(this.peer);
        }
    }

    DOMStringListImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static DOMStringList create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new DOMStringListImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof DOMStringListImpl) && this.peer == ((DOMStringListImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(DOMStringList arg) {
        if (arg == null) {
            return 0L;
        }
        return ((DOMStringListImpl) arg).getPeer();
    }

    static DOMStringList getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.DOMStringList
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.DOMStringList
    public String item(int index) {
        return itemImpl(getPeer(), index);
    }

    @Override // org.w3c.dom.DOMStringList
    public boolean contains(String string) {
        return containsImpl(getPeer(), string);
    }
}
