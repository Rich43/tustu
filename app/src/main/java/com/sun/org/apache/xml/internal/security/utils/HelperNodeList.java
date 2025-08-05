package com.sun.org.apache.xml.internal.security.utils;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/HelperNodeList.class */
public class HelperNodeList implements NodeList {
    List<Node> nodes;
    boolean allNodesMustHaveSameParent;

    public HelperNodeList() {
        this(false);
    }

    public HelperNodeList(boolean z2) {
        this.nodes = new ArrayList();
        this.allNodesMustHaveSameParent = false;
        this.allNodesMustHaveSameParent = z2;
    }

    @Override // org.w3c.dom.NodeList
    public Node item(int i2) {
        return this.nodes.get(i2);
    }

    @Override // org.w3c.dom.NodeList
    public int getLength() {
        return this.nodes.size();
    }

    public void appendChild(Node node) throws IllegalArgumentException {
        if (this.allNodesMustHaveSameParent && getLength() > 0 && item(0).getParentNode() != node.getParentNode()) {
            throw new IllegalArgumentException("Nodes have not the same Parent");
        }
        this.nodes.add(node);
    }

    public Document getOwnerDocument() {
        if (getLength() == 0) {
            return null;
        }
        return XMLUtils.getOwnerDocument(item(0));
    }
}
