package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xml.internal.res.XMLMessages;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/UnImplNode.class */
public class UnImplNode implements Node, Element, NodeList, Document {
    protected String fDocumentURI;
    protected String actualEncoding;
    private String xmlEncoding;
    private boolean xmlStandalone;
    private String xmlVersion;

    public void error(String msg) {
        System.out.println("DOM ERROR! class: " + getClass().getName());
        throw new RuntimeException(XMLMessages.createXMLMessage(msg, null));
    }

    public void error(String msg, Object[] args) {
        System.out.println("DOM ERROR! class: " + getClass().getName());
        throw new RuntimeException(XMLMessages.createXMLMessage(msg, args));
    }

    @Override // org.w3c.dom.Node
    public Node appendChild(Node newChild) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public boolean hasChildNodes() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    @Override // org.w3c.dom.Node
    public short getNodeType() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return (short) 0;
    }

    @Override // org.w3c.dom.Node
    public Node getParentNode() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public NodeList getChildNodes() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getFirstChild() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getLastChild() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getNextSibling() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.NodeList
    public int getLength() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return 0;
    }

    @Override // org.w3c.dom.NodeList
    public Node item(int index) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Document getOwnerDocument() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Element
    public String getTagName() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public String getNodeName() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public void normalize() {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    @Override // org.w3c.dom.Element
    public NodeList getElementsByTagName(String name) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Element
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Element
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttribute(String name) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttributeNS(String name, String x2) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    @Override // org.w3c.dom.Element
    public Attr getAttributeNode(String name) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Element
    public void removeAttribute(String name) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    @Override // org.w3c.dom.Element
    public void setAttribute(String name, String value) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    @Override // org.w3c.dom.Element
    public String getAttribute(String name) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public boolean hasAttributes() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    @Override // org.w3c.dom.Element
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Element
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Element
    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Element
    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    @Override // org.w3c.dom.Element
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    @Override // org.w3c.dom.Element
    public String getAttributeNS(String namespaceURI, String localName) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node getPreviousSibling() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public String getNodeValue() throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public void setNodeValue(String nodeValue) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void setValue(String value) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public Element getOwnerElement() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public boolean getSpecified() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    @Override // org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public Node removeChild(Node oldChild) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public boolean isSupported(String feature, String version) {
        return false;
    }

    @Override // org.w3c.dom.Node
    public String getNamespaceURI() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public String getPrefix() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Node
    public void setPrefix(String prefix) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    @Override // org.w3c.dom.Node
    public String getLocalName() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public DocumentType getDoctype() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Element getDocumentElement() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Element createElement(String tagName) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public DocumentFragment createDocumentFragment() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Text createTextNode(String data) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Comment createComment(String data) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public CDATASection createCDATASection(String data) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Attr createAttribute(String name) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public EntityReference createEntityReference(String name) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Element getElementById(String elementId) {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void setData(String data) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public String substringData(int offset, int count) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void appendData(String arg) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void insertData(int offset, String arg) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void deleteData(int offset, int count) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public void replaceData(int offset, int count, String arg) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public Text splitText(int offset) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public Node adoptNode(Node source) throws DOMException {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    @Override // org.w3c.dom.Document
    public String getInputEncoding() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void setInputEncoding(String encoding) {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public boolean getStandalone() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    public void setStandalone(boolean standalone) {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    @Override // org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return false;
    }

    @Override // org.w3c.dom.Document
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        error("ER_FUNCTION_NOT_SUPPORTED");
    }

    public String getVersion() {
        error("ER_FUNCTION_NOT_SUPPORTED");
        return null;
    }

    public void setVersion(String version) {
        error("ER_FUNCTION_NOT_SUPPORTED");
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

    @Override // org.w3c.dom.Document
    public Node renameNode(Node n2, String namespaceURI, String name) throws DOMException {
        return n2;
    }

    @Override // org.w3c.dom.Document
    public void normalizeDocument() {
    }

    @Override // org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public void setDocumentURI(String documentURI) {
        this.fDocumentURI = documentURI;
    }

    @Override // org.w3c.dom.Document
    public String getDocumentURI() {
        return this.fDocumentURI;
    }

    public String getActualEncoding() {
        return this.actualEncoding;
    }

    public void setActualEncoding(String value) {
        this.actualEncoding = value;
    }

    public Text replaceWholeText(String content) throws DOMException {
        return null;
    }

    public String getWholeText() {
        return null;
    }

    public boolean isWhitespaceInElementContent() {
        return false;
    }

    public void setIdAttribute(boolean id) {
    }

    @Override // org.w3c.dom.Element
    public void setIdAttribute(String name, boolean makeId) {
    }

    @Override // org.w3c.dom.Element
    public void setIdAttributeNode(Attr at2, boolean makeId) {
    }

    @Override // org.w3c.dom.Element
    public void setIdAttributeNS(String namespaceURI, String localName, boolean makeId) {
    }

    @Override // org.w3c.dom.Element
    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    public boolean isId() {
        return false;
    }

    @Override // org.w3c.dom.Document
    public String getXmlEncoding() {
        return this.xmlEncoding;
    }

    public void setXmlEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    @Override // org.w3c.dom.Document
    public boolean getXmlStandalone() {
        return this.xmlStandalone;
    }

    @Override // org.w3c.dom.Document
    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        this.xmlStandalone = xmlStandalone;
    }

    @Override // org.w3c.dom.Document
    public String getXmlVersion() {
        return this.xmlVersion;
    }

    @Override // org.w3c.dom.Document
    public void setXmlVersion(String xmlVersion) throws DOMException {
        this.xmlVersion = xmlVersion;
    }
}
