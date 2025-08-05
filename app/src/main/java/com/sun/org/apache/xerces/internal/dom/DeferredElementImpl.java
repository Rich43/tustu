package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredElementImpl.class */
public class DeferredElementImpl extends ElementImpl implements DeferredNode {
    static final long serialVersionUID = -7670981133940934842L;
    protected transient int fNodeIndex;

    DeferredElementImpl(DeferredDocumentImpl ownerDoc, int nodeIndex) {
        super(ownerDoc, null);
        this.fNodeIndex = nodeIndex;
        needsSyncChildren(true);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.DeferredNode
    public final int getNodeIndex() {
        return this.fNodeIndex;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ElementImpl, com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected final void synchronizeData() throws DOMException {
        needsSyncData(false);
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) this.ownerDocument;
        boolean orig = ownerDocument.mutationEvents;
        ownerDocument.mutationEvents = false;
        this.name = ownerDocument.getNodeName(this.fNodeIndex);
        setupDefaultAttributes();
        int index = ownerDocument.getNodeExtra(this.fNodeIndex);
        if (index != -1) {
            NamedNodeMap attrs = getAttributes();
            do {
                NodeImpl attr = (NodeImpl) ownerDocument.getNodeObject(index);
                attrs.setNamedItem(attr);
                index = ownerDocument.getPrevSibling(index);
            } while (index != -1);
        }
        ownerDocument.mutationEvents = orig;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode
    protected final void synchronizeChildren() {
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) ownerDocument();
        ownerDocument.synchronizeChildren(this, this.fNodeIndex);
    }
}
