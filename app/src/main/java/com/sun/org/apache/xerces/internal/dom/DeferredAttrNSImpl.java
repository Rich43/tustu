package com.sun.org.apache.xerces.internal.dom;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredAttrNSImpl.class */
public final class DeferredAttrNSImpl extends AttrNSImpl implements DeferredNode {
    static final long serialVersionUID = 6074924934945957154L;
    protected transient int fNodeIndex;

    DeferredAttrNSImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
        int index = this.name.indexOf(58);
        if (index < 0) {
            this.localName = this.name;
        } else {
            this.localName = this.name.substring(index + 1);
        }
        int extra = ownerDocument.getNodeExtra(this.fNodeIndex);
        isSpecified((extra & 32) != 0);
        isIdAttribute((extra & 512) != 0);
        this.namespaceURI = ownerDocument.getNodeURI(this.fNodeIndex);
        int extraNode = ownerDocument.getLastChild(this.fNodeIndex);
        this.type = ownerDocument.getTypeInfo(extraNode);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.AttrImpl
    protected void synchronizeChildren() {
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) ownerDocument();
        ownerDocument.synchronizeChildren(this, this.fNodeIndex);
    }
}
