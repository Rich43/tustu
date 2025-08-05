package com.sun.org.apache.xerces.internal.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/NamedNodeMapImpl.class */
public class NamedNodeMapImpl implements NamedNodeMap {
    Attr[] attrs;

    public NamedNodeMapImpl(Attr[] attrs) {
        this.attrs = attrs;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItem(String name) {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getName().equals(name)) {
                return this.attrs[i2];
            }
        }
        return null;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node item(int index) {
        if (index < 0 && index > getLength()) {
            return null;
        }
        return this.attrs[index];
    }

    @Override // org.w3c.dom.NamedNodeMap
    public int getLength() {
        return this.attrs.length;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node getNamedItemNS(String namespaceURI, String localName) {
        for (int i2 = 0; i2 < this.attrs.length; i2++) {
            if (this.attrs[i2].getName().equals(localName) && this.attrs[i2].getNamespaceURI().equals(namespaceURI)) {
                return this.attrs[i2];
            }
        }
        return null;
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItemNS(Node arg) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node setNamedItem(Node arg) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItem(String name) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.NamedNodeMap
    public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }
}
