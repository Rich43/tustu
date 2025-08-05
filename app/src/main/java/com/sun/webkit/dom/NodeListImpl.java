package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/NodeListImpl.class */
public class NodeListImpl implements NodeList {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native int getLengthImpl(long j2);

    static native long itemImpl(long j2, int i2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/NodeListImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            NodeListImpl.dispose(this.peer);
        }
    }

    NodeListImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static NodeList create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new NodeListImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof NodeListImpl) && this.peer == ((NodeListImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(NodeList arg) {
        if (arg == null) {
            return 0L;
        }
        return ((NodeListImpl) arg).getPeer();
    }

    static NodeList getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.NodeList
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.NodeList
    public Node item(int index) {
        return NodeImpl.getImpl(itemImpl(getPeer(), index));
    }
}
