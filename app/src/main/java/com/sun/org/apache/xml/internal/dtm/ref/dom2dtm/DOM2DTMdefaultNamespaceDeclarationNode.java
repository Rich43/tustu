package com.sun.org.apache.xml.internal.dtm.ref.dom2dtm;

import com.sun.org.apache.xml.internal.dtm.DTMException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/dom2dtm/DOM2DTMdefaultNamespaceDeclarationNode.class */
public class DOM2DTMdefaultNamespaceDeclarationNode implements Attr, TypeInfo {
    final String NOT_SUPPORTED_ERR = "Unsupported operation on pseudonode";
    Element pseudoparent;
    String prefix;
    String uri;
    String nodename;
    int handle;

    DOM2DTMdefaultNamespaceDeclarationNode(Element pseudoparent, String prefix, String uri, int handle) {
        this.pseudoparent = pseudoparent;
        this.prefix = prefix;
        this.uri = uri;
        this.handle = handle;
        this.nodename = "xmlns:" + prefix;
    }

    @Override // org.w3c.dom.Node
    public String getNodeName() {
        return this.nodename;
    }

    @Override // org.w3c.dom.Attr
    public String getName() {
        return this.nodename;
    }

    @Override // org.w3c.dom.Node
    public String getNamespaceURI() {
        return "http://www.w3.org/2000/xmlns/";
    }

    @Override // org.w3c.dom.Node
    public String getPrefix() {
        return this.prefix;
    }

    @Override // org.w3c.dom.Node
    public String getLocalName() {
        return this.prefix;
    }

    @Override // org.w3c.dom.Node
    public String getNodeValue() {
        return this.uri;
    }

    @Override // org.w3c.dom.Attr
    public String getValue() {
        return this.uri;
    }

    @Override // org.w3c.dom.Attr
    public Element getOwnerElement() {
        return this.pseudoparent;
    }

    @Override // org.w3c.dom.Node
    public boolean isSupported(String feature, String version) {
        return false;
    }

    @Override // org.w3c.dom.Node
    public boolean hasChildNodes() {
        return false;
    }

    @Override // org.w3c.dom.Node
    public boolean hasAttributes() {
        return false;
    }

    @Override // org.w3c.dom.Node
    public Node getParentNode() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getFirstChild() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getLastChild() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getPreviousSibling() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getNextSibling() {
        return null;
    }

    @Override // org.w3c.dom.Attr
    public boolean getSpecified() {
        return false;
    }

    @Override // org.w3c.dom.Node
    public void normalize() {
    }

    @Override // org.w3c.dom.Node
    public NodeList getChildNodes() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        return null;
    }

    @Override // org.w3c.dom.Node
    public short getNodeType() {
        return (short) 2;
    }

    @Override // org.w3c.dom.Node
    public void setNodeValue(String value) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    @Override // org.w3c.dom.Attr
    public void setValue(String value) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    @Override // org.w3c.dom.Node
    public void setPrefix(String value) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    @Override // org.w3c.dom.Node
    public Node insertBefore(Node a2, Node b2) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    @Override // org.w3c.dom.Node
    public Node replaceChild(Node a2, Node b2) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    @Override // org.w3c.dom.Node
    public Node appendChild(Node a2) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    @Override // org.w3c.dom.Node
    public Node removeChild(Node a2) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    @Override // org.w3c.dom.Node
    public Document getOwnerDocument() {
        return this.pseudoparent.getOwnerDocument();
    }

    @Override // org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        throw new DTMException("Unsupported operation on pseudonode");
    }

    public int getHandleOfNode() {
        return this.handle;
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
    public boolean isDerivedFrom(String ns, String localName, int derivationMethod) {
        return false;
    }

    @Override // org.w3c.dom.Attr
    public TypeInfo getSchemaTypeInfo() {
        return this;
    }

    @Override // org.w3c.dom.Attr
    public boolean isId() {
        return false;
    }

    @Override // org.w3c.dom.Node
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return getOwnerDocument().setUserData(key, data, handler);
    }

    @Override // org.w3c.dom.Node
    public Object getUserData(String key) {
        return getOwnerDocument().getUserData(key);
    }

    @Override // org.w3c.dom.Node
    public Object getFeature(String feature, String version) {
        if (isSupported(feature, version)) {
            return this;
        }
        return null;
    }

    @Override // org.w3c.dom.Node
    public boolean isEqualNode(Node arg) {
        if (arg == this) {
            return true;
        }
        if (arg.getNodeType() != getNodeType()) {
            return false;
        }
        if (getNodeName() == null) {
            if (arg.getNodeName() != null) {
                return false;
            }
        } else if (!getNodeName().equals(arg.getNodeName())) {
            return false;
        }
        if (getLocalName() == null) {
            if (arg.getLocalName() != null) {
                return false;
            }
        } else if (!getLocalName().equals(arg.getLocalName())) {
            return false;
        }
        if (getNamespaceURI() == null) {
            if (arg.getNamespaceURI() != null) {
                return false;
            }
        } else if (!getNamespaceURI().equals(arg.getNamespaceURI())) {
            return false;
        }
        if (getPrefix() == null) {
            if (arg.getPrefix() != null) {
                return false;
            }
        } else if (!getPrefix().equals(arg.getPrefix())) {
            return false;
        }
        if (getNodeValue() == null) {
            if (arg.getNodeValue() != null) {
                return false;
            }
            return true;
        }
        if (!getNodeValue().equals(arg.getNodeValue())) {
            return false;
        }
        return true;
    }

    @Override // org.w3c.dom.Node
    public String lookupNamespaceURI(String specifiedPrefix) throws DOMException {
        String namespace;
        short type = getNodeType();
        switch (type) {
            case 1:
                namespace = getNamespaceURI();
                String prefix = getPrefix();
                if (namespace != null) {
                    if (specifiedPrefix != null || prefix != specifiedPrefix) {
                        if (prefix != null && prefix.equals(specifiedPrefix)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (hasAttributes()) {
                    NamedNodeMap map = getAttributes();
                    int length = map.getLength();
                    for (int i2 = 0; i2 < length; i2++) {
                        Node attr = map.item(i2);
                        String attrPrefix = attr.getPrefix();
                        String value = attr.getNodeValue();
                        String namespace2 = attr.getNamespaceURI();
                        if (namespace2 != null && namespace2.equals("http://www.w3.org/2000/xmlns/")) {
                            if (specifiedPrefix == null && attr.getNodeName().equals("xmlns")) {
                                break;
                            } else if (attrPrefix != null && attrPrefix.equals("xmlns") && attr.getLocalName().equals(specifiedPrefix)) {
                                break;
                            }
                        }
                    }
                    break;
                }
                break;
            case 2:
                if (getOwnerElement().getNodeType() == 1) {
                    break;
                }
                break;
        }
        return namespace;
    }

    @Override // org.w3c.dom.Node
    public boolean isDefaultNamespace(String namespaceURI) {
        return false;
    }

    @Override // org.w3c.dom.Node
    public String lookupPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            return null;
        }
        short type = getNodeType();
        switch (type) {
            case 2:
                if (getOwnerElement().getNodeType() == 1) {
                    return getOwnerElement().lookupPrefix(namespaceURI);
                }
                return null;
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            case 9:
            default:
                return null;
            case 6:
            case 10:
            case 11:
            case 12:
                return null;
        }
    }

    @Override // org.w3c.dom.Node
    public boolean isSameNode(Node other) {
        return this == other;
    }

    @Override // org.w3c.dom.Node
    public void setTextContent(String textContent) throws DOMException {
        setNodeValue(textContent);
    }

    @Override // org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return getNodeValue();
    }

    @Override // org.w3c.dom.Node
    public short compareDocumentPosition(Node other) throws DOMException {
        return (short) 0;
    }

    @Override // org.w3c.dom.Node
    public String getBaseURI() {
        return null;
    }
}
