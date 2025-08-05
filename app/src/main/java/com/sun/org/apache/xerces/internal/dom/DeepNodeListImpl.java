package com.sun.org.apache.xerces.internal.dom;

import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DeepNodeListImpl.class */
public class DeepNodeListImpl implements NodeList {
    protected NodeImpl rootNode;
    protected String tagName;
    protected int changes;
    protected Vector nodes;
    protected String nsName;
    protected boolean enableNS;

    public DeepNodeListImpl(NodeImpl rootNode, String tagName) {
        this.changes = 0;
        this.enableNS = false;
        this.rootNode = rootNode;
        this.tagName = tagName;
        this.nodes = new Vector();
    }

    public DeepNodeListImpl(NodeImpl rootNode, String nsName, String tagName) {
        this(rootNode, tagName);
        this.nsName = (nsName == null || nsName.equals("")) ? null : nsName;
        this.enableNS = true;
    }

    @Override // org.w3c.dom.NodeList
    public int getLength() {
        item(Integer.MAX_VALUE);
        return this.nodes.size();
    }

    @Override // org.w3c.dom.NodeList
    public Node item(int index) {
        Node thisNode;
        if (this.rootNode.changes() != this.changes) {
            this.nodes = new Vector();
            this.changes = this.rootNode.changes();
        }
        if (index < this.nodes.size()) {
            return (Node) this.nodes.elementAt(index);
        }
        if (this.nodes.size() == 0) {
            thisNode = this.rootNode;
        } else {
            thisNode = (NodeImpl) this.nodes.lastElement();
        }
        while (thisNode != null && index >= this.nodes.size()) {
            thisNode = nextMatchingElementAfter(thisNode);
            if (thisNode != null) {
                this.nodes.addElement(thisNode);
            }
        }
        return thisNode;
    }

    protected Node nextMatchingElementAfter(Node current) {
        Node next;
        while (current != null) {
            if (current.hasChildNodes()) {
                current = current.getFirstChild();
            } else if (current != this.rootNode && null != (next = current.getNextSibling())) {
                current = next;
            } else {
                Node next2 = null;
                while (current != this.rootNode) {
                    next2 = current.getNextSibling();
                    if (next2 != null) {
                        break;
                    }
                    current = current.getParentNode();
                }
                current = next2;
            }
            if (current != this.rootNode && current != null && current.getNodeType() == 1) {
                if (!this.enableNS) {
                    if (this.tagName.equals("*") || ((ElementImpl) current).getTagName().equals(this.tagName)) {
                        return current;
                    }
                } else if (this.tagName.equals("*")) {
                    if (this.nsName != null && this.nsName.equals("*")) {
                        return current;
                    }
                    ElementImpl el = (ElementImpl) current;
                    if ((this.nsName == null && el.getNamespaceURI() == null) || (this.nsName != null && this.nsName.equals(el.getNamespaceURI()))) {
                        return current;
                    }
                } else {
                    ElementImpl el2 = (ElementImpl) current;
                    if (el2.getLocalName() != null && el2.getLocalName().equals(this.tagName)) {
                        if (this.nsName != null && this.nsName.equals("*")) {
                            return current;
                        }
                        if ((this.nsName == null && el2.getNamespaceURI() == null) || (this.nsName != null && this.nsName.equals(el2.getNamespaceURI()))) {
                            return current;
                        }
                    }
                }
            }
        }
        return null;
    }
}
