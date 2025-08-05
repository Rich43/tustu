package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/ChildNode.class */
public abstract class ChildNode extends NodeImpl {
    static final long serialVersionUID = -6112455738802414002L;
    transient StringBuffer fBufferStr;
    protected ChildNode previousSibling;
    protected ChildNode nextSibling;

    protected ChildNode(CoreDocumentImpl ownerDocument) {
        super(ownerDocument);
        this.fBufferStr = null;
    }

    public ChildNode() {
        this.fBufferStr = null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        ChildNode newnode = (ChildNode) super.cloneNode(deep);
        newnode.previousSibling = null;
        newnode.nextSibling = null;
        newnode.isFirstChild(false);
        return newnode;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node getParentNode() {
        if (isOwned()) {
            return this.ownerNode;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    final NodeImpl parentNode() {
        if (isOwned()) {
            return this.ownerNode;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node getNextSibling() {
        return this.nextSibling;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node getPreviousSibling() {
        if (isFirstChild()) {
            return null;
        }
        return this.previousSibling;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    final ChildNode previousSibling() {
        if (isFirstChild()) {
            return null;
        }
        return this.previousSibling;
    }
}
