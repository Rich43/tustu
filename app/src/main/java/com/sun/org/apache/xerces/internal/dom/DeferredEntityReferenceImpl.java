package com.sun.org.apache.xerces.internal.dom;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredEntityReferenceImpl.class */
public class DeferredEntityReferenceImpl extends EntityReferenceImpl implements DeferredNode {
    static final long serialVersionUID = 390319091370032223L;
    protected transient int fNodeIndex;

    DeferredEntityReferenceImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) this.ownerDocument;
        this.name = ownerDocument.getNodeName(this.fNodeIndex);
        this.baseURI = ownerDocument.getNodeValue(this.fNodeIndex);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.EntityReferenceImpl, com.sun.org.apache.xerces.internal.dom.ParentNode
    protected void synchronizeChildren() {
        needsSyncChildren(false);
        isReadOnly(false);
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) ownerDocument();
        ownerDocument.synchronizeChildren(this, this.fNodeIndex);
        setReadOnly(true, true);
    }
}
