package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeferredElementNSImpl.class */
public class DeferredElementNSImpl extends ElementNSImpl implements DeferredNode {
    static final long serialVersionUID = -5001885145370927385L;
    protected transient int fNodeIndex;

    DeferredElementNSImpl(DeferredDocumentImpl ownerDoc, int nodeIndex) {
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
        int index = this.name.indexOf(58);
        if (index < 0) {
            this.localName = this.name;
        } else {
            this.localName = this.name.substring(index + 1);
        }
        this.namespaceURI = ownerDocument.getNodeURI(this.fNodeIndex);
        this.type = (XSTypeDefinition) ownerDocument.getTypeInfo(this.fNodeIndex);
        setupDefaultAttributes();
        int attrIndex = ownerDocument.getNodeExtra(this.fNodeIndex);
        if (attrIndex != -1) {
            NamedNodeMap attrs = getAttributes();
            boolean seenSchemaDefault = false;
            do {
                AttrImpl attr = (AttrImpl) ownerDocument.getNodeObject(attrIndex);
                if (!attr.getSpecified() && (seenSchemaDefault || (attr.getNamespaceURI() != null && attr.getNamespaceURI() != NamespaceContext.XMLNS_URI && attr.getName().indexOf(58) < 0))) {
                    seenSchemaDefault = true;
                    attrs.setNamedItemNS(attr);
                } else {
                    attrs.setNamedItem(attr);
                }
                attrIndex = ownerDocument.getPrevSibling(attrIndex);
            } while (attrIndex != -1);
        }
        ownerDocument.mutationEvents = orig;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode
    protected final void synchronizeChildren() {
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) ownerDocument();
        ownerDocument.synchronizeChildren(this, this.fNodeIndex);
    }
}
