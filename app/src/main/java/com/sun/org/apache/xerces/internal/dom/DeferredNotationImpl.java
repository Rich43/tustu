package com.sun.org.apache.xerces.internal.dom;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredNotationImpl.class */
public class DeferredNotationImpl extends NotationImpl implements DeferredNode {
    static final long serialVersionUID = 5705337172887990848L;
    protected transient int fNodeIndex;

    DeferredNotationImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
        this.name = ownerDocument.getNodeName(this.fNodeIndex);
        ownerDocument.getNodeType(this.fNodeIndex);
        this.publicId = ownerDocument.getNodeValue(this.fNodeIndex);
        this.systemId = ownerDocument.getNodeURI(this.fNodeIndex);
        int extraDataIndex = ownerDocument.getNodeExtra(this.fNodeIndex);
        ownerDocument.getNodeType(extraDataIndex);
        this.baseURI = ownerDocument.getNodeName(extraDataIndex);
    }
}
