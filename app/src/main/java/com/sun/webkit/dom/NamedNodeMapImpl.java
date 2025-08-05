package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/NamedNodeMapImpl.class */
public class NamedNodeMapImpl implements NamedNodeMap {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native int getLengthImpl(long j2);

    static native long getNamedItemImpl(long j2, String str);

    static native long setNamedItemImpl(long j2, long j3);

    static native long removeNamedItemImpl(long j2, String str);

    static native long itemImpl(long j2, int i2);

    static native long getNamedItemNSImpl(long j2, String str, String str2);

    static native long setNamedItemNSImpl(long j2, long j3);

    static native long removeNamedItemNSImpl(long j2, String str, String str2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/NamedNodeMapImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            NamedNodeMapImpl.dispose(this.peer);
        }
    }

    NamedNodeMapImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static NamedNodeMap create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new NamedNodeMapImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof NamedNodeMapImpl) && this.peer == ((NamedNodeMapImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(NamedNodeMap arg) {
        if (arg == null) {
            return 0L;
        }
        return ((NamedNodeMapImpl) arg).getPeer();
    }

    static NamedNodeMap getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItem(String name) {
        return NodeImpl.getImpl(getNamedItemImpl(getPeer(), name));
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItem(Node node) throws DOMException {
        return NodeImpl.getImpl(setNamedItemImpl(getPeer(), NodeImpl.getPeer(node)));
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItem(String name) throws DOMException {
        return NodeImpl.getImpl(removeNamedItemImpl(getPeer(), name));
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node item(int index) {
        return NodeImpl.getImpl(itemImpl(getPeer(), index));
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItemNS(String namespaceURI, String localName) {
        return NodeImpl.getImpl(getNamedItemNSImpl(getPeer(), namespaceURI, localName));
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItemNS(Node node) throws DOMException {
        return NodeImpl.getImpl(setNamedItemNSImpl(getPeer(), NodeImpl.getPeer(node)));
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
        return NodeImpl.getImpl(removeNamedItemNSImpl(getPeer(), namespaceURI, localName));
    }
}
