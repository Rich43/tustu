package com.sun.org.apache.xerces.internal.dom;

import java.util.Vector;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.ranges.RangeException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/RangeImpl.class */
public class RangeImpl implements Range {
    DocumentImpl fDocument;
    Node fStartContainer;
    Node fEndContainer;
    boolean fIsCollapsed;
    boolean fDetach;
    static final int EXTRACT_CONTENTS = 1;
    static final int CLONE_CONTENTS = 2;
    static final int DELETE_CONTENTS = 3;
    Node fInsertNode = null;
    Node fDeleteNode = null;
    Node fSplitNode = null;
    boolean fInsertedFromRange = false;
    Node fRemoveChild = null;
    int fStartOffset = 0;
    int fEndOffset = 0;

    public RangeImpl(DocumentImpl document) {
        this.fDetach = false;
        this.fDocument = document;
        this.fStartContainer = document;
        this.fEndContainer = document;
        this.fDetach = false;
    }

    @Override // org.w3c.dom.ranges.Range
    public Node getStartContainer() {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        return this.fStartContainer;
    }

    @Override // org.w3c.dom.ranges.Range
    public int getStartOffset() {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        return this.fStartOffset;
    }

    @Override // org.w3c.dom.ranges.Range
    public Node getEndContainer() {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        return this.fEndContainer;
    }

    @Override // org.w3c.dom.ranges.Range
    public int getEndOffset() {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        return this.fEndOffset;
    }

    @Override // org.w3c.dom.ranges.Range
    public boolean getCollapsed() {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        return this.fStartContainer == this.fEndContainer && this.fStartOffset == this.fEndOffset;
    }

    @Override // org.w3c.dom.ranges.Range
    public Node getCommonAncestorContainer() {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        Vector startV = new Vector();
        Node parentNode = this.fStartContainer;
        while (true) {
            Node node = parentNode;
            if (node == null) {
                break;
            }
            startV.addElement(node);
            parentNode = node.getParentNode();
        }
        Vector endV = new Vector();
        Node parentNode2 = this.fEndContainer;
        while (true) {
            Node node2 = parentNode2;
            if (node2 == null) {
                break;
            }
            endV.addElement(node2);
            parentNode2 = node2.getParentNode();
        }
        int s2 = startV.size() - 1;
        Object result = null;
        for (int e2 = endV.size() - 1; s2 >= 0 && e2 >= 0 && startV.elementAt(s2) == endV.elementAt(e2); e2--) {
            result = startV.elementAt(s2);
            s2--;
        }
        return (Node) result;
    }

    @Override // org.w3c.dom.ranges.Range
    public void setStart(Node refNode, int offset) throws RangeException, DOMException {
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (!isLegalContainer(refNode)) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
            if (this.fDocument != refNode.getOwnerDocument() && this.fDocument != refNode) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        checkIndex(refNode, offset);
        this.fStartContainer = refNode;
        this.fStartOffset = offset;
        if (getCommonAncestorContainer() == null || (this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset)) {
            collapse(true);
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void setEnd(Node refNode, int offset) throws RangeException, DOMException {
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (!isLegalContainer(refNode)) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
            if (this.fDocument != refNode.getOwnerDocument() && this.fDocument != refNode) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        checkIndex(refNode, offset);
        this.fEndContainer = refNode;
        this.fEndOffset = offset;
        if (getCommonAncestorContainer() == null || (this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset)) {
            collapse(false);
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void setStartBefore(Node refNode) throws RangeException {
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (!hasLegalRootContainer(refNode) || !isLegalContainedNode(refNode)) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
            if (this.fDocument != refNode.getOwnerDocument() && this.fDocument != refNode) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        this.fStartContainer = refNode.getParentNode();
        int i2 = 0;
        Node previousSibling = refNode;
        while (true) {
            Node n2 = previousSibling;
            if (n2 == null) {
                break;
            }
            i2++;
            previousSibling = n2.getPreviousSibling();
        }
        this.fStartOffset = i2 - 1;
        if (getCommonAncestorContainer() == null || (this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset)) {
            collapse(true);
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void setStartAfter(Node refNode) throws RangeException {
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (!hasLegalRootContainer(refNode) || !isLegalContainedNode(refNode)) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
            if (this.fDocument != refNode.getOwnerDocument() && this.fDocument != refNode) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        this.fStartContainer = refNode.getParentNode();
        int i2 = 0;
        Node previousSibling = refNode;
        while (true) {
            Node n2 = previousSibling;
            if (n2 == null) {
                break;
            }
            i2++;
            previousSibling = n2.getPreviousSibling();
        }
        this.fStartOffset = i2;
        if (getCommonAncestorContainer() == null || (this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset)) {
            collapse(true);
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void setEndBefore(Node refNode) throws RangeException {
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (!hasLegalRootContainer(refNode) || !isLegalContainedNode(refNode)) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
            if (this.fDocument != refNode.getOwnerDocument() && this.fDocument != refNode) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        this.fEndContainer = refNode.getParentNode();
        int i2 = 0;
        Node previousSibling = refNode;
        while (true) {
            Node n2 = previousSibling;
            if (n2 == null) {
                break;
            }
            i2++;
            previousSibling = n2.getPreviousSibling();
        }
        this.fEndOffset = i2 - 1;
        if (getCommonAncestorContainer() == null || (this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset)) {
            collapse(false);
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void setEndAfter(Node refNode) throws RangeException {
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (!hasLegalRootContainer(refNode) || !isLegalContainedNode(refNode)) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
            if (this.fDocument != refNode.getOwnerDocument() && this.fDocument != refNode) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        this.fEndContainer = refNode.getParentNode();
        int i2 = 0;
        Node previousSibling = refNode;
        while (true) {
            Node n2 = previousSibling;
            if (n2 == null) {
                break;
            }
            i2++;
            previousSibling = n2.getPreviousSibling();
        }
        this.fEndOffset = i2;
        if (getCommonAncestorContainer() == null || (this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset)) {
            collapse(false);
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void collapse(boolean toStart) {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        if (toStart) {
            this.fEndContainer = this.fStartContainer;
            this.fEndOffset = this.fStartOffset;
        } else {
            this.fStartContainer = this.fEndContainer;
            this.fStartOffset = this.fEndOffset;
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void selectNode(Node refNode) throws RangeException {
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (!isLegalContainer(refNode.getParentNode()) || !isLegalContainedNode(refNode)) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
            if (this.fDocument != refNode.getOwnerDocument() && this.fDocument != refNode) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        Node parent = refNode.getParentNode();
        if (parent != null) {
            this.fStartContainer = parent;
            this.fEndContainer = parent;
            int i2 = 0;
            Node previousSibling = refNode;
            while (true) {
                Node n2 = previousSibling;
                if (n2 != null) {
                    i2++;
                    previousSibling = n2.getPreviousSibling();
                } else {
                    this.fStartOffset = i2 - 1;
                    this.fEndOffset = this.fStartOffset + 1;
                    return;
                }
            }
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void selectNodeContents(Node refNode) throws RangeException {
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (!isLegalContainer(refNode)) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
            if (this.fDocument != refNode.getOwnerDocument() && this.fDocument != refNode) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        this.fStartContainer = refNode;
        this.fEndContainer = refNode;
        Node first = refNode.getFirstChild();
        this.fStartOffset = 0;
        if (first == null) {
            this.fEndOffset = 0;
            return;
        }
        int i2 = 0;
        Node nextSibling = first;
        while (true) {
            Node n2 = nextSibling;
            if (n2 != null) {
                i2++;
                nextSibling = n2.getNextSibling();
            } else {
                this.fEndOffset = i2;
                return;
            }
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public short compareBoundaryPoints(short how, Range sourceRange) throws DOMException {
        Node endPointA;
        Node endPointB;
        int offsetA;
        int offsetB;
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if ((this.fDocument != sourceRange.getStartContainer().getOwnerDocument() && this.fDocument != sourceRange.getStartContainer() && sourceRange.getStartContainer() != null) || (this.fDocument != sourceRange.getEndContainer().getOwnerDocument() && this.fDocument != sourceRange.getEndContainer() && sourceRange.getStartContainer() != null)) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        if (how == 0) {
            endPointA = sourceRange.getStartContainer();
            endPointB = this.fStartContainer;
            offsetA = sourceRange.getStartOffset();
            offsetB = this.fStartOffset;
        } else if (how == 1) {
            endPointA = sourceRange.getStartContainer();
            endPointB = this.fEndContainer;
            offsetA = sourceRange.getStartOffset();
            offsetB = this.fEndOffset;
        } else if (how == 3) {
            endPointA = sourceRange.getEndContainer();
            endPointB = this.fStartContainer;
            offsetA = sourceRange.getEndOffset();
            offsetB = this.fStartOffset;
        } else {
            endPointA = sourceRange.getEndContainer();
            endPointB = this.fEndContainer;
            offsetA = sourceRange.getEndOffset();
            offsetB = this.fEndOffset;
        }
        if (endPointA == endPointB) {
            if (offsetA < offsetB) {
                return (short) 1;
            }
            return offsetA == offsetB ? (short) 0 : (short) -1;
        }
        Node c2 = endPointB;
        Node parentNode = c2.getParentNode();
        while (true) {
            Node p2 = parentNode;
            if (p2 != null) {
                if (p2 != endPointA) {
                    c2 = p2;
                    parentNode = p2.getParentNode();
                } else {
                    int index = indexOf(c2, endPointA);
                    return offsetA <= index ? (short) 1 : (short) -1;
                }
            } else {
                Node c3 = endPointA;
                Node parentNode2 = c3.getParentNode();
                while (true) {
                    Node p3 = parentNode2;
                    if (p3 != null) {
                        if (p3 != endPointB) {
                            c3 = p3;
                            parentNode2 = p3.getParentNode();
                        } else {
                            int index2 = indexOf(c3, endPointB);
                            return index2 < offsetB ? (short) 1 : (short) -1;
                        }
                    } else {
                        int depthDiff = 0;
                        Node parentNode3 = endPointA;
                        while (true) {
                            Node n2 = parentNode3;
                            if (n2 == null) {
                                break;
                            }
                            depthDiff++;
                            parentNode3 = n2.getParentNode();
                        }
                        Node parentNode4 = endPointB;
                        while (true) {
                            Node n3 = parentNode4;
                            if (n3 == null) {
                                break;
                            }
                            depthDiff--;
                            parentNode4 = n3.getParentNode();
                        }
                        while (depthDiff > 0) {
                            endPointA = endPointA.getParentNode();
                            depthDiff--;
                        }
                        while (depthDiff < 0) {
                            endPointB = endPointB.getParentNode();
                            depthDiff++;
                        }
                        Node pA = endPointA.getParentNode();
                        Node parentNode5 = endPointB.getParentNode();
                        while (true) {
                            Node pB = parentNode5;
                            if (pA == pB) {
                                break;
                            }
                            endPointA = pA;
                            endPointB = pB;
                            pA = pA.getParentNode();
                            parentNode5 = pB.getParentNode();
                        }
                        Node nextSibling = endPointA.getNextSibling();
                        while (true) {
                            Node n4 = nextSibling;
                            if (n4 != null) {
                                if (n4 != endPointB) {
                                    nextSibling = n4.getNextSibling();
                                } else {
                                    return (short) 1;
                                }
                            } else {
                                return (short) -1;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // org.w3c.dom.ranges.Range
    public void deleteContents() throws DOMException {
        traverseContents(3);
    }

    @Override // org.w3c.dom.ranges.Range
    public DocumentFragment extractContents() throws DOMException {
        return traverseContents(1);
    }

    @Override // org.w3c.dom.ranges.Range
    public DocumentFragment cloneContents() throws DOMException {
        return traverseContents(2);
    }

    @Override // org.w3c.dom.ranges.Range
    public void insertNode(Node newNode) throws RangeException, DOMException {
        if (newNode == null) {
            return;
        }
        int type = newNode.getNodeType();
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (this.fDocument != newNode.getOwnerDocument()) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
            if (type == 2 || type == 6 || type == 12 || type == 9) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
        }
        int currentChildren = 0;
        this.fInsertedFromRange = true;
        if (this.fStartContainer.getNodeType() == 3) {
            Node parent = this.fStartContainer.getParentNode();
            int currentChildren2 = parent.getChildNodes().getLength();
            Node cloneCurrent = this.fStartContainer.cloneNode(false);
            ((TextImpl) cloneCurrent).setNodeValueInternal(cloneCurrent.getNodeValue().substring(this.fStartOffset));
            ((TextImpl) this.fStartContainer).setNodeValueInternal(this.fStartContainer.getNodeValue().substring(0, this.fStartOffset));
            Node next = this.fStartContainer.getNextSibling();
            if (next != null) {
                if (parent != null) {
                    parent.insertBefore(newNode, next);
                    parent.insertBefore(cloneCurrent, next);
                }
            } else if (parent != null) {
                parent.appendChild(newNode);
                parent.appendChild(cloneCurrent);
            }
            if (this.fEndContainer == this.fStartContainer) {
                this.fEndContainer = cloneCurrent;
                this.fEndOffset -= this.fStartOffset;
            } else if (this.fEndContainer == parent) {
                this.fEndOffset += parent.getChildNodes().getLength() - currentChildren2;
            }
            signalSplitData(this.fStartContainer, cloneCurrent, this.fStartOffset);
        } else {
            if (this.fEndContainer == this.fStartContainer) {
                currentChildren = this.fEndContainer.getChildNodes().getLength();
            }
            Node current = this.fStartContainer.getFirstChild();
            for (int i2 = 0; i2 < this.fStartOffset && current != null; i2++) {
                current = current.getNextSibling();
            }
            if (current != null) {
                this.fStartContainer.insertBefore(newNode, current);
            } else {
                this.fStartContainer.appendChild(newNode);
            }
            if (this.fEndContainer == this.fStartContainer && this.fEndOffset != 0) {
                this.fEndOffset += this.fEndContainer.getChildNodes().getLength() - currentChildren;
            }
        }
        this.fInsertedFromRange = false;
    }

    @Override // org.w3c.dom.ranges.Range
    public void surroundContents(Node newParent) throws RangeException, DOMException {
        if (newParent == null) {
            return;
        }
        int type = newParent.getNodeType();
        if (this.fDocument.errorChecking) {
            if (this.fDetach) {
                throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
            }
            if (type == 2 || type == 6 || type == 12 || type == 10 || type == 9 || type == 11) {
                throw new RangeExceptionImpl((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_NODE_TYPE_ERR", null));
            }
        }
        Node realStart = this.fStartContainer;
        Node realEnd = this.fEndContainer;
        if (this.fStartContainer.getNodeType() == 3) {
            realStart = this.fStartContainer.getParentNode();
        }
        if (this.fEndContainer.getNodeType() == 3) {
            realEnd = this.fEndContainer.getParentNode();
        }
        if (realStart != realEnd) {
            throw new RangeExceptionImpl((short) 1, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "BAD_BOUNDARYPOINTS_ERR", null));
        }
        DocumentFragment frag = extractContents();
        insertNode(newParent);
        newParent.appendChild(frag);
        selectNode(newParent);
    }

    @Override // org.w3c.dom.ranges.Range
    public Range cloneRange() throws RangeException, DOMException {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        Range range = this.fDocument.createRange();
        range.setStart(this.fStartContainer, this.fStartOffset);
        range.setEnd(this.fEndContainer, this.fEndOffset);
        return range;
    }

    @Override // org.w3c.dom.ranges.Range
    public String toString() {
        Node node;
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        Node node2 = this.fStartContainer;
        Node stopNode = this.fEndContainer;
        StringBuffer sb = new StringBuffer();
        if (this.fStartContainer.getNodeType() == 3 || this.fStartContainer.getNodeType() == 4) {
            if (this.fStartContainer == this.fEndContainer) {
                sb.append(this.fStartContainer.getNodeValue().substring(this.fStartOffset, this.fEndOffset));
                return sb.toString();
            }
            sb.append(this.fStartContainer.getNodeValue().substring(this.fStartOffset));
            node = nextNode(node2, true);
        } else {
            node = node2.getFirstChild();
            if (this.fStartOffset > 0) {
                for (int counter = 0; counter < this.fStartOffset && node != null; counter++) {
                    node = node.getNextSibling();
                }
            }
            if (node == null) {
                node = nextNode(this.fStartContainer, false);
            }
        }
        if (this.fEndContainer.getNodeType() != 3 && this.fEndContainer.getNodeType() != 4) {
            int i2 = this.fEndOffset;
            Node firstChild = this.fEndContainer.getFirstChild();
            while (true) {
                stopNode = firstChild;
                if (i2 <= 0 || stopNode == null) {
                    break;
                }
                i2--;
                firstChild = stopNode.getNextSibling();
            }
            if (stopNode == null) {
                stopNode = nextNode(this.fEndContainer, false);
            }
        }
        while (node != stopNode && node != null) {
            if (node.getNodeType() == 3 || node.getNodeType() == 4) {
                sb.append(node.getNodeValue());
            }
            node = nextNode(node, true);
        }
        if (this.fEndContainer.getNodeType() == 3 || this.fEndContainer.getNodeType() == 4) {
            sb.append(this.fEndContainer.getNodeValue().substring(0, this.fEndOffset));
        }
        return sb.toString();
    }

    @Override // org.w3c.dom.ranges.Range
    public void detach() {
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        this.fDetach = true;
        this.fDocument.removeRange(this);
    }

    void signalSplitData(Node node, Node newNode, int offset) {
        this.fSplitNode = node;
        this.fDocument.splitData(node, newNode, offset);
        this.fSplitNode = null;
    }

    void receiveSplitData(Node node, Node newNode, int offset) {
        if (node == null || newNode == null || this.fSplitNode == node) {
            return;
        }
        if (node == this.fStartContainer && this.fStartContainer.getNodeType() == 3 && this.fStartOffset > offset) {
            this.fStartOffset -= offset;
            this.fStartContainer = newNode;
        }
        if (node == this.fEndContainer && this.fEndContainer.getNodeType() == 3 && this.fEndOffset > offset) {
            this.fEndOffset -= offset;
            this.fEndContainer = newNode;
        }
    }

    void deleteData(CharacterData node, int offset, int count) throws DOMException {
        this.fDeleteNode = node;
        node.deleteData(offset, count);
        this.fDeleteNode = null;
    }

    void receiveDeletedText(Node node, int offset, int count) {
        if (node == null || this.fDeleteNode == node) {
            return;
        }
        if (node == this.fStartContainer && this.fStartContainer.getNodeType() == 3) {
            if (this.fStartOffset > offset + count) {
                this.fStartOffset = offset + (this.fStartOffset - (offset + count));
            } else if (this.fStartOffset > offset) {
                this.fStartOffset = offset;
            }
        }
        if (node == this.fEndContainer && this.fEndContainer.getNodeType() == 3) {
            if (this.fEndOffset > offset + count) {
                this.fEndOffset = offset + (this.fEndOffset - (offset + count));
            } else if (this.fEndOffset > offset) {
                this.fEndOffset = offset;
            }
        }
    }

    void insertData(CharacterData node, int index, String insert) throws DOMException {
        this.fInsertNode = node;
        node.insertData(index, insert);
        this.fInsertNode = null;
    }

    void receiveInsertedText(Node node, int index, int len) {
        if (node == null || this.fInsertNode == node) {
            return;
        }
        if (node == this.fStartContainer && this.fStartContainer.getNodeType() == 3 && index < this.fStartOffset) {
            this.fStartOffset += len;
        }
        if (node == this.fEndContainer && this.fEndContainer.getNodeType() == 3 && index < this.fEndOffset) {
            this.fEndOffset += len;
        }
    }

    void receiveReplacedText(Node node) {
        if (node == null) {
            return;
        }
        if (node == this.fStartContainer && this.fStartContainer.getNodeType() == 3) {
            this.fStartOffset = 0;
        }
        if (node == this.fEndContainer && this.fEndContainer.getNodeType() == 3) {
            this.fEndOffset = 0;
        }
    }

    public void insertedNodeFromDOM(Node node) {
        if (node == null || this.fInsertNode == node || this.fInsertedFromRange) {
            return;
        }
        Node parent = node.getParentNode();
        if (parent == this.fStartContainer) {
            int index = indexOf(node, this.fStartContainer);
            if (index < this.fStartOffset) {
                this.fStartOffset++;
            }
        }
        if (parent == this.fEndContainer) {
            int index2 = indexOf(node, this.fEndContainer);
            if (index2 < this.fEndOffset) {
                this.fEndOffset++;
            }
        }
    }

    Node removeChild(Node parent, Node child) throws DOMException {
        this.fRemoveChild = child;
        Node n2 = parent.removeChild(child);
        this.fRemoveChild = null;
        return n2;
    }

    void removeNode(Node node) {
        if (node == null || this.fRemoveChild == node) {
            return;
        }
        Node parent = node.getParentNode();
        if (parent == this.fStartContainer) {
            int index = indexOf(node, this.fStartContainer);
            if (index < this.fStartOffset) {
                this.fStartOffset--;
            }
        }
        if (parent == this.fEndContainer) {
            int index2 = indexOf(node, this.fEndContainer);
            if (index2 < this.fEndOffset) {
                this.fEndOffset--;
            }
        }
        if (parent != this.fStartContainer || parent != this.fEndContainer) {
            if (isAncestorOf(node, this.fStartContainer)) {
                this.fStartContainer = parent;
                this.fStartOffset = indexOf(node, parent);
            }
            if (isAncestorOf(node, this.fEndContainer)) {
                this.fEndContainer = parent;
                this.fEndOffset = indexOf(node, parent);
            }
        }
    }

    private DocumentFragment traverseContents(int how) throws DOMException {
        if (this.fStartContainer == null || this.fEndContainer == null) {
            return null;
        }
        if (this.fDetach) {
            throw new DOMException((short) 11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
        }
        if (this.fStartContainer == this.fEndContainer) {
            return traverseSameContainer(how);
        }
        int endContainerDepth = 0;
        Node c2 = this.fEndContainer;
        Node parentNode = c2.getParentNode();
        while (true) {
            Node p2 = parentNode;
            if (p2 != null) {
                if (p2 == this.fStartContainer) {
                    return traverseCommonStartContainer(c2, how);
                }
                endContainerDepth++;
                c2 = p2;
                parentNode = p2.getParentNode();
            } else {
                int startContainerDepth = 0;
                Node c3 = this.fStartContainer;
                Node parentNode2 = c3.getParentNode();
                while (true) {
                    Node p3 = parentNode2;
                    if (p3 != null) {
                        if (p3 == this.fEndContainer) {
                            return traverseCommonEndContainer(c3, how);
                        }
                        startContainerDepth++;
                        c3 = p3;
                        parentNode2 = p3.getParentNode();
                    } else {
                        int depthDiff = startContainerDepth - endContainerDepth;
                        Node startNode = this.fStartContainer;
                        while (depthDiff > 0) {
                            startNode = startNode.getParentNode();
                            depthDiff--;
                        }
                        Node endNode = this.fEndContainer;
                        while (depthDiff < 0) {
                            endNode = endNode.getParentNode();
                            depthDiff++;
                        }
                        Node sp = startNode.getParentNode();
                        Node parentNode3 = endNode.getParentNode();
                        while (true) {
                            Node ep = parentNode3;
                            if (sp != ep) {
                                startNode = sp;
                                endNode = ep;
                                sp = sp.getParentNode();
                                parentNode3 = ep.getParentNode();
                            } else {
                                return traverseCommonAncestors(startNode, endNode, how);
                            }
                        }
                    }
                }
            }
        }
    }

    private DocumentFragment traverseSameContainer(int how) throws DOMException {
        DocumentFragment frag = null;
        if (how != 3) {
            frag = this.fDocument.createDocumentFragment();
        }
        if (this.fStartOffset == this.fEndOffset) {
            return frag;
        }
        if (this.fStartContainer.getNodeType() == 3) {
            String s2 = this.fStartContainer.getNodeValue();
            String sub = s2.substring(this.fStartOffset, this.fEndOffset);
            if (how != 2) {
                ((TextImpl) this.fStartContainer).deleteData(this.fStartOffset, this.fEndOffset - this.fStartOffset);
                collapse(true);
            }
            if (how == 3) {
                return null;
            }
            frag.appendChild(this.fDocument.createTextNode(sub));
            return frag;
        }
        Node n2 = getSelectedNode(this.fStartContainer, this.fStartOffset);
        int cnt = this.fEndOffset - this.fStartOffset;
        while (cnt > 0) {
            Node sibling = n2.getNextSibling();
            Node xferNode = traverseFullySelected(n2, how);
            if (frag != null) {
                frag.appendChild(xferNode);
            }
            cnt--;
            n2 = sibling;
        }
        if (how != 2) {
            collapse(true);
        }
        return frag;
    }

    private DocumentFragment traverseCommonStartContainer(Node endAncestor, int how) throws RangeException, DOMException {
        DocumentFragment frag = null;
        if (how != 3) {
            frag = this.fDocument.createDocumentFragment();
        }
        Node n2 = traverseRightBoundary(endAncestor, how);
        if (frag != null) {
            frag.appendChild(n2);
        }
        int endIdx = indexOf(endAncestor, this.fStartContainer);
        int cnt = endIdx - this.fStartOffset;
        if (cnt <= 0) {
            if (how != 2) {
                setEndBefore(endAncestor);
                collapse(false);
            }
            return frag;
        }
        Node previousSibling = endAncestor.getPreviousSibling();
        while (true) {
            Node n3 = previousSibling;
            if (cnt <= 0) {
                break;
            }
            Node sibling = n3.getPreviousSibling();
            Node xferNode = traverseFullySelected(n3, how);
            if (frag != null) {
                frag.insertBefore(xferNode, frag.getFirstChild());
            }
            cnt--;
            previousSibling = sibling;
        }
        if (how != 2) {
            setEndBefore(endAncestor);
            collapse(false);
        }
        return frag;
    }

    private DocumentFragment traverseCommonEndContainer(Node startAncestor, int how) throws RangeException, DOMException {
        DocumentFragment frag = null;
        if (how != 3) {
            frag = this.fDocument.createDocumentFragment();
        }
        Node n2 = traverseLeftBoundary(startAncestor, how);
        if (frag != null) {
            frag.appendChild(n2);
        }
        int startIdx = indexOf(startAncestor, this.fEndContainer);
        int cnt = this.fEndOffset - (startIdx + 1);
        Node nextSibling = startAncestor.getNextSibling();
        while (true) {
            Node n3 = nextSibling;
            if (cnt <= 0) {
                break;
            }
            Node sibling = n3.getNextSibling();
            Node xferNode = traverseFullySelected(n3, how);
            if (frag != null) {
                frag.appendChild(xferNode);
            }
            cnt--;
            nextSibling = sibling;
        }
        if (how != 2) {
            setStartAfter(startAncestor);
            collapse(true);
        }
        return frag;
    }

    private DocumentFragment traverseCommonAncestors(Node startAncestor, Node endAncestor, int how) throws RangeException, DOMException {
        DocumentFragment frag = null;
        if (how != 3) {
            frag = this.fDocument.createDocumentFragment();
        }
        Node n2 = traverseLeftBoundary(startAncestor, how);
        if (frag != null) {
            frag.appendChild(n2);
        }
        Node commonParent = startAncestor.getParentNode();
        int startOffset = indexOf(startAncestor, commonParent);
        int endOffset = indexOf(endAncestor, commonParent);
        Node sibling = startAncestor.getNextSibling();
        for (int cnt = endOffset - (startOffset + 1); cnt > 0; cnt--) {
            Node nextSibling = sibling.getNextSibling();
            Node n3 = traverseFullySelected(sibling, how);
            if (frag != null) {
                frag.appendChild(n3);
            }
            sibling = nextSibling;
        }
        Node n4 = traverseRightBoundary(endAncestor, how);
        if (frag != null) {
            frag.appendChild(n4);
        }
        if (how != 2) {
            setStartAfter(startAncestor);
            collapse(true);
        }
        return frag;
    }

    private Node traverseRightBoundary(Node root, int how) throws DOMException {
        Node next = getSelectedNode(this.fEndContainer, this.fEndOffset - 1);
        boolean isFullySelected = next != this.fEndContainer;
        if (next == root) {
            return traverseNode(next, isFullySelected, false, how);
        }
        Node parent = next.getParentNode();
        Node nodeTraverseNode = traverseNode(parent, false, false, how);
        while (true) {
            Node clonedParent = nodeTraverseNode;
            if (parent != null) {
                while (next != null) {
                    Node prevSibling = next.getPreviousSibling();
                    Node clonedChild = traverseNode(next, isFullySelected, false, how);
                    if (how != 3) {
                        clonedParent.insertBefore(clonedChild, clonedParent.getFirstChild());
                    }
                    isFullySelected = true;
                    next = prevSibling;
                }
                if (parent == root) {
                    return clonedParent;
                }
                next = parent.getPreviousSibling();
                parent = parent.getParentNode();
                Node clonedGrandParent = traverseNode(parent, false, false, how);
                if (how != 3) {
                    clonedGrandParent.appendChild(clonedParent);
                }
                nodeTraverseNode = clonedGrandParent;
            } else {
                return null;
            }
        }
    }

    private Node traverseLeftBoundary(Node root, int how) throws DOMException {
        Node next = getSelectedNode(getStartContainer(), getStartOffset());
        boolean isFullySelected = next != getStartContainer();
        if (next == root) {
            return traverseNode(next, isFullySelected, true, how);
        }
        Node parent = next.getParentNode();
        Node nodeTraverseNode = traverseNode(parent, false, true, how);
        while (true) {
            Node clonedParent = nodeTraverseNode;
            if (parent != null) {
                while (next != null) {
                    Node nextSibling = next.getNextSibling();
                    Node clonedChild = traverseNode(next, isFullySelected, true, how);
                    if (how != 3) {
                        clonedParent.appendChild(clonedChild);
                    }
                    isFullySelected = true;
                    next = nextSibling;
                }
                if (parent == root) {
                    return clonedParent;
                }
                next = parent.getNextSibling();
                parent = parent.getParentNode();
                Node clonedGrandParent = traverseNode(parent, false, true, how);
                if (how != 3) {
                    clonedGrandParent.appendChild(clonedParent);
                }
                nodeTraverseNode = clonedGrandParent;
            } else {
                return null;
            }
        }
    }

    private Node traverseNode(Node n2, boolean isFullySelected, boolean isLeft, int how) {
        if (isFullySelected) {
            return traverseFullySelected(n2, how);
        }
        if (n2.getNodeType() == 3) {
            return traverseTextNode(n2, isLeft, how);
        }
        return traversePartiallySelected(n2, how);
    }

    private Node traverseFullySelected(Node n2, int how) throws DOMException {
        switch (how) {
            case 1:
                if (n2.getNodeType() == 10) {
                    throw new DOMException((short) 3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
                }
                return n2;
            case 2:
                return n2.cloneNode(true);
            case 3:
                n2.getParentNode().removeChild(n2);
                return null;
            default:
                return null;
        }
    }

    private Node traversePartiallySelected(Node n2, int how) {
        switch (how) {
            case 1:
            case 2:
                return n2.cloneNode(false);
            case 3:
                return null;
            default:
                return null;
        }
    }

    private Node traverseTextNode(Node n2, boolean isLeft, int how) throws DOMException {
        String newNodeValue;
        String oldNodeValue;
        String txtValue = n2.getNodeValue();
        if (isLeft) {
            int offset = getStartOffset();
            newNodeValue = txtValue.substring(offset);
            oldNodeValue = txtValue.substring(0, offset);
        } else {
            int offset2 = getEndOffset();
            newNodeValue = txtValue.substring(0, offset2);
            oldNodeValue = txtValue.substring(offset2);
        }
        if (how != 2) {
            n2.setNodeValue(oldNodeValue);
        }
        if (how == 3) {
            return null;
        }
        Node newNode = n2.cloneNode(false);
        newNode.setNodeValue(newNodeValue);
        return newNode;
    }

    void checkIndex(Node refNode, int offset) throws DOMException {
        if (offset < 0) {
            throw new DOMException((short) 1, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INDEX_SIZE_ERR", null));
        }
        int type = refNode.getNodeType();
        if (type == 3 || type == 4 || type == 8 || type == 7) {
            if (offset > refNode.getNodeValue().length()) {
                throw new DOMException((short) 1, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INDEX_SIZE_ERR", null));
            }
        } else if (offset > refNode.getChildNodes().getLength()) {
            throw new DOMException((short) 1, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INDEX_SIZE_ERR", null));
        }
    }

    private Node getRootContainer(Node node) {
        if (node == null) {
            return null;
        }
        while (node.getParentNode() != null) {
            node = node.getParentNode();
        }
        return node;
    }

    private boolean isLegalContainer(Node node) {
        if (node == null) {
            return false;
        }
        while (node != null) {
            switch (node.getNodeType()) {
                case 6:
                case 10:
                case 12:
                    return false;
                default:
                    node = node.getParentNode();
            }
        }
        return true;
    }

    private boolean hasLegalRootContainer(Node node) {
        if (node == null) {
            return false;
        }
        Node rootContainer = getRootContainer(node);
        switch (rootContainer.getNodeType()) {
            case 2:
            case 9:
            case 11:
                return true;
            default:
                return false;
        }
    }

    private boolean isLegalContainedNode(Node node) {
        if (node == null) {
            return false;
        }
        switch (node.getNodeType()) {
        }
        return false;
    }

    Node nextNode(Node node, boolean visitChildren) {
        Node result;
        if (node == null) {
            return null;
        }
        if (visitChildren && (result = node.getFirstChild()) != null) {
            return result;
        }
        Node result2 = node.getNextSibling();
        if (result2 != null) {
            return result2;
        }
        Node parentNode = node.getParentNode();
        while (true) {
            Node parent = parentNode;
            if (parent != null && parent != this.fDocument) {
                Node result3 = parent.getNextSibling();
                if (result3 != null) {
                    return result3;
                }
                parentNode = parent.getParentNode();
            } else {
                return null;
            }
        }
    }

    boolean isAncestorOf(Node a2, Node b2) {
        Node parentNode = b2;
        while (true) {
            Node node = parentNode;
            if (node != null) {
                if (node == a2) {
                    return true;
                }
                parentNode = node.getParentNode();
            } else {
                return false;
            }
        }
    }

    int indexOf(Node child, Node parent) {
        if (child.getParentNode() != parent) {
            return -1;
        }
        int i2 = 0;
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != child) {
                i2++;
                firstChild = node.getNextSibling();
            } else {
                return i2;
            }
        }
    }

    private Node getSelectedNode(Node container, int offset) {
        Node child;
        if (container.getNodeType() == 3) {
            return container;
        }
        if (offset < 0) {
            return container;
        }
        Node firstChild = container.getFirstChild();
        while (true) {
            child = firstChild;
            if (child == null || offset <= 0) {
                break;
            }
            offset--;
            firstChild = child.getNextSibling();
        }
        if (child != null) {
            return child;
        }
        return container;
    }
}
