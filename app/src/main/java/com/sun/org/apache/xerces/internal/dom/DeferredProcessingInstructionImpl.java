package com.sun.org.apache.xerces.internal.dom;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredProcessingInstructionImpl.class */
public class DeferredProcessingInstructionImpl extends ProcessingInstructionImpl implements DeferredNode {
    static final long serialVersionUID = -4643577954293565388L;
    protected transient int fNodeIndex;

    DeferredProcessingInstructionImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null, null);
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
        this.target = ownerDocument.getNodeName(this.fNodeIndex);
        this.data = ownerDocument.getNodeValueString(this.fNodeIndex);
    }
}
