package com.sun.org.apache.xerces.internal.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/AttributeMap.class */
public class AttributeMap extends NamedNodeMapImpl {
    static final long serialVersionUID = 8872606282138665383L;

    protected AttributeMap(ElementImpl ownerNode, NamedNodeMapImpl defaults) {
        super(ownerNode);
        if (defaults != null) {
            cloneContent(defaults);
            if (this.nodes != null) {
                hasDefaults(true);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NamedNodeMapImpl, org.w3c.dom.NamedNodeMap
    public Node setNamedItem(Node arg) throws DOMException {
        boolean errCheck = this.ownerNode.ownerDocument().errorChecking;
        if (errCheck) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (arg.getOwnerDocument() != this.ownerNode.ownerDocument()) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException((short) 4, msg2);
            }
            if (arg.getNodeType() != 2) {
                String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                throw new DOMException((short) 3, msg3);
            }
        }
        AttrImpl argn = (AttrImpl) arg;
        if (argn.isOwned()) {
            if (errCheck && argn.getOwnerElement() != this.ownerNode) {
                String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INUSE_ATTRIBUTE_ERR", null);
                throw new DOMException((short) 10, msg4);
            }
            return arg;
        }
        argn.ownerNode = this.ownerNode;
        argn.isOwned(true);
        int i2 = findNamePoint(argn.getNodeName(), 0);
        AttrImpl previous = null;
        if (i2 >= 0) {
            previous = (AttrImpl) this.nodes.get(i2);
            this.nodes.set(i2, arg);
            previous.ownerNode = this.ownerNode.ownerDocument();
            previous.isOwned(false);
            previous.isSpecified(true);
        } else {
            int i3 = (-1) - i2;
            if (null == this.nodes) {
                this.nodes = new ArrayList(5);
            }
            this.nodes.add(i3, arg);
        }
        this.ownerNode.ownerDocument().setAttrNode(argn, previous);
        if (!argn.isNormalized()) {
            this.ownerNode.isNormalized(false);
        }
        return previous;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NamedNodeMapImpl, org.w3c.dom.NamedNodeMap
    public Node setNamedItemNS(Node arg) throws DOMException {
        boolean errCheck = this.ownerNode.ownerDocument().errorChecking;
        if (errCheck) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (arg.getOwnerDocument() != this.ownerNode.ownerDocument()) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException((short) 4, msg2);
            }
            if (arg.getNodeType() != 2) {
                String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                throw new DOMException((short) 3, msg3);
            }
        }
        AttrImpl argn = (AttrImpl) arg;
        if (argn.isOwned()) {
            if (errCheck && argn.getOwnerElement() != this.ownerNode) {
                String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INUSE_ATTRIBUTE_ERR", null);
                throw new DOMException((short) 10, msg4);
            }
            return arg;
        }
        argn.ownerNode = this.ownerNode;
        argn.isOwned(true);
        int i2 = findNamePoint(argn.getNamespaceURI(), argn.getLocalName());
        AttrImpl previous = null;
        if (i2 >= 0) {
            previous = (AttrImpl) this.nodes.get(i2);
            this.nodes.set(i2, arg);
            previous.ownerNode = this.ownerNode.ownerDocument();
            previous.isOwned(false);
            previous.isSpecified(true);
        } else {
            int i3 = findNamePoint(arg.getNodeName(), 0);
            if (i3 >= 0) {
                previous = (AttrImpl) this.nodes.get(i3);
                this.nodes.add(i3, arg);
            } else {
                int i4 = (-1) - i3;
                if (null == this.nodes) {
                    this.nodes = new ArrayList(5);
                }
                this.nodes.add(i4, arg);
            }
        }
        this.ownerNode.ownerDocument().setAttrNode(argn, previous);
        if (!argn.isNormalized()) {
            this.ownerNode.isNormalized(false);
        }
        return previous;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NamedNodeMapImpl, org.w3c.dom.NamedNodeMap
    public Node removeNamedItem(String name) throws DOMException {
        return internalRemoveNamedItem(name, true);
    }

    Node safeRemoveNamedItem(String name) {
        return internalRemoveNamedItem(name, false);
    }

    protected Node removeItem(Node item, boolean addDefault) throws DOMException, MissingResourceException {
        int index = -1;
        if (this.nodes != null) {
            int size = this.nodes.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    break;
                }
                if (this.nodes.get(i2) != item) {
                    i2++;
                } else {
                    index = i2;
                    break;
                }
            }
        }
        if (index < 0) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException((short) 8, msg);
        }
        return remove((AttrImpl) item, index, addDefault);
    }

    protected final Node internalRemoveNamedItem(String name, boolean raiseEx) throws MissingResourceException {
        if (isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        int i2 = findNamePoint(name, 0);
        if (i2 < 0) {
            if (raiseEx) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException((short) 8, msg2);
            }
            return null;
        }
        return remove((AttrImpl) this.nodes.get(i2), i2, true);
    }

    private final Node remove(AttrImpl attr, int index, boolean addDefault) {
        NamedNodeMapImpl defaults;
        Node d2;
        CoreDocumentImpl ownerDocument = this.ownerNode.ownerDocument();
        String name = attr.getNodeName();
        if (attr.isIdAttribute()) {
            ownerDocument.removeIdentifier(attr.getValue());
        }
        if (hasDefaults() && addDefault && (defaults = ((ElementImpl) this.ownerNode).getDefaultAttributes()) != null && (d2 = defaults.getNamedItem(name)) != null && findNamePoint(name, index + 1) < 0) {
            NodeImpl clone = (NodeImpl) d2.cloneNode(true);
            if (d2.getLocalName() != null) {
                ((AttrNSImpl) clone).namespaceURI = attr.getNamespaceURI();
            }
            clone.ownerNode = this.ownerNode;
            clone.isOwned(true);
            clone.isSpecified(false);
            this.nodes.set(index, clone);
            if (attr.isIdAttribute()) {
                ownerDocument.putIdentifier(clone.getNodeValue(), (ElementImpl) this.ownerNode);
            }
        } else {
            this.nodes.remove(index);
        }
        attr.ownerNode = ownerDocument;
        attr.isOwned(false);
        attr.isSpecified(true);
        attr.isIdAttribute(false);
        ownerDocument.removedAttrNode(attr, this.ownerNode, name);
        return attr;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NamedNodeMapImpl, org.w3c.dom.NamedNodeMap
    public Node removeNamedItemNS(String namespaceURI, String name) throws DOMException {
        return internalRemoveNamedItemNS(namespaceURI, name, true);
    }

    Node safeRemoveNamedItemNS(String namespaceURI, String name) {
        return internalRemoveNamedItemNS(namespaceURI, name, false);
    }

    protected final Node internalRemoveNamedItemNS(String namespaceURI, String name, boolean raiseEx) throws MissingResourceException {
        NamedNodeMapImpl defaults;
        Node d2;
        int j2;
        CoreDocumentImpl ownerDocument = this.ownerNode.ownerDocument();
        if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        int i2 = findNamePoint(namespaceURI, name);
        if (i2 < 0) {
            if (raiseEx) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException((short) 8, msg2);
            }
            return null;
        }
        AttrImpl n2 = (AttrImpl) this.nodes.get(i2);
        if (n2.isIdAttribute()) {
            ownerDocument.removeIdentifier(n2.getValue());
        }
        String nodeName = n2.getNodeName();
        if (hasDefaults() && (defaults = ((ElementImpl) this.ownerNode).getDefaultAttributes()) != null && (d2 = defaults.getNamedItem(nodeName)) != null && (j2 = findNamePoint(nodeName, 0)) >= 0 && findNamePoint(nodeName, j2 + 1) < 0) {
            NodeImpl clone = (NodeImpl) d2.cloneNode(true);
            clone.ownerNode = this.ownerNode;
            if (d2.getLocalName() != null) {
                ((AttrNSImpl) clone).namespaceURI = namespaceURI;
            }
            clone.isOwned(true);
            clone.isSpecified(false);
            this.nodes.set(i2, clone);
            if (clone.isIdAttribute()) {
                ownerDocument.putIdentifier(clone.getNodeValue(), (ElementImpl) this.ownerNode);
            }
        } else {
            this.nodes.remove(i2);
        }
        n2.ownerNode = ownerDocument;
        n2.isOwned(false);
        n2.isSpecified(true);
        n2.isIdAttribute(false);
        ownerDocument.removedAttrNode(n2, this.ownerNode, name);
        return n2;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NamedNodeMapImpl
    public NamedNodeMapImpl cloneMap(NodeImpl ownerNode) {
        AttributeMap newmap = new AttributeMap((ElementImpl) ownerNode, null);
        newmap.hasDefaults(hasDefaults());
        newmap.cloneContent(this);
        return newmap;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NamedNodeMapImpl
    protected void cloneContent(NamedNodeMapImpl srcmap) {
        int size;
        List srcnodes = srcmap.nodes;
        if (srcnodes != null && (size = srcnodes.size()) != 0) {
            if (this.nodes == null) {
                this.nodes = new ArrayList(size);
            } else {
                this.nodes.clear();
            }
            for (int i2 = 0; i2 < size; i2++) {
                NodeImpl n2 = (NodeImpl) srcnodes.get(i2);
                NodeImpl clone = (NodeImpl) n2.cloneNode(true);
                clone.isSpecified(n2.isSpecified());
                this.nodes.add(clone);
                clone.ownerNode = this.ownerNode;
                clone.isOwned(true);
            }
        }
    }

    void moveSpecifiedAttributes(AttributeMap srcmap) {
        int nsize = srcmap.nodes != null ? srcmap.nodes.size() : 0;
        for (int i2 = nsize - 1; i2 >= 0; i2--) {
            AttrImpl attr = (AttrImpl) srcmap.nodes.get(i2);
            if (attr.isSpecified()) {
                srcmap.remove(attr, i2, false);
                if (attr.getLocalName() != null) {
                    setNamedItem(attr);
                } else {
                    setNamedItemNS(attr);
                }
            }
        }
    }

    protected void reconcileDefaults(NamedNodeMapImpl defaults) {
        int nsize = this.nodes != null ? this.nodes.size() : 0;
        for (int i2 = nsize - 1; i2 >= 0; i2--) {
            AttrImpl attr = (AttrImpl) this.nodes.get(i2);
            if (!attr.isSpecified()) {
                remove(attr, i2, false);
            }
        }
        if (defaults == null) {
            return;
        }
        if (this.nodes == null || this.nodes.size() == 0) {
            cloneContent(defaults);
            return;
        }
        int dsize = defaults.nodes.size();
        for (int n2 = 0; n2 < dsize; n2++) {
            AttrImpl d2 = (AttrImpl) defaults.nodes.get(n2);
            int i3 = findNamePoint(d2.getNodeName(), 0);
            if (i3 < 0) {
                int i4 = (-1) - i3;
                NodeImpl clone = (NodeImpl) d2.cloneNode(true);
                clone.ownerNode = this.ownerNode;
                clone.isOwned(true);
                clone.isSpecified(false);
                this.nodes.add(i4, clone);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NamedNodeMapImpl
    protected final int addItem(Node arg) {
        AttrImpl argn = (AttrImpl) arg;
        argn.ownerNode = this.ownerNode;
        argn.isOwned(true);
        int i2 = findNamePoint(argn.getNamespaceURI(), argn.getLocalName());
        if (i2 >= 0) {
            this.nodes.set(i2, arg);
        } else {
            i2 = findNamePoint(argn.getNodeName(), 0);
            if (i2 >= 0) {
                this.nodes.add(i2, arg);
            } else {
                i2 = (-1) - i2;
                if (null == this.nodes) {
                    this.nodes = new ArrayList(5);
                }
                this.nodes.add(i2, arg);
            }
        }
        this.ownerNode.ownerDocument().setAttrNode(argn, null);
        return i2;
    }
}
