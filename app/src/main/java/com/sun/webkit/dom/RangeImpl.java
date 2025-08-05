package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/RangeImpl.class */
public class RangeImpl implements Range {
    private final long peer;
    public static final int START_TO_START = 0;
    public static final int START_TO_END = 1;
    public static final int END_TO_END = 2;
    public static final int END_TO_START = 3;
    public static final int NODE_BEFORE = 0;
    public static final int NODE_AFTER = 1;
    public static final int NODE_BEFORE_AND_AFTER = 2;
    public static final int NODE_INSIDE = 3;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native long getStartContainerImpl(long j2);

    static native int getStartOffsetImpl(long j2);

    static native long getEndContainerImpl(long j2);

    static native int getEndOffsetImpl(long j2);

    static native boolean getCollapsedImpl(long j2);

    static native long getCommonAncestorContainerImpl(long j2);

    static native String getTextImpl(long j2);

    static native void setStartImpl(long j2, long j3, int i2);

    static native void setEndImpl(long j2, long j3, int i2);

    static native void setStartBeforeImpl(long j2, long j3);

    static native void setStartAfterImpl(long j2, long j3);

    static native void setEndBeforeImpl(long j2, long j3);

    static native void setEndAfterImpl(long j2, long j3);

    static native void collapseImpl(long j2, boolean z2);

    static native void selectNodeImpl(long j2, long j3);

    static native void selectNodeContentsImpl(long j2, long j3);

    static native short compareBoundaryPointsImpl(long j2, short s2, long j3);

    static native void deleteContentsImpl(long j2);

    static native long extractContentsImpl(long j2);

    static native long cloneContentsImpl(long j2);

    static native void insertNodeImpl(long j2, long j3);

    static native void surroundContentsImpl(long j2, long j3);

    static native long cloneRangeImpl(long j2);

    static native String toStringImpl(long j2);

    static native void detachImpl(long j2);

    static native long createContextualFragmentImpl(long j2, String str);

    static native short compareNodeImpl(long j2, long j3);

    static native short comparePointImpl(long j2, long j3, int i2);

    static native boolean intersectsNodeImpl(long j2, long j3);

    static native boolean isPointInRangeImpl(long j2, long j3, int i2);

    static native void expandImpl(long j2, String str);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/RangeImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            RangeImpl.dispose(this.peer);
        }
    }

    RangeImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static Range create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new RangeImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof RangeImpl) && this.peer == ((RangeImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(Range arg) {
        if (arg == null) {
            return 0L;
        }
        return ((RangeImpl) arg).getPeer();
    }

    static Range getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.ranges.Range
    public Node getStartContainer() {
        return NodeImpl.getImpl(getStartContainerImpl(getPeer()));
    }

    @Override // org.w3c.dom.ranges.Range
    public int getStartOffset() {
        return getStartOffsetImpl(getPeer());
    }

    @Override // org.w3c.dom.ranges.Range
    public Node getEndContainer() {
        return NodeImpl.getImpl(getEndContainerImpl(getPeer()));
    }

    @Override // org.w3c.dom.ranges.Range
    public int getEndOffset() {
        return getEndOffsetImpl(getPeer());
    }

    @Override // org.w3c.dom.ranges.Range
    public boolean getCollapsed() {
        return getCollapsedImpl(getPeer());
    }

    @Override // org.w3c.dom.ranges.Range
    public Node getCommonAncestorContainer() {
        return NodeImpl.getImpl(getCommonAncestorContainerImpl(getPeer()));
    }

    public String getText() {
        return getTextImpl(getPeer());
    }

    @Override // org.w3c.dom.ranges.Range
    public void setStart(Node refNode, int offset) throws DOMException {
        setStartImpl(getPeer(), NodeImpl.getPeer(refNode), offset);
    }

    @Override // org.w3c.dom.ranges.Range
    public void setEnd(Node refNode, int offset) throws DOMException {
        setEndImpl(getPeer(), NodeImpl.getPeer(refNode), offset);
    }

    @Override // org.w3c.dom.ranges.Range
    public void setStartBefore(Node refNode) throws DOMException {
        setStartBeforeImpl(getPeer(), NodeImpl.getPeer(refNode));
    }

    @Override // org.w3c.dom.ranges.Range
    public void setStartAfter(Node refNode) throws DOMException {
        setStartAfterImpl(getPeer(), NodeImpl.getPeer(refNode));
    }

    @Override // org.w3c.dom.ranges.Range
    public void setEndBefore(Node refNode) throws DOMException {
        setEndBeforeImpl(getPeer(), NodeImpl.getPeer(refNode));
    }

    @Override // org.w3c.dom.ranges.Range
    public void setEndAfter(Node refNode) throws DOMException {
        setEndAfterImpl(getPeer(), NodeImpl.getPeer(refNode));
    }

    @Override // org.w3c.dom.ranges.Range
    public void collapse(boolean toStart) {
        collapseImpl(getPeer(), toStart);
    }

    @Override // org.w3c.dom.ranges.Range
    public void selectNode(Node refNode) throws DOMException {
        selectNodeImpl(getPeer(), NodeImpl.getPeer(refNode));
    }

    @Override // org.w3c.dom.ranges.Range
    public void selectNodeContents(Node refNode) throws DOMException {
        selectNodeContentsImpl(getPeer(), NodeImpl.getPeer(refNode));
    }

    @Override // org.w3c.dom.ranges.Range
    public short compareBoundaryPoints(short how, Range sourceRange) throws DOMException {
        return compareBoundaryPointsImpl(getPeer(), how, getPeer(sourceRange));
    }

    @Override // org.w3c.dom.ranges.Range
    public void deleteContents() throws DOMException {
        deleteContentsImpl(getPeer());
    }

    @Override // org.w3c.dom.ranges.Range
    public DocumentFragment extractContents() throws DOMException {
        return DocumentFragmentImpl.getImpl(extractContentsImpl(getPeer()));
    }

    @Override // org.w3c.dom.ranges.Range
    public DocumentFragment cloneContents() throws DOMException {
        return DocumentFragmentImpl.getImpl(cloneContentsImpl(getPeer()));
    }

    @Override // org.w3c.dom.ranges.Range
    public void insertNode(Node newNode) throws DOMException {
        insertNodeImpl(getPeer(), NodeImpl.getPeer(newNode));
    }

    @Override // org.w3c.dom.ranges.Range
    public void surroundContents(Node newParent) throws DOMException {
        surroundContentsImpl(getPeer(), NodeImpl.getPeer(newParent));
    }

    @Override // org.w3c.dom.ranges.Range
    public Range cloneRange() {
        return getImpl(cloneRangeImpl(getPeer()));
    }

    @Override // org.w3c.dom.ranges.Range
    public String toString() {
        return toStringImpl(getPeer());
    }

    @Override // org.w3c.dom.ranges.Range
    public void detach() {
        detachImpl(getPeer());
    }

    public DocumentFragment createContextualFragment(String html) throws DOMException {
        return DocumentFragmentImpl.getImpl(createContextualFragmentImpl(getPeer(), html));
    }

    public short compareNode(Node refNode) throws DOMException {
        return compareNodeImpl(getPeer(), NodeImpl.getPeer(refNode));
    }

    public short comparePoint(Node refNode, int offset) throws DOMException {
        return comparePointImpl(getPeer(), NodeImpl.getPeer(refNode), offset);
    }

    public boolean intersectsNode(Node refNode) throws DOMException {
        return intersectsNodeImpl(getPeer(), NodeImpl.getPeer(refNode));
    }

    public boolean isPointInRange(Node refNode, int offset) throws DOMException {
        return isPointInRangeImpl(getPeer(), NodeImpl.getPeer(refNode), offset);
    }

    public void expand(String unit) throws DOMException {
        expandImpl(getPeer(), unit);
    }
}
