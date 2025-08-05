package com.sun.org.apache.xpath.internal.domapi;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.xpath.XPathNamespace;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/domapi/XPathNamespaceImpl.class */
class XPathNamespaceImpl implements XPathNamespace {
    private final Node m_attributeNode;
    private String textContent;

    XPathNamespaceImpl(Node node) {
        this.m_attributeNode = node;
    }

    @Override // org.w3c.dom.xpath.XPathNamespace
    public Element getOwnerElement() {
        return ((Attr) this.m_attributeNode).getOwnerElement();
    }

    @Override // org.w3c.dom.Node
    public String getNodeName() {
        return "#namespace";
    }

    @Override // org.w3c.dom.Node
    public String getNodeValue() throws DOMException {
        return this.m_attributeNode.getNodeValue();
    }

    @Override // org.w3c.dom.Node
    public void setNodeValue(String arg0) throws DOMException {
    }

    @Override // org.w3c.dom.Node
    public short getNodeType() {
        return (short) 13;
    }

    @Override // org.w3c.dom.Node
    public Node getParentNode() {
        return this.m_attributeNode.getParentNode();
    }

    @Override // org.w3c.dom.Node
    public NodeList getChildNodes() {
        return this.m_attributeNode.getChildNodes();
    }

    @Override // org.w3c.dom.Node
    public Node getFirstChild() {
        return this.m_attributeNode.getFirstChild();
    }

    @Override // org.w3c.dom.Node
    public Node getLastChild() {
        return this.m_attributeNode.getLastChild();
    }

    @Override // org.w3c.dom.Node
    public Node getPreviousSibling() {
        return this.m_attributeNode.getPreviousSibling();
    }

    @Override // org.w3c.dom.Node
    public Node getNextSibling() {
        return this.m_attributeNode.getNextSibling();
    }

    @Override // org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        return this.m_attributeNode.getAttributes();
    }

    @Override // org.w3c.dom.Node
    public Document getOwnerDocument() {
        return this.m_attributeNode.getOwnerDocument();
    }

    @Override // org.w3c.dom.Node
    public Node insertBefore(Node arg0, Node arg1) throws DOMException {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node replaceChild(Node arg0, Node arg1) throws DOMException {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node removeChild(Node arg0) throws DOMException {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node appendChild(Node arg0) throws DOMException {
        return null;
    }

    @Override // org.w3c.dom.Node
    public boolean hasChildNodes() {
        return false;
    }

    @Override // org.w3c.dom.Node
    public Node cloneNode(boolean arg0) {
        throw new DOMException((short) 9, null);
    }

    @Override // org.w3c.dom.Node
    public void normalize() {
        this.m_attributeNode.normalize();
    }

    @Override // org.w3c.dom.Node
    public boolean isSupported(String arg0, String arg1) {
        return this.m_attributeNode.isSupported(arg0, arg1);
    }

    @Override // org.w3c.dom.Node
    public String getNamespaceURI() {
        return this.m_attributeNode.getNodeValue();
    }

    @Override // org.w3c.dom.Node
    public String getPrefix() {
        return this.m_attributeNode.getPrefix();
    }

    @Override // org.w3c.dom.Node
    public void setPrefix(String arg0) throws DOMException {
    }

    @Override // org.w3c.dom.Node
    public String getLocalName() {
        return this.m_attributeNode.getPrefix();
    }

    @Override // org.w3c.dom.Node
    public boolean hasAttributes() {
        return this.m_attributeNode.hasAttributes();
    }

    @Override // org.w3c.dom.Node
    public String getBaseURI() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public short compareDocumentPosition(Node other) throws DOMException {
        return (short) 0;
    }

    @Override // org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return this.textContent;
    }

    @Override // org.w3c.dom.Node
    public void setTextContent(String textContent) throws DOMException {
        this.textContent = textContent;
    }

    @Override // org.w3c.dom.Node
    public boolean isSameNode(Node other) {
        return false;
    }

    @Override // org.w3c.dom.Node
    public String lookupPrefix(String namespaceURI) {
        return "";
    }

    @Override // org.w3c.dom.Node
    public boolean isDefaultNamespace(String namespaceURI) {
        return false;
    }

    @Override // org.w3c.dom.Node
    public String lookupNamespaceURI(String prefix) {
        return null;
    }

    @Override // org.w3c.dom.Node
    public boolean isEqualNode(Node arg) {
        return false;
    }

    @Override // org.w3c.dom.Node
    public Object getFeature(String feature, String version) {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Object getUserData(String key) {
        return null;
    }
}
