package com.sun.org.apache.xerces.internal.dom;

import java.util.MissingResourceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/TreeWalkerImpl.class */
public class TreeWalkerImpl implements TreeWalker {
    private boolean fEntityReferenceExpansion;
    int fWhatToShow;
    NodeFilter fNodeFilter;
    Node fCurrentNode;
    Node fRoot;

    public TreeWalkerImpl(Node root, int whatToShow, NodeFilter nodeFilter, boolean entityReferenceExpansion) {
        this.fEntityReferenceExpansion = false;
        this.fWhatToShow = -1;
        this.fCurrentNode = root;
        this.fRoot = root;
        this.fWhatToShow = whatToShow;
        this.fNodeFilter = nodeFilter;
        this.fEntityReferenceExpansion = entityReferenceExpansion;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node getRoot() {
        return this.fRoot;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public int getWhatToShow() {
        return this.fWhatToShow;
    }

    public void setWhatShow(int whatToShow) {
        this.fWhatToShow = whatToShow;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public NodeFilter getFilter() {
        return this.fNodeFilter;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public boolean getExpandEntityReferences() {
        return this.fEntityReferenceExpansion;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node getCurrentNode() {
        return this.fCurrentNode;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public void setCurrentNode(Node node) throws MissingResourceException {
        if (node == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
            throw new DOMException((short) 9, msg);
        }
        this.fCurrentNode = node;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node parentNode() {
        if (this.fCurrentNode == null) {
            return null;
        }
        Node node = getParentNode(this.fCurrentNode);
        if (node != null) {
            this.fCurrentNode = node;
        }
        return node;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node firstChild() {
        if (this.fCurrentNode == null) {
            return null;
        }
        Node node = getFirstChild(this.fCurrentNode);
        if (node != null) {
            this.fCurrentNode = node;
        }
        return node;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node lastChild() {
        if (this.fCurrentNode == null) {
            return null;
        }
        Node node = getLastChild(this.fCurrentNode);
        if (node != null) {
            this.fCurrentNode = node;
        }
        return node;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node previousSibling() {
        if (this.fCurrentNode == null) {
            return null;
        }
        Node node = getPreviousSibling(this.fCurrentNode);
        if (node != null) {
            this.fCurrentNode = node;
        }
        return node;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node nextSibling() {
        if (this.fCurrentNode == null) {
            return null;
        }
        Node node = getNextSibling(this.fCurrentNode);
        if (node != null) {
            this.fCurrentNode = node;
        }
        return node;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node previousNode() {
        if (this.fCurrentNode == null) {
            return null;
        }
        Node result = getPreviousSibling(this.fCurrentNode);
        if (result == null) {
            Node result2 = getParentNode(this.fCurrentNode);
            if (result2 != null) {
                this.fCurrentNode = result2;
                return this.fCurrentNode;
            }
            return null;
        }
        Node lastChild = getLastChild(result);
        Node prev = lastChild;
        while (lastChild != null) {
            prev = lastChild;
            lastChild = getLastChild(prev);
        }
        Node lastChild2 = prev;
        if (lastChild2 != null) {
            this.fCurrentNode = lastChild2;
            return this.fCurrentNode;
        }
        if (result != null) {
            this.fCurrentNode = result;
            return this.fCurrentNode;
        }
        return null;
    }

    @Override // org.w3c.dom.traversal.TreeWalker
    public Node nextNode() {
        if (this.fCurrentNode == null) {
            return null;
        }
        Node result = getFirstChild(this.fCurrentNode);
        if (result != null) {
            this.fCurrentNode = result;
            return result;
        }
        Node result2 = getNextSibling(this.fCurrentNode);
        if (result2 != null) {
            this.fCurrentNode = result2;
            return result2;
        }
        Node parentNode = getParentNode(this.fCurrentNode);
        while (true) {
            Node parent = parentNode;
            if (parent != null) {
                Node result3 = getNextSibling(parent);
                if (result3 != null) {
                    this.fCurrentNode = result3;
                    return result3;
                }
                parentNode = getParentNode(parent);
            } else {
                return null;
            }
        }
    }

    Node getParentNode(Node node) {
        Node newNode;
        if (node == null || node == this.fRoot || (newNode = node.getParentNode()) == null) {
            return null;
        }
        int accept = acceptNode(newNode);
        if (accept == 1) {
            return newNode;
        }
        return getParentNode(newNode);
    }

    Node getNextSibling(Node node) {
        return getNextSibling(node, this.fRoot);
    }

    Node getNextSibling(Node node, Node root) {
        if (node == null || node == root) {
            return null;
        }
        Node newNode = node.getNextSibling();
        if (newNode == null) {
            Node newNode2 = node.getParentNode();
            if (newNode2 == null || newNode2 == root) {
                return null;
            }
            int parentAccept = acceptNode(newNode2);
            if (parentAccept == 3) {
                return getNextSibling(newNode2, root);
            }
            return null;
        }
        int accept = acceptNode(newNode);
        if (accept == 1) {
            return newNode;
        }
        if (accept == 3) {
            Node fChild = getFirstChild(newNode);
            if (fChild == null) {
                return getNextSibling(newNode, root);
            }
            return fChild;
        }
        return getNextSibling(newNode, root);
    }

    Node getPreviousSibling(Node node) {
        return getPreviousSibling(node, this.fRoot);
    }

    Node getPreviousSibling(Node node, Node root) {
        if (node == null || node == root) {
            return null;
        }
        Node newNode = node.getPreviousSibling();
        if (newNode == null) {
            Node newNode2 = node.getParentNode();
            if (newNode2 == null || newNode2 == root) {
                return null;
            }
            int parentAccept = acceptNode(newNode2);
            if (parentAccept == 3) {
                return getPreviousSibling(newNode2, root);
            }
            return null;
        }
        int accept = acceptNode(newNode);
        if (accept == 1) {
            return newNode;
        }
        if (accept == 3) {
            Node fChild = getLastChild(newNode);
            if (fChild == null) {
                return getPreviousSibling(newNode, root);
            }
            return fChild;
        }
        return getPreviousSibling(newNode, root);
    }

    Node getFirstChild(Node node) {
        Node newNode;
        if (node == null) {
            return null;
        }
        if ((!this.fEntityReferenceExpansion && node.getNodeType() == 5) || (newNode = node.getFirstChild()) == null) {
            return null;
        }
        int accept = acceptNode(newNode);
        if (accept == 1) {
            return newNode;
        }
        if (accept == 3 && newNode.hasChildNodes()) {
            Node fChild = getFirstChild(newNode);
            if (fChild == null) {
                return getNextSibling(newNode, node);
            }
            return fChild;
        }
        return getNextSibling(newNode, node);
    }

    Node getLastChild(Node node) {
        Node newNode;
        if (node == null) {
            return null;
        }
        if ((!this.fEntityReferenceExpansion && node.getNodeType() == 5) || (newNode = node.getLastChild()) == null) {
            return null;
        }
        int accept = acceptNode(newNode);
        if (accept == 1) {
            return newNode;
        }
        if (accept == 3 && newNode.hasChildNodes()) {
            Node lChild = getLastChild(newNode);
            if (lChild == null) {
                return getPreviousSibling(newNode, node);
            }
            return lChild;
        }
        return getPreviousSibling(newNode, node);
    }

    short acceptNode(Node node) {
        if (this.fNodeFilter == null) {
            if ((this.fWhatToShow & (1 << (node.getNodeType() - 1))) != 0) {
                return (short) 1;
            }
            return (short) 3;
        }
        if ((this.fWhatToShow & (1 << (node.getNodeType() - 1))) != 0) {
            return this.fNodeFilter.acceptNode(node);
        }
        return (short) 3;
    }
}
