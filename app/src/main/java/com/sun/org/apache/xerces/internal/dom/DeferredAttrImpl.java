package com.sun.org.apache.xerces.internal.dom;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredAttrImpl.class */
public final class DeferredAttrImpl extends AttrImpl implements DeferredNode {
    static final long serialVersionUID = 6903232312469148636L;
    protected transient int fNodeIndex;

    DeferredAttrImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null);
        this.fNodeIndex = nodeIndex;
        needsSyncData(true);
        needsSyncChildren(true);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.DeferredNode
    public int getNodeIndex() {
        return this.fNodeIndex;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected void synchronizeData() {
        needsSyncData(false);
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) ownerDocument();
        this.name = ownerDocument.getNodeName(this.fNodeIndex);
        int extra = ownerDocument.getNodeExtra(this.fNodeIndex);
        isSpecified((extra & 32) != 0);
        isIdAttribute((extra & 512) != 0);
        int extraNode = ownerDocument.getLastChild(this.fNodeIndex);
        this.type = ownerDocument.getTypeInfo(extraNode);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.AttrImpl
    protected void synchronizeChildren() {
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) ownerDocument();
        ownerDocument.synchronizeChildren(this, this.fNodeIndex);
    }
}
