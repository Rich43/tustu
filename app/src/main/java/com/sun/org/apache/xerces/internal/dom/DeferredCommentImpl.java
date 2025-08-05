package com.sun.org.apache.xerces.internal.dom;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredCommentImpl.class */
public class DeferredCommentImpl extends CommentImpl implements DeferredNode {
    static final long serialVersionUID = 6498796371083589338L;
    protected transient int fNodeIndex;

    DeferredCommentImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null);
        this.fNodeIndex = nodeIndex;
        needsSyncData(true);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.DeferredNode
    public int getNodeIndex() {
        return this.fNodeIndex;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected void synchronizeData() {
        needsSyncData(false);
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) ownerDocument();
        this.data = ownerDocument.getNodeValueString(this.fNodeIndex);
    }
}
