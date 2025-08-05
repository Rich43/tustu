package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DocumentFragmentImpl.class */
public class DocumentFragmentImpl extends ParentNode implements DocumentFragment {
    static final long serialVersionUID = -7596449967279236746L;

    public DocumentFragmentImpl(CoreDocumentImpl ownerDoc) {
        super(ownerDoc);
    }

    public DocumentFragmentImpl() {
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 11;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        return "#document-fragment";
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void normalize() {
        if (isNormalized()) {
            return;
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        ChildNode childNode = this.firstChild;
        while (true) {
            ChildNode childNode2 = childNode;
            if (childNode2 != 0) {
                ChildNode next = childNode2.nextSibling;
                if (childNode2.getNodeType() == 3) {
                    if (next != null && next.getNodeType() == 3) {
                        ((Text) childNode2).appendData(next.getNodeValue());
                        removeChild(next);
                        next = childNode2;
                    } else if (childNode2.getNodeValue() == null || childNode2.getNodeValue().length() == 0) {
                        removeChild(childNode2);
                    }
                }
                childNode2.normalize();
                childNode = next;
            } else {
                isNormalized(true);
                return;
            }
        }
    }
}
