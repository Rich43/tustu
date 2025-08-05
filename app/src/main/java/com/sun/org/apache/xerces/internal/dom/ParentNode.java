package com.sun.org.apache.xerces.internal.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/ParentNode.class */
public abstract class ParentNode extends ChildNode {
    static final long serialVersionUID = 2815829867152120872L;
    protected CoreDocumentImpl ownerDocument;
    protected ChildNode firstChild;
    protected transient NodeListCache fNodeListCache;

    protected ParentNode(CoreDocumentImpl ownerDocument) {
        super(ownerDocument);
        this.firstChild = null;
        this.fNodeListCache = null;
        this.ownerDocument = ownerDocument;
    }

    public ParentNode() {
        this.firstChild = null;
        this.fNodeListCache = null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        ParentNode newnode = (ParentNode) super.cloneNode(deep);
        newnode.ownerDocument = this.ownerDocument;
        newnode.firstChild = null;
        newnode.fNodeListCache = null;
        if (deep) {
            ChildNode childNode = this.firstChild;
            while (true) {
                ChildNode child = childNode;
                if (child == null) {
                    break;
                }
                newnode.appendChild(child.cloneNode(true));
                childNode = child.nextSibling;
            }
        }
        return newnode;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Document getOwnerDocument() {
        return this.ownerDocument;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    CoreDocumentImpl ownerDocument() {
        return this.ownerDocument;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    void setOwnerDocument(CoreDocumentImpl doc) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        ChildNode childNode = this.firstChild;
        while (true) {
            ChildNode child = childNode;
            if (child != null) {
                child.setOwnerDocument(doc);
                childNode = child.nextSibling;
            } else {
                super.setOwnerDocument(doc);
                this.ownerDocument = doc;
                return;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public boolean hasChildNodes() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.firstChild != null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public NodeList getChildNodes() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node getFirstChild() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.firstChild;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node getLastChild() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return lastChild();
    }

    final ChildNode lastChild() {
        if (this.firstChild != null) {
            return this.firstChild.previousSibling;
        }
        return null;
    }

    final void lastChild(ChildNode node) {
        if (this.firstChild != null) {
            this.firstChild.previousSibling = node;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return internalInsertBefore(newChild, refChild, false);
    }

    Node internalInsertBefore(Node newChild, Node refChild, boolean replace) throws DOMException {
        boolean errorChecking = this.ownerDocument.errorChecking;
        if (newChild.getNodeType() == 11) {
            if (errorChecking) {
                Node firstChild = newChild.getFirstChild();
                while (true) {
                    Node kid = firstChild;
                    if (kid == null) {
                        break;
                    }
                    if (this.ownerDocument.isKidOK(this, kid)) {
                        firstChild = kid.getNextSibling();
                    } else {
                        throw new DOMException((short) 3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
                    }
                }
            }
            while (newChild.hasChildNodes()) {
                insertBefore(newChild.getFirstChild(), refChild);
            }
            return newChild;
        }
        if (newChild == refChild) {
            Node refChild2 = refChild.getNextSibling();
            removeChild(newChild);
            insertBefore(newChild, refChild2);
            return newChild;
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (errorChecking) {
            if (isReadOnly()) {
                throw new DOMException((short) 7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
            }
            if (newChild.getOwnerDocument() != this.ownerDocument && newChild != this.ownerDocument) {
                throw new DOMException((short) 4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
            if (!this.ownerDocument.isKidOK(this, newChild)) {
                throw new DOMException((short) 3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
            }
            if (refChild != null && refChild.getParentNode() != this) {
                throw new DOMException((short) 8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
            }
            if (this.ownerDocument.ancestorChecking) {
                boolean treeSafe = true;
                NodeImpl nodeImplParentNode = this;
                while (true) {
                    NodeImpl a2 = nodeImplParentNode;
                    if (!treeSafe || a2 == null) {
                        break;
                    }
                    treeSafe = newChild != a2;
                    nodeImplParentNode = a2.parentNode();
                }
                if (!treeSafe) {
                    throw new DOMException((short) 3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
                }
            }
        }
        this.ownerDocument.insertingNode(this, replace);
        ChildNode newInternal = (ChildNode) newChild;
        Node oldparent = newInternal.parentNode();
        if (oldparent != null) {
            oldparent.removeChild(newInternal);
        }
        ChildNode refInternal = (ChildNode) refChild;
        newInternal.ownerNode = this;
        newInternal.isOwned(true);
        if (this.firstChild == null) {
            this.firstChild = newInternal;
            newInternal.isFirstChild(true);
            newInternal.previousSibling = newInternal;
        } else if (refInternal == null) {
            ChildNode lastChild = this.firstChild.previousSibling;
            lastChild.nextSibling = newInternal;
            newInternal.previousSibling = lastChild;
            this.firstChild.previousSibling = newInternal;
        } else if (refChild == this.firstChild) {
            this.firstChild.isFirstChild(false);
            newInternal.nextSibling = this.firstChild;
            newInternal.previousSibling = this.firstChild.previousSibling;
            this.firstChild.previousSibling = newInternal;
            this.firstChild = newInternal;
            newInternal.isFirstChild(true);
        } else {
            ChildNode prev = refInternal.previousSibling;
            newInternal.nextSibling = refInternal;
            prev.nextSibling = newInternal;
            refInternal.previousSibling = newInternal;
            newInternal.previousSibling = prev;
        }
        changed();
        if (this.fNodeListCache != null) {
            if (this.fNodeListCache.fLength != -1) {
                this.fNodeListCache.fLength++;
            }
            if (this.fNodeListCache.fChildIndex != -1) {
                if (this.fNodeListCache.fChild == refInternal) {
                    this.fNodeListCache.fChild = newInternal;
                } else {
                    this.fNodeListCache.fChildIndex = -1;
                }
            }
        }
        this.ownerDocument.insertedNode(this, newInternal, replace);
        checkNormalizationAfterInsert(newInternal);
        return newChild;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node removeChild(Node oldChild) throws DOMException {
        return internalRemoveChild(oldChild, false);
    }

    Node internalRemoveChild(Node oldChild, boolean replace) throws DOMException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                throw new DOMException((short) 7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
            }
            if (oldChild != null && oldChild.getParentNode() != this) {
                throw new DOMException((short) 8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
            }
        }
        ChildNode oldInternal = (ChildNode) oldChild;
        ownerDocument.removingNode(this, oldInternal, replace);
        if (this.fNodeListCache != null) {
            if (this.fNodeListCache.fLength != -1) {
                this.fNodeListCache.fLength--;
            }
            if (this.fNodeListCache.fChildIndex != -1) {
                if (this.fNodeListCache.fChild == oldInternal) {
                    this.fNodeListCache.fChildIndex--;
                    this.fNodeListCache.fChild = oldInternal.previousSibling();
                } else {
                    this.fNodeListCache.fChildIndex = -1;
                }
            }
        }
        if (oldInternal == this.firstChild) {
            oldInternal.isFirstChild(false);
            this.firstChild = oldInternal.nextSibling;
            if (this.firstChild != null) {
                this.firstChild.isFirstChild(true);
                this.firstChild.previousSibling = oldInternal.previousSibling;
            }
        } else {
            ChildNode prev = oldInternal.previousSibling;
            ChildNode next = oldInternal.nextSibling;
            prev.nextSibling = next;
            if (next == null) {
                this.firstChild.previousSibling = prev;
            } else {
                next.previousSibling = prev;
            }
        }
        ChildNode oldPreviousSibling = oldInternal.previousSibling();
        oldInternal.ownerNode = ownerDocument;
        oldInternal.isOwned(false);
        oldInternal.nextSibling = null;
        oldInternal.previousSibling = null;
        changed();
        ownerDocument.removedNode(this, replace);
        checkNormalizationAfterRemove(oldPreviousSibling);
        return oldInternal;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        this.ownerDocument.replacingNode(this);
        internalInsertBefore(newChild, oldChild, true);
        if (newChild != oldChild) {
            internalRemoveChild(oldChild, true);
        }
        this.ownerDocument.replacedNode(this);
        return oldChild;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        Node child = getFirstChild();
        if (child != null) {
            Node next = child.getNextSibling();
            if (next == null) {
                return hasTextContent(child) ? ((NodeImpl) child).getTextContent() : "";
            }
            if (this.fBufferStr == null) {
                this.fBufferStr = new StringBuffer();
            } else {
                this.fBufferStr.setLength(0);
            }
            getTextContent(this.fBufferStr);
            return this.fBufferStr.toString();
        }
        return "";
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    void getTextContent(StringBuffer buf) throws DOMException {
        Node firstChild = getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (hasTextContent(child)) {
                    ((NodeImpl) child).getTextContent(buf);
                }
                firstChild = child.getNextSibling();
            } else {
                return;
            }
        }
    }

    final boolean hasTextContent(Node child) {
        return (child.getNodeType() == 8 || child.getNodeType() == 7 || (child.getNodeType() == 3 && ((TextImpl) child).isIgnorableWhitespace())) ? false : true;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void setTextContent(String textContent) throws DOMException {
        while (true) {
            Node child = getFirstChild();
            if (child == null) {
                break;
            } else {
                removeChild(child);
            }
        }
        if (textContent != null && textContent.length() != 0) {
            appendChild(ownerDocument().createTextNode(textContent));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int nodeListGetLength() {
        ChildNode n2;
        int l2;
        if (this.fNodeListCache == null) {
            if (this.firstChild == null) {
                return 0;
            }
            if (this.firstChild == lastChild()) {
                return 1;
            }
            this.fNodeListCache = this.ownerDocument.getNodeListCache(this);
        }
        if (this.fNodeListCache.fLength == -1) {
            if (this.fNodeListCache.fChildIndex != -1 && this.fNodeListCache.fChild != null) {
                l2 = this.fNodeListCache.fChildIndex;
                n2 = this.fNodeListCache.fChild;
            } else {
                n2 = this.firstChild;
                l2 = 0;
            }
            while (n2 != null) {
                l2++;
                n2 = n2.nextSibling;
            }
            this.fNodeListCache.fLength = l2;
        }
        return this.fNodeListCache.fLength;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.NodeList
    public int getLength() {
        return nodeListGetLength();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Node nodeListItem(int index) {
        if (this.fNodeListCache == null) {
            if (this.firstChild == lastChild()) {
                if (index == 0) {
                    return this.firstChild;
                }
                return null;
            }
            this.fNodeListCache = this.ownerDocument.getNodeListCache(this);
        }
        int i2 = this.fNodeListCache.fChildIndex;
        ChildNode n2 = this.fNodeListCache.fChild;
        boolean firstAccess = true;
        if (i2 != -1 && n2 != null) {
            firstAccess = false;
            if (i2 < index) {
                while (i2 < index && n2 != null) {
                    i2++;
                    n2 = n2.nextSibling;
                }
            } else if (i2 > index) {
                while (i2 > index && n2 != null) {
                    i2--;
                    n2 = n2.previousSibling();
                }
            }
        } else {
            if (index < 0) {
                return null;
            }
            n2 = this.firstChild;
            i2 = 0;
            while (i2 < index && n2 != null) {
                n2 = n2.nextSibling;
                i2++;
            }
        }
        if (!firstAccess && (n2 == this.firstChild || n2 == lastChild())) {
            this.fNodeListCache.fChildIndex = -1;
            this.fNodeListCache.fChild = null;
            this.ownerDocument.freeNodeListCache(this.fNodeListCache);
        } else {
            this.fNodeListCache.fChildIndex = i2;
            this.fNodeListCache.fChild = n2;
        }
        return n2;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.NodeList
    public Node item(int index) {
        return nodeListItem(index);
    }

    protected final NodeList getChildNodesUnoptimized() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return new NodeList() { // from class: com.sun.org.apache.xerces.internal.dom.ParentNode.1
            @Override // org.w3c.dom.NodeList
            public int getLength() {
                return ParentNode.this.nodeListGetLength();
            }

            @Override // org.w3c.dom.NodeList
            public Node item(int index) {
                return ParentNode.this.nodeListItem(index);
            }
        };
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void normalize() {
        if (isNormalized()) {
            return;
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        ChildNode childNode = this.firstChild;
        while (true) {
            ChildNode kid = childNode;
            if (kid != null) {
                kid.normalize();
                childNode = kid.nextSibling;
            } else {
                isNormalized(true);
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x003e, code lost:
    
        if (r5 == r6) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0041, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0043, code lost:
    
        return true;
     */
    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isEqualNode(org.w3c.dom.Node r4) {
        /*
            r3 = this;
            r0 = r3
            r1 = r4
            boolean r0 = super.isEqualNode(r1)
            if (r0 != 0) goto La
            r0 = 0
            return r0
        La:
            r0 = r3
            org.w3c.dom.Node r0 = r0.getFirstChild()
            r5 = r0
            r0 = r4
            org.w3c.dom.Node r0 = r0.getFirstChild()
            r6 = r0
        L16:
            r0 = r5
            if (r0 == 0) goto L3c
            r0 = r6
            if (r0 == 0) goto L3c
            r0 = r5
            com.sun.org.apache.xerces.internal.dom.NodeImpl r0 = (com.sun.org.apache.xerces.internal.dom.NodeImpl) r0
            r1 = r6
            boolean r0 = r0.isEqualNode(r1)
            if (r0 != 0) goto L2b
            r0 = 0
            return r0
        L2b:
            r0 = r5
            org.w3c.dom.Node r0 = r0.getNextSibling()
            r5 = r0
            r0 = r6
            org.w3c.dom.Node r0 = r0.getNextSibling()
            r6 = r0
            goto L16
        L3c:
            r0 = r5
            r1 = r6
            if (r0 == r1) goto L43
            r0 = 0
            return r0
        L43:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.dom.ParentNode.isEqualNode(org.w3c.dom.Node):boolean");
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    public void setReadOnly(boolean readOnly, boolean deep) {
        super.setReadOnly(readOnly, deep);
        if (deep) {
            if (needsSyncChildren()) {
                synchronizeChildren();
            }
            ChildNode childNode = this.firstChild;
            while (true) {
                ChildNode mykid = childNode;
                if (mykid != null) {
                    if (mykid.getNodeType() != 5) {
                        mykid.setReadOnly(readOnly, true);
                    }
                    childNode = mykid.nextSibling;
                } else {
                    return;
                }
            }
        }
    }

    protected void synchronizeChildren() {
        needsSyncChildren(false);
    }

    void checkNormalizationAfterInsert(ChildNode insertedChild) {
        if (insertedChild.getNodeType() == 3) {
            ChildNode prev = insertedChild.previousSibling();
            ChildNode next = insertedChild.nextSibling;
            if ((prev != null && prev.getNodeType() == 3) || (next != null && next.getNodeType() == 3)) {
                isNormalized(false);
                return;
            }
            return;
        }
        if (!insertedChild.isNormalized()) {
            isNormalized(false);
        }
    }

    void checkNormalizationAfterRemove(ChildNode previousSibling) {
        ChildNode next;
        if (previousSibling != null && previousSibling.getNodeType() == 3 && (next = previousSibling.nextSibling) != null && next.getNodeType() == 3) {
            isNormalized(false);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        needsSyncChildren(false);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/ParentNode$UserDataRecord.class */
    protected class UserDataRecord implements Serializable {
        private static final long serialVersionUID = 3258126977134310455L;
        Object fData;
        UserDataHandler fHandler;

        UserDataRecord(Object data, UserDataHandler handler) {
            this.fData = data;
            this.fHandler = handler;
        }
    }
}
