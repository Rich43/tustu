package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/NodeFilterImpl.class */
public class NodeFilterImpl implements NodeFilter {
    private final long peer;
    public static final int FILTER_ACCEPT = 1;
    public static final int FILTER_REJECT = 2;
    public static final int FILTER_SKIP = 3;
    public static final int SHOW_ALL = -1;
    public static final int SHOW_ELEMENT = 1;
    public static final int SHOW_ATTRIBUTE = 2;
    public static final int SHOW_TEXT = 4;
    public static final int SHOW_CDATA_SECTION = 8;
    public static final int SHOW_ENTITY_REFERENCE = 16;
    public static final int SHOW_ENTITY = 32;
    public static final int SHOW_PROCESSING_INSTRUCTION = 64;
    public static final int SHOW_COMMENT = 128;
    public static final int SHOW_DOCUMENT = 256;
    public static final int SHOW_DOCUMENT_TYPE = 512;
    public static final int SHOW_DOCUMENT_FRAGMENT = 1024;
    public static final int SHOW_NOTATION = 2048;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native short acceptNodeImpl(long j2, long j3);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/NodeFilterImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            NodeFilterImpl.dispose(this.peer);
        }
    }

    NodeFilterImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static NodeFilter create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new NodeFilterImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof NodeFilterImpl) && this.peer == ((NodeFilterImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(NodeFilter arg) {
        if (arg == null) {
            return 0L;
        }
        return ((NodeFilterImpl) arg).getPeer();
    }

    static NodeFilter getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.traversal.NodeFilter
    public short acceptNode(Node n2) {
        return acceptNodeImpl(getPeer(), NodeImpl.getPeer(n2));
    }
}
