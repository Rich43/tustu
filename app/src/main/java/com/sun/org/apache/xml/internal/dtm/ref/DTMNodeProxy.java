package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMDOMException;
import com.sun.org.apache.xpath.internal.NodeSet;
import java.util.Objects;
import java.util.Vector;
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

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMNodeProxy.class */
public class DTMNodeProxy implements Node, Document, Text, Element, Attr, ProcessingInstruction, Comment, DocumentFragment {
    public DTM dtm;
    int node;
    private static final String EMPTYSTRING = "";
    static final DOMImplementation implementation = new DTMNodeProxyImplementation();
    protected String fDocumentURI;
    protected String actualEncoding;
    private String xmlEncoding;
    private boolean xmlStandalone;
    private String xmlVersion;

    public DTMNodeProxy(DTM dtm, int node) {
        this.dtm = dtm;
        this.node = node;
    }

    public final DTM getDTM() {
        return this.dtm;
    }

    public final int getDTMNodeNumber() {
        return this.node;
    }

    public final boolean equals(Node node) {
        try {
            DTMNodeProxy dtmp = (DTMNodeProxy) node;
            if (dtmp.node == this.node) {
                if (dtmp.dtm == this.dtm) {
                    return true;
                }
            }
            return false;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public final boolean equals(Object node) {
        return (node instanceof Node) && equals((Node) node);
    }

    public int hashCode() {
        int hash = (29 * 7) + Objects.hashCode(this.dtm);
        return (29 * hash) + this.node;
    }

    public final boolean sameNodeAs(Node other) {
        if (!(other instanceof DTMNodeProxy)) {
            return false;
        }
        DTMNodeProxy that = (DTMNodeProxy) other;
        return this.dtm == that.dtm && this.node == that.node;
    }

    @Override // org.w3c.dom.Node
    public final String getNodeName() {
        return this.dtm.getNodeName(this.node);
    }

    @Override // org.w3c.dom.ProcessingInstruction
    public final String getTarget() {
        return this.dtm.getNodeName(this.node);
    }

    @Override // org.w3c.dom.Node
    public final String getLocalName() {
        return this.dtm.getLocalName(this.node);
    }

    @Override // org.w3c.dom.Node
    public final String getPrefix() {
        return this.dtm.getPrefix(this.node);
    }

    @Override // org.w3c.dom.Node
    public final void setPrefix(String prefix) throws DOMException {
        throw new DTMDOMException((short) 7);
    }

    @Override // org.w3c.dom.Node
    public final String getNamespaceURI() {
        return this.dtm.getNamespaceURI(this.node);
    }

    public final boolean supports(String feature, String version) {
        return implementation.hasFeature(feature, version);
    }

    @Override // org.w3c.dom.Node
    public final boolean isSupported(String feature, String version) {
        return implementation.hasFeature(feature, version);
    }

    @Override // org.w3c.dom.Node
    public final String getNodeValue() throws DOMException {
        return this.dtm.getNodeValue(this.node);
    }

    public final String getStringValue() throws DOMException {
        return this.dtm.getStringValue(this.node).toString();
    }

    @Override // org.w3c.dom.Node
    public final void setNodeValue(String nodeValue) throws DOMException {
        throw new DTMDOMException((short) 7);
    }

    @Override // org.w3c.dom.Node
    public final short getNodeType() {
        return this.dtm.getNodeType(this.node);
    }

    @Override // org.w3c.dom.Node
    public final Node getParentNode() {
        int newnode;
        if (getNodeType() == 2 || (newnode = this.dtm.getParent(this.node)) == -1) {
            return null;
        }
        return this.dtm.getNode(newnode);
    }

    public final Node getOwnerNode() {
        int newnode = this.dtm.getParent(this.node);
        if (newnode == -1) {
            return null;
        }
        return this.dtm.getNode(newnode);
    }

    @Override // org.w3c.dom.Node
    public final NodeList getChildNodes() {
        return new DTMChildIterNodeList(this.dtm, this.node);
    }

    @Override // org.w3c.dom.Node
    public final Node getFirstChild() {
        int newnode = this.dtm.getFirstChild(this.node);
        if (newnode == -1) {
            return null;
        }
        return this.dtm.getNode(newnode);
    }

    @Override // org.w3c.dom.Node
    public final Node getLastChild() {
        int newnode = this.dtm.getLastChild(this.node);
        if (newnode == -1) {
            return null;
        }
        return this.dtm.getNode(newnode);
    }

    @Override // org.w3c.dom.Node
    public final Node getPreviousSibling() {
        int newnode = this.dtm.getPreviousSibling(this.node);
        if (newnode == -1) {
            return null;
        }
        return this.dtm.getNode(newnode);
    }

    @Override // org.w3c.dom.Node
    public final Node getNextSibling() {
        int newnode;
        if (this.dtm.getNodeType(this.node) == 2 || (newnode = this.dtm.getNextSibling(this.node)) == -1) {
            return null;
        }
        return this.dtm.getNode(newnode);
    }

    @Override // org.w3c.dom.Node
    public final NamedNodeMap getAttributes() {
        return new DTMNamedNodeMap(this.dtm, this.node);
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttribute(String name) {
        return -1 != this.dtm.getAttributeNode(this.node, null, name);
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return -1 != this.dtm.getAttributeNode(this.node, namespaceURI, localName);
    }

    @Override // org.w3c.dom.Node
    public final Document getOwnerDocument() {
        return (Document) this.dtm.getNode(this.dtm.getOwnerDocument(this.node));
    }

    @Override // org.w3c.dom.Node
    public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
        throw new DTMDOMException((short) 7);
    }

    @Override // org.w3c.dom.Node
    public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        throw new DTMDOMException((short) 7);
    }

    @Override // org.w3c.dom.Node
    public final Node removeChild(Node oldChild) throws DOMException {
        throw new DTMDOMException((short) 7);
    }

    @Override // org.w3c.dom.Node
    public final Node appendChild(Node newChild) throws DOMException {
        throw new DTMDOMException((short) 7);
    }

    @Override // org.w3c.dom.Node
    public final boolean hasChildNodes() {
        return -1 != this.dtm.getFirstChild(this.node);
    }

    @Override // org.w3c.dom.Node
    public final Node cloneNode(boolean deep) {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final DocumentType getDoctype() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public final DOMImplementation getImplementation() {
        return implementation;
    }

    @Override // org.w3c.dom.Document
    public final Element getDocumentElement() {
        int dochandle = this.dtm.getDocument();
        int elementhandle = -1;
        int firstChild = this.dtm.getFirstChild(dochandle);
        while (true) {
            int kidhandle = firstChild;
            if (kidhandle != -1) {
                switch (this.dtm.getNodeType(kidhandle)) {
                    case 1:
                        if (elementhandle != -1) {
                            elementhandle = -1;
                            kidhandle = this.dtm.getLastChild(dochandle);
                            break;
                        } else {
                            elementhandle = kidhandle;
                            break;
                        }
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 9:
                    default:
                        elementhandle = -1;
                        kidhandle = this.dtm.getLastChild(dochandle);
                        break;
                    case 7:
                    case 8:
                    case 10:
                        break;
                }
                firstChild = this.dtm.getNextSibling(kidhandle);
            } else {
                if (elementhandle == -1) {
                    throw new DTMDOMException((short) 9);
                }
                return (Element) this.dtm.getNode(elementhandle);
            }
        }
    }

    @Override // org.w3c.dom.Document
    public final Element createElement(String tagName) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final DocumentFragment createDocumentFragment() {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final Text createTextNode(String data) {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final Comment createComment(String data) {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final CDATASection createCDATASection(String data) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final Attr createAttribute(String name) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final EntityReference createEntityReference(String name) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final NodeList getElementsByTagName(String tagname) {
        Vector listVector = new Vector();
        Node retNode = this.dtm.getNode(this.node);
        if (retNode != null) {
            boolean isTagNameWildCard = "*".equals(tagname);
            if (1 == retNode.getNodeType()) {
                NodeList nodeList = retNode.getChildNodes();
                for (int i2 = 0; i2 < nodeList.getLength(); i2++) {
                    traverseChildren(listVector, nodeList.item(i2), tagname, isTagNameWildCard);
                }
            } else if (9 == retNode.getNodeType()) {
                traverseChildren(listVector, this.dtm.getNode(this.node), tagname, isTagNameWildCard);
            }
        }
        int size = listVector.size();
        NodeSet nodeSet = new NodeSet(size);
        for (int i3 = 0; i3 < size; i3++) {
            nodeSet.addNode((Node) listVector.elementAt(i3));
        }
        return nodeSet;
    }

    private final void traverseChildren(Vector listVector, Node tempNode, String tagname, boolean isTagNameWildCard) {
        if (tempNode == null) {
            return;
        }
        if (tempNode.getNodeType() == 1 && (isTagNameWildCard || tempNode.getNodeName().equals(tagname))) {
            listVector.add(tempNode);
        }
        if (tempNode.hasChildNodes()) {
            NodeList nodeList = tempNode.getChildNodes();
            for (int i2 = 0; i2 < nodeList.getLength(); i2++) {
                traverseChildren(listVector, nodeList.item(i2), tagname, isTagNameWildCard);
            }
        }
    }

    @Override // org.w3c.dom.Document
    public final Node importNode(Node importedNode, boolean deep) throws DOMException {
        throw new DTMDOMException((short) 7);
    }

    @Override // org.w3c.dom.Document
    public final Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public final NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        Vector listVector = new Vector();
        Node retNode = this.dtm.getNode(this.node);
        if (retNode != null) {
            boolean isNamespaceURIWildCard = "*".equals(namespaceURI);
            boolean isLocalNameWildCard = "*".equals(localName);
            if (1 == retNode.getNodeType()) {
                NodeList nodeList = retNode.getChildNodes();
                for (int i2 = 0; i2 < nodeList.getLength(); i2++) {
                    traverseChildren(listVector, nodeList.item(i2), namespaceURI, localName, isNamespaceURIWildCard, isLocalNameWildCard);
                }
            } else if (9 == retNode.getNodeType()) {
                traverseChildren(listVector, this.dtm.getNode(this.node), namespaceURI, localName, isNamespaceURIWildCard, isLocalNameWildCard);
            }
        }
        int size = listVector.size();
        NodeSet nodeSet = new NodeSet(size);
        for (int i3 = 0; i3 < size; i3++) {
            nodeSet.addNode((Node) listVector.elementAt(i3));
        }
        return nodeSet;
    }

    private final void traverseChildren(Vector listVector, Node tempNode, String namespaceURI, String localname, boolean isNamespaceURIWildCard, boolean isLocalNameWildCard) {
        if (tempNode == null) {
            return;
        }
        if (tempNode.getNodeType() == 1 && (isLocalNameWildCard || tempNode.getLocalName().equals(localname))) {
            String nsURI = tempNode.getNamespaceURI();
            if ((namespaceURI == null && nsURI == null) || isNamespaceURIWildCard || (namespaceURI != null && namespaceURI.equals(nsURI))) {
                listVector.add(tempNode);
            }
        }
        if (tempNode.hasChildNodes()) {
            NodeList nl = tempNode.getChildNodes();
            for (int i2 = 0; i2 < nl.getLength(); i2++) {
                traverseChildren(listVector, nl.item(i2), namespaceURI, localname, isNamespaceURIWildCard, isLocalNameWildCard);
            }
        }
    }

    @Override // org.w3c.dom.Document
    public final Element getElementById(String elementId) {
        return (Element) this.dtm.getNode(this.dtm.getElementById(elementId));
    }

    @Override // org.w3c.dom.Text
    public final Text splitText(int offset) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.CharacterData
    public final String getData() throws DOMException {
        return this.dtm.getNodeValue(this.node);
    }

    @Override // org.w3c.dom.CharacterData
    public final void setData(String data) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.CharacterData
    public final int getLength() {
        return this.dtm.getNodeValue(this.node).length();
    }

    @Override // org.w3c.dom.CharacterData
    public final String substringData(int offset, int count) throws DOMException {
        return getData().substring(offset, offset + count);
    }

    @Override // org.w3c.dom.CharacterData
    public final void appendData(String arg) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.CharacterData
    public final void insertData(int offset, String arg) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.CharacterData
    public final void deleteData(int offset, int count) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.CharacterData
    public final void replaceData(int offset, int count, String arg) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Element
    public final String getTagName() {
        return this.dtm.getNodeName(this.node);
    }

    @Override // org.w3c.dom.Element
    public final String getAttribute(String name) {
        DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
        Node n2 = map.getNamedItem(name);
        return null == n2 ? "" : n2.getNodeValue();
    }

    @Override // org.w3c.dom.Element
    public final void setAttribute(String name, String value) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Element
    public final void removeAttribute(String name) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Element
    public final Attr getAttributeNode(String name) {
        DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
        return (Attr) map.getNamedItem(name);
    }

    @Override // org.w3c.dom.Element
    public final Attr setAttributeNode(Attr newAttr) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Element
    public final Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Node
    public boolean hasAttributes() {
        return -1 != this.dtm.getFirstAttribute(this.node);
    }

    @Override // org.w3c.dom.Node
    public final void normalize() {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Element
    public final String getAttributeNS(String namespaceURI, String localName) {
        Node retNode = null;
        int n2 = this.dtm.getAttributeNode(this.node, namespaceURI, localName);
        if (n2 != -1) {
            retNode = this.dtm.getNode(n2);
        }
        return null == retNode ? "" : retNode.getNodeValue();
    }

    @Override // org.w3c.dom.Element
    public final void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Element
    public final void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Element
    public final Attr getAttributeNodeNS(String namespaceURI, String localName) {
        Attr retAttr = null;
        int n2 = this.dtm.getAttributeNode(this.node, namespaceURI, localName);
        if (n2 != -1) {
            retAttr = (Attr) this.dtm.getNode(n2);
        }
        return retAttr;
    }

    @Override // org.w3c.dom.Element
    public final Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Attr
    public final String getName() {
        return this.dtm.getNodeName(this.node);
    }

    @Override // org.w3c.dom.Attr
    public final boolean getSpecified() {
        return true;
    }

    @Override // org.w3c.dom.Attr
    public final String getValue() {
        return this.dtm.getNodeValue(this.node);
    }

    @Override // org.w3c.dom.Attr
    public final void setValue(String value) {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Attr
    public final Element getOwnerElement() {
        int newnode;
        if (getNodeType() == 2 && (newnode = this.dtm.getParent(this.node)) != -1) {
            return (Element) this.dtm.getNode(newnode);
        }
        return null;
    }

    @Override // org.w3c.dom.Document
    public Node adoptNode(Node source) throws DOMException {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public String getInputEncoding() {
        throw new DTMDOMException((short) 9);
    }

    public void setEncoding(String encoding) {
        throw new DTMDOMException((short) 9);
    }

    public boolean getStandalone() {
        throw new DTMDOMException((short) 9);
    }

    public void setStandalone(boolean standalone) {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        throw new DTMDOMException((short) 9);
    }

    @Override // org.w3c.dom.Document
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        throw new DTMDOMException((short) 9);
    }

    public String getVersion() {
        throw new DTMDOMException((short) 9);
    }

    public void setVersion(String version) {
        throw new DTMDOMException((short) 9);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMNodeProxy$DTMNodeProxyImplementation.class */
    static class DTMNodeProxyImplementation implements DOMImplementation {
        DTMNodeProxyImplementation() {
        }

        @Override // org.w3c.dom.DOMImplementation
        public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) {
            throw new DTMDOMException((short) 9);
        }

        @Override // org.w3c.dom.DOMImplementation
        public Document createDocument(String namespaceURI, String qualfiedName, DocumentType doctype) {
            throw new DTMDOMException((short) 9);
        }

        @Override // org.w3c.dom.DOMImplementation
        public boolean hasFeature(String feature, String version) {
            if ("CORE".equals(feature.toUpperCase()) || "XML".equals(feature.toUpperCase())) {
                if ("1.0".equals(version) || "2.0".equals(version)) {
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override // org.w3c.dom.DOMImplementation
        public Object getFeature(String feature, String version) {
            return null;
        }
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
        return this.dtm.getStringValue(this.node).toString();
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

    @Override // org.w3c.dom.Text
    public Text replaceWholeText(String content) throws DOMException {
        return null;
    }

    @Override // org.w3c.dom.Text
    public String getWholeText() {
        return null;
    }

    @Override // org.w3c.dom.Text
    public boolean isElementContentWhitespace() {
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

    @Override // org.w3c.dom.Attr
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
