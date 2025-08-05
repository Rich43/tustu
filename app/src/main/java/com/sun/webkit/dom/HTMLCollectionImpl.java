package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLCollectionImpl.class */
public class HTMLCollectionImpl implements HTMLCollection {
    private final long peer;
    private static final int TYPE_HTMLOptionsCollection = 1;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    private static native int getCPPTypeImpl(long j2);

    static native int getLengthImpl(long j2);

    static native long itemImpl(long j2, int i2);

    static native long namedItemImpl(long j2, String str);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLCollectionImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            HTMLCollectionImpl.dispose(this.peer);
        }
    }

    HTMLCollectionImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static HTMLCollection create(long peer) {
        if (peer == 0) {
            return null;
        }
        switch (getCPPTypeImpl(peer)) {
            case 1:
                return new HTMLOptionsCollectionImpl(peer);
            default:
                return new HTMLCollectionImpl(peer);
        }
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof HTMLCollectionImpl) && this.peer == ((HTMLCollectionImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(HTMLCollection arg) {
        if (arg == null) {
            return 0L;
        }
        return ((HTMLCollectionImpl) arg).getPeer();
    }

    static HTMLCollection getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.html.HTMLCollection
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLCollection
    public Node item(int index) {
        return NodeImpl.getImpl(itemImpl(getPeer(), index));
    }

    @Override // org.w3c.dom.html.HTMLCollection
    public Node namedItem(String name) {
        return NodeImpl.getImpl(namedItemImpl(getPeer(), name));
    }
}
