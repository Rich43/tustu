package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/DOMSelectionImpl.class */
public class DOMSelectionImpl {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native long getAnchorNodeImpl(long j2);

    static native int getAnchorOffsetImpl(long j2);

    static native long getFocusNodeImpl(long j2);

    static native int getFocusOffsetImpl(long j2);

    static native boolean getIsCollapsedImpl(long j2);

    static native int getRangeCountImpl(long j2);

    static native long getBaseNodeImpl(long j2);

    static native int getBaseOffsetImpl(long j2);

    static native long getExtentNodeImpl(long j2);

    static native int getExtentOffsetImpl(long j2);

    static native String getTypeImpl(long j2);

    static native void collapseImpl(long j2, long j3, int i2);

    static native void collapseToEndImpl(long j2);

    static native void collapseToStartImpl(long j2);

    static native void deleteFromDocumentImpl(long j2);

    static native boolean containsNodeImpl(long j2, long j3, boolean z2);

    static native void selectAllChildrenImpl(long j2, long j3);

    static native void extendImpl(long j2, long j3, int i2);

    static native long getRangeAtImpl(long j2, int i2);

    static native void removeAllRangesImpl(long j2);

    static native void addRangeImpl(long j2, long j3);

    static native void modifyImpl(long j2, String str, String str2, String str3);

    static native void setBaseAndExtentImpl(long j2, long j3, int i2, long j4, int i3);

    static native void setPositionImpl(long j2, long j3, int i2);

    static native void emptyImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/DOMSelectionImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            DOMSelectionImpl.dispose(this.peer);
        }
    }

    DOMSelectionImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static DOMSelectionImpl create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new DOMSelectionImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof DOMSelectionImpl) && this.peer == ((DOMSelectionImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(DOMSelectionImpl arg) {
        if (arg == null) {
            return 0L;
        }
        return arg.getPeer();
    }

    static DOMSelectionImpl getImpl(long peer) {
        return create(peer);
    }

    public Node getAnchorNode() {
        return NodeImpl.getImpl(getAnchorNodeImpl(getPeer()));
    }

    public int getAnchorOffset() {
        return getAnchorOffsetImpl(getPeer());
    }

    public Node getFocusNode() {
        return NodeImpl.getImpl(getFocusNodeImpl(getPeer()));
    }

    public int getFocusOffset() {
        return getFocusOffsetImpl(getPeer());
    }

    public boolean getIsCollapsed() {
        return getIsCollapsedImpl(getPeer());
    }

    public int getRangeCount() {
        return getRangeCountImpl(getPeer());
    }

    public Node getBaseNode() {
        return NodeImpl.getImpl(getBaseNodeImpl(getPeer()));
    }

    public int getBaseOffset() {
        return getBaseOffsetImpl(getPeer());
    }

    public Node getExtentNode() {
        return NodeImpl.getImpl(getExtentNodeImpl(getPeer()));
    }

    public int getExtentOffset() {
        return getExtentOffsetImpl(getPeer());
    }

    public String getType() {
        return getTypeImpl(getPeer());
    }

    public void collapse(Node node, int index) throws DOMException {
        collapseImpl(getPeer(), NodeImpl.getPeer(node), index);
    }

    public void collapseToEnd() throws DOMException {
        collapseToEndImpl(getPeer());
    }

    public void collapseToStart() throws DOMException {
        collapseToStartImpl(getPeer());
    }

    public void deleteFromDocument() {
        deleteFromDocumentImpl(getPeer());
    }

    public boolean containsNode(Node node, boolean allowPartial) {
        return containsNodeImpl(getPeer(), NodeImpl.getPeer(node), allowPartial);
    }

    public void selectAllChildren(Node node) throws DOMException {
        selectAllChildrenImpl(getPeer(), NodeImpl.getPeer(node));
    }

    public void extend(Node node, int offset) throws DOMException {
        extendImpl(getPeer(), NodeImpl.getPeer(node), offset);
    }

    public Range getRangeAt(int index) throws DOMException {
        return RangeImpl.getImpl(getRangeAtImpl(getPeer(), index));
    }

    public void removeAllRanges() {
        removeAllRangesImpl(getPeer());
    }

    public void addRange(Range range) {
        addRangeImpl(getPeer(), RangeImpl.getPeer(range));
    }

    public void modify(String alter, String direction, String granularity) {
        modifyImpl(getPeer(), alter, direction, granularity);
    }

    public void setBaseAndExtent(Node baseNode, int baseOffset, Node extentNode, int extentOffset) throws DOMException {
        setBaseAndExtentImpl(getPeer(), NodeImpl.getPeer(baseNode), baseOffset, NodeImpl.getPeer(extentNode), extentOffset);
    }

    public void setPosition(Node node, int offset) throws DOMException {
        setPositionImpl(getPeer(), NodeImpl.getPeer(node), offset);
    }

    public void empty() {
        emptyImpl(getPeer());
    }
}
