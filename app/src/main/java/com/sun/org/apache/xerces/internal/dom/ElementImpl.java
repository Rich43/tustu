package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.util.URI;
import java.util.MissingResourceException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/ElementImpl.class */
public class ElementImpl extends ParentNode implements Element, TypeInfo {
    static final long serialVersionUID = 3717253516652722278L;
    protected String name;
    protected AttributeMap attributes;

    public ElementImpl(CoreDocumentImpl ownerDoc, String name) {
        super(ownerDoc);
        this.name = name;
        needsSyncData(true);
    }

    protected ElementImpl() {
    }

    void rename(String name) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.name = name;
        reconcileDefaultAttributes();
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 1;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            this.attributes = new AttributeMap(this, null);
        }
        return this.attributes;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        ElementImpl newnode = (ElementImpl) super.cloneNode(deep);
        if (this.attributes != null) {
            newnode.attributes = (AttributeMap) this.attributes.cloneMap(newnode);
        }
        return newnode;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getBaseURI() {
        Attr attrNode;
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes != null && (attrNode = (Attr) this.attributes.getNamedItem("xml:base")) != null) {
            String uri = attrNode.getNodeValue();
            if (uri.length() != 0) {
                try {
                    uri = new URI(uri).toString();
                    return uri;
                } catch (URI.MalformedURIException e2) {
                    String parentBaseURI = this.ownerNode != null ? this.ownerNode.getBaseURI() : null;
                    if (parentBaseURI != null) {
                        try {
                            return new URI(new URI(parentBaseURI), uri).toString();
                        } catch (URI.MalformedURIException e3) {
                            return null;
                        }
                    }
                    return null;
                }
            }
        }
        String baseURI = this.ownerNode != null ? this.ownerNode.getBaseURI() : null;
        if (baseURI != null) {
            try {
                return new URI(baseURI).toString();
            } catch (URI.MalformedURIException e4) {
                return null;
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl
    void setOwnerDocument(CoreDocumentImpl doc) {
        super.setOwnerDocument(doc);
        if (this.attributes != null) {
            this.attributes.setOwnerDocument(doc);
        }
    }

    @Override // org.w3c.dom.Element
    public String getAttribute(String name) {
        Attr attr;
        if (needsSyncData()) {
            synchronizeData();
        }
        return (this.attributes == null || (attr = (Attr) this.attributes.getNamedItem(name)) == null) ? "" : attr.getValue();
    }

    @Override // org.w3c.dom.Element
    public Attr getAttributeNode(String name) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            return null;
        }
        return (Attr) this.attributes.getNamedItem(name);
    }

    @Override // org.w3c.dom.Element
    public NodeList getElementsByTagName(String tagname) {
        return new DeepNodeListImpl(this, tagname);
    }

    @Override // org.w3c.dom.Element
    public String getTagName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
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
            if (childNode2 == 0) {
                break;
            }
            ChildNode next = childNode2.nextSibling;
            if (childNode2.getNodeType() == 3) {
                if (next != null && next.getNodeType() == 3) {
                    ((Text) childNode2).appendData(next.getNodeValue());
                    removeChild(next);
                    next = childNode2;
                } else if (childNode2.getNodeValue() == null || childNode2.getNodeValue().length() == 0) {
                    removeChild(childNode2);
                }
            } else if (childNode2.getNodeType() == 1) {
                childNode2.normalize();
            }
            childNode = next;
        }
        if (this.attributes != null) {
            for (int i2 = 0; i2 < this.attributes.getLength(); i2++) {
                Node attr = this.attributes.item(i2);
                attr.normalize();
            }
        }
        isNormalized(true);
    }

    @Override // org.w3c.dom.Element
    public void removeAttribute(String name) throws MissingResourceException {
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            return;
        }
        this.attributes.safeRemoveNamedItem(name);
    }

    @Override // org.w3c.dom.Element
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException, MissingResourceException {
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException((short) 8, msg2);
        }
        return (Attr) this.attributes.removeItem(oldAttr, true);
    }

    @Override // org.w3c.dom.Element
    public void setAttribute(String name, String value) throws DOMException, MissingResourceException {
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        Attr newAttr = getAttributeNode(name);
        if (newAttr == null) {
            Attr newAttr2 = getOwnerDocument().createAttribute(name);
            if (this.attributes == null) {
                this.attributes = new AttributeMap(this, null);
            }
            newAttr2.setNodeValue(value);
            this.attributes.setNamedItem(newAttr2);
            return;
        }
        newAttr.setNodeValue(value);
    }

    @Override // org.w3c.dom.Element
    public Attr setAttributeNode(Attr newAttr) throws DOMException, MissingResourceException {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (newAttr.getOwnerDocument() != this.ownerDocument) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException((short) 4, msg2);
            }
        }
        if (this.attributes == null) {
            this.attributes = new AttributeMap(this, null);
        }
        return (Attr) this.attributes.setNamedItem(newAttr);
    }

    @Override // org.w3c.dom.Element
    public String getAttributeNS(String namespaceURI, String localName) {
        Attr attr;
        if (needsSyncData()) {
            synchronizeData();
        }
        return (this.attributes == null || (attr = (Attr) this.attributes.getNamedItemNS(namespaceURI, localName)) == null) ? "" : attr.getValue();
    }

    @Override // org.w3c.dom.Element
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException, MissingResourceException {
        String prefix;
        String localName;
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        int index = qualifiedName.indexOf(58);
        if (index < 0) {
            prefix = null;
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, index);
            localName = qualifiedName.substring(index + 1);
        }
        Attr newAttr = getAttributeNodeNS(namespaceURI, localName);
        if (newAttr == null) {
            Attr newAttr2 = getOwnerDocument().createAttributeNS(namespaceURI, qualifiedName);
            if (this.attributes == null) {
                this.attributes = new AttributeMap(this, null);
            }
            newAttr2.setNodeValue(value);
            this.attributes.setNamedItemNS(newAttr2);
            return;
        }
        if (newAttr instanceof AttrNSImpl) {
            String origNodeName = ((AttrNSImpl) newAttr).name;
            String newName = prefix != null ? prefix + CallSiteDescriptor.TOKEN_DELIMITER + localName : localName;
            ((AttrNSImpl) newAttr).name = newName;
            if (!newName.equals(origNodeName)) {
                newAttr = (Attr) this.attributes.removeItem(newAttr, false);
                this.attributes.addItem(newAttr);
            }
        } else {
            newAttr = new AttrNSImpl((CoreDocumentImpl) getOwnerDocument(), namespaceURI, qualifiedName, localName);
            this.attributes.setNamedItemNS(newAttr);
        }
        newAttr.setNodeValue(value);
    }

    @Override // org.w3c.dom.Element
    public void removeAttributeNS(String namespaceURI, String localName) throws MissingResourceException {
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            return;
        }
        this.attributes.safeRemoveNamedItemNS(namespaceURI, localName);
    }

    @Override // org.w3c.dom.Element
    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            return null;
        }
        return (Attr) this.attributes.getNamedItemNS(namespaceURI, localName);
    }

    @Override // org.w3c.dom.Element
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException, MissingResourceException {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (newAttr.getOwnerDocument() != this.ownerDocument) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException((short) 4, msg2);
            }
        }
        if (this.attributes == null) {
            this.attributes = new AttributeMap(this, null);
        }
        return (Attr) this.attributes.setNamedItemNS(newAttr);
    }

    protected int setXercesAttributeNode(Attr attr) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            this.attributes = new AttributeMap(this, null);
        }
        return this.attributes.addItem(attr);
    }

    protected int getXercesAttribute(String namespaceURI, String localName) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            return -1;
        }
        return this.attributes.getNamedItemIndex(namespaceURI, localName);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public boolean hasAttributes() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return (this.attributes == null || this.attributes.getLength() == 0) ? false : true;
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttribute(String name) {
        return getAttributeNode(name) != null;
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return getAttributeNodeNS(namespaceURI, localName) != null;
    }

    @Override // org.w3c.dom.Element
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return new DeepNodeListImpl(this, namespaceURI, localName);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public boolean isEqualNode(Node arg) throws DOMException {
        boolean hasAttrs;
        if (!super.isEqualNode(arg) || (hasAttrs = hasAttributes()) != ((Element) arg).hasAttributes()) {
            return false;
        }
        if (hasAttrs) {
            NamedNodeMap map1 = getAttributes();
            NamedNodeMap map2 = ((Element) arg).getAttributes();
            int len = map1.getLength();
            if (len != map2.getLength()) {
                return false;
            }
            for (int i2 = 0; i2 < len; i2++) {
                Node n1 = map1.item(i2);
                if (n1.getLocalName() == null) {
                    Node n2 = map2.getNamedItem(n1.getNodeName());
                    if (n2 == null || !((NodeImpl) n1).isEqualNode(n2)) {
                        return false;
                    }
                } else {
                    Node n22 = map2.getNamedItemNS(n1.getNamespaceURI(), n1.getLocalName());
                    if (n22 == null || !((NodeImpl) n1).isEqualNode(n22)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return true;
    }

    @Override // org.w3c.dom.Element
    public void setIdAttributeNode(Attr at2, boolean makeId) throws MissingResourceException {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (at2.getOwnerElement() != this) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException((short) 8, msg2);
            }
        }
        ((AttrImpl) at2).isIdAttribute(makeId);
        if (!makeId) {
            this.ownerDocument.removeIdentifier(at2.getValue());
        } else {
            this.ownerDocument.putIdentifier(at2.getValue(), this);
        }
    }

    @Override // org.w3c.dom.Element
    public void setIdAttribute(String name, boolean makeId) throws MissingResourceException {
        if (needsSyncData()) {
            synchronizeData();
        }
        Attr at2 = getAttributeNode(name);
        if (at2 == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException((short) 8, msg);
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg2);
            }
            if (at2.getOwnerElement() != this) {
                String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException((short) 8, msg3);
            }
        }
        ((AttrImpl) at2).isIdAttribute(makeId);
        if (!makeId) {
            this.ownerDocument.removeIdentifier(at2.getValue());
        } else {
            this.ownerDocument.putIdentifier(at2.getValue(), this);
        }
    }

    @Override // org.w3c.dom.Element
    public void setIdAttributeNS(String namespaceURI, String localName, boolean makeId) throws MissingResourceException {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (namespaceURI != null) {
            namespaceURI = namespaceURI.length() == 0 ? null : namespaceURI;
        }
        Attr at2 = getAttributeNodeNS(namespaceURI, localName);
        if (at2 == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException((short) 8, msg);
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg2);
            }
            if (at2.getOwnerElement() != this) {
                String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException((short) 8, msg3);
            }
        }
        ((AttrImpl) at2).isIdAttribute(makeId);
        if (!makeId) {
            this.ownerDocument.removeIdentifier(at2.getValue());
        } else {
            this.ownerDocument.putIdentifier(at2.getValue(), this);
        }
    }

    @Override // org.w3c.dom.TypeInfo
    public String getTypeName() {
        return null;
    }

    @Override // org.w3c.dom.TypeInfo
    public String getTypeNamespace() {
        return null;
    }

    @Override // org.w3c.dom.TypeInfo
    public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod) {
        return false;
    }

    @Override // org.w3c.dom.Element
    public TypeInfo getSchemaTypeInfo() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl
    public void setReadOnly(boolean readOnly, boolean deep) {
        super.setReadOnly(readOnly, deep);
        if (this.attributes != null) {
            this.attributes.setReadOnly(readOnly, true);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl
    protected void synchronizeData() {
        needsSyncData(false);
        boolean orig = this.ownerDocument.getMutationEvents();
        this.ownerDocument.setMutationEvents(false);
        setupDefaultAttributes();
        this.ownerDocument.setMutationEvents(orig);
    }

    void moveSpecifiedAttributes(ElementImpl el) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (el.hasAttributes()) {
            if (this.attributes == null) {
                this.attributes = new AttributeMap(this, null);
            }
            this.attributes.moveSpecifiedAttributes(el.attributes);
        }
    }

    protected void setupDefaultAttributes() {
        NamedNodeMapImpl defaults = getDefaultAttributes();
        if (defaults != null) {
            this.attributes = new AttributeMap(this, defaults);
        }
    }

    protected void reconcileDefaultAttributes() {
        if (this.attributes != null) {
            NamedNodeMapImpl defaults = getDefaultAttributes();
            this.attributes.reconcileDefaults(defaults);
        }
    }

    protected NamedNodeMapImpl getDefaultAttributes() {
        ElementDefinitionImpl eldef;
        DocumentTypeImpl doctype = (DocumentTypeImpl) this.ownerDocument.getDoctype();
        if (doctype == null || (eldef = (ElementDefinitionImpl) doctype.getElements().getNamedItem(getNodeName())) == null) {
            return null;
        }
        return (NamedNodeMapImpl) eldef.getAttributes();
    }
}
