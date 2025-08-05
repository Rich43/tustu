package com.sun.org.apache.xerces.internal.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Vector;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/NamedNodeMapImpl.class */
public class NamedNodeMapImpl implements NamedNodeMap, Serializable {
    static final long serialVersionUID = -7039242451046758020L;
    protected short flags;
    protected static final short READONLY = 1;
    protected static final short CHANGED = 2;
    protected static final short HASDEFAULTS = 4;
    protected List nodes;
    protected NodeImpl ownerNode;

    protected NamedNodeMapImpl(NodeImpl ownerNode) {
        this.ownerNode = ownerNode;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public int getLength() {
        if (this.nodes != null) {
            return this.nodes.size();
        }
        return 0;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node item(int index) {
        if (this.nodes == null || index >= this.nodes.size()) {
            return null;
        }
        return (Node) this.nodes.get(index);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItem(String name) {
        int i2 = findNamePoint(name, 0);
        if (i2 < 0) {
            return null;
        }
        return (Node) this.nodes.get(i2);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItemNS(String namespaceURI, String localName) {
        int i2 = findNamePoint(namespaceURI, localName);
        if (i2 < 0) {
            return null;
        }
        return (Node) this.nodes.get(i2);
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItem(Node arg) throws DOMException, MissingResourceException {
        CoreDocumentImpl ownerDocument = this.ownerNode.ownerDocument();
        if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (arg.getOwnerDocument() != ownerDocument) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException((short) 4, msg2);
            }
        }
        int i2 = findNamePoint(arg.getNodeName(), 0);
        NodeImpl previous = null;
        if (i2 >= 0) {
            previous = (NodeImpl) this.nodes.get(i2);
            this.nodes.set(i2, arg);
        } else {
            int i3 = (-1) - i2;
            if (null == this.nodes) {
                this.nodes = new ArrayList(5);
            }
            this.nodes.add(i3, arg);
        }
        return previous;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItemNS(Node arg) throws DOMException, MissingResourceException {
        CoreDocumentImpl ownerDocument = this.ownerNode.ownerDocument();
        if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (arg.getOwnerDocument() != ownerDocument) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException((short) 4, msg2);
            }
        }
        int i2 = findNamePoint(arg.getNamespaceURI(), arg.getLocalName());
        NodeImpl previous = null;
        if (i2 >= 0) {
            previous = (NodeImpl) this.nodes.get(i2);
            this.nodes.set(i2, arg);
        } else {
            int i3 = findNamePoint(arg.getNodeName(), 0);
            if (i3 >= 0) {
                previous = (NodeImpl) this.nodes.get(i3);
                this.nodes.add(i3, arg);
            } else {
                int i4 = (-1) - i3;
                if (null == this.nodes) {
                    this.nodes = new ArrayList(5);
                }
                this.nodes.add(i4, arg);
            }
        }
        return previous;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItem(String name) throws DOMException, MissingResourceException {
        if (isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        int i2 = findNamePoint(name, 0);
        if (i2 < 0) {
            String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException((short) 8, msg2);
        }
        NodeImpl n2 = (NodeImpl) this.nodes.get(i2);
        this.nodes.remove(i2);
        return n2;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItemNS(String namespaceURI, String name) throws DOMException, MissingResourceException {
        if (isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException((short) 7, msg);
        }
        int i2 = findNamePoint(namespaceURI, name);
        if (i2 < 0) {
            String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException((short) 8, msg2);
        }
        NodeImpl n2 = (NodeImpl) this.nodes.get(i2);
        this.nodes.remove(i2);
        return n2;
    }

    public NamedNodeMapImpl cloneMap(NodeImpl ownerNode) {
        NamedNodeMapImpl newmap = new NamedNodeMapImpl(ownerNode);
        newmap.cloneContent(this);
        return newmap;
    }

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
                NodeImpl n2 = (NodeImpl) srcmap.nodes.get(i2);
                NodeImpl clone = (NodeImpl) n2.cloneNode(true);
                clone.isSpecified(n2.isSpecified());
                this.nodes.add(clone);
            }
        }
    }

    void setReadOnly(boolean readOnly, boolean deep) {
        isReadOnly(readOnly);
        if (deep && this.nodes != null) {
            for (int i2 = this.nodes.size() - 1; i2 >= 0; i2--) {
                ((NodeImpl) this.nodes.get(i2)).setReadOnly(readOnly, deep);
            }
        }
    }

    boolean getReadOnly() {
        return isReadOnly();
    }

    protected void setOwnerDocument(CoreDocumentImpl doc) {
        if (this.nodes != null) {
            int size = this.nodes.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((NodeImpl) item(i2)).setOwnerDocument(doc);
            }
        }
    }

    final boolean isReadOnly() {
        return (this.flags & 1) != 0;
    }

    final void isReadOnly(boolean value) {
        this.flags = (short) (value ? this.flags | 1 : this.flags & (-2));
    }

    final boolean changed() {
        return (this.flags & 2) != 0;
    }

    final void changed(boolean value) {
        this.flags = (short) (value ? this.flags | 2 : this.flags & (-3));
    }

    final boolean hasDefaults() {
        return (this.flags & 4) != 0;
    }

    final void hasDefaults(boolean value) {
        this.flags = (short) (value ? this.flags | 4 : this.flags & (-5));
    }

    protected int findNamePoint(String name, int start) {
        int i2 = 0;
        if (this.nodes != null) {
            int first = start;
            int last = this.nodes.size() - 1;
            while (first <= last) {
                i2 = (first + last) / 2;
                int test = name.compareTo(((Node) this.nodes.get(i2)).getNodeName());
                if (test == 0) {
                    return i2;
                }
                if (test < 0) {
                    last = i2 - 1;
                } else {
                    first = i2 + 1;
                }
            }
            if (first > i2) {
                i2 = first;
            }
        }
        return (-1) - i2;
    }

    protected int findNamePoint(String namespaceURI, String name) {
        if (this.nodes == null || name == null) {
            return -1;
        }
        int size = this.nodes.size();
        for (int i2 = 0; i2 < size; i2++) {
            NodeImpl a2 = (NodeImpl) this.nodes.get(i2);
            String aNamespaceURI = a2.getNamespaceURI();
            String aLocalName = a2.getLocalName();
            if (namespaceURI == null) {
                if (aNamespaceURI == null && (name.equals(aLocalName) || (aLocalName == null && name.equals(a2.getNodeName())))) {
                    return i2;
                }
            } else if (namespaceURI.equals(aNamespaceURI) && name.equals(aLocalName)) {
                return i2;
            }
        }
        return -1;
    }

    protected boolean precedes(Node a2, Node b2) {
        if (this.nodes != null) {
            int size = this.nodes.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node n2 = (Node) this.nodes.get(i2);
                if (n2 == a2) {
                    return true;
                }
                if (n2 == b2) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    protected void removeItem(int index) {
        if (this.nodes != null && index < this.nodes.size()) {
            this.nodes.remove(index);
        }
    }

    protected Object getItem(int index) {
        if (this.nodes != null) {
            return this.nodes.get(index);
        }
        return null;
    }

    protected int addItem(Node arg) {
        int i2 = findNamePoint(arg.getNamespaceURI(), arg.getLocalName());
        if (i2 >= 0) {
            this.nodes.set(i2, arg);
        } else {
            i2 = findNamePoint(arg.getNodeName(), 0);
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
        return i2;
    }

    protected ArrayList cloneMap(ArrayList list) {
        if (list == null) {
            list = new ArrayList(5);
        }
        list.clear();
        if (this.nodes != null) {
            int size = this.nodes.size();
            for (int i2 = 0; i2 < size; i2++) {
                list.add(this.nodes.get(i2));
            }
        }
        return list;
    }

    protected int getNamedItemIndex(String namespaceURI, String localName) {
        return findNamePoint(namespaceURI, localName);
    }

    public void removeAll() {
        if (this.nodes != null) {
            this.nodes.clear();
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (this.nodes != null) {
            this.nodes = new ArrayList((Vector) this.nodes);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        List oldNodes = this.nodes;
        if (oldNodes != null) {
            try {
                this.nodes = new Vector(oldNodes);
            } finally {
                this.nodes = oldNodes;
            }
        }
        out.defaultWriteObject();
    }
}
