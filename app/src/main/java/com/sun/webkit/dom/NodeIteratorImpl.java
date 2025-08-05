package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/NodeIteratorImpl.class */
public class NodeIteratorImpl implements NodeIterator {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native long getRootImpl(long j2);

    static native int getWhatToShowImpl(long j2);

    static native long getFilterImpl(long j2);

    static native boolean getExpandEntityReferencesImpl(long j2);

    static native long getReferenceNodeImpl(long j2);

    static native boolean getPointerBeforeReferenceNodeImpl(long j2);

    static native long nextNodeImpl(long j2);

    static native long previousNodeImpl(long j2);

    static native void detachImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/NodeIteratorImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            NodeIteratorImpl.dispose(this.peer);
        }
    }

    NodeIteratorImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static NodeIterator create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new NodeIteratorImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof NodeIteratorImpl) && this.peer == ((NodeIteratorImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(NodeIterator arg) {
        if (arg == null) {
            return 0L;
        }
        return ((NodeIteratorImpl) arg).getPeer();
    }

    static NodeIterator getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node getRoot() {
        return NodeImpl.getImpl(getRootImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public int getWhatToShow() {
        return getWhatToShowImpl(getPeer());
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public NodeFilter getFilter() {
        return NodeFilterImpl.getImpl(getFilterImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public boolean getExpandEntityReferences() {
        return getExpandEntityReferencesImpl(getPeer());
    }

    public Node getReferenceNode() {
        return NodeImpl.getImpl(getReferenceNodeImpl(getPeer()));
    }

    public boolean getPointerBeforeReferenceNode() {
        return getPointerBeforeReferenceNodeImpl(getPeer());
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node nextNode() {
        return NodeImpl.getImpl(nextNodeImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public Node previousNode() {
        return NodeImpl.getImpl(previousNodeImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.NodeIterator
    public void detach() {
        detachImpl(getPeer());
    }
}
