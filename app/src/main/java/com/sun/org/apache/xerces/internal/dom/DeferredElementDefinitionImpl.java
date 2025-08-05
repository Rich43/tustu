package com.sun.org.apache.xerces.internal.dom;

import java.util.MissingResourceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredElementDefinitionImpl.class */
public class DeferredElementDefinitionImpl extends ElementDefinitionImpl implements DeferredNode {
    static final long serialVersionUID = 6703238199538041591L;
    protected transient int fNodeIndex;

    DeferredElementDefinitionImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) this.ownerDocument;
        this.name = ownerDocument.getNodeName(this.fNodeIndex);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode
    protected void synchronizeChildren() throws DOMException, MissingResourceException {
        boolean orig = this.ownerDocument.getMutationEvents();
        this.ownerDocument.setMutationEvents(false);
        needsSyncChildren(false);
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) this.ownerDocument;
        this.attributes = new NamedNodeMapImpl(ownerDocument);
        int lastChild = ownerDocument.getLastChild(this.fNodeIndex);
        while (true) {
            int nodeIndex = lastChild;
            if (nodeIndex != -1) {
                Node attr = ownerDocument.getNodeObject(nodeIndex);
                this.attributes.setNamedItem(attr);
                lastChild = ownerDocument.getPrevSibling(nodeIndex);
            } else {
                ownerDocument.setMutationEvents(orig);
                return;
            }
        }
    }
}
