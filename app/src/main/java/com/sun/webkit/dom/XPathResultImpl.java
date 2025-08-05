package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathResult;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/XPathResultImpl.class */
public class XPathResultImpl implements XPathResult {
    private final long peer;
    public static final int ANY_TYPE = 0;
    public static final int NUMBER_TYPE = 1;
    public static final int STRING_TYPE = 2;
    public static final int BOOLEAN_TYPE = 3;
    public static final int UNORDERED_NODE_ITERATOR_TYPE = 4;
    public static final int ORDERED_NODE_ITERATOR_TYPE = 5;
    public static final int UNORDERED_NODE_SNAPSHOT_TYPE = 6;
    public static final int ORDERED_NODE_SNAPSHOT_TYPE = 7;
    public static final int ANY_UNORDERED_NODE_TYPE = 8;
    public static final int FIRST_ORDERED_NODE_TYPE = 9;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native short getResultTypeImpl(long j2);

    static native double getNumberValueImpl(long j2);

    static native String getStringValueImpl(long j2);

    static native boolean getBooleanValueImpl(long j2);

    static native long getSingleNodeValueImpl(long j2);

    static native boolean getInvalidIteratorStateImpl(long j2);

    static native int getSnapshotLengthImpl(long j2);

    static native long iterateNextImpl(long j2);

    static native long snapshotItemImpl(long j2, int i2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/XPathResultImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            XPathResultImpl.dispose(this.peer);
        }
    }

    XPathResultImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static XPathResult create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new XPathResultImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof XPathResultImpl) && this.peer == ((XPathResultImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(XPathResult arg) {
        if (arg == null) {
            return 0L;
        }
        return ((XPathResultImpl) arg).getPeer();
    }

    static XPathResult getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public short getResultType() {
        return getResultTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public double getNumberValue() throws DOMException {
        return getNumberValueImpl(getPeer());
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public String getStringValue() throws DOMException {
        return getStringValueImpl(getPeer());
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public boolean getBooleanValue() throws DOMException {
        return getBooleanValueImpl(getPeer());
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public Node getSingleNodeValue() throws DOMException {
        return NodeImpl.getImpl(getSingleNodeValueImpl(getPeer()));
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public boolean getInvalidIteratorState() {
        return getInvalidIteratorStateImpl(getPeer());
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public int getSnapshotLength() throws DOMException {
        return getSnapshotLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public Node iterateNext() throws DOMException {
        return NodeImpl.getImpl(iterateNextImpl(getPeer()));
    }

    @Override // org.w3c.dom.xpath.XPathResult
    public Node snapshotItem(int index) throws DOMException {
        return NodeImpl.getImpl(snapshotItemImpl(getPeer(), index));
    }
}
