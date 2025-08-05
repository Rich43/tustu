package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/ElementDefinitionImpl.class */
public class ElementDefinitionImpl extends ParentNode {
    static final long serialVersionUID = -8373890672670022714L;
    protected String name;
    protected NamedNodeMapImpl attributes;

    public ElementDefinitionImpl(CoreDocumentImpl ownerDocument, String name) {
        super(ownerDocument);
        this.name = name;
        this.attributes = new NamedNodeMapImpl(ownerDocument);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 21;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        ElementDefinitionImpl newnode = (ElementDefinitionImpl) super.cloneNode(deep);
        newnode.attributes = this.attributes.cloneMap(newnode);
        return newnode;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.attributes;
    }
}
