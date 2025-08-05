package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/TreeWalkerImpl.class */
public class TreeWalkerImpl implements TreeWalker {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native long getRootImpl(long j2);

    static native int getWhatToShowImpl(long j2);

    static native long getFilterImpl(long j2);

    static native boolean getExpandEntityReferencesImpl(long j2);

    static native long getCurrentNodeImpl(long j2);

    static native void setCurrentNodeImpl(long j2, long j3);

    static native long parentNodeImpl(long j2);

    static native long firstChildImpl(long j2);

    static native long lastChildImpl(long j2);

    static native long previousSiblingImpl(long j2);

    static native long nextSiblingImpl(long j2);

    static native long previousNodeImpl(long j2);

    static native long nextNodeImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/TreeWalkerImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            TreeWalkerImpl.dispose(this.peer);
        }
    }

    TreeWalkerImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static TreeWalker create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new TreeWalkerImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof TreeWalkerImpl) && this.peer == ((TreeWalkerImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(TreeWalker arg) {
        if (arg == null) {
            return 0L;
        }
        return ((TreeWalkerImpl) arg).getPeer();
    }

    static TreeWalker getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node getRoot() {
        return NodeImpl.getImpl(getRootImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public int getWhatToShow() {
        return getWhatToShowImpl(getPeer());
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public NodeFilter getFilter() {
        return NodeFilterImpl.getImpl(getFilterImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public boolean getExpandEntityReferences() {
        return getExpandEntityReferencesImpl(getPeer());
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node getCurrentNode() {
        return NodeImpl.getImpl(getCurrentNodeImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public void setCurrentNode(Node value) throws DOMException {
        setCurrentNodeImpl(getPeer(), NodeImpl.getPeer(value));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node parentNode() {
        return NodeImpl.getImpl(parentNodeImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node firstChild() {
        return NodeImpl.getImpl(firstChildImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node lastChild() {
        return NodeImpl.getImpl(lastChildImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node previousSibling() {
        return NodeImpl.getImpl(previousSiblingImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node nextSibling() {
        return NodeImpl.getImpl(nextSiblingImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node previousNode() {
        return NodeImpl.getImpl(previousNodeImpl(getPeer()));
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node nextNode() {
        return NodeImpl.getImpl(nextNodeImpl(getPeer()));
    }
}
