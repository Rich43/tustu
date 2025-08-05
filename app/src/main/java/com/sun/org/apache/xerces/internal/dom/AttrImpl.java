package com.sun.org.apache.xerces.internal.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.MissingResourceException;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/AttrImpl.class */
public class AttrImpl extends NodeImpl implements Attr, TypeInfo {
    static final long serialVersionUID = 7277707688218972102L;
    static final String DTD_URI = "http://www.w3.org/TR/REC-xml";
    protected Object value;
    protected String name;
    transient Object type;
    protected TextImpl textNode;

    protected AttrImpl(CoreDocumentImpl ownerDocument, String name) {
        super(ownerDocument);
        this.value = null;
        this.textNode = null;
        this.name = name;
        isSpecified(true);
        hasStringValue(true);
    }

    protected AttrImpl() {
        this.value = null;
        this.textNode = null;
    }

    void rename(String name) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.name = name;
    }

    protected void makeChildNode() {
        if (hasStringValue()) {
            if (this.value != null) {
                TextImpl text = (TextImpl) ownerDocument().createTextNode((String) this.value);
                this.value = text;
                text.isFirstChild(true);
                text.previousSibling = text;
                text.ownerNode = this;
                text.isOwned(true);
            }
            hasStringValue(false);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    void setOwnerDocument(CoreDocumentImpl doc) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        super.setOwnerDocument(doc);
        if (!hasStringValue()) {
            ChildNode childNode = (ChildNode) this.value;
            while (true) {
                ChildNode child = childNode;
                if (child != null) {
                    child.setOwnerDocument(doc);
                    childNode = child.nextSibling;
                } else {
                    return;
                }
            }
        }
    }

    public void setIdAttribute(boolean id) {
        if (needsSyncData()) {
            synchronizeData();
        }
        isIdAttribute(id);
    }

    @Override // org.w3c.dom.Attr
    public boolean isId() {
        return isIdAttribute();
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        AttrImpl clone = (AttrImpl) super.cloneNode(deep);
        if (!clone.hasStringValue()) {
            clone.value = null;
            Node nextSibling = (Node) this.value;
            while (true) {
                Node child = nextSibling;
                if (child == null) {
                    break;
                }
                clone.appendChild(child.cloneNode(true));
                nextSibling = child.getNextSibling();
            }
        }
        clone.isSpecified(true);
        return clone;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 2;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void setNodeValue(String value) throws DOMException {
        setValue(value);
    }

    @Override // org.w3c.dom.TypeInfo
    public String getTypeName() {
        return (String) this.type;
    }

    @Override // org.w3c.dom.TypeInfo
    public String getTypeNamespace() {
        if (this.type != null) {
            return "http://www.w3.org/TR/REC-xml";
        }
        return null;
    }

    @Override // org.w3c.dom.Attr
    public TypeInfo getSchemaTypeInfo() {
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeValue() {
        return getValue();
    }

    @Override // org.w3c.dom.Attr
    public String getName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // org.w3c.dom.Attr
    public void setValue(String newvalue) {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        Element ownerElement = getOwnerElement();
        String oldvalue = "";
        if (needsSyncData()) {
            synchronizeData();
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (this.value != null) {
            if (ownerDocument.getMutationEvents()) {
                if (hasStringValue()) {
                    oldvalue = (String) this.value;
                    if (this.textNode == null) {
                        this.textNode = (TextImpl) ownerDocument.createTextNode((String) this.value);
                    } else {
                        this.textNode.data = (String) this.value;
                    }
                    this.value = this.textNode;
                    this.textNode.isFirstChild(true);
                    this.textNode.previousSibling = this.textNode;
                    this.textNode.ownerNode = this;
                    this.textNode.isOwned(true);
                    hasStringValue(false);
                    internalRemoveChild(this.textNode, true);
                } else {
                    oldvalue = getValue();
                    while (this.value != null) {
                        internalRemoveChild((Node) this.value, true);
                    }
                }
            } else {
                if (hasStringValue()) {
                    oldvalue = (String) this.value;
                } else {
                    oldvalue = getValue();
                    ChildNode firstChild = (ChildNode) this.value;
                    firstChild.previousSibling = null;
                    firstChild.isFirstChild(false);
                    firstChild.ownerNode = ownerDocument;
                }
                this.value = null;
                needsSyncChildren(false);
            }
            if (isIdAttribute() && ownerElement != null) {
                ownerDocument.removeIdentifier(oldvalue);
            }
        }
        isSpecified(true);
        if (ownerDocument.getMutationEvents()) {
            internalInsertBefore(ownerDocument.createTextNode(newvalue), null, true);
            hasStringValue(false);
            ownerDocument.modifiedAttrValue(this, oldvalue);
        } else {
            this.value = newvalue;
            hasStringValue(true);
            changed();
        }
        if (isIdAttribute() && ownerElement != null) {
            ownerDocument.putIdentifier(newvalue, ownerElement);
        }
    }

    @Override // org.w3c.dom.Attr
    public String getValue() {
        String data;
        if (needsSyncData()) {
            synchronizeData();
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (this.value == null) {
            return "";
        }
        if (hasStringValue()) {
            return (String) this.value;
        }
        ChildNode firstChild = (ChildNode) this.value;
        if (firstChild.getNodeType() == 5) {
            data = ((EntityReferenceImpl) firstChild).getEntityRefValue();
        } else {
            data = firstChild.getNodeValue();
        }
        ChildNode node = firstChild.nextSibling;
        if (node == null || data == null) {
            return data == null ? "" : data;
        }
        StringBuffer value = new StringBuffer(data);
        while (node != null) {
            if (node.getNodeType() == 5) {
                String data2 = ((EntityReferenceImpl) node).getEntityRefValue();
                if (data2 == null) {
                    return "";
                }
                value.append(data2);
            } else {
                value.append(node.getNodeValue());
            }
            node = node.nextSibling;
        }
        return value.toString();
    }

    @Override // org.w3c.dom.Attr
    public boolean getSpecified() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return isSpecified();
    }

    public Element getElement() {
        return (Element) (isOwned() ? this.ownerNode : null);
    }

    @Override // org.w3c.dom.Attr
    public Element getOwnerElement() {
        return (Element) (isOwned() ? this.ownerNode : null);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void normalize() throws DOMException {
        if (isNormalized() || hasStringValue()) {
            return;
        }
        ChildNode firstChild = (ChildNode) this.value;
        Node node = firstChild;
        while (true) {
            Node kid = node;
            if (kid != null) {
                Node next = kid.getNextSibling();
                if (kid.getNodeType() == 3) {
                    if (next != null && next.getNodeType() == 3) {
                        ((Text) kid).appendData(next.getNodeValue());
                        removeChild(next);
                        next = kid;
                    } else if (kid.getNodeValue() == null || kid.getNodeValue().length() == 0) {
                        removeChild(kid);
                    }
                }
                node = next;
            } else {
                isNormalized(true);
                return;
            }
        }
    }

    public void setSpecified(boolean arg) {
        if (needsSyncData()) {
            synchronizeData();
        }
        isSpecified(arg);
    }

    public void setType(Object type) {
        this.type = type;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    public String toString() {
        return getName() + "=\"" + getValue() + PdfOps.DOUBLE_QUOTE__TOKEN;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public boolean hasChildNodes() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.value != null;
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
        makeChildNode();
        return (Node) this.value;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node getLastChild() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return lastChild();
    }

    final ChildNode lastChild() {
        makeChildNode();
        if (this.value != null) {
            return ((ChildNode) this.value).previousSibling;
        }
        return null;
    }

    final void lastChild(ChildNode node) {
        if (this.value != null) {
            ((ChildNode) this.value).previousSibling = node;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return internalInsertBefore(newChild, refChild, false);
    }

    Node internalInsertBefore(Node newChild, Node refChild, boolean replace) throws DOMException, MissingResourceException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        boolean errorChecking = ownerDocument.errorChecking;
        if (newChild.getNodeType() == 11) {
            if (errorChecking) {
                Node firstChild = newChild.getFirstChild();
                while (true) {
                    Node kid = firstChild;
                    if (kid == null) {
                        break;
                    }
                    if (ownerDocument.isKidOK(this, kid)) {
                        firstChild = kid.getNextSibling();
                    } else {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                        throw new DOMException((short) 3, msg);
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
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg2);
            }
            if (newChild.getOwnerDocument() != ownerDocument) {
                String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException((short) 4, msg3);
            }
            if (!ownerDocument.isKidOK(this, newChild)) {
                String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                throw new DOMException((short) 3, msg4);
            }
            if (refChild != null && refChild.getParentNode() != this) {
                String msg5 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException((short) 8, msg5);
            }
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
                String msg6 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                throw new DOMException((short) 3, msg6);
            }
        }
        makeChildNode();
        ownerDocument.insertingNode(this, replace);
        ChildNode newInternal = (ChildNode) newChild;
        Node oldparent = newInternal.parentNode();
        if (oldparent != null) {
            oldparent.removeChild(newInternal);
        }
        ChildNode refInternal = (ChildNode) refChild;
        newInternal.ownerNode = this;
        newInternal.isOwned(true);
        ChildNode firstChild2 = (ChildNode) this.value;
        if (firstChild2 == null) {
            this.value = newInternal;
            newInternal.isFirstChild(true);
            newInternal.previousSibling = newInternal;
        } else if (refInternal == null) {
            ChildNode lastChild = firstChild2.previousSibling;
            lastChild.nextSibling = newInternal;
            newInternal.previousSibling = lastChild;
            firstChild2.previousSibling = newInternal;
        } else if (refChild == firstChild2) {
            firstChild2.isFirstChild(false);
            newInternal.nextSibling = firstChild2;
            newInternal.previousSibling = firstChild2.previousSibling;
            firstChild2.previousSibling = newInternal;
            this.value = newInternal;
            newInternal.isFirstChild(true);
        } else {
            ChildNode prev = refInternal.previousSibling;
            newInternal.nextSibling = refInternal;
            prev.nextSibling = newInternal;
            refInternal.previousSibling = newInternal;
            newInternal.previousSibling = prev;
        }
        changed();
        ownerDocument.insertedNode(this, newInternal, replace);
        checkNormalizationAfterInsert(newInternal);
        return newChild;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node removeChild(Node oldChild) throws DOMException {
        if (hasStringValue()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException((short) 8, msg);
        }
        return internalRemoveChild(oldChild, false);
    }

    Node internalRemoveChild(Node oldChild, boolean replace) throws DOMException, MissingResourceException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (oldChild != null && oldChild.getParentNode() != this) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException((short) 8, msg2);
            }
        }
        ChildNode oldInternal = (ChildNode) oldChild;
        ownerDocument.removingNode(this, oldInternal, replace);
        if (oldInternal == this.value) {
            oldInternal.isFirstChild(false);
            this.value = oldInternal.nextSibling;
            ChildNode firstChild = (ChildNode) this.value;
            if (firstChild != null) {
                firstChild.isFirstChild(true);
                firstChild.previousSibling = oldInternal.previousSibling;
            }
        } else {
            ChildNode prev = oldInternal.previousSibling;
            ChildNode next = oldInternal.nextSibling;
            prev.nextSibling = next;
            if (next == null) {
                ((ChildNode) this.value).previousSibling = prev;
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
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException, MissingResourceException {
        makeChildNode();
        CoreDocumentImpl ownerDocument = ownerDocument();
        ownerDocument.replacingNode(this);
        internalInsertBefore(newChild, oldChild, true);
        if (newChild != oldChild) {
            internalRemoveChild(oldChild, true);
        }
        ownerDocument.replacedNode(this);
        return oldChild;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.NodeList
    public int getLength() {
        if (hasStringValue()) {
            return 1;
        }
        int length = 0;
        for (ChildNode node = (ChildNode) this.value; node != null; node = node.nextSibling) {
            length++;
        }
        return length;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.NodeList
    public Node item(int index) {
        if (hasStringValue()) {
            if (index != 0 || this.value == null) {
                return null;
            }
            makeChildNode();
            return (Node) this.value;
        }
        if (index < 0) {
            return null;
        }
        ChildNode node = (ChildNode) this.value;
        for (int i2 = 0; i2 < index && node != null; i2++) {
            node = node.nextSibling;
        }
        return node;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public boolean isEqualNode(Node arg) {
        return super.isEqualNode(arg);
    }

    @Override // org.w3c.dom.TypeInfo
    public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod) {
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    public void setReadOnly(boolean readOnly, boolean deep) {
        super.setReadOnly(readOnly, deep);
        if (deep) {
            if (needsSyncChildren()) {
                synchronizeChildren();
            }
            if (hasStringValue()) {
                return;
            }
            ChildNode childNode = (ChildNode) this.value;
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
}
