package com.sun.org.apache.xerces.internal.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/DefaultElement.class */
public class DefaultElement extends NodeImpl implements Element {
    public DefaultElement() {
    }

    public DefaultElement(String prefix, String localpart, String rawname, String uri, short nodeType) {
        super(prefix, localpart, rawname, uri, nodeType);
    }

    @Override // org.w3c.dom.Element
    public String getTagName() {
        return null;
    }

    @Override // org.w3c.dom.Element
    public String getAttribute(String name) {
        return null;
    }

    @Override // org.w3c.dom.Element
    public Attr getAttributeNode(String name) {
        return null;
    }

    @Override // org.w3c.dom.Element
    public NodeList getElementsByTagName(String name) {
        return null;
    }

    @Override // org.w3c.dom.Element
    public String getAttributeNS(String namespaceURI, String localName) {
        return null;
    }

    @Override // org.w3c.dom.Element
    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        return null;
    }

    @Override // org.w3c.dom.Element
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return null;
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return false;
    }

    @Override // org.w3c.dom.Element
    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    @Override // org.w3c.dom.Element
    public void setAttribute(String name, String value) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public void removeAttribute(String name) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public void setIdAttributeNode(Attr at2, boolean makeId) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public void setIdAttribute(String name, boolean makeId) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Element
    public void setIdAttributeNS(String namespaceURI, String localName, boolean makeId) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }
}
