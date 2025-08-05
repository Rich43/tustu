package com.sun.org.apache.xerces.internal.dom;

import java.util.MissingResourceException;
import org.w3c.dom.DOMException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredDocumentTypeImpl.class */
public class DeferredDocumentTypeImpl extends DocumentTypeImpl implements DeferredNode {
    static final long serialVersionUID = -2172579663227313509L;
    protected transient int fNodeIndex;

    DeferredDocumentTypeImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
        this.publicID = ownerDocument.getNodeValue(this.fNodeIndex);
        this.systemID = ownerDocument.getNodeURI(this.fNodeIndex);
        int extraDataIndex = ownerDocument.getNodeExtra(this.fNodeIndex);
        this.internalSubset = ownerDocument.getNodeValue(extraDataIndex);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode
    protected void synchronizeChildren() throws DOMException, MissingResourceException {
        boolean orig = ownerDocument().getMutationEvents();
        ownerDocument().setMutationEvents(false);
        needsSyncChildren(false);
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) this.ownerDocument;
        this.entities = new NamedNodeMapImpl(this);
        this.notations = new NamedNodeMapImpl(this);
        this.elements = new NamedNodeMapImpl(this);
        DeferredNode last = null;
        int lastChild = ownerDocument.getLastChild(this.fNodeIndex);
        while (true) {
            int index = lastChild;
            if (index != -1) {
                DeferredNode node = ownerDocument.getNodeObject(index);
                int type = node.getNodeType();
                switch (type) {
                    case 1:
                        if (((DocumentImpl) getOwnerDocument()).allowGrammarAccess) {
                            insertBefore(node, last);
                            last = node;
                        }
                        lastChild = ownerDocument.getPrevSibling(index);
                        break;
                    case 6:
                        this.entities.setNamedItem(node);
                        continue;
                        lastChild = ownerDocument.getPrevSibling(index);
                    case 12:
                        this.notations.setNamedItem(node);
                        continue;
                        lastChild = ownerDocument.getPrevSibling(index);
                    case 21:
                        this.elements.setNamedItem(node);
                        continue;
                        lastChild = ownerDocument.getPrevSibling(index);
                }
                System.out.println("DeferredDocumentTypeImpl#synchronizeInfo: node.getNodeType() = " + ((int) node.getNodeType()) + ", class = " + node.getClass().getName());
                lastChild = ownerDocument.getPrevSibling(index);
            } else {
                ownerDocument().setMutationEvents(orig);
                setReadOnly(true, false);
                return;
            }
        }
    }
}
